package com.company;

import java.awt.*;
import javax.swing.*;


public class Grid extends JFrame{


    public Grid() {
        DijkstraPanel dijkstraPanel = new DijkstraPanel();

        add(dijkstraPanel);

        setResizable(false);
        setTitle("Dijkstra's Algorithm");
//        setLayout(new FlowLayout(FlowLayout.CENTER, 0, DijkstraPanel.HEIGHT / 2 - 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DijkstraPanel.WIDTH, DijkstraPanel.HEIGHT);
        setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        Grid grid = new Grid();

        grid.setVisible(true);
    }
}
