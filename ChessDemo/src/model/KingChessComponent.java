package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class KingChessComponent extends ChessComponent {
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    public int KingMoveStepBlack=0;
    public int KingMoveStepWhite=0;

    private Image kingImage;

    public void loadResource() throws IOException {
        if(KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }
        if(KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }

    private void initiateKingChessImage(ChessColor color) {
        try {
            loadResource();
            if(color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            }
            else if(color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKingChessImage(color);
    }


    @Override
    public  boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if(chessComponents[source.getX()][source.getY()].getChessColor() == chessComponents[destination.getX()][destination.getY()].getChessColor()) return false;

        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
            boolean a=chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent||chessComponents[destination.getX()][destination.getY()].getChessColor()==ChessColor.BLACK;
            if(Math.abs(source.getX()-destination.getX())==1 && source.getY() == destination.getY() && a){
                KingMoveStepWhite++;
                return true;
            }
            if(Math.abs(source.getY()-destination.getY())==1 && source.getX() == destination.getX() && a) {
                KingMoveStepWhite++;
                return true;
            }
            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a){
                KingMoveStepWhite++;
                return true;
            }
            if (KingMoveStepWhite==0&&RookChessComponent.RookMoveStepWhite1==0){
                if (source.getY()==destination.getY()&&Math.abs(destination.getX()-source.getX())==2){

                    return true;
                }
            }
        }
        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK) {
            boolean a=chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent||chessComponents[destination.getX()][destination.getY()].getChessColor()==ChessColor.WHITE;
            if(Math.abs(source.getX()-destination.getX())==1 && source.getY() == destination.getY() && a){
                KingMoveStepBlack++;
                return true;
            }
            if(Math.abs(source.getY()-destination.getY())==1 && source.getX() == destination.getX() && a) {
                KingMoveStepBlack++;
                return true;
            }
            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a){
                KingMoveStepBlack++;
                return true;
            }
        }
        return false;
    }
    public void Castling(){

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        return getChessColor() == ChessColor.BLACK ? "K" : "k";
    }
    //这个是有问题的,两方的棋子颜色不一样的
}
