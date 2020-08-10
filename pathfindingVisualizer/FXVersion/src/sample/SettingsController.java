package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class SettingsController {
    @FXML
    protected SVGPath menuMaximizeIcon;
    @FXML
    protected VBox menuControls;
    @FXML
    protected Label textControls;
    @FXML
    protected Label textRestraints;

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
    }

    @FXML
    protected void showControls ( ActionEvent actionEvent )
    {
        menuControls.setVisible(true);
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
