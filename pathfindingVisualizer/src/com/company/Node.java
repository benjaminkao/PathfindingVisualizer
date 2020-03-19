package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Node extends JButton {
    public int row;
    public int col;

    private int distanceFromStart;
    private boolean visited;
    private boolean wall = false;
    private boolean weighted = false;
    private Node previousNode;

    private boolean start;
    private boolean end;


    public Node(int row, int col) {
        setBackground(Color.WHITE);
//        addMouseListener(mouseListener);
        this.row = row;
        this.col = col;
        distanceFromStart = Integer.MAX_VALUE;
        visited = false;
    }


    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart() {
        start = true;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd() {
        end = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWeighted() {
        return weighted;
    }

    public void setWeighted(boolean weighted) {
        this.weighted = weighted;
    }

    public void resetNode() {
        distanceFromStart = Integer.MAX_VALUE;
        visited = false;
        start = false;
        end = false;
        previousNode = null;
        this.setBackground(Color.WHITE);
    }
}
