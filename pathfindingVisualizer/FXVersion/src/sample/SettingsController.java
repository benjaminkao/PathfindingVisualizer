package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class SettingsController {
    @FXML
    protected SVGPath menuMaximizeIcon;
    @FXML
    protected VBox menuControls;
    @FXML
    protected VBox menuAlgorithms;
    @FXML
    protected Label textControls;
    @FXML
    protected Label textRestraints;
    @FXML
    protected Label textAlgorithms;
    @FXML
    protected ToggleButton controlsBtn;
    @FXML
    protected ToggleButton algorithmsBtn;
    @FXML
    protected BorderPane root;

    private Stage stage;
    private double offsetX;
    private double offsetY;
    private double stageHeight;
    private double stageWidth;


    public void sendStage ( Stage stage )
    {
        this.stage = stage;
        stageHeight = stage.getHeight ();
        stageWidth = stage.getWidth ();
        WindowResizeHelper.addResizeListener ( stage );
    }

    public void shutdown ()
    {
        stage.close ();
    }


    @FXML
    protected void initialize() {
        textControls.setText("Left Click (& Drag) - Sets whatever cell that your mouse goes over as the currently " +
                "chosen node type.\n\nRight Click (& Drag) - Resets any cells that your mouse goes over.");
        textRestraints.setText("- Only one Start node allowed\n\n- Only one End node allowed");
        textAlgorithms.setText("Unweighted Algorithms:\n\n\nBreadth First Search (BFS) - Searches every " +
                "single node around the start node until it finds the end node. Nodes to search first are " +
                "determined by how close they are to the start. Similar to Dijkstra's algorithm " +
                "but unweighted. This algorithm guarantees the " +
                "shortest path.\n\nDepth First Search (DFS) - Starts searching at start node and will randomly search" +
                " one of the nodes neighbors and continues to randomly search one of the current nodes neighbors " +
                "until no current node neighbors are left. Then, will backtrack to last previous node with an " +
                "unsearched neighbor and continue until it finds the end node. This algorithm does not guarantee the " +
                "shortest path and it will find a different path every time it is ran.\n\n\nWeighted " +
                "Algorithms:\n\n\nA* - Searches the node with the shortest distance from the start node as well as " +
                "the smallest distance from the end node heuristic. This algorithm should give the shortest path." +
                ".\n\nDijsktra's - Searches every single node around the start node until it finds the end node. " +
                "Supports weighted nodes. Nodes to search first are determined by how close they are to the start " +
                "while also considering weighted nodes. This algorithm guarantees the shortest path.");
        menuAlgorithms.getStyleClass ().add("settings-unselected");
        controlsBtn.setSelected ( true );
        root.setCenter(menuControls);
    }

    @FXML
    protected void showControls ( ActionEvent actionEvent )
    {
        menuAlgorithms.setVisible(false);
        menuControls.setVisible(true);
        controlsBtn.setSelected ( true );
        algorithmsBtn.setSelected(false);
        menuAlgorithms.getStyleClass ().add("settings-unselected");
        menuControls.getStyleClass ().remove("settings-unselected");
        root.setCenter(menuControls);
    }

    @FXML
    protected void showAlgorithms (ActionEvent actionEvent) {
        menuControls.setVisible(false);
        menuAlgorithms.setVisible(true);
        algorithmsBtn.setSelected ( true );
        controlsBtn.setSelected ( false );
        menuControls.getStyleClass ().add("settings-unselected");
        menuAlgorithms.getStyleClass ().remove("settings-unselected");
        root.setCenter(menuAlgorithms);
    }

    @FXML
    protected void getWindowLocation ( MouseEvent mouseEvent )
    {

        offsetX = stage.getX () - mouseEvent.getScreenX ();
        offsetY = stage.getY () - mouseEvent.getScreenY ();
    }

    @FXML
    protected void moveWindow ( MouseEvent mouseEvent )
    {
        stage.setX ( mouseEvent.getScreenX () + offsetX );
        stage.setY ( mouseEvent.getScreenY () + offsetY );
    }

    @FXML
    protected void maximize ( MouseEvent mouseEvent )
    {
        if ( stage.isMaximized () )
        {       //If stage is already maximized
            //Set window size to previous stageHeight and stageWidth
            stage.setMaximized ( false );
            stage.setHeight ( stageHeight );
            stage.setWidth ( stageWidth );

            //Change icon back to rectangle
            menuMaximizeIcon.setContent ( "M 5 5 L 5 15 L 15 15 L 15 5 Z" );
        }
        else
        {
            //Store previous stageHeight and stageWidth
            stageHeight = ( int ) stage.getHeight ();
            stageWidth = ( int ) stage.getWidth ();

            //Maximize window
            stage.setMaximized ( true );

            menuMaximizeIcon.setContent ( "M 3 5 L 3 13 L 11 13 L 11 5 L 3 5 M 6 5 L 6 3 L 14 3 L 14 11 L 11 11" );
        }

    }

    @FXML
    protected void close ( MouseEvent mouseEvent )
    {
        stage.close ();
    }
}
