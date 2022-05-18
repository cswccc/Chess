package view;

import controller.GameController;
import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private boolean checkData() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("src/data.txt"));
        StringBuilder s = new StringBuilder();

        int temp = in.read();
        while(temp != -1) {
            s.append((char)temp);
            temp = in.read();
        }
        return s.length() == 0;
    }

    private void loadGame() throws Exception {
        BufferedReader in = new BufferedReader(new FileReader("src/data.txt"));
        StringBuilder s = new StringBuilder();

        int temp = in.read();
        while(temp != -1) {
            s.append((char)temp);
            temp = in.read();
        }

        ArrayList<Chessboard> chessboards = new ArrayList<>();
        ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
        mainFrame.setVisible(true);

        int i = 0, j = 0, num = 0;
        int len = s.length();
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,mainFrame);

        for(int cnt = 0; cnt < len; cnt++) {
            chessboard.setChess(i,j,s.charAt(cnt));

            if(i == 7 && j == 7)  {
                num++;
                if(num % 2 == 0) chessboard.setCurrentColor(ChessColor.WHITE);
                else chessboard.setCurrentColor(ChessColor.BLACK);

                chessboards.add(chessboard);
                System.out.println(cnt);
                chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,mainFrame);
            }
            j = (j+1)%8;
            if(j == 0) i = (i+1)%8;
        }

        dispose();

        mainFrame.setChessboards(chessboards);
        mainFrame.changeChessboard();
    }

    private void LoadDialog() throws Exception {
        int loa = JOptionPane.showConfirmDialog(null,"Detecting that you have saved chess games, do you want to load them?","Save",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(loa == JOptionPane.YES_NO_OPTION) {
            loadGame();
        }
        else {
            dispose();
            ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
            mainFrame.setVisible(true);
        }
    }

    private void addPvPButton() {
        JButton PvPButton = new JButton("P v P");

        PvPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if(checkData()) {
                        dispose();
                        ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                        mainFrame.setVisible(true);
                    }
                    else {
                        LoadDialog();

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
                ChessGameFrame mainFrame = null;
                try {
                    mainFrame = new ChessGameFrame(1500, 1000);
                    mainFrame.setType(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
                ChessGameFrame mainFrame = null;
                try {
                    mainFrame = new ChessGameFrame(1500, 1000);
                    mainFrame.setType(2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
                ChessGameFrame mainFrame = null;
                try {
                    mainFrame = new ChessGameFrame(1500, 1000);
                    mainFrame.setType(3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mainFrame.setVisible(true);
            }
        });
        PvEButton.setLocation(HEIGTH / 2,HEIGTH / 2 + 150);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Difficult",Font.BOLD,20));
        add(PvEButton);
    }
}
