package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static final int WINHEIGHT = 800;
    public static final int WINWIDTH = 800;
    public static final int NODEWIDTH = 15;

    public static void main ( String[] args )
    {
        launch ( args );
    }

    @Override
    public void start ( Stage primaryStage ) throws Exception
    {
        FXMLLoader loader = new FXMLLoader (getClass().getResource( "Visualizer.fxml" ));
        Parent root = loader.load();

        Controller controller = loader.getController();

        Scene scene = new Scene(root, WINHEIGHT, WINWIDTH);
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
