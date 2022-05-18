package ComputerPlayer;

import model.ChessColor;
import model.ChessComponent;
import model.EmptySlotComponent;
import view.Chessboard;
import view.ChessboardPoint;

import java.util.Random;

public class ForEasy {
    private ChessColor color;
    private Chessboard chessboard;

    public ForEasy(ChessColor color, Chessboard chessboard) {
        this.color = color;
        this.chessboard = chessboard;
    }

    public void ComputerWork() {
        ChessComponent[][] chessComponents = chessboard.getChessComponents();
        Random random = new Random();

        while(true) {
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            if(chessComponents[x][y].getChessColor() == color) {
                int dx,dy;
                dx = random.nextInt(8);
                dy = random.nextInt(8);
                if(chessComponents[x][y].canMoveTo(chessComponents,new ChessboardPoint(dx,dy))) {
                    chessboard.swapChessComponents(chessComponents[x][y],chessComponents[dx][dy]);
                    return;
                }
            }
        }
    }
}
