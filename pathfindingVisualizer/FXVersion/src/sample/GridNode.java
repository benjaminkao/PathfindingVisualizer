package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class GridNode extends StackPane {

    public static final int NORMAL = -1, START = 0, END = 1, WALL = 2, WEIGHTED = 3, NORMALCHECKED = 4,
            WEIGHTEDCHECKED = 5, NORMALCHECKING = 6, WEIGHTEDCHECKING = 7, ENDCHECKING = 8, SHORTESTNORMAL = 9,
            SHORTESTWEIGHTED = 10;

    private Point coord = new Point ( -1, -1 );

    public final String WHITE = "3f4851";
    public final String GREEN = "00FF00";
    public final String CRIMSON = Color.CRIMSON.toString().substring(2);
    public final String BLACK = "090E18";
    public final String ORANGE = Color.ORANGE.toString().substring(2);
    public final String LIGHTBLUE = Color.LIGHTBLUE.toString().substring(2);
    public final String PURPLE = Color.PURPLE.toString().substring(2);
    public final String BLUE = "0e9dd9";
    public final String MAGENTA = Color.MAGENTA.toString().substring(2);
    public final String DARKRED = Color.DARKRED.toString().substring(2);
    public final String PINK = Color.PINK.toString().substring(2);
    public final String DEEPPINK = Color.DEEPPINK.toString().substring(2);





    //This variable will be used in the minHeap to sort by ascending distance
    private int distanceFromStart = Integer.MAX_VALUE;
    private int heuristic = Integer.MAX_VALUE;

    //This variable will be used to backtrack when getting the shortest path
    private GridNode previous = null;

    /**
     * This variable determines what kind of node this will be.
     * -1: normal, reset
     * 0: startNode
     * 1: endNode
     * 2: wallNode
     * 3: weightedNode
     */
    private int type;

    public GridNode ( int type, int x, int y )
    {
        this.getStyleClass().add("grid-node");
        this.type = type;
        this.coord.setLocation ( x, y );
        updateColor ();
    }

    public int getType ()
    {
        return this.type;
    }

    public void setType ( int type )
    {
        this.type = type;
        updateColor ();
    }

    public Point getCoord ()
    {
        return this.coord;
    }

    public void setCoord ( Point coord )
    {
        this.coord.setLocation ( coord );
    }

    public void setCoord ( int x, int y )
    {
        this.coord.setLocation ( x, y );
    }

    public int getDistanceFromStart ()
    {
        return this.distanceFromStart;
    }

    public void setDistanceFromStart ( int distanceFromStart )
    {
        this.distanceFromStart = distanceFromStart;
    }

    public int getHeuristic() {
        return this.heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public GridNode getPrevious ()
    {
        return this.previous;
    }

    public void setPrevious ( GridNode previous )
    {
        this.previous = previous;
    }

    private void updateColor ()
    {
        switch (this.type)
        {
            case -1:
                this.setBackgroundColor ( WHITE );
                break;
            case 0:
                this.setBackgroundColor ( GREEN );
                break;
            case 1:
                this.setBackgroundColor ( CRIMSON );
                break;
            case 2:
                this.setBackgroundColor ( BLACK );
                break;
            case 3:
                this.setBackgroundColor ( ORANGE );
                break;
            case 4:
                this.setBackgroundColor ( LIGHTBLUE );
                break;
            case 5:
                this.setBackgroundColor ( PURPLE );
                break;
            case 6:
                this.setBackgroundColor ( BLUE );
                break;
            case 7:
                this.setBackgroundColor ( MAGENTA );
                break;
            case 8:
                this.setBackgroundColor ( DARKRED );
                break;
            case 9:
                this.setBackgroundColor ( PINK );
                break;
            case 10:
                this.setBackgroundColor (DEEPPINK);
                break;
            default:
                System.out.println ( "NODE TYPE ERROR: Invalid node type specifier" );
                this.setBackgroundColor ( WHITE );
        }
    }

    private void setBackgroundColor(String string) {
        this.setStyle("-fx-background-color: #" + string);
    }

}
