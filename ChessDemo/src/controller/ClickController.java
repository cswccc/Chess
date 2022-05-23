package controller;


import model.ChessComponent;
import model.PawnChessComponent;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    private ChessGameFrame chessGameFrame;

    public ClickController(Chessboard chessboard, ChessGameFrame chessGameFrame) {
        this.chessboard = chessboard;
        this.chessGameFrame = chessGameFrame;
    }

    public void onClick(ChessComponent chessComponent) throws Exception {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                ChessboardPoint chessboardPoint = first.getChessboardPoint();
                chessboard.remove(first);
                first = chessboard.getChessComponents()[chessboardPoint.getX()][chessboardPoint.getY()];

                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();

                chessGameFrame.addForChessBoards();
                chessGameFrame.changeChessboard();

                first.setSelected(false);
                first = null;
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


}
