package model;

import controller.ClickController;
import view.ChessGameFrame;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PawnChessComponent extends ChessComponent {
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;

    private Image pawnImage;
    private boolean theFirstStep;

    public PawnChessComponent(ChessboardPoint chessboardPoint, ChessColor color) {
        super(chessboardPoint,color);
    }

    public void loadResource() throws IOException {
        if(PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }
        if(PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }

    private void initiatePawnChessImage(ChessColor color) {
        try{
            loadResource();
            if(color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            }
            else if(color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnChessImage(color);
    }

    @Override
    public  boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK && source.getX() != 1) theFirstStep = true;
        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE && source.getX() != 6) theFirstStep = true;
        if(chessComponents[source.getX()][source.getY()].getChessColor() == chessComponents[destination.getX()][destination.getY()].getChessColor()) return false;

        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
            if(!theFirstStep) {

                if(source.getX() == destination.getX()+2 && source.getY() == destination.getY() && chessComponents[destination.getX()+1][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                    theFirstStep = true;
                    return true;
                }
            }
            if(source.getX() == destination.getX()+1 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) return true;
            if(source.getX() == destination.getX()+1 && Math.abs(source.getY() - destination.getY()) == 1 && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)) return true;
        }

        else {
            if(!theFirstStep) {
                if(source.getX() == destination.getX()-2 && source.getY() == destination.getY() && chessComponents[destination.getX()-1][destination.getY()] instanceof EmptySlotComponent) {
                    theFirstStep = true;
                    return true;
                }
            }
            if(source.getX() == destination.getX()-1 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) return true;
            if(source.getX() == destination.getX()-1 && Math.abs(source.getY() - destination.getY()) == 1 && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)) return true;
        }
        return false;
    }

    public void setTheFirstStep(boolean theFirstStep) {
        this.theFirstStep = theFirstStep;
    }

    public void Promotion(ChessComponent[][] chessComponents, ChessboardPoint destination){

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        return getChessColor() == ChessColor.BLACK ? "P" : "p";
    }
}
