package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class Controller {

    //All FXML objects
    @FXML
    GridPane grid;
    @FXML
    GridPane buttonGrid;
    @FXML
    Button setStartBtn;
    @FXML
    Button setEndBtn;
    @FXML
    Button setWallBtn;
    @FXML
    Button setWeightedBtn;

    //All variables
    public int currentNodeType = GridNode.NORMAL;

    @FXML
    protected void initialize() {
        initializeGrid();
    }


    /**
     * This function initializes the grid
     */
    private void initializeGrid() {
        //Grab grid's columns and rows
        List<ColumnConstraints> columns = grid.getColumnConstraints();
        List<RowConstraints> rows = grid.getRowConstraints();


        //Add column and row constraints for grid
        for(int i = 0; i < MainFX.NUMCOLS; i++) {
            columns.add(new ColumnConstraints(MainFX.NODEWIDTH));
            rows.add(new RowConstraints(MainFX.NODEWIDTH));
        }

        //Grab grid's children
        List<Node> gridChildren = grid.getChildren();

        //Add a node to each cell of the grid, these will be used in the pathfinding algorithms
        for(int i = 0; i < MainFX.NUMCOLS; i++) {
            for(int j = 0; j < MainFX.NUMROWS; j++) {
                addNode(GridNode.NORMAL, i, j);
            }
        }

        grid.setGridLinesVisible(true);
        grid.requestFocus();
    }

    /**
     * This function creates a new node, adds event handlers, and finally adds the node to the grid
     * @param type
     * @param row
     * @param col
     */
    private void addNode(int type, int row, int col) {
        GridNode node = new GridNode(type);
        //On mouse click
        node.setOnMouseClicked(event -> {
            node.setType(currentNodeType);
        });

        //If user starts to drag mouse, enable mouse drag
        node.setOnDragDetected(event -> {
            node.startFullDrag();
        });

        //Update node type on mouse drag
        node.setOnMouseDragEntered(event -> {
            node.setType(currentNodeType);
        });

        grid.add(node, col, row);
    }

    @FXML
    protected void changeNodeType(ActionEvent actionEvent) {
        Button buttonPressed = (Button) actionEvent.getSource();
        String buttonType = buttonPressed.getText();

        //Change currentNodeType to user selected node type
        if(buttonType.equals("Set Start")) {
            currentNodeType = GridNode.START;
        } else if(buttonType.equals("Set End")) {
            currentNodeType = GridNode.END;
        } else if(buttonType.equals("Set Wall")) {
            currentNodeType = GridNode.WALL;
        } else if(buttonType.equals("Set Weighted")) {
            currentNodeType = GridNode.WEIGHTED;
        }

        //Enable all buttons
        setStartBtn.setDisable(false);
        setEndBtn.setDisable(false);
        setWallBtn.setDisable(false);
        setWeightedBtn.setDisable(false);

        //Disable button that was clicked
        buttonPressed.setDisable(true);

        //Give focus back to grid so that user doesn't need to press on grid again before nodes can update from mouse
        // events
        grid.requestFocus();
    }
}
