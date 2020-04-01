package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class OldDijkstraPanel extends JPanel {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;

    private int rows = 50;
    private int columns = 50;

    private OldNode[][] grid;
    private PriorityQueue<OldNode> minHeap;

    private boolean start = false;
    private int startRow;
    private int startCol;

    private boolean end = false;
    private int endRow;
    private int endCol;

    private boolean found = false;

    private boolean diagonal = false;

    private boolean started = false;

    private String[] algorithms = {"Dijkstra's", "A*"};
    private JComboBox<String> algorithmSelector = new JComboBox<String>(algorithms);

    private JButton startButton = new JButton("Start Algorithm");
    private JButton deselectButton = new JButton("Deselect Wall");

    private JCheckBox directionCheckbox = new JCheckBox("Allow Diagonal Movement");

    private JLabel consoleText = new JLabel();
    private JPanel panel1;

    public OldDijkstraPanel() {
        //Set overall panel layout to vertical box layout
        setSize(WIDTH, HEIGHT);
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        grid = new OldNode[columns][rows];

        //Add grid


        //Create button panel and set layout to horizontal box layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setMinimumSize(new Dimension(WIDTH, 50));
        topPanel.setPreferredSize(new Dimension(WIDTH, 50));


        //Create buttons



        startButton.setSize(100, 50);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if(source.getText().equals("Start Algorithm")) {
                    started = true;
                    source.setEnabled(false);
                    directionCheckbox.setEnabled(false);
                    deselectButton.setEnabled(false);

                    dijkstraIteration();
                    startButton.setText("Reset");
                    startButton.setEnabled(true);
                } else if(source.getText().equals("Reset")) {
                    System.out.println("Should reset");
                    reset();
                }
            }
        });


        deselectButton.setSize(100, 50);

        algorithmSelector.setPreferredSize(new Dimension(200, 25));
        algorithmSelector.setMaximumSize(new Dimension(200, 25));

        JLabel titleLabel = new JLabel("<html>Welcome to a <br> Pathfinding Visualizer </html>");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 16));
        titleLabel.setMaximumSize(new Dimension(200, 50));


        directionCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                diagonal = directionCheckbox.isSelected();
            }
        });


        topPanel.add(titleLabel);
        topPanel.add(algorithmSelector);
        topPanel.add(Box.createRigidArea(new Dimension(75, 0)));
        topPanel.add(directionCheckbox);
        topPanel.add(Box.createRigidArea(new Dimension(75, 0)));
        topPanel.add(startButton);
        topPanel.add(deselectButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        bottomPanel.setSize(WIDTH, 200);

        JButton test = new JButton("Test");


        bottomPanel.add(createLegend());
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(test);

        add(topPanel);
        dijkstraInitialize();
        add(bottomPanel);
        minHeap = new PriorityQueue<OldNode>(Comparator.comparingInt(OldNode::getDistanceFromStart));
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(Color.WHITE);


    }

    //Initialize grid with unvisited nodes
    public void dijkstraInitialize() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(50, 50));
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                OldNode node = new OldNode(i, j);

                node.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        JButton source = (JButton) mouseEvent.getSource();

                        if(!started) {
                            if (!start) {
                                start = true;
                                node.setBackground(new Color(0, 100, 0));
                                node.setStart();
                                startRow = node.getRow();
                                startCol = node.getCol();
                                minHeap.add(node);
                            } else if (!end) {
                                end = true;
                                node.setBackground(new Color(150, 0, 0));
                                node.setEnd();
                                endRow = node.getRow();
                                endCol = node.getCol();
                            } else {
                                if (mouseEvent.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK && !node.isWeighted()) {
                                    node.setBackground(Color.BLACK);
                                    node.setWall(true);
                                } else if (mouseEvent.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK && !node.isWall()) {
                                    node.setBackground(new Color(200, 99, 0));
                                    node.setWeighted(true);
                                }
                            }
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {
                        if(!started) {
                            if (start && end) {
                                if (mouseEvent.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK && !node.isStart() && !node.isEnd() && !node.isWeighted()) {
                                    node.setBackground(Color.BLACK);
                                    node.setWall(true);

                                }

                                if (mouseEvent.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK && !node.isStart() && !node.isEnd() && !node.isWall()) {
                                    node.setBackground(new Color(200, 99, 0));
                                    node.setWeighted(true);
                                }
                            }
                        }
                    }
                });
                grid[i][j] = node;
                gridPanel.add(grid[i][j]);
            }
        }
        add(gridPanel);
    }

    public void dijkstraIteration() {
        System.out.println("got here");
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!minHeap.isEmpty()) {
                    //Get next closest node from start or get start node
                    OldNode closest = minHeap.poll();
                    if (!closest.isWall()) {
                        closest.setVisited(true);
                        if(closest.getRow() == endRow && closest.getCol() == endCol) {
                            found = true;
                            showShortestPath(closest);
                            timer.cancel();
                        }
                        if(!found) {
                            if (!closest.isStart()) {
                                if(!closest.isWeighted()) {
                                    grid[closest.getRow()][closest.getCol()].setBackground(new Color(102, 255, 255));
                                } else {
                                    grid[closest.getRow()][closest.getCol()].setBackground(new Color(155, 75, 255));
                                }
                            }
                            updateUnvisitedNeighbors(closest.row, closest.col);
                        }
                    }
                }
            }
        }, 100, 50);


    }

    private ArrayList<OldNode> getUnvisitedNeighbors(int row, int col) {
        ArrayList<OldNode> unvisited = new ArrayList<>();
        boolean foundEnd = false;

        if(row > 0 && !grid[row-1][col].isVisited() && !grid[row-1][col].isWall()) {
            if(row - 1 == endRow && col == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row-1][col]);
        }
        if(row < grid.length - 1 && !grid[row+1][col].isVisited() && !grid[row+1][col].isWall()) {
            if(row + 1 == endRow && col == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row+1][col]);
        }
        if(col > 0 && !grid[row][col-1].isVisited() && !grid[row][col-1].isWall()) {
            if(row == endRow && col - 1 == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row][col-1]);
        }

        if(col < grid[0].length - 1 && !grid[row][col+1].isVisited() && !grid[row][col+1].isWall()) {
            if(row == endRow && col + 1 == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row][col+1]);
        }

        if(diagonal && col > 0 && row > 0 && !grid[row-1][col-1].isVisited() && !grid[row-1][col-1].isWall() && !grid[row-1][col].isWall() && !grid[row][col-1].isWall()) {
            if(row - 1 == endRow && col - 1 == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row-1][col-1]);
        }

        if(diagonal && col < grid.length - 1 && row > 0 && !grid[row-1][col+1].isVisited() && !grid[row-1][col+1].isWall() && !grid[row-1][col].isWall() && !grid[row][col+1].isWall()) {
            if(row - 1 == endRow && col + 1 == endCol) {
                foundEnd = true;
            }
            unvisited.add(grid[row-1][col+1]);
        }

        if(diagonal && col > 0 && row < grid.length - 1 && !grid[row+1][col-1].isVisited() && !grid[row+1][col-1].isWall() && !grid[row+1][col].isWall() && !grid[row][col-1].isWall()) {

            unvisited.add(grid[row+1][col-1]);
        }

        if(diagonal && col < grid.length - 1 && row < grid.length - 1 && !grid[row+1][col+1].isVisited() && !grid[row+1][col+1].isWall() && !grid[row+1][col].isWall() && !grid[row][col+1].isWall()) {

            unvisited.add(grid[row+1][col+1]);
        }

        return unvisited;
    }

    private void updateUnvisitedNeighbors(int row, int col) {
        ArrayList<OldNode> unvisited = getUnvisitedNeighbors(row, col);

        for(OldNode node : unvisited) {
            if(node.isWeighted()) {
                node.setDistanceFromStart(grid[row][col].getDistanceFromStart() + 3);
            } else {
                node.setDistanceFromStart(grid[row][col].getDistanceFromStart() + 1);
            }
            node.setPreviousNode(grid[row][col]);
            node.setVisited(true);
            if(node.getRow() != endRow || node.getCol() != endCol) {
                if(node.isWeighted()) {
                    node.setBackground(new Color(95, 3, 255));
                } else {
                    node.setBackground(Color.BLUE);
                }
            } else {
                node.setBackground(Color.orange);
            }
            minHeap.add(node);
        }
    }

    private void showShortestPath(OldNode endNode) {
        if(endNode.getPreviousNode() == null) {
            return;
        }

        if(endNode.getRow() == endRow && endNode.getCol() == endCol) {
            showShortestPath(endNode.getPreviousNode());

        } else {
            grid[endNode.getRow()][endNode.getCol()].setBackground(Color.MAGENTA);
            showShortestPath(endNode.getPreviousNode());
        }
    }

    private void reset() {
        started = false;
        start = false;
        end = false;
        directionCheckbox.setEnabled(true);
        deselectButton.setEnabled(true);
        minHeap.clear();

        for(OldNode[] row : grid) {
            for(OldNode cell : row) {
                cell.resetNode();
            }
        }

        startButton.setText("Start Algorithm");
    }

    private JPanel createLegend() {
        //Create dimension for keys in legend
        Dimension keyDimension = new Dimension(75, 50);

        //Create key/legend
        JPanel legend = new JPanel();
        legend.setSize(400, 200);

//        adf;dsafkjasfklja
        //LOOK UP HOW TO USE GROUP LAYOUT
//        legend.setLayout(new GroupLayout(this));
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);
        legend.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        //Create key for start

        //Get image from file
        Image startImage = null;
        try {
            startImage = ImageIO.read(this.getClass().getResource("legend-icons/startIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scale image down
        startImage = startImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        //Turn image into imageIcon to put into JLabel
        ImageIcon startIcon = new ImageIcon(startImage);

        //Create start key JLabel with startIcon
        JLabel startLegend = new JLabel(startIcon);
        startLegend.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        //Set max size of label
        startLegend.setMaximumSize(keyDimension);

        //Add text
        startLegend.setText("Start");


        //Create key for end

        //Get image from file
        Image endImage = null;
        try {
            endImage = ImageIO.read(this.getClass().getResource("legend-icons/endIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scale image down
        endImage = endImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        //Turn image into imageIcon to put into JLabel
        ImageIcon endIcon = new ImageIcon(endImage);

        //Create end key JLabel with endIcoon
        JLabel endLegend = new JLabel(endIcon);
        endLegend.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        //Set max size of endLabel
        endLegend.setMaximumSize(keyDimension);

        //Add text to endLabel
        endLegend.setText("End");


        //Create key for wall

        //Get image from file
        Image wallImage = null;
        try {
            wallImage = ImageIO.read(this.getClass().getResource("legend-icons/wallIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scale image down
        wallImage = wallImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        //Turn image into imageIcon to put into JLabel
        ImageIcon wallIcon = new ImageIcon(wallImage);

        //Create wall key JLabel with wallIcoon
        JLabel wallLegend = new JLabel(wallIcon);
        wallLegend.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        //Set max size of endLabel
        wallLegend.setMaximumSize(keyDimension);

        //Add text to endLabel
        wallLegend.setText("Wall");


        //Create key for weighted

        //Get image from file
        Image weightedImage = null;
        try {
            weightedImage = ImageIO.read(this.getClass().getResource("legend-icons/weightedIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Scale image down
        weightedImage = weightedImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        //Turn image into imageIcon to put into JLabel
        ImageIcon weightedIcon = new ImageIcon(weightedImage);

        //Create weighted key JLabel with weightedIcoon
        JLabel weightedLegend = new JLabel(weightedIcon);
        weightedLegend.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        //Set max size of endLabel
        weightedLegend.setMaximumSize(keyDimension);

        //Add text to endLabel
        weightedLegend.setText("Weighted");




        //Add all keys into legend
        legend.add(startLegend);
        legend.add(endLegend);

        legend.add(wallLegend);
        legend.add(weightedLegend);

        return legend;
    }
}
