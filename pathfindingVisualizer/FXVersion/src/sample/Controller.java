package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Controller {
    public Stage stage;
    //All variables
    public int currentNodeType = GridNode.START;
    public boolean placedStart = false;
    public boolean placedEnd = false;
    public boolean found = false;
    public Point startCoord = new Point ( -1, -1 );
    public Point endCoord = new Point ( -1, -1 );
    //Variables that require FXML Variables
    public int NUMROWS = 50;
    public int NUMCOLS = 50;
    //Variables for algorithms
    private String algorithm;
    private final int costStraight = 3;
    private final int costDiagonal = 5;
    //This minHeap sorts GridNodes by distanceFromStart
    PriorityQueue<GridNode> minHeap = new PriorityQueue<> ( new Comparator<GridNode> () {
        @Override
        public int compare ( GridNode x, GridNode y )
        {
            if(algorithm.equals("A*"))
            {
                int dx = x.getDistanceFromStart () + x.getHeuristic ();
                int dy = y.getDistanceFromStart() + y.getHeuristic ();

                //If dx and dy are equal, compare using only heuristics
                if(Math.abs(dx - dy) == 0) {
                    //This ensures that program will not check unnecessary nodes that are further away from end node
                    return x.getHeuristic() - y.getHeuristic();
                }

                return dx - dy;
            } else if(algorithm.equals("Depth First Search (DFS)")) {
                return y.getDistanceFromStart() - x.getDistanceFromStart();
            }

            return x.getDistanceFromStart () - y.getDistanceFromStart ();
        }
    } );
    boolean allowDiagonal = false;
    //All FXML objects
    @FXML
    BorderPane root;
    @FXML
    GridPane grid;
    @FXML
    GridPane buttonGrid;
    @FXML
    ChoiceBox<String> algorithmSelector;
    @FXML
    Button setStartBtn;
    @FXML
    Button setEndBtn;
    @FXML
    Button setWallBtn;
    @FXML
    Button setWeightedBtn;
    @FXML
    Button startVisualizerBtn;
    @FXML
    CheckBox diagonalCheckBox;
    @FXML
    Label setTypeLabel;
    @FXML
    SVGPath menuMaximizeIcon;

    private List<Node> gridChildren;
    private Timer timer;

    private int stageWidth;
    private int stageHeight;
    public int contentWidth;
    private Service<Task<Void>> service;

    private double offsetX;
    private double offsetY;

    @FXML
    protected void initialize ()
    {
        //Disable setStartBtn
        setStartBtn.setDisable ( true );
        setWeightedBtn.setDisable(true);
        setTypeLabel.setWrapText(true);
        startVisualizerBtn.setDisable(true);
        initializeGrid();
        gridChildren = grid.getChildren ();
    }


    /**
     * This function initializes the grid
     */
    private void initializeGrid ()
    {
        //Grab grid's columns and rows
        List<ColumnConstraints> columns = grid.getColumnConstraints ();
        List<RowConstraints> rows = grid.getRowConstraints ();


        //Add column and row constraints for grid
        for (int i = 0; i < NUMCOLS; i++)
        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow( Priority.SOMETIMES);
            columnConstraints.setPercentWidth ( NUMCOLS );
            columnConstraints.setMaxWidth(20);
            columns.add ( columnConstraints );
        }

        for (int i = 0; i < NUMROWS; i++)
        {
            RowConstraints rowConstraints = new RowConstraints ();
            rowConstraints.setVgrow ( Priority.SOMETIMES );
            rowConstraints.setPercentHeight ( NUMROWS );
            rowConstraints.setMaxHeight ( 20 );
            rows.add ( rowConstraints );
        }


        //Add a node to each cell of the grid, these will be used in the pathfinding algorithms
        for (int i = 0; i < NUMCOLS; i++)
        {
            for (int j = 0; j < NUMROWS; j++)
            {
                addNode ( GridNode.NORMAL, j, i );
            }
        }


        grid.setGridLinesVisible ( false );

        grid.requestFocus ();
    }

    /**
     * This function creates a new node, adds event handlers, and finally adds the node to the grid
     *
     * @param type
     * @param row
     * @param col
     */
    private void addNode ( int type, int row, int col )
    {
        GridNode node = new GridNode ( type, col, row );
        //On mouse click
        node.setOnMouseClicked ( event -> {
            //If node clicked already has a type
            if(event.getButton () == MouseButton.PRIMARY)
            {
                if ( node.getType () == -1 )
                {
                    //If start node already placed and about to place another start node, move start node
                    if ( placedStart && currentNodeType == GridNode.START )
                    {
                        //Get currentStartNode
                        GridNode currentStart = ( GridNode ) gridChildren.get ( NUMROWS * startCoord.x + startCoord.y );


                        //Reset currentStartNode
                        currentStart.setType ( GridNode.NORMAL );

                        //Set node to new start and replace startCoord with new coordinates
                        node.setType ( currentNodeType );
                        startCoord.setLocation ( col, row );
                        System.out.println ("added start");
                    }
                    //If end node already placed and about to place another end node, move end node
                    else if ( placedEnd && currentNodeType == GridNode.END )
                    {
                        //Get currentEndNode
                        GridNode currentEnd = ( GridNode ) gridChildren.get ( NUMROWS * endCoord.x + endCoord.y );

                        //Reset currentEndNode
                        currentEnd.setType ( GridNode.NORMAL );

                        //Set node to new end and replace endCoord with new coordinates
                        node.setType ( currentNodeType );
                        endCoord.setLocation ( col, row );
                    }
                    else
                    {
                        node.setType ( currentNodeType );
                        if ( currentNodeType == GridNode.START )
                        {
                            //Set placed start node to true
                            placedStart = true;

                            //Change setTypeLabel text
                            setTypeLabel.setText ( " Moving Start Node" );

                            //Store coordinates into start node point
                            startCoord.setLocation ( col, row );

                            //Check if both start and end nodes are placed
                            if(placedStart && placedEnd)
                            {
                                startVisualizerBtn.setDisable(false);
                            }
                        }
                        if ( currentNodeType == GridNode.END )
                        {
                            //Set placed end node to true
                            placedEnd = true;

                            //Change setTypeLabel text
                            setTypeLabel.setText ( " Moving End Node" );

                            //Store coordinates into end node point
                            endCoord.setLocation ( col, row );

                            //Check if both start and end nodes are placed
                            if(placedStart && placedEnd)
                            {
                                startVisualizerBtn.setDisable(false);
                            }
                        }
                    }
                }
            } else if(event.getButton() == MouseButton.SECONDARY)
            {       //If right click, "erase"
                if(node.getType() == GridNode.NORMAL) {
                    return;
                }
                if(node.getType() == GridNode.START) {
                    placedStart = false;
                    startVisualizerBtn.setDisable(true);
                } else if(node.getType() == GridNode.END) {
                    placedEnd = false;
                    startVisualizerBtn.setDisable(true);
                }
                node.setType(GridNode.NORMAL);
            }
        } );

        //If user starts to drag mouse, enable mouse drag
        node.setOnDragDetected ( event -> {
            if(event.getButton() == MouseButton.PRIMARY)
            {
                if ( currentNodeType != GridNode.START && currentNodeType != GridNode.END )
                {
                    node.startFullDrag ();
                }
            } else if(event.getButton() == MouseButton.SECONDARY){
                node.startFullDrag();
            }
        } );

        //Update node type on mouse drag
        node.setOnMouseDragEntered ( event -> {
            if(event.getButton() == MouseButton.PRIMARY)
            {
                if ( node.getType () == -1 && currentNodeType != GridNode.START && currentNodeType != GridNode.END )
                {
                    node.setType ( currentNodeType );
                }
            } else if(event.getButton() == MouseButton.SECONDARY)
            {       //If right click, then "erase"
                if(node.getType() == GridNode.NORMAL) {
                    return;
                }

                if(node.getType() == GridNode.START) {
                    placedStart = false;
                    startVisualizerBtn.setDisable ( true );
                } else if(node.getType() == GridNode.END) {
                    placedEnd = false;
                    startVisualizerBtn.setDisable(true);
                }
                node.setType(GridNode.NORMAL);
            }
        } );

        grid.add ( node, col, row );
    }

    /**
     * This function is called in MainFX so that Controller can access and monitor stage properties
     *
     * @param stage Function will be used to change grid size, numRows, and numCols on stage resize
     */
    public void sendStage ( Stage stage )
    {
        this.stage = stage;
        WindowResizeHelper.addResizeListener ( stage );
    }

    @FXML
    protected void changeNodeType ( ActionEvent actionEvent )
    {
        algorithm = algorithmSelector.getValue();
        Button buttonPressed = ( Button ) actionEvent.getSource ();
        String buttonType = buttonPressed.getText ();

        //Change currentNodeType to user selected node type
        if ( buttonType.equals ( "Start" ) )
        {
            currentNodeType = GridNode.START;
            if(!placedStart)
            {
                setTypeLabel.setText ( " Selecting Start Node" );
            } else {
                setTypeLabel.setText(" Moving Start Node");
            }
        }
        else if ( buttonType.equals ( "End" ) )
        {
            currentNodeType = GridNode.END;
            if(!placedEnd)
            {
                setTypeLabel.setText ( " Selecting End Node" );
            } else {
                setTypeLabel.setText(" Moving End Node");
            }
        }
        else if ( buttonType.equals ( "Wall" ) )
        {
            currentNodeType = GridNode.WALL;
            setTypeLabel.setText(" Selecting Wall Nodes");
        }
        else if ( buttonType.equals ( "Weighted" ) )
        {
            currentNodeType = GridNode.WEIGHTED;
            setTypeLabel.setText(" Selecting Weighted Nodes");
        }

        //Enable all buttons
        setStartBtn.setDisable ( false );
        setEndBtn.setDisable ( false );
        setWallBtn.setDisable ( false );

        if(algorithm.equals("Depth First Search (DFS)") || algorithm.equals("Breadth First Search (BFS)"))
        {
            setWeightedBtn.setDisable ( true );
        } else {
            setWeightedBtn.setDisable(false);
        }

        //Disable button that was clicked
        buttonPressed.setDisable ( true );

        //Give focus back to grid so that user doesn't need to press on grid again before nodes can update from mouse
        // events
        grid.requestFocus ();
    }

    @FXML
    protected void changeAlgorithm ( ActionEvent actionEvent )
    {
        algorithm = algorithmSelector.getValue();

        if(algorithm.equals("Depth First Search (DFS)") || algorithm.equals("Breadth First Search (BFS)")) {
            setWeightedBtn.setDisable(true);
        } else {
            setWeightedBtn.setDisable(false);
        }
    }


    @FXML
    protected void startVisualizer ( ActionEvent actionEvent )
    {
        //Check if startVisualizerBtn text is "Start Visualizer"
        if(startVisualizerBtn.getText().equals("Start Visualizer"))
        {
            //Get all needed user selected algorithm variables before starting algorithm
            algorithm = algorithmSelector.getValue();
            allowDiagonal = diagonalCheckBox.isSelected ();

            //Disable all constant user selected algorithm variables
            algorithmSelector.setDisable ( true );
            startVisualizerBtn.setDisable ( true );
            diagonalCheckBox.setDisable ( true );
            setStartBtn.setDisable ( true );
            setEndBtn.setDisable ( true );
            setWeightedBtn.setDisable(true);
            setWallBtn.setDisable(true);

            grid.setDisable(true);

            startAlgorithm ();
        } else if(startVisualizerBtn.getText().equals("Reset")) {   //Reset program
            //Reset grid
            for(int i = 0; i < NUMROWS; i++) {
                for(int j = 0; j < NUMCOLS; j++) {
                    GridNode node = (GridNode) gridChildren.get(NUMROWS * j + i);

                    node.setType(GridNode.NORMAL);
                    node.setPrevious(null);
                    node.setDistanceFromStart ( Integer.MAX_VALUE );
                    node.setHeuristic(Integer.MAX_VALUE);
                }
            }

            //Reset all variables
            placedStart = false;
            placedEnd = false;
            currentNodeType = GridNode.START;

            startCoord = new Point(-1, -1);
            endCoord = new Point(-1, -1);

            setStartBtn.setDisable(true);
            setEndBtn.setDisable(false);
            setWallBtn.setDisable(false);
            setWeightedBtn.setDisable(false);

            algorithmSelector.setDisable(false);
            diagonalCheckBox.setDisable(false);

            minHeap.clear();

            setTypeLabel.setText("Selecting Start Node");

            //Set startVisualizerBtn test back to start visualizer
            startVisualizerBtn.setText("Start Visualizer");

            //Give focus back to grid
            grid.setDisable(false);
            grid.requestFocus();
        }
    }

    private void startAlgorithm ()
    {
        //Get startNode
        GridNode node = ( GridNode ) gridChildren.get ( NUMROWS * startCoord.x + startCoord.y );

        //Set startNode distance to 0
        node.setDistanceFromStart ( 0 );

        //Add startNode to minHeap
        minHeap.add ( node );

        //This is the task/thread that will run Dijkstra's/A* algorithm if chosen
        Task<Void> task = new Task<Void> () {
            @Override
            protected Void call () throws Exception
            {
                GridNode currentNode;
                //Until minHeap is empty
                while ( !minHeap.isEmpty () )
                {
                    currentNode = minHeap.poll ();
                    if ( currentNode.getCoord ().equals ( endCoord ) )
                    {
                        found = true;
                        showShortestPath(currentNode);
                        break;
                    } else
                    {

                        getNeighbors ( currentNode );

                        //If startNode, do not need to change nodeType
                        if ( !currentNode.getCoord ().equals ( startCoord ) )
                        {

                            if ( currentNode.getType () == GridNode.WEIGHTEDCHECKING )
                            {
                                currentNode.setType ( GridNode.WEIGHTEDCHECKED );
                            }
                            else
                            {
                                currentNode.setType ( GridNode.NORMALCHECKED );
                            }
                        }
                    }
                    try {
                        Thread.sleep(25);

                    } catch (InterruptedException e) {
                        if(isCancelled ()) {
                            updateMessage("Cancelled");
                            break;
                        }
                    }
                }

                if(!found) {
                    System.out.println ("Could not find shortest path");

                }
                //Update startVisualizerBtn text to reset
                Platform.runLater(()-> {
                    startVisualizerBtn.setText("Reset");
                    startVisualizerBtn.setDisable(false);
                });
                return null;
            }

            @Override
            protected void failed() {
                Throwable throwable = this.getException ();
                throwable.printStackTrace ();
            }
        };

        service = new Service<Task<Void>> () {
            @Override
            protected Task createTask ()
            {
                return task;
            }
        };

        service.restart();

    }

    /**
     * This function checks all nodes surrounding the given parameter node and adds it to the PriorityQueue minHeap
     * Used for Dijkstra's and A*
     * @param node
     */
    private void getNeighbors ( GridNode node )
    {
        boolean dfs = algorithm.equals("Depth First Search (DFS)");
        //Cache node coordinates
        Point coord = node.getCoord ();

        //This arraylist will be used for dfs
        ArrayList<GridNode> neighbors = new ArrayList<>();
        //North
        if ( coord.x - 1 >= 0 )
        {
            if(dfs) {
                neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x - 1) + coord.y));
            } else
            {
                addNeighborDijkstraAStar ( node, coord.x - 1, coord.y, false );
            }
        }
        //South
        if ( coord.x + 1 < NUMCOLS )
        {
            if(dfs) {
                neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x + 1) + coord.y));
            } else
            {
                addNeighborDijkstraAStar ( node, coord.x + 1, coord.y, false );
            }
        }
        //East
        if ( coord.y + 1 < NUMROWS )
        {
            if(dfs) {
                neighbors.add((GridNode) gridChildren.get(NUMROWS * coord.x + coord.y + 1));
            } else
            {
                addNeighborDijkstraAStar ( node, coord.x, coord.y + 1, false );
            }
        }
        //West
        if ( coord.y - 1 >= 0 )
        {
            if(dfs) {
                neighbors.add((GridNode) gridChildren.get(NUMROWS * coord.x + coord.y - 1));
            } else
            {
                addNeighborDijkstraAStar ( node, coord.x, coord.y - 1, false );
            }
        }

        //Diagonal movement
        if ( allowDiagonal )
        {
            //Allocate memory for two nodes to check for walls
            GridNode node1;
            GridNode node2;

            if ( coord.x - 1 >= 0 )
            {
                //NorthWest, also check that North node and West node aren't walls
                node1 = (GridNode) gridChildren.get(NUMROWS * (coord.x-1) + coord.y);
                if ( coord.y - 1 >= 0 && node1.getType() != GridNode.WALL)
                {
                    node2 = (GridNode) gridChildren.get(NUMROWS * coord.x + (coord.y-1));
                    if(node2.getType() != GridNode.WALL)
                    {
                        if(dfs) {
                            neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x - 1) + coord.y - 1));
                        } else
                        {
                            addNeighborDijkstraAStar ( node, coord.x - 1, coord.y - 1, true );
                        }
                    }
                }
                //NorthEast
                if ( coord.y + 1 < NUMROWS && node1.getType() != GridNode.WALL)
                {
                    node2 = (GridNode) gridChildren.get(NUMROWS * coord.x + (coord.y + 1));
                    if(node2.getType() != GridNode.WALL)
                    {
                        if(dfs) {
                            neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x - 1) + coord.y + 1));
                        } else
                        {
                            addNeighborDijkstraAStar ( node, coord.x - 1, coord.y + 1, true );
                        }
                    }
                }
            }
            if ( coord.x + 1 < NUMCOLS )
            {
                //SouthWest, also check that South node and West node aren't walls
                node1 = (GridNode) gridChildren.get(NUMROWS * (coord.x+1) + coord.y);

                if ( coord.y - 1 >= 0 && node1.getType() != GridNode.WALL)
                {
                    node2 = (GridNode) gridChildren.get(NUMROWS * coord.x + (coord.y-1));
                    if(node2.getType() != GridNode.WALL)
                    {
                        if(dfs) {
                            neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x + 1) + coord.y - 1));
                        } else
                        {
                            addNeighborDijkstraAStar ( node, coord.x + 1, coord.y - 1, true );
                        }
                    }
                }
                //SouthEast
                if ( coord.y + 1 < NUMROWS && node1.getType() != GridNode.WALL)
                {
                    node2 = (GridNode) gridChildren.get(NUMROWS * coord.x + (coord.y + 1));
                    if(node2.getType() != GridNode.WALL)
                    {
                        if(dfs) {
                            neighbors.add((GridNode) gridChildren.get(NUMROWS * (coord.x + 1) + coord.y + 1));
                        } else
                        {
                            addNeighborDijkstraAStar ( node, coord.x + 1, coord.y + 1, true );
                        }
                    }
                }
            }
        }

        if(dfs)
        {
            addNeighborDFS ( neighbors, node );
        }
    }

    /**
     * This is a helper function for getNeighbors() where it will check if the neighbor node is valid and then adds
     * it to the PriorityQueue minHeap
     * @param node
     * @param x
     * @param y
     */
    private void addNeighborDijkstraAStar ( GridNode node, int x, int y, boolean diagonal)
    {
        boolean bfs = algorithm.equals ("Breadth First Search (BFS)");

        GridNode tmp = ( GridNode ) gridChildren.get ( NUMROWS * x + y );

        int nodeType = tmp.getType ();
        //Check that tmp node hasn't already been checked or is a wall
        if ( nodeType != GridNode.START && nodeType != GridNode.WALL && nodeType != GridNode.NORMALCHECKED && nodeType != GridNode.WEIGHTEDCHECKED && nodeType != GridNode.WEIGHTEDCHECKING && nodeType != GridNode.NORMALCHECKING )
        {
            //Set previous node
            tmp.setPrevious ( node );

            //Set color of neighbor node if it is weighted or normal, not if node is end node
                if ( nodeType == GridNode.WEIGHTED && !bfs)
                {
                    tmp.setType ( GridNode.WEIGHTEDCHECKING );
                }
                else if ( nodeType == GridNode.NORMAL )
                {
                    tmp.setType ( GridNode.NORMALCHECKING );
                }
                else
                {
                    tmp.setType ( GridNode.ENDCHECKING );
                    minHeap.clear();
                }

            int newDistanceFromStart = node.getDistanceFromStart();
            //Update distanceFromStart
            if(algorithm.equals("Dijkstra's")) {
                dijkstra(tmp, newDistanceFromStart, diagonal, false);
            } else if(algorithm.equals("A*") && !allowDiagonal) {
                aStarManhatten(tmp, newDistanceFromStart);
            } else if(algorithm.equals("A*")) {
                aStarDiagonal(tmp, newDistanceFromStart);
            } else if( bfs) {
                dijkstra(tmp, newDistanceFromStart, diagonal, true);
            }

            //Add neighbor node to minHeap
            minHeap.add ( tmp );
        }
    }

    private void addNeighborDFS(ArrayList<GridNode> nodes, GridNode previous) {
        Random rand = new Random();
        while(!nodes.isEmpty())
        {
            //Randomly get a node from this list
            GridNode tmp = nodes.remove(rand.nextInt(nodes.size()));

            //Cache nodeType
            int nodeType = tmp.getType ();

            //Check that node hasn't been checked or a wall
            if ( nodeType != GridNode.START && nodeType != GridNode.WALL && nodeType != GridNode.NORMALCHECKED && nodeType != GridNode.WEIGHTEDCHECKED && nodeType != GridNode.WEIGHTEDCHECKING && nodeType != GridNode.NORMALCHECKING )
            {

                //Set previous node
                tmp.setPrevious ( previous );

                /*Since Depth First Search is an unweighted search algorithm do not need to worry about weighted or
                  unweighted
                 */
                if ( nodeType == GridNode.END )
                {
                    tmp.setType ( GridNode.ENDCHECKING );
                    minHeap.clear ();
                }
                else
                {
                    tmp.setType ( GridNode.NORMALCHECKING );
                }

                //Update distanceFromStart
                tmp.setDistanceFromStart ( previous.getDistanceFromStart () + 1 );

                minHeap.add ( tmp );
            }
        }
    }

    /**
     * This function is used when user selects Dijkstra's algorithm and it will properly add the correct
     * distanceFromStart
     * @param node
     * @param previousDistanceFromStart
     */
    private void dijkstra(GridNode node, int previousDistanceFromStart, boolean diagonal, boolean bfs) {
        if(!bfs && node.getType() == GridNode.WEIGHTED) {
            if(diagonal) {
                node.setDistanceFromStart ( previousDistanceFromStart + (2 * costDiagonal) );
            } else
            {
                node.setDistanceFromStart ( previousDistanceFromStart + (3 * costStraight) );
            }
        } else {
            if(diagonal) {
                node.setDistanceFromStart ( previousDistanceFromStart + costDiagonal );
            } else
            {
                node.setDistanceFromStart ( previousDistanceFromStart + costStraight );
            }
        }
    }


    /**
     * This function is used when user selects A* algorithm/NO Diagonal and it will properly add the correct
     * distanceFromStart
     * @param node
     * @param previousDistanceFromStart
     */
    private void aStarManhatten(GridNode node, int previousDistanceFromStart) {
        Point coord = node.getCoord();
        //Get heuristic distance to endNode
        int heuristic = costStraight * (Math.abs(coord.x - endCoord.x) + Math.abs(coord.y - endCoord.y));

        if(node.getType() == GridNode.WEIGHTED) {
            node.setDistanceFromStart(previousDistanceFromStart + (3 * costStraight));
        } else {
            node.setDistanceFromStart(previousDistanceFromStart + costStraight);
        }
        //Set heuristic of node
        node.setHeuristic(heuristic);
    }

    /**
     * This function is used when user selects A* algorithm/ALLOW Diagonal and it will properly add the correct
     * distanceFromStart
     * @param node
     * @param previousDistanceFromStart
     */
    private void aStarDiagonal(GridNode node, int previousDistanceFromStart) {
        Point coord = node.getCoord();

        int dx = Math.abs(coord.x - endCoord.x);
        int dy = Math.abs(coord.y - endCoord.y);

        int heuristic = costStraight * (dx + dy) + (costDiagonal - (2 * costStraight)) * Math.min(dx, dy);

        if(node.getType () == GridNode.WEIGHTED) {
            node.setDistanceFromStart(previousDistanceFromStart + (3 * costStraight));
        } else {
            node.setDistanceFromStart ( previousDistanceFromStart + costStraight );
        }
        node.setHeuristic(heuristic);
    }



    /**
     * This recursive function backtracks from the endNode and changes the color of every node until it reaches the
     * startNode
     * @param endNode
     */
    private void showShortestPath(GridNode endNode) {
         if(endNode.getPrevious() == null) {    //If at startNode
             return;
         }
         if(!endNode.getCoord().equals(endCoord)) {      //If not endNode, change color of node to show path

             if(endNode.getType() == GridNode.WEIGHTEDCHECKED) {        //If weightedNode
                 endNode.setType(GridNode.SHORTESTWEIGHTED);
             } else {
                 endNode.setType(GridNode.SHORTESTNORMAL);
             }
         } else {
             //Change endNode back to bright red
             endNode.setType(GridNode.END);
         }
         showShortestPath(endNode.getPrevious());
    }


    /**
     * This function is used so that when the user closes the window and the algorithm is still running, algorithm is
     * stopped.
     */
    public void shutdown ()
    {
        stage.close();
        Platform.exit();
    }


    @FXML
    protected void openSettings (ActionEvent actionEvent) throws Exception
    {
        System.out.println ("Open settings window");
        FXMLLoader loader = new FXMLLoader (getClass ().getResource("Settings.fxml"));
        Parent root = loader.load();
        SettingsController settingsController = loader.getController ();
        Scene sceneSettings = new Scene(root, MainFX.SETTINGSWIDTH, MainFX.SETTINGSHEIGHT);
        Stage stageSettings = new Stage();
        stageSettings.initStyle ( StageStyle.UNDECORATED );
        stageSettings.setScene(sceneSettings);
        stageSettings.setTitle("Settings");
        stageSettings.getIcons ().add(new Image (getClass().getResourceAsStream ( "AppIcon.png" )));
        settingsController.sendStage(stageSettings);
        stageSettings.setOnCloseRequest (e -> {
            settingsController.shutdown ();
        });

        //Get current stage's position so that if application is on another window, new stage will be too
        double currentScreenX = stage.getX();
        double currentScreenY = stage.getY();

        //Iterate through all screens
        for(Screen screen : Screen.getScreens ()) {
            Rectangle2D screenBounds = screen.getBounds();

            //Cache dimensions of screen
            double minX = screenBounds.getMinX ();
            double minY = screenBounds.getMinY ();
            double maxX = screenBounds.getMaxX ();
            double maxY = screenBounds.getMaxY ();


            if(minX < currentScreenX && maxX > currentScreenX
                    && minY < currentScreenY && maxY > currentScreenY)
            {       //Current iterated screen is current screen with Visualizer stage

                //Set location of stageSettings to middle of current iterated screen
                stageSettings.setX(minX + ((maxX - minX) / 2) - (MainFX.SETTINGSWIDTH / 2));
                stageSettings.setY(minY + ((maxY - minY) / 2) - (MainFX.SETTINGSHEIGHT / 2));
            }

        }

        stageSettings.show();
    }


    @FXML
    protected void getWindowLocation (MouseEvent mouseEvent) {

        offsetX = stage.getX () - mouseEvent.getScreenX();
        offsetY = stage.getY() - mouseEvent.getScreenY();
    }

    @FXML
    protected void moveWindow (MouseEvent mouseEvent) {
        stage.setX(mouseEvent.getScreenX () + offsetX);
        stage.setY(mouseEvent.getScreenY () + offsetY);
    }

    @FXML
    protected void minimize (MouseEvent mouseEvent) {
        stage.setIconified ( true );
    }

    @FXML
    protected void maximize ( MouseEvent mouseEvent) {
        if(stage.isMaximized()) {       //If stage is already maximized
            //Set window size to previous stageHeight and stageWidth
            stage.setMaximized(false);
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);

            //Change icon back to rectangle
            menuMaximizeIcon.setContent("M 5 5 L 5 15 L 15 15 L 15 5 Z");
        } else {
            //Store previous stageHeight and stageWidth
            stageHeight = (int) stage.getHeight();
            stageWidth = (int) stage.getWidth();

            //Maximize window
            stage.setMaximized(true);

            menuMaximizeIcon.setContent("M 3 5 L 3 13 L 11 13 L 11 5 L 3 5 M 6 5 L 6 3 L 14 3 L 14 11 L 11 11");
        }

    }

    @FXML
    protected void close( MouseEvent mouseEvent) {
        stage.close();
        Platform.exit();
    }
}

