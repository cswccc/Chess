package view;


import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private ChessGameFrame chessGameFrame;

    public Chessboard(int width, int height, ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

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

        for(int i = 0 ; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1,i,ChessColor.BLACK);
        }

        for(int i = 0 ; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(CHESSBOARD_SIZE-2,i,ChessColor.WHITE);
        }
    }

    public Chessboard(int width, int height, ChessComponent[][] chessComponents, ChessGameFrame chessGameFrame,ChessColor currentColor) {
        this.chessGameFrame = chessGameFrame;
        this.currentColor = currentColor;
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.chessComponents[i][j] = chessComponents[i][j];
            }
        }
    }

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    public void setChessBoard(ChessComponent[][] chessComponents,ChessColor currentColor) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.chessComponents[i][j] = chessComponents[i][j];
            }
        }
        this.currentColor = currentColor;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

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
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
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
            }
        }
    }

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
            default:  initEmptyChessOnboard(row,col); break;
        }
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }
}
