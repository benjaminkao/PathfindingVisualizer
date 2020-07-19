package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GridNode extends StackPane {

    public static final int NORMAL = -1, START = 0, END = 1, WALL = 2, WEIGHTED = 3, NORMALCHECKED = 4,
            WEIGHTEDCHECKED = 5;

    public final Background WHITE = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                                                       Insets.EMPTY));
    public final Background GREEN = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));
    public final Background RED = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
    public final Background BLACK = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    public final Background ORANGE = new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY));
    public final Background BLUE = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
    public final Background PURPLE = new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY));

    /**
     * This variable determines what kind of node this will be.
     * -1: normal, reset
     * 0: startNode
     * 1: endNode
     * 2: wallNode
     * 3: weightedNode
     */
    private int type;

    public GridNode(int type) {
        this.type = type;
        updateColor();
    }

    public void setType(int type) {
        this.type = type;
        updateColor();
    }

    public int getType() {
        return this.type;
    }

    private void updateColor() {
        switch (this.type) {
            case -1:
                this.setBackground(WHITE);
                break;
            case 0:
                this.setBackground(GREEN);
                break;
            case 1:
                this.setBackground(RED);
                break;
            case 2:
                this.setBackground(BLACK);
                break;
            case 3:
                this.setBackground(ORANGE);
                break;
            case 4:
                this.setBackground(BLUE);
                break;
            case 5:
                this.setBackground(PURPLE);
                break;
            default:
                System.out.println("NODE TYPE ERROR: Invalid node type specifier");
                this.setBackground(WHITE);
        }

    }


}
