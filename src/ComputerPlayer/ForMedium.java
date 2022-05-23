package ComputerPlayer;

import model.*;
import view.*;

import java.util.ArrayList;
import java.util.Random;

public class ForMedium {
    private ChessColor color;
    private Chessboard chessboard;
    private ChessboardPoint destination,source;
    private int theBestChoice;
    private int flag;

    public ForMedium(ChessColor color, Chessboard chessboard) {
        this.color = color;//记录人机的颜色
        this.chessboard = chessboard;//传一下棋盘,因为人机走的棋子要使得棋盘发生改变
    }

    private int dfs(int x, int y, int haveEaten, ChessColor currentColor, ChessComponent[][] chessComponents, int depth) {
        int maxiHaveEaten = haveEaten;
        ChessColor nextPlayer = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;

        if(haveEaten >= theBestChoice) {
            theBestChoice = haveEaten;
        }

        if(depth > 6) return haveEaten;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) if(chessComponents[x][y].canMoveTo(chessComponents,new ChessboardPoint(i,j))) {
                if(!(chessComponents[i][j] instanceof EmptySlotComponent) && chessComponents[i][j].getChessColor() != color) {
                    ChessComponent chess1 = chessComponents[x][y];
                    ChessComponent chess2 = chessComponents[i][j];
                    chessComponents[i][j] = chess1;
                    chessComponents[x][y] = new EmptySlotComponent(new ChessboardPoint(x,y),ChessColor.NONE);

                    maxiHaveEaten = Math.max(maxiHaveEaten,dfs(i,j,haveEaten+1,nextPlayer,chessComponents, depth+1));
                    if(depth == 0 && theBestChoice >= maxiHaveEaten) {
                        destination = new ChessboardPoint(i,j);
                    }

                    chessComponents[i][j] = chess2;
                    chessComponents[x][y] = chess1;
                }

                else {
                    ChessComponent chess1 = chessComponents[x][y];
                    ChessComponent chess2 = chessComponents[i][j];
                    chessComponents[i][j] = chess1;

                    chessComponents[x][y] = new EmptySlotComponent(new ChessboardPoint(x,y),ChessColor.NONE);

                    maxiHaveEaten = Math.max(maxiHaveEaten,dfs(i,j,haveEaten,nextPlayer,chessComponents, depth+1));
                    if(depth == 0 && theBestChoice >= maxiHaveEaten) {
                        destination = new ChessboardPoint(i,j);
                    }

                    chessComponents[i][j] = chess2;
                    chessComponents[x][y] = chess1;
                }
            }
        }
        if(depth == 0 && theBestChoice >= maxiHaveEaten) {
            source = new ChessboardPoint(x,y);
        }

        return maxiHaveEaten;
    }

    private void dispose(int x, int y, char c, ChessComponent[][] chessComponents) {
        ChessColor color = ChessColor.NONE;
        if(c >= 'a' && c <= 'z') color = ChessColor.WHITE;
        else if(c >= 'A' && c <= 'Z') color = ChessColor.BLACK;
        switch (c) {
            case 'R':
            case 'r':
                chessComponents[x][y] = new RookChessComponent(new ChessboardPoint(x,y),color); break;
            case 'N':
            case 'n':
                chessComponents[x][y] = new KnightChessComponent(new ChessboardPoint(x,y),color); break;
            case 'B':
            case 'b':
                chessComponents[x][y] = new BishopChessComponent(new ChessboardPoint(x,y),color); break;
            case 'Q':
            case 'q':
                chessComponents[x][y] = new QueenChessComponent(new ChessboardPoint(x,y),color); break;
            case 'K':
            case 'k':
                chessComponents[x][y] = new KingChessComponent(new ChessboardPoint(x,y),color); break;
            case 'P':
            case 'p':
                chessComponents[x][y] = new PawnChessComponent(new ChessboardPoint(x,y),color); break;
            default: chessComponents[x][y] = new EmptySlotComponent(new ChessboardPoint(x,y),color); break;
        }
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

    private void changeForPawn(int x, int y) {
        if(chessboard.getChessComponents()[x][y] instanceof PawnChessComponent) ((PawnChessComponent) chessboard.getChessComponents()[x][y]).setTheFirstStep(true);
    }

    private boolean protectForKing(ChessComponent[][] chessComponents) {
        int kx = -1, ky = -1;
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessComponents[i][j].getChessColor() == color && !(chessComponents[i][j] instanceof KingChessComponent)) {
            kx = i; ky = j; break;
        }
        if(kx == -1) return true;

        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessComponents[i][j].canMoveTo(chessComponents,new ChessboardPoint(kx,ky))) {
            for(int x = 0; x < 8; x++) for(int y = 0; y < 8; y++) if(!chessComponents[i][j].canMoveTo(chessComponents,new ChessboardPoint(x,y)) && chessComponents[kx][ky].canMoveTo(chessComponents,new ChessboardPoint(x,y))) {
                ChessComponent chess1 = chessComponents[x][y];
                ChessComponent chess2 = chessComponents[kx][ky];
                chessComponents[x][y] = chess2;
                chessComponents[kx][ky] = new EmptySlotComponent(new ChessboardPoint(x,y),ChessColor.NONE);
                if(chessComponents[i][j].canMoveTo(chessComponents,new ChessboardPoint(x,y))) {
                    chessComponents[x][y] = chess1;
                    chessComponents[kx][ky] = chess2;
                }
                else {
                    source = new ChessboardPoint(kx,ky);
                    destination = new ChessboardPoint(x,y);
                    chessboard.swapChessComponents(chessboard.getChessComponents()[source.getX()][source.getY()],chessboard.getChessComponents()[destination.getX()][destination.getY()]);
                    return true;
                }
            }
        }

        return false;
    }

    public void ComputerWork() {
        ArrayList<String> chess = changeInto();//虚拟棋盘获得和转字符串为棋盘
        ChessComponent[][] chessComponents = new ChessComponent[8][8];
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) {
            char c = chess.get(i).charAt(j);
            dispose(i,j,c,chessComponents);
        }


        if(protectForKing(chessComponents)) return;

        //第一种优先,能吃子
        for(int i = 0; i < 8; i++) for(int j = 0; j < 8; j++) if(chessComponents[i][j].getChessColor() == color && !(chessComponents[i][j] instanceof KingChessComponent)) {
            for(int x = 0; x < 8; x++) for(int y = 0;  y < 8; y++) if(!(chessComponents[x][y] instanceof EmptySlotComponent) && chessComponents[i][j].canMoveTo(chessComponents,new ChessboardPoint(x,y))){
                source = new ChessboardPoint(i,j);
                destination = new ChessboardPoint(x,y);
                if(chessComponents[i][j] instanceof PawnChessComponent) changeForPawn(i,j);
                chessboard.swapChessComponents(chessboard.getChessComponents()[source.getX()][source.getY()],chessboard.getChessComponents()[destination.getX()][destination.getY()]);
                return;
            }
        }

        theBestChoice = 0;//第二种,谁吃的多谁走
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++) if(chessComponents[i][j].getChessColor() == color) {
                dfs(i, j, 0, color, chessComponents, 0);
            }

        if(theBestChoice == 0) {//第三种,找不到吃子的随机走
            chessComponents = chessboard.getChessComponents();
            Random random = new Random();

            while(true) {
                int x = random.nextInt(8);
                int y = random.nextInt(8);
                if(chessComponents[x][y].getChessColor() == color) {
                    ArrayList<ChessboardPoint> canMoveTo = chessComponents[x][y].canReachPoints(chessComponents);
                    if(canMoveTo.size() != 0) {
                        ChessboardPoint destination = canMoveTo.get(random.nextInt(canMoveTo.size()));
                        if(chessComponents[x][y] instanceof PawnChessComponent) changeForPawn(x,y);
                        chessboard.swapChessComponents(chessComponents[x][y],chessComponents[destination.getX()][destination.getY()]);
                        return;
                    }
                }
            }
        }
        else {
            if(chessboard.getChessComponents()[source.getX()][source.getY()] instanceof PawnChessComponent) changeForPawn(source.getX(),source.getY());
            chessboard.swapChessComponents(chessboard.getChessComponents()[source.getX()][source.getY()],chessboard.getChessComponents()[destination.getX()][destination.getY()]);
        }
    }
}
