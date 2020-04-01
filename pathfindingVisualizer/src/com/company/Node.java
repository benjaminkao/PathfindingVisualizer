package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Node {
    private int distanceFromStart = Integer.MAX_VALUE;
    private Node previousNode = null;

    private boolean start = false;
    private boolean end = false;
    private boolean wall = false;
    private boolean weighted = false;

    private int row;
    private int col;
    private boolean visited = false;
    private boolean neighbor = false;

    private boolean shortest = false;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Node(int keyNum, int row, int col) {
        switch(keyNum) {
            case 1:
                this.setStart(true);
                break;
            case -1:
                this.setEnd(true);
                break;
            case 2:
                this.setWall(true);
                break;
            case 3:
                this.setWeighted(true);
                break;
            default:
                System.out.println("Invalid keyNum");
        }

        this.row = row;
        this.col = col;
    }

    public boolean isShortest() {
        return this.shortest;
    }

    public void setShortest(boolean shortest) {
        this.shortest = shortest;
    }

    public boolean isNeighbor() {
        return this.neighbor;
    }

    public void setNeighbor(boolean neighbor) {
        this.neighbor = neighbor;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getPreviousNode() {
        return this.previousNode;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return this.start;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isEnd() {
        return this.end;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isWall() {
        return this.wall;
    }

    public void setWeighted(boolean weighted) {
        this.weighted = weighted;
    }

    public boolean isWeighted() {
        return this.weighted;
    }
}
