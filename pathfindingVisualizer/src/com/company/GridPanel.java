package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class GridPanel {

    public DefaultTableModel grid;


    public GridPanel(int row, int col) {
        grid = new DefaultTableModel(row, col) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                grid.setValueAt(new Node(i, j), i, j);
            }
        }
    }

    public Node getNodeAt(int row, int col) {
        return (Node) grid.getValueAt(row, col);
    }


    public int getDistanceFromStartAt(int rowIndex, int columnIndex) {
        Node node = (Node) grid.getValueAt(rowIndex, columnIndex);

        return node.getDistanceFromStart();
    }

    public void changeDistanceFromStartAt(int distanceFromStart, int row, int col) {
        Node node = (Node) grid.getValueAt(row, col);

        node.setDistanceFromStart(distanceFromStart);

        grid.setValueAt(node, row, col);
    }

    /**
     * KEY:
     * 1 - start
     * -1 - end
     * 2 - wall
     * 3 - weighted
     */

    public void setNodeAs(int keyNum, int row, int col, boolean activate) {
        Node node = (Node) grid.getValueAt(row, col);

        switch(keyNum) {
            case 1:
                node.setStart(activate);
                node.setVisited(activate);
                break;
            case -1:
                node.setEnd(activate);
                break;
            case 2:
                node.setWall(activate);
                break;
            case 3:
                node.setWeighted(activate);
                break;
            default:
                System.out.println("Neighbor: " + node.isNeighbor());
                System.out.println("Visited: " + node.isVisited());
        }

    }

    public void nodeVisited(int row, int col) {
        Node tmp = ((Node) grid.getValueAt(row, col));
        tmp.setVisited(true);
    }

    public void setNodeAsNeighbor(int row, int col) {
        Node tmp = ((Node) grid.getValueAt(row, col));
        tmp.setNeighbor(true);
    }

}
