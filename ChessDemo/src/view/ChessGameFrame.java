package view;

import SaveTheChess.Clear;
import SaveTheChess.Save;
import controller.GameController;
import model.ChessColor;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
    private ArrayList<Chessboard> chessboards = new ArrayList<>();//存储走完每一步的棋盘
    private int type;
    private ChessColor computerColor;
    public static String s;
    private Clear clear = new Clear();
    static JPanel panel=new JPanel();
    private Clip clip;

    public ChessGameFrame(int width, int height) throws Exception {
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
        addBackGroundMusic();
        addTheBGMButton();
        addTheSoundEffectButton();
    }
    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
        gameController = new GameController(chessboard);
        chessboard.setTypeForSoundEffect(typeForSoundEffect);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);

        add(chessboard);
        chessboards.add(new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this));
    }

    private void WrongDialog() {//无法退步,退到最后一步了不能再退了
        JOptionPane.showMessageDialog(null,"You can't step back anymore!","错误 ",0);
    }

    public void changeChessboard() throws Exception {//更改棋盘,用于悔棋刷新棋盘
        int Siz = chessboards.size();
        if(Siz == 1) {
            WrongDialog();
            return;
        }

        if(type == 0) {
            chessboards.remove(Siz - 1); Siz--;

            remove(chessboard);
            chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,chessboards.get(Siz - 1).getChessComponents(),this,chessboards.get(Siz - 1).getCurrentColor());
            chessboard.setTypeForSoundEffect(typeForSoundEffect);
            chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
            chessboard.initiateChessboard();

            add(chessboard);
            chessboard.repaint();

            addCurrentPlayer();
        }
        else {
            chessboards.remove(Siz - 1); Siz--;
            chessboards.remove(Siz - 1); Siz--;

            remove(chessboard);
            chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,chessboards.get(Siz - 1).getChessComponents(),this,chessboards.get(Siz - 1).getCurrentColor());
            chessboard.setTypeForSoundEffect(typeForSoundEffect);
            chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
            chessboard.initiateChessboard();
            setType(type);
            setComputerColor(computerColor);


            add(chessboard);
            chessboard.repaint();

            addCurrentPlayer();
        }
    }

    public void showChessboard() {
        int Siz = chessboards.size();

        remove(chessboard);
        chessboard = new Chessboard(CHESSBOARD_SIZE,CHESSBOARD_SIZE,chessboards.get(Siz - 1).getChessComponents(),this,chessboards.get(Siz - 1).getCurrentColor());
        chessboard.setTypeForSoundEffect(typeForSoundEffect);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        chessboard.initiateChessboard();
        add(chessboard);

        chessboard.repaint();
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {//当前行棋方的显示
        JLabel statusLabel = new JLabel("Current Player:");
        statusLabel.setLocation(HEIGTH - 20, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(statusLabel);
    }

    public void setChessboards(ArrayList<Chessboard> chessboards) {//接收行棋数据
        chessboards.remove(0);
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
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void SaveDialog(Save save) throws Exception {//保存弹出对话框
        int sav = JOptionPane.showConfirmDialog(null,"Are you sure to save the chessboard and exit?","Save",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(sav == JOptionPane.YES_NO_OPTION) {
            save.SaveTheData(type);
            dispose();
            MainInterface mainInterFace = new MainInterface(1500,1000);
            mainInterFace.setVisible(true);
        }
    }

    private void addSaveButton() {//保存键
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 140);
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {

            Save save = new Save(chessboards,computerColor);

            try {
                SaveDialog(save);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addBackToInterfaceButton() {//返回到主界面
        JButton button = new JButton("Back To Interface");
        button.setLocation(HEIGTH, HEIGTH / 10 + 210);
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainInterface mainInterface = null;
                try {
                    mainInterface = new MainInterface(1500,1000);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                mainInterface.setVisible(true);
            }
        });
    }

    private void addRestartGameButton() {//重新游戏
        JButton button = new JButton("Restart Game");
        button.setLocation(HEIGTH, HEIGTH / 10 + 280);
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessGameFrame mainFrame = null;
                try {
                    mainFrame = new ChessGameFrame(1500, 1000);
                    mainFrame.setType(type);
                    mainFrame.setTypeForSoundEffect(typeForSoundEffect);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mainFrame.setVisible(true);
            }
        });
    }

    private void addRepentanceButton() {//悔棋
        JButton button = new JButton("Repentance");
        button.setLocation(HEIGTH, HEIGTH / 10 + 350);
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeChessboard();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private int cnt;
    private JButton BGMButton;
    private JLabel jLabel;

    private void addTheBGMButton() {
        BGMButton = new JButton("Stop/Play The BGM");
        BGMButton.setLocation(HEIGTH, HEIGTH / 10 + 420);
        BGMButton.setSize(300, 60);
        BGMButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(BGMButton);

        BGMButton.addActionListener(e -> {
            cnt++;
            if(cnt % 2 == 1) {
                clip.stop();
            }
            else {
                try {
                    addBackGroundMusic();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private int cnt2;
    private int typeForSoundEffect;

    private void addTheSoundEffectButton() {
        JButton button = new JButton("Open/Close the Sound Effect");
        button.setLocation(HEIGTH, HEIGTH / 10 + 490);
        button.setSize(300, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(button);

        button.addActionListener(e -> {
            cnt2++;
            if(cnt2 % 2 == 1) {
                setTypeForSoundEffect(1);
            }
            else {
                setTypeForSoundEffect(0);
            }
        });
    }

    public void setTypeForSoundEffect(int typeForSoundEffect) {
        this.typeForSoundEffect = typeForSoundEffect;
        chessboard.setTypeForSoundEffect(typeForSoundEffect);
    }

    public void addCurrentPlayer() {//变换当前行棋方
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

    public void setType(int type) throws Exception {//收参数传给chessboard,同chessboard定义
        this.type = type;
        clear.deleteData(type);
        chessboard.setType(type);
    }

    public void setComputerColor(ChessColor computerColor) {//同上
        this.computerColor = computerColor;
        chessboard.setComputerColor(computerColor);
    }

    public  static void Promotion(){
        String[] str={"Queen","Rook","Knight","Bishop"};
        s= (String) JOptionPane.showInputDialog(panel,"Which chess do you want it to be:","Promotion",1,null,str,str[0]);
    }


    public void addForChessBoards() {
        chessboards.add(new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this));
    }

    private AudioInputStream audioInput;
    private File bgm;

    private void addBackGroundMusic() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        bgm = new File("BGM/The King's Arrival - Dominik Hauser.wav");
        try {
            clip = AudioSystem.getClip();
            //将传入的文件转成可播放的文件
            audioInput = AudioSystem.getAudioInputStream(bgm);
            //播放器打开这个文件
            clip.open(audioInput);
            //clip.start();//只播放一次
            //循环播放
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
