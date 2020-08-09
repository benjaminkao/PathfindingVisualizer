package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainFX extends Application {

    public static final int WINHEIGHT = 800;
    public static final int WINWIDTH = 1000;
    public static final int NODEWIDTH = 15;
    public static final int MINHEIGHT = 400;
    public static final int MINWIDTH = 500;

    public static void main ( String[] args )
    {
        launch ( args );
    }

    @Override
    public void start ( Stage primaryStage ) throws Exception
    {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader (getClass().getResource( "Visualizer.fxml" ));
        Parent root = loader.load();

        Controller controller = loader.getController();

        Scene scene = new Scene(root, WINWIDTH, WINHEIGHT);
        primaryStage.setMinHeight(MINHEIGHT);
        primaryStage.setMinWidth(MINWIDTH);
        primaryStage.setTitle ( "Pathfinding Visualizer" );
        primaryStage.setScene ( scene );
        primaryStage.setOnCloseRequest(e -> {
           controller.shutdown();
           Platform.exit();
        });
        primaryStage.show ();
        controller.sendStage(primaryStage);
    }
}
