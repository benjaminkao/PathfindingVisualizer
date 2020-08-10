package sample;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowResizeHelper {

    public static void addResizeListener(Stage stage) {
        ResizeListener resizeListener = new ResizeListener ( stage );
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
    }

    private static class ResizeListener implements EventHandler<MouseEvent> {
        private Stage stage;
        private Cursor cursorType = Cursor.DEFAULT;
        private double startX = 0;
        private double startY = 0;
        private double startScreenX = 0;
        private double startScreenY = 0;

        public ResizeListener ( Stage stage )
        {
            this.stage = stage;
        }

        @Override
        public void handle ( MouseEvent mouseEvent )
        {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType ();
            Scene scene = stage.getScene ();

            //Buffer of resize
            int border = 4;

            //Get location of mouseEvent
            double mouseEventX = mouseEvent.getSceneX ();
            double mouseEventY = mouseEvent.getSceneY ();

            //Get current dimensions of scene
            double sceneWidth = scene.getWidth ();
            double sceneHeight = scene.getHeight ();

            //If mouse moved
            if ( MouseEvent.MOUSE_MOVED.equals ( mouseEventType ) )
            {

                if ( mouseEventX < border && mouseEventY < border )
                {      //If mouse is at top left corner
                    //Change cursor type
                    cursorType = Cursor.NW_RESIZE;
                }
                else if ( mouseEventX < border && mouseEventY > sceneHeight - border )
                {     //If mouse is at bottom left corner
                    cursorType = Cursor.SW_RESIZE;
                }
                else if ( mouseEventX > sceneWidth - border && mouseEventY < border )
                {       //If mouse is at top right corner
                    cursorType = Cursor.NE_RESIZE;
                }
                else if ( mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border )
                {       //If mouse is at bottom right corner
                    cursorType = Cursor.SE_RESIZE;
                }
                else if ( mouseEventX < border )
                {       //If mouse is on left side of window
                    cursorType = Cursor.W_RESIZE;
                }
                else if ( mouseEventX > sceneWidth - border )
                {       //If mouse is on right side of window
                    cursorType = Cursor.E_RESIZE;
                }
                else if ( mouseEventY < border )
                {       //If mouse is on top side of window
                    cursorType = Cursor.N_RESIZE;
                }
                else if ( mouseEventY > sceneHeight - border )
                {       //If mouse is on bottom side of window
                    cursorType = Cursor.S_RESIZE;
                }
                else
                {      //If mouse is not on any window edge
                    cursorType = Cursor.DEFAULT;
                }
                scene.setCursor(cursorType);
            }
            else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType))
            {
                scene.setCursor(Cursor.DEFAULT);
            }
            else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType))
            {
                startX = stage.getWidth() - mouseEventX;
                startY = stage.getHeight () - mouseEventY;
            }
            else if (MouseEvent.MOUSE_DRAGGED.equals ( mouseEventType ))
            {
                if(!Cursor.DEFAULT.equals(cursorType))
                {       //Check that cursor isn't on any window edge
                    if(!Cursor.W_RESIZE.equals(cursorType) && !Cursor.E_RESIZE.equals(cursorType))
                    {       //Check that resize is occurring only for height
                        if(Cursor.NW_RESIZE.equals(cursorType) || Cursor.N_RESIZE.equals(cursorType) || Cursor.NE_RESIZE.equals(cursorType))
                        {   //Check that resize event occurring on north edge of window, if so, need to resize

                            if(stage.getHeight () > stage.getMinHeight () || mouseEventY < 0)
                            {   //Check that resize event isn't making window too small, or mouse isn't going past
                                // screen bounds

                                stage.setHeight ( stage.getY () - mouseEvent.getScreenY () + stage.getHeight () );

                                stage.setY(mouseEvent.getScreenY());

                            }
                        } else
                        {   //If resize event is occuring on south edges, no need to move location of screen to
                            // accommodate window resize
                            if(stage.getHeight() > stage.getMinHeight () || mouseEventY + startY - stage.getHeight () > 0)
                            {
                                setStageHeight ( mouseEventY + startY );
                            }
                        }
                    }
                    if(!Cursor.N_RESIZE.equals(cursorType) && !Cursor.S_RESIZE.equals(cursorType))
                    {       //Check that resize is occurring only for width

                        if(Cursor.NW_RESIZE.equals(cursorType) || Cursor.W_RESIZE.equals(cursorType) || Cursor.SW_RESIZE.equals(cursorType))
                        {   //Check that resize event is also affecting window position if on west side of window

                            if(stage.getWidth() > stage.getMinWidth() || mouseEventX < 0)
                            {   //Check that resize event isn't making window too small
                                stage.setWidth(stage.getX () - mouseEvent.getScreenX () + stage.getWidth());
                                stage.setX(mouseEvent.getScreenX());
                            }
                        } else
                        {
                            if(stage.getWidth () > stage.getMinWidth () || mouseEventX + startX - stage.getWidth() > 0)
                            {
                                setStageWidth(mouseEventX + startX);
                            }
                        }

                    }


                }
            }
        }

        private void setStageHeight (double height) {
            height = Math.max(height, stage.getMinHeight ());
            stage.setHeight(height);
        }

        private void setStageWidth (double width) {
            width = Math.max(width, stage.getMinWidth());
            stage.setWidth(width);
        }
    }
}
