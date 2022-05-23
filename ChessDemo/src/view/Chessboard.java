package view;


import ComputerPlayer.ForEasy;
import ComputerPlayer.ForMedium;
import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.BLACK;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController;
    private final int CHESS_SIZE;
    private ChessGameFrame chessGameFrame;
    private int type;//人机类型 1简单 2中等 3困难,用的时候就传参
    private ForEasy easyComputer;//简易人机
    private ForMedium mediumComputer;
    private ChessColor computerColor;//人机颜色

    public Chessboard(int width, int height, ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);
        clickController = new ClickController(this,chessGameFrame);

        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);

        initBishopOnBoard(0,2,ChessColor.BLACK);
        initBishopOnBoard(0,CHESSBOARD_SIZE-3,ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE-1,2,ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE-1,CHESSBOARD_SIZE-3,ChessColor.WHITE);

        initKnightOnBoard(0,1,ChessColor.BLACK);
        initKnightOnBoard(0,CHESSBOARD_SIZE-2,ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE-1,1,ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE-1,CHESSBOARD_SIZE-2,ChessColor.WHITE);

        initKingOnBoard(0, 3, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);

        initQueenOnBoard(0, 4, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);

        for(int i = 0 ; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1,i,ChessColor.BLACK);
        }

        for(int i = 0 ; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(CHESSBOARD_SIZE-2,i,ChessColor.WHITE);
        }

        setForChess();

        if(currentColor == computerColor) ComputerToDo();
    }
    /*
    另外的初始化棋盘的方法,这个主要在悔棋中调用了,因为悔棋你需要回到之前的棋盘.基本类似,只是初始化棋盘简单了一些
     */
    public Chessboard(int width, int height, ChessComponent[][] chessComponents, ChessGameFrame chessGameFrame,ChessColor currentColor) {
        this.chessGameFrame = chessGameFrame;
        this.currentColor = currentColor;
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        clickController = new ClickController(this,chessGameFrame);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.chessComponents[i][j] = chessComponents[i][j];
            }
        }

        if(currentColor == computerColor) ComputerToDo();//如果当前是电脑走,直接调用
    }

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    /*
    我也忘了干啥用到了
    这个好像没用到,我注释掉也没事
     */
//    public void setChessBoard(ChessComponent[][] chessComponents,ChessColor currentColor) {
//        for(int i = 0; i < 8; i++) {
//            for(int j = 0; j < 8; j++) {
//                this.chessComponents[i][j] = chessComponents[i][j];
//            }
//        }
//        this.currentColor = currentColor;
//    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    /*
    下面的两个方法都是demo里面的,一个是把棋子放到棋盘上
    另外一个是交换棋子顺便吃棋
     */
    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        Chessboard c = new Chessboard(WIDTH,HEIGHT,chessComponents,chessGameFrame,currentColor);
        chessGameFrame.addChessboards(c);
        chessGameFrame.addCurrentPlayer();
        if(currentColor == computerColor) ComputerToDo();
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initEmptyChessOnboard(int row,int col) {
        ChessComponent chessComponent = new EmptySlotComponent(new ChessboardPoint(row, col), calculatePoint(row, col), clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        chessData.forEach(System.out::println);
    }

    public void initiateChessboard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(chessComponents[i][j] instanceof BishopChessComponent) {
                    initBishopOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
                else if(chessComponents[i][j] instanceof KnightChessComponent) {
                    initKnightOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
                else if(chessComponents[i][j] instanceof PawnChessComponent) {
                    initPawnOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
                else if(chessComponents[i][j] instanceof RookChessComponent) {
                    initRookOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
                else if(chessComponents[i][j] instanceof EmptySlotComponent) {
                    initEmptyChessOnboard(i,j);
                }
                else if(chessComponents[i][j] instanceof KingChessComponent) {
                    initKingOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
                else if(chessComponents[i][j] instanceof QueenChessComponent) {
                    initQueenOnBoard(i,j,chessComponents[i][j].getChessColor());
                }
            }
        }

        setForChess();
    }

    /*
    放置棋子,读取data.txt时使用到了
     */
    public void setChess(int row, int col,char c) {
        ChessColor color = ChessColor.NONE;
        if(c >= 'A' && c <= 'Z') color = ChessColor.BLACK;
        else if(c >= 'a' && c <= 'z') color = ChessColor.WHITE;

        switch (c) {
            case 'r':
            case 'R': initRookOnBoard(row,col,color); break;

            case 'n':
            case 'N': initKnightOnBoard(row,col,color); break;

            case 'b':
            case 'B': initBishopOnBoard(row,col,color); break;

            case 'p':
            case 'P': initPawnOnBoard(row,col,color); break;

            case 'k':
            case 'K': initKingOnBoard(row,col,color); break;

            case 'q':
            case 'Q': initQueenOnBoard(row,col,color); break;

            default:  initEmptyChessOnboard(row,col); break;
        }
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public void ComputerToDo() {//人机调用
        if(type == 0) return;

        if(currentColor == computerColor) {
            switch (type) {
                case 1: easyComputer = new ForEasy(computerColor,this); easyComputer.ComputerWork(); break;
                case 2: mediumComputer = new ForMedium(computerColor,this); mediumComputer.ComputerWork(); break;
                case 3: break;
            }
        }
        swapColor();//人机下完之后一定要记得换出棋方
    }

//    public void setEasyComputer(ChessColor color) {//简易人机的设定,将电脑操控方传入.貌似没用到
//        easyComputer = new ForEasy(color,this);
//    }

    public void setType(int type) {
        this.type = type;
    }

    public void setComputerColor(ChessColor computerColor) {//设定电脑的控制棋方
        this.computerColor = computerColor;
        if(currentColor == computerColor) ComputerToDo();
    }

    private void setForChess() {
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) chessComponents[i][j].setChessboard(this);
    }


}
