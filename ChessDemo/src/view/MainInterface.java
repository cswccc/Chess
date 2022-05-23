package view;

import controller.GameController;
import model.ChessColor;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainInterface extends JFrame {

    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private ImageIcon backGround;
    private JPanel imagePanel;

    public MainInterface(int width, int height) throws IOException {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addBackGround();
        addPvPButton();
        addPvEButtonForEasy();
        addPvEButtonForMedium();
    }

    private boolean checkData(int type) throws IOException {//检测是否有保存棋盘
        BufferedReader in;
        if(type == 0)  in = new BufferedReader(new FileReader("src/data.txt"));
        else if(type == 1) in = new BufferedReader(new FileReader("src/data1.txt"));
        else in = new BufferedReader(new FileReader("src/data2.txt"));
        StringBuilder s = new StringBuilder();

        int temp = in.read();
        while(temp != -1) {
            s.append((char)temp);
            temp = in.read();
        }
        return s.length() == 0;
    }

    private void loadGame(int type) throws Exception {//加载游戏
        BufferedReader in;
        if(type == 0)  in = new BufferedReader(new FileReader("src/data.txt"));
        else if(type == 1) in = new BufferedReader(new FileReader("src/data1.txt"));
        else in = new BufferedReader(new FileReader("src/data2.txt"));
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
        if(type != 0) len--;
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

        ChessColor color = ChessColor.NONE;
        if(type != 0) color = s.charAt(len) == 'B' ? ChessColor.BLACK : ChessColor.WHITE;

        mainFrame.setChessboards(chessboards);
        mainFrame.showChessboard();
        mainFrame.setType(type);
        mainFrame.setComputerColor(color);
    }

    private void LoadDialog(int type) throws Exception {//加载对话,选择是否加载
        int loa = JOptionPane.showConfirmDialog(null,"Detecting that you have saved chess games, do you want to load them?","Save",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(loa == JOptionPane.YES_NO_OPTION) {
            loadGame(type);
        }
        else {
            dispose();
            ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
            mainFrame.setType(type);
            if(type != 0) {
                ChooseColorDialog(mainFrame);
            }
            mainFrame.setVisible(true);
        }
    }

    private void addBackGround() throws IOException {
        backGround = new ImageIcon("images/graph(1).jpg");

        Image image = backGround.getImage();
        Image newImage = image.getScaledInstance(1500,1000,2);

        backGround.setImage(newImage);

        JLabel label = new JLabel(backGround);
        label.setBounds(0,0,backGround.getIconWidth(),backGround.getIconHeight());

        setLocationRelativeTo(null);//窗口居中
        getContentPane().setLayout(null);//设置成无布局

        JPanel panel = (JPanel) getContentPane();//注意内容面板必须强转为JPanel才可以实现下面的设置透明
        panel.setOpaque(false);//将内容面板设为透明

        getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));//标签添加到层面板
    }

    private void addPvPButton() {//人人
        JButton PvPButton = new JButton("P v P");

        PvPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
//                    playBGM();
                    if(checkData(0)) {
                        dispose();
                        ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                        mainFrame.setType(0);
                        mainFrame.setVisible(true);
                    }
                    else {
                        LoadDialog(0);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        PvPButton.setLocation(HEIGTH / 2 + 100,HEIGTH / 2);
        PvPButton.setSize(200,50);
        PvPButton.setFont(new Font("P v P",Font.BOLD,20));
        add(PvPButton);
    }

    private void ChooseColorDialog(ChessGameFrame mainFrame) {//人机简易
        int loa = JOptionPane.showConfirmDialog(null," Would you like to play chess first?","Choose",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(loa == JOptionPane.YES_NO_OPTION) {
            mainFrame.setComputerColor(ChessColor.WHITE);
        }
        else {
            mainFrame.setComputerColor(ChessColor.BLACK);
        }
    }

    private void addPvEButtonForEasy() {
        JButton PvEButton = new JButton("PvE For Easy");
        PvEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    playBGM();

                    if(checkData(1)) {
                        dispose();
                        ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                        mainFrame.setType(1);
                        ChooseColorDialog(mainFrame);
                        mainFrame.setVisible(true);
                    }
                    else {
                        LoadDialog(1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        PvEButton.setLocation(HEIGTH / 2 + 100,HEIGTH / 2 + 60);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Easy",Font.BOLD,20));
        add(PvEButton);
    }

    private void addPvEButtonForMedium() {
        JButton PvEButton = new JButton("PvE For Medium");
        PvEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playBGM();

                    if(checkData(2)) {
                        dispose();
                        ChessGameFrame mainFrame = new ChessGameFrame(1500, 1000);
                        mainFrame.setType(2);
                        ChooseColorDialog(mainFrame);
                        mainFrame.setVisible(true);
                    }
                    else {
                        LoadDialog(2);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        PvEButton.setLocation(HEIGTH / 2 + 100,HEIGTH / 2 + 120);
        PvEButton.setSize(200,50);
        PvEButton.setFont(new Font("PvE For Medium",Font.BOLD,20));
        add(PvEButton);
    }

    private void playBGM() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("BGM/StartPlayButton.WAV"));
        AudioFormat aif = ais.getFormat();

        final SourceDataLine sdl;

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);

        sdl = (SourceDataLine) AudioSystem.getLine(info);
        sdl.open(aif);
        sdl.start();

        FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);

        double value = 2;
        float db = (float) (Math.log(value == 0.0f ? 0.0001f : value) / Math.log(10.0) * 20.0);

        fc.setValue(db);

        int nByte = 0;
        final int SIZE= 1024*64;
        byte[] buffer = new byte[SIZE];
        int n=3;

        while(n-- != 0) {
            nByte = ais.read(buffer,0,SIZE);
            sdl.write(buffer,0,nByte);
        }
        sdl.stop();
    }
}
