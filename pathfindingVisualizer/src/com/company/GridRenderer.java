package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

import static java.awt.Color.BLUE;

public class GridRenderer extends DefaultTableCellRenderer {

    public GridRenderer() {
    }



    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int col) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, col);

        TableModel table = jTable.getModel();

        Node cellNode = null;
        if(table.getValueAt(row, col) != null) {
            cellNode = (Node) table.getValueAt(row, col);
        }
            if(cellNode == null) {
                cell.setBackground(Color.WHITE);
            } else if (cellNode.isShortest()) {
                cell.setBackground(Color.ORANGE);
            } else if (cellNode.isStart()) {
                cell.setBackground(new Color(0, 100, 0));
            } else if (cellNode.isEnd()) {
                cell.setBackground(new Color(150, 0, 0));
            } else if (cellNode.isWall()) {
                cell.setBackground(Color.BLACK);
            } else if (cellNode.isWeighted()) {
                if(!cellNode.isVisited()) {
                    if(!cellNode.isNeighbor()) {
                        cell.setBackground(new Color(200, 99, 0));
                    } else {
                        cell.setBackground(new Color(95, 3, 255));
                    }
                } else {
                    cell.setBackground(new Color(155, 75, 255));

                }
            } else {
                if(cellNode.isNeighbor()){
//                    System.out.println("Visited: " + cellNode.isVisited());
                    if(cellNode.isVisited()) {
                        cell.setBackground(new Color(102, 255, 255));
                    } else {
                        cell.setBackground(Color.BLUE);
                    }
                } else {
                    cell.setBackground(Color.white);
                }
            }
            cell.setText("");


//        int cellVal = 0;
//        if(table.getValueAt(row, col) != null) {
//            cellVal = (int) table.getValueAt(row, col);
//        }
//        if (cellVal == 1) {
//                cell.setBackground(new Color(0, 100, 0));
//            } else if (cellVal == -1) {
//                cell.setBackground(new Color(150, 0, 0));
//            } else if (cellVal == 2) {
//                cell.setBackground(Color.BLACK);
//            } else if (cellVal == 3) {
//
//                cell.setBackground(new Color(200, 99, 0));
//            } else {
//                cell.setBackground(Color.WHITE);
//            }
//            cell.setText("");
//
        return cell;
    }
}
