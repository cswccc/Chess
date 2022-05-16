package view;

import SaveTheChess.Save;
import controller.GameController;
import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private JLabel currentPlayer = new JLabel("Black");
    private boolean flag = false;
    private Chessboard chessboard;
    private ArrayList<Chessboard> chessboards = new ArrayList<>();
    private int cnt;

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        currentPlayer.setLocation(HEIGTH + 100, HEIGTH / 10);
        currentPlayer.setSize(200, 60);
        currentPlayer.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(currentPlayer);

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addLabel();
        addHelloButton();
        addBackToInterfaceButton();
        addRestartGameButton();
        addSaveButton();
        addRepentanceButton();
    }
    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);

        add(chessboard);
        chessboards.add(new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,chessboard.getChessComponents(),this,chessboard.getCurrentColor()));
    }

    public void changeChessboard() {

        cnt++;
        int Siz = chessboards.size();
        remove(chessboard);
        chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,chessboards.get(Siz - 1).getChessComponents(),this,chessboards.get(Siz - 1).getCurrentColor());
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        chessboard.initiateChessboard();

        add(chessboard);
        chessboard.repaint();

        addCurrentPlayer();
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        JLabel statusLabel = new JLabel("Current Player:");
        statusLabel.setLocation(HEIGTH - 20, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(statusLabel);
    }

    public void setChessboards(ArrayList<Chessboard> chessboards) {
        for(Chessboard chessboard : chessboards) {
            chessboard.setChessGameFrame(this);
            this.chessboards.add(chessboard);
        }
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(HEIGTH, HEIGTH / 10 + 70);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void SaveDialog(Save save) throws Exception {
        int sav = JOptionPane.showConfirmDialog(null,"Are you sure to save the chessboard and exit?","Save",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(sav == JOptionPane.YES_NO_OPTION) {
            save.SaveTheData();
            dispose();
        }
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 140);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            Save save = new Save(chessboards);

            try {
                SaveDialog(save);
                save.SaveTheData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dispose();
        });
    }

    private void addBackToInterfaceButton() {
        JButton button = new JButton("Back To Interface");
        button.setLocation(HEIGTH -25, HEIGTH / 10 + 210);
        button.setSize(250, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainInterface mainInterface = new MainInterface(1500,1000);
                mainInterface.setVisible(true);
            }
        });
    }

    private void addRestartGameButton() {
        JButton button = new JButton("Restart Game");
        button.setLocation(HEIGTH -25, HEIGTH / 10 + 280);
        button.setSize(250, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                mainFrame.setVisible(true);
            }
        });
    }

    private void addRepentanceButton() {
        JButton button = new JButton("Repentance");
        button.setLocation(HEIGTH -25, HEIGTH / 10 + 350);
        button.setSize(250, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Siz = chessboards.size();
                chessboards.remove(Siz - 1);
                changeChessboard();
            }
        });
    }

    public void addCurrentPlayer() {
        add(currentPlayer);
        if(chessboard.getCurrentColor() == ChessColor.WHITE) {
            currentPlayer.setText("White");
        }
        else {
            currentPlayer.setText("Black");
        }
    }

    public void addChessboards(Chessboard chessboard) {
        chessboards.add(chessboard);
    }
}
