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

public class DijkstraPanel {
    public JPanel panel1;
    private JLabel title;
    private JTable grid;
    private JButton startButton;
    private JButton chooseStartButton;
    private JButton chooseEndButton;
    private JButton chooseWallButton;
    private JButton chooseWeightedButton;




    //----------------JTABLE VERSION-----------------------------------------------------------------------------------

    /**
     * COLOR KEY:
     * 0 - default
     * 1 - start node
     * -1 - end node
     * 2 - wall node
     * 3 - weighted node
     */
    private static int numRows = 40;
    private static int numCols = 40;

    public static GridPanel gridPanel = new GridPanel(numRows, numCols);
    private PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(Node::getDistanceFromStart));

    private int keyNum = 0;

    private boolean start = false;
    private boolean end = false;

    private int startRow = 0;
    private int startCol = 0;

    private int endRow = 0;
    private int endCol = 0;

    private boolean found = false;

//    private GridPanel gridPanel;

    public DijkstraPanel() {
        createUIComponents();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyNum = 4;
                dijkstraIteration();
            }
        });
        chooseStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyNum = 1;
                chooseStartButton.setEnabled(false);
                chooseEndButton.setEnabled(true);
                chooseWeightedButton.setEnabled(true);
                chooseWallButton.setEnabled(true);
            }
        });
        chooseEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyNum = -1;
                chooseStartButton.setEnabled(true);
                chooseEndButton.setEnabled(false);
                chooseWeightedButton.setEnabled(true);
                chooseWallButton.setEnabled(true);
            }
        });
        chooseWallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyNum = 2;
                chooseStartButton.setEnabled(true);
                chooseEndButton.setEnabled(true);
                chooseWeightedButton.setEnabled(true);
                chooseWallButton.setEnabled(false);
            }
        });
        chooseWeightedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyNum = 3;
                chooseStartButton.setEnabled(true);
                chooseEndButton.setEnabled(true);
                chooseWeightedButton.setEnabled(false);
                chooseWallButton.setEnabled(true);
            }
        });
    }


    private void dijkstraIteration() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!minHeap.isEmpty()) {
                    //Get next closest node from start or get start node
                    Node closest = minHeap.poll();
                    if (!closest.isWall()) {
                        if(closest.getRow() == endRow && closest.getCol() == endCol) {
                            found = true;
                            showShortestPath(closest);
                            gridPanel.grid.fireTableDataChanged();
                            timer.cancel();
                        }
                        if(!found) {
                            if (closest.getRow() != startRow || closest.getCol() != startCol) {
                                gridPanel.nodeVisited(closest.getRow(), closest.getCol());
                            }
                            updateUnvisitedNeighbors(closest.getRow(), closest.getCol());
                            gridPanel.grid.fireTableDataChanged();
                        }
                    }
                }
            }
        }, 100, 50);
    }

    private void updateUnvisitedNeighbors(int row, int col) {
        ArrayList<Node> unvisited = getUnvisitedNeighbors(row, col);

        for(Node node : unvisited) {
            node.setPreviousNode(gridPanel.getNodeAt(row, col));
            if(node.isWeighted()) {
                node.setDistanceFromStart(node.getPreviousNode().getDistanceFromStart() + 3);
            } else {
                node.setDistanceFromStart(node.getPreviousNode().getDistanceFromStart() + 1);
            }
            node.setNeighbor(true);
            gridPanel.grid.setValueAt(node, node.getRow(), node.getCol());
            minHeap.add(node);
        }
    }

    private ArrayList<Node> getUnvisitedNeighbors(int row, int col) {
        ArrayList<Node> unvisited = new ArrayList<>();

        if(row - 1 > 0) {
            if(!(gridPanel.getNodeAt(row-1, col)).isNeighbor()) {
//                System.out.println("up");
                unvisited.add(gridPanel.getNodeAt(row-1, col));
            }
        }
        if(row + 1 < numRows) {
            if(!(gridPanel.getNodeAt(row+1, col)).isNeighbor()) {
//                System.out.println("down");
                unvisited.add(gridPanel.getNodeAt(row+1, col));
            }
        }
        if(col - 1 > 0) {
            if(!(gridPanel.getNodeAt(row, col-1)).isNeighbor()) {
//                System.out.println("left");
                unvisited.add(gridPanel.getNodeAt(row, col-1));
            }
        }
        if(col + 1 < numCols) {
            if(!(gridPanel.getNodeAt(row, col+1)).isNeighbor()) {
//                System.out.println("right");
                unvisited.add(gridPanel.getNodeAt(row, col+1));
            }
        }

        return unvisited;
    }

    public void showShortestPath(Node endNode) {
        if(endNode.getRow() == startRow && endNode.getCol() == startCol) {
            return;
        }

        if(endNode.getRow() != endRow || endNode.getCol() != endCol) {
            endNode.setShortest(true);
        }

        showShortestPath(endNode.getPreviousNode());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        grid = new JTable(gridPanel.grid);
        grid.setVisible(true);
        grid.setPreferredSize(new Dimension(950, 300));
        grid.setMaximumSize(new Dimension(950, 300));
        grid.setRowSelectionAllowed(false);
        grid.setDefaultRenderer(Object.class, new GridRenderer());
        grid.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                        if(keyNum == 1) {
                        gridPanel.setNodeAs(keyNum, startRow, startCol, false);
                        gridPanel.setNodeAs(keyNum, row, col, true);
                        minHeap.clear();
                        minHeap.add(gridPanel.getNodeAt(row, col));
                        startRow = row;
                        startCol = col;
                        start = true;
                    } else if(keyNum == -1) {
                        gridPanel.setNodeAs(keyNum, endRow, endCol, false);
                        gridPanel.setNodeAs(keyNum, row, col, true);
                        endRow = row;
                        endCol = col;
                        end = true;
                    } else {
                        gridPanel.setNodeAs(keyNum, row, col, true);
                    }
                }
            }

        });

        grid.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                JTable table = (JTable) e.getSource();
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if(start && end && keyNum != -1 && keyNum != 1) {
                    if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                        gridPanel.setNodeAs(keyNum, row, col, true);
                    }
                }
            }
        });
    }





}
