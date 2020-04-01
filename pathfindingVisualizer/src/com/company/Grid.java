package com.company;

import java.io.IOException;
import javax.swing.*;


public class Grid extends JFrame{


    public Grid() throws IOException {
//        setContentPane(new OldDijkstraPanel());

        DijkstraPanel dijkstraPanel = new DijkstraPanel();
        setContentPane(new DijkstraPanel().panel1);
        setResizable(true);
        setTitle("Dijkstra's Algorithm");
//        setLayout(new FlowLayout(FlowLayout.CENTER, 0, DijkstraPanel.HEIGHT / 2 - 100));
//        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(OldDijkstraPanel.WIDTH, OldDijkstraPanel.HEIGHT);
        setLocationRelativeTo(null);
        pack();
    }


    public static void main(String[] args) {
        Grid grid = null;
        try {
            grid = new Grid();
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.setVisible(true);
    }
}
