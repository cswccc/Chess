package controller;

import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;
import view.MainInterface;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class CheckWhoWinTheGame {

    ChessComponent[][] chessComponents = new ChessComponent[8][8];
    Chessboard chessboard;

    public CheckWhoWinTheGame(Chessboard chessboard) {
        this.chessboard = chessboard;
        initiateChessBoard(chessComponents);
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

    public boolean CheckWhetherBeAttacked(ChessboardPoint King, ChessColor color) {
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessComponents[i][j].getChessColor() != color && chessComponents[i][j].canMoveTo(chessComponents, King)) {
            initiateChessBoard(chessComponents);
            return true;
        }
        initiateChessBoard(chessComponents);

        return false;
    }

    public boolean CheckWhetherCanBeProtected(ChessboardPoint King, ChessColor color, int x, int y) {
        for(int dx = 0; dx < 8; dx++) for(int dy = 0; dy < 8; dy++) if(chessComponents[x][y].getChessColor() == color && chessComponents[x][y].canMoveTo(chessComponents,new ChessboardPoint(dx,dy))) {
            ChessComponent[][] chess = new ChessComponent[1][2];
            dispose(0,0,chessComponents[x][y].toString().charAt(0),chess);
            dispose(0,1,chessComponents[dx][dy].toString().charAt(0),chess);

            chessComponents[x][y] = new EmptySlotComponent(new ChessboardPoint(x,y),ChessColor.NONE);
            chessComponents[dx][dy] = chess[0][0];

            if(chessComponents[x][y] instanceof KingChessComponent) {//王自保
                if(!CheckWhetherBeAttacked(new ChessboardPoint(dx,dy),color)) {
                    initiateChessBoard(chessComponents);
                    return true;
                }
            }
            else if(!CheckWhetherBeAttacked(King,color)) {//小兵保护
                initiateChessBoard(chessComponents);
                return true;
            }

            initiateChessBoard(chessComponents);
        }

        return false;
    }

    private boolean CheckWhoCanProtect(ChessboardPoint King, ChessColor color) {
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessComponents[i][j].getChessColor() == color) {
            if(CheckWhetherCanBeProtected(King,color,i,j)) return true;
        }

        return false;
    }

    private void DefeatDialog(ChessColor color) {

        if(color == ChessColor.WHITE)
            JOptionPane.showMessageDialog(null,"The White player has been defeated by the Black One! Congratulations to the Black Player!","Defeat",JOptionPane.PLAIN_MESSAGE);
        else
            JOptionPane.showMessageDialog(null,"The Black player has been defeated by the White One! Congratulations to the White Player!","Defeat",JOptionPane.PLAIN_MESSAGE);

        ChessGameFrame chessGameFrame = chessboard.getChessGameFrame();
        chessGameFrame.dispose();

        MainInterface mainInterface = null;
        try {
            mainInterface = new MainInterface(1500,1000);
            mainInterface.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AttackingKingDialog() {
        JOptionPane.showMessageDialog(null,"Your King is being Attacked!！","Warning ",3);
    }

    public void Check() {
        ChessboardPoint K1 = null,K2 = null;
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) {
            if(chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.WHITE) {
                K1 = new ChessboardPoint(i,j);
            }
            else if(chessComponents[i][j] instanceof KingChessComponent && chessComponents[i][j].getChessColor() == ChessColor.BLACK) {
                K2 = new ChessboardPoint(i,j);
            }
        }

        boolean flag1;
        if(chessboard.getCurrentColor() == ChessColor.WHITE) {
            flag1 = CheckWhetherBeAttacked(K1, ChessColor.WHITE);
            if(!flag1) return;//未被攻击

            boolean flag2 = CheckWhoCanProtect(K1,ChessColor.WHITE);
            if(!flag2) {
                DefeatDialog(ChessColor.WHITE);//白方失败
                return;
            }

        }
        else {
            flag1 = CheckWhetherBeAttacked(K2, ChessColor.BLACK);
            if(!flag1) return;//未被攻击

            boolean flag2 = CheckWhoCanProtect(K2,ChessColor.BLACK);
            if(!flag2) {
                DefeatDialog(ChessColor.BLACK);//黑方失败
                return;
            }

        }
        AttackingKingDialog();
    }
}
