package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class GridNode extends StackPane {

    public static final int NORMAL = -1, START = 0, END = 1, WALL = 2, WEIGHTED = 3, NORMALCHECKED = 4,
            WEIGHTEDCHECKED = 5, NORMALCHECKING = 6, WEIGHTEDCHECKING = 7, ENDCHECKING = 8, SHORTESTNORMAL = 9,
            SHORTESTWEIGHTED = 10;

    public final Insets nodeInsets = new Insets ( 1 );
    public final CornerRadii nodeRadii = new CornerRadii(3);

    public final Background WHITE = new Background ( new BackgroundFill ( Color.WHITE, nodeRadii, nodeInsets ) );
    public final Background GREEN = new Background ( new BackgroundFill ( Color.GREEN, nodeRadii, nodeInsets ) );
    public final Background DEEPPINK = new Background ( new BackgroundFill ( Color.DEEPPINK, nodeRadii, nodeInsets ) );
    public final Background BLACK = new Background ( new BackgroundFill ( Color.BLACK, nodeRadii, nodeInsets ) );
    public final Background ORANGE = new Background ( new BackgroundFill ( Color.ORANGE, nodeRadii, nodeInsets ) );
    public final Background BLUE = new Background ( new BackgroundFill ( Color.BLUE, nodeRadii, nodeInsets ) );
    public final Background LIGHTBLUE = new Background ( new BackgroundFill ( Color.LIGHTBLUE, nodeRadii,
            nodeInsets ) );
    public final Background PINK = new Background ( new BackgroundFill ( Color.PINK, nodeRadii, nodeInsets ) );
    public final Background PURPLE = new Background ( new BackgroundFill ( Color.MEDIUMPURPLE, nodeRadii, nodeInsets ) );
    public final Background MAGENTA = new Background ( new BackgroundFill ( Color.MAGENTA, nodeRadii, nodeInsets ) );
    public final Background DARKRED = new Background ( new BackgroundFill ( Color.DARKRED, nodeRadii, nodeInsets ) );
    public final Background CRIMSON = new Background(new BackgroundFill(Color.CRIMSON, nodeRadii, nodeInsets));

    private Point coord = new Point ( -1, -1 );

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
                this.setBackground ( WHITE );
                break;
            case 0:
                this.setBackground ( GREEN );
                break;
            case 1:
                this.setBackground ( CRIMSON );
                break;
            case 2:
                this.setBackground ( BLACK );
                break;
            case 3:
                this.setBackground ( ORANGE );
                break;
            case 4:
                this.setBackground ( LIGHTBLUE );
                break;
            case 5:
                this.setBackground ( PURPLE );
                break;
            case 6:
                this.setBackground ( BLUE );
                break;
            case 7:
                this.setBackground ( MAGENTA );
                break;
            case 8:
                this.setBackground ( DARKRED );
                break;
            case 9:
                this.setBackground ( PINK );
                break;
            case 10:
                this.setBackground(DEEPPINK);
                break;
            default:
                System.out.println ( "NODE TYPE ERROR: Invalid node type specifier" );
                this.setBackground ( WHITE );
        }
    }


}
