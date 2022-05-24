package controller;


import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    private ChessGameFrame chessGameFrame;
    private int type;

    public ClickController(Chessboard chessboard, ChessGameFrame chessGameFrame) {
        this.chessboard = chessboard;
        this.chessGameFrame = chessGameFrame;
    }

    private void addBGM(ChessComponent chessComponent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais;

        if(chessComponent instanceof PawnChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Pawn.wav"));
        }
        else if(chessComponent instanceof KingChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/King.WAV"));
        }
        else if(chessComponent instanceof BishopChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Bishop.wav"));
        }
        else if(chessComponent instanceof QueenChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Queen.wav"));
        }
        else if(chessComponent instanceof RookChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Rook.wav"));
        }
        else {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Knight.wav"));
        }

        AudioFormat aif = ais.getFormat();

        final SourceDataLine sdl;

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);

        sdl = (SourceDataLine) AudioSystem.getLine(info);
        sdl.open(aif);
        sdl.start();

        FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);

        double value = 1;
        float db = (float) (Math.log(value == 0.0f ? 0.0001f : value) / Math.log(10.0) * 20.0);

        fc.setValue(db);

        int nByte = 0;
        final int SIZE= 1024*64;
        byte[] buffer = new byte[SIZE];
        int n=5;

        while(n-- != 0) {
            nByte = ais.read(buffer,0,SIZE);
            sdl.write(buffer,0,nByte);
        }
        sdl.stop();

        ais.close();
        sdl.close();
    }

    private void addBGM2(ChessComponent chessComponent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("BGM/setChess.wav"));;

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
        int n=2;

        while(n-- != 0) {
            nByte = ais.read(buffer,0,SIZE);
            sdl.write(buffer,0,nByte);
        }
        sdl.stop();

        ais.close();
        sdl.close();
    }

    private ArrayList<String> changeInto() {
        ArrayList<String> ret = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            StringBuilder s = new StringBuilder();
            for(int j = 0; j < 8; j++) s.append(chessboard.getChessComponents()[i][j]);

            ret.add(String.valueOf(s));
        }

        return ret;
    }

    private void dispose(int x, int y, char c, ChessComponent[][] chessComponents) {
        ChessColor color = ChessColor.NONE;
        if(c >= 'a' && c <= 'z') color = ChessColor.WHITE;
        else if(c >= 'A' && c <= 'Z') color = ChessColor.BLACK;
        switch (c) {
            case 'R', 'r' -> chessComponents[x][y] = new RookChessComponent(new ChessboardPoint(x, y), color);
            case 'N', 'n' -> chessComponents[x][y] = new KnightChessComponent(new ChessboardPoint(x, y), color);
            case 'B', 'b' -> chessComponents[x][y] = new BishopChessComponent(new ChessboardPoint(x, y), color);
            case 'Q', 'q' -> chessComponents[x][y] = new QueenChessComponent(new ChessboardPoint(x, y), color);
            case 'K', 'k' -> chessComponents[x][y] = new KingChessComponent(new ChessboardPoint(x, y), color);
            case 'P', 'p' -> chessComponents[x][y] = new PawnChessComponent(new ChessboardPoint(x, y), color);
            default -> chessComponents[x][y] = new EmptySlotComponent(new ChessboardPoint(x, y), color);
        }
    }

    private void initiateChessBoard(ChessComponent[][] chessComponents) {
        ArrayList<String> chess = changeInto();//虚拟棋盘获得和转字符串为棋盘

        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) {
            char c = chess.get(i).charAt(j);
            dispose(i,j,c,chessComponents);
        }

        Chessboard chessboard1 = new Chessboard(100,100,chessComponents,null,chessboard.getCurrentColor());
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8 ; j++) {
            chessComponents[i][j].setChessboard(chessboard1);
        }

        ChessComponent[][] components = chessboard.getChessComponents();

        for(int i = 0 ; i < 8 ; i++) for(int j = 0; j < 8 ; j++) {
            if(chessComponents[i][j] instanceof PawnChessComponent) ((PawnChessComponent) chessComponents[i][j]).setPawnStep(((PawnChessComponent) components[i][j]).getPawnStep());
            else if(chessComponents[i][j] instanceof KingChessComponent) {
                if(chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                    ((KingChessComponent) chessComponents[i][j]).setKingMoveStepWhite(((KingChessComponent) components[i][j]).getKingMoveStepWhite());
                }
                else {
                    ((KingChessComponent) chessComponents[i][j]).setKingMoveStepBlack(((KingChessComponent) components[i][j]).getKingMoveStepBlack());
                }
            }
            else if(chessComponents[i][j] instanceof RookChessComponent) {
                ((RookChessComponent) chessComponents[i][j]).setRookMoveStepBlack1(((RookChessComponent) components[i][j]).getRookMoveStepBlack1());
                ((RookChessComponent) chessComponents[i][j]).setRookMoveStepBlack2(((RookChessComponent) components[i][j]).getRookMoveStepBlack2());
                ((RookChessComponent) chessComponents[i][j]).setRookMoveStepWhite1(((RookChessComponent) components[i][j]).getRookMoveStepWhite1());
                ((RookChessComponent) chessComponents[i][j]).setRookMoveStepWhite2(((RookChessComponent) components[i][j]).getRookMoveStepWhite2());
            }
        }
    }

    private void tipsForWhereCanMove(ChessComponent chessComponent) {
        int sx = chessComponent.getChessboardPoint().getX(), sy = chessComponent.getChessboardPoint().getY();
        ArrayList<ChessboardPoint> canMoveTo = new ArrayList<>();

        ChessComponent[][] chessComponents = new ChessComponent[8][8];

        initiateChessBoard(chessComponents);

        for(int i = 0 ; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(chessComponents[sx][sy].canMoveTo(chessComponents,new ChessboardPoint(i,j))){
                    canMoveTo.add(new ChessboardPoint(i,j));
                    initiateChessBoard(chessComponents);
        }

        for(ChessboardPoint chessboardPoint : canMoveTo) {
            int x = chessboardPoint.getX(), y = chessboardPoint.getY();
            chessboard.getChessComponents()[x][y].setSelected(!chessboard.getChessComponents()[x][y].getSelected());
            chessboard.getChessComponents()[x][y].repaint();
        }
    }

    private void WaringDialog() {
        JOptionPane.showMessageDialog(null,"Your King is being Attacked!！","Warning ",3);
    }

    private void checkWhetherBeingAttacked() {
        ChessColor currentColor = chessboard.getCurrentColor();

        ChessboardPoint King = null;

        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessboard.getChessComponents()[i][j] instanceof KingChessComponent && chessboard.getChessComponents()[i][j].getChessColor() == currentColor){
            King = new ChessboardPoint(i,j); break;
        }

        CheckWhoWinTheGame checkWhoWinTheGame = new CheckWhoWinTheGame(chessboard);

        if(checkWhoWinTheGame.CheckWhetherBeAttacked(King,currentColor) &&
        !checkWhoWinTheGame.CheckWhetherCanBeProtected(King,chessboard.getCurrentColor(),first.getChessboardPoint().getX(),first.getChessboardPoint().getY())) {
            WaringDialog();

            first.setSelected(false);
            first.repaint();
            first = null;
        }
    }

    public void onClick(ChessComponent chessComponent) throws Exception {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
                checkWhetherBeingAttacked();
                if(first!=null) tipsForWhereCanMove(chessComponent);
                if(type == 0) addBGM(chessComponent);
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                tipsForWhereCanMove(chessComponent);
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                addBGM2(chessComponent);
                ChessboardPoint chessboardPoint = first.getChessboardPoint();
                chessboard.remove(first);
                first = chessboard.getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()];

                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();

                for(int i = 0;  i < 8; i++) for(int j = 0; j < 8; j++)
                    if(!(i == first.getChessboardPoint().getX() && j == first.getChessboardPoint().getY()))
                        if(chessboard.getChessComponents()[i][j] instanceof PawnChessComponent && ((PawnChessComponent) chessboard.getChessComponents()[i][j]).getPawnStep() == 1) {
                            ((PawnChessComponent) chessboard.getChessComponents()[i][j]).setPawnStep(2);
                        }

                chessGameFrame.addForChessBoards();
                chessGameFrame.changeChessboard();

                first.setSelected(false);
                first = null;

                CheckWhoWinTheGame checkWhoWinTheGame = new CheckWhoWinTheGame(chessboard);
                checkWhoWinTheGame.Check();
            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    public void setType(int type) {
        this.type = type;
    }
}
