package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {

    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;

    public MainInterface(int width, int height) {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addPvPButton();
        addPvEButtonForEasy();
        addPvEButtonForMedium();
        addPvEButtonForDifficult();
    }

    private void addPvPButton() {
        JButton PvPButton = new JButton("P v P");

        PvPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                mainFrame.setVisible(true);
            }
        });
        PvPButton.setLocation(HEIGTH / 2,HEIGTH / 2);
        PvPButton.setSize(200,50);
        PvPButton.setFont(new Font("P v P",Font.BOLD,20));
        add(PvPButton);
    }

    private void addPvEButtonForEasy() {
        JButton PvEButton = new JButton("PvE For Easy");
        PvEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                mainFrame.setVisible(true);
            }
        });
        PvEButton.setLocation(HEIGTH / 2,HEIGTH / 2 + 50);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Easy",Font.BOLD,20));
        add(PvEButton);
    }

    private void addPvEButtonForMedium() {
        JButton PvEButton = new JButton("PvE For Medium");
        PvEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                mainFrame.setVisible(true);
            }
        });
        PvEButton.setLocation(HEIGTH / 2,HEIGTH / 2 + 100);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Medium",Font.BOLD,20));
        add(PvEButton);
    }

    private void addPvEButtonForDifficult() {
        JButton PvEButton = new JButton("PvE For Difficult");
        PvEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                mainFrame.setVisible(true);
            }
        });
        PvEButton.setLocation(HEIGTH / 2,HEIGTH / 2 + 150);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Difficult",Font.BOLD,20));
        add(PvEButton);
    }
}
