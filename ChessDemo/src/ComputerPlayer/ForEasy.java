package ComputerPlayer;

import model.ChessColor;
import model.ChessComponent;
import model.EmptySlotComponent;
import view.Chessboard;
import view.ChessboardPoint;

import java.util.ArrayList;
import java.util.Random;
//简单人机
public class ForEasy {
    private ChessColor color;
    private Chessboard chessboard;

    public ForEasy(ChessColor color, Chessboard chessboard) {
        this.color = color;//记录人机的颜色
        this.chessboard = chessboard;//传一下棋盘,因为人机走的棋子要使得棋盘发生改变
    }
    /*
    简单人机的思想:随机挑到一个棋子,随机走
     */

    public void ComputerWork() {
        ChessComponent[][] chessComponents = chessboard.getChessComponents();
        Random random = new Random();

        while(true) {
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            if(chessComponents[x][y].getChessColor() == color) {
                ArrayList<ChessboardPoint> canMoveTo = chessComponents[x][y].canReachPoints(chessComponents);
                if(canMoveTo.size() != 0) {
                    ChessboardPoint destination = canMoveTo.get(random.nextInt(canMoveTo.size()));
                    chessboard.swapChessComponents(chessComponents[x][y],chessComponents[destination.getX()][destination.getY()]);
                    return;
                }
            }
        }
    }
}
