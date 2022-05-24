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
    private int pawnStep=0;

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

    public boolean eatPassant (ChessComponent[][] chessComponents ,ChessboardPoint destination){
        ChessboardPoint source = getChessboardPoint();
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE&&source.getX()==3){
            if (destination.getX()==2&&Math.abs(destination.getY()-source.getY())==1){
                if (chessComponents[destination.getX()+1][destination.getY()].getChessColor() == ChessColor.BLACK&&chessComponents[destination.getX()+1][destination.getY()]instanceof PawnChessComponent){
                    if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                        //让chessComponents[destination.getX()+1][destination.getY()]变成空棋
                        if (((PawnChessComponent) chessComponents[destination.getX()+1][destination.getY()]).getPawnStep() == 1) {
                            chessboard.swapChessComponents(chessComponents[destination.getX()][destination.getY()], chessComponents[destination.getX() + 1][destination.getY()]);
                            chessboard.swapChessComponents(chessComponents[destination.getX() + 1][destination.getY()], chessComponents[destination.getX()][destination.getY()]);

                            return true;
                        }
                    }
                }

            }

        }
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK&&source.getX()==4){
            if (destination.getX()==5&&Math.abs(destination.getY()-source.getY())==1){
                if (chessComponents[destination.getX()-1][destination.getY()].getChessColor() == ChessColor.WHITE&&chessComponents[destination.getX()-1][destination.getY()]instanceof PawnChessComponent){
                    if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                        //让chessComponents[destination.getX()+1][destination.getY()]变成空棋
                        if (((PawnChessComponent) chessComponents[destination.getX()-1][destination.getY()]).getPawnStep() == 1) {
                            chessboard.swapChessComponents(chessComponents[destination.getX()][destination.getY()], chessComponents[destination.getX() - 1][destination.getY()]);
                            chessboard.swapChessComponents(chessComponents[destination.getX() - 1][destination.getY()], chessComponents[destination.getX()][destination.getY()]);

//                            chessComponents[destination.getX()-1][destination.getY()]=new EmptySlotComponent(new ChessboardPoint(destination.getX()-1,destination.getY()),getLocation(),getClickController(),size);
                            return true;
                        }
                    }
                }

            }

        }
        return false;
    }



    @Override
    public  boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if(chessComponents[source.getX()][source.getY()].getChessColor() == chessComponents[destination.getX()][destination.getY()].getChessColor()) return false;
        if (canPromotion(chessComponents,destination)){
            pawnStep++;
            return true;
        }
        if (eatPassant(chessComponents,destination)){
            pawnStep++;
            return true;
        }

        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
            if(pawnStep==0) {
                if(source.getX() == destination.getX()+2 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()+1][destination.getY()] instanceof EmptySlotComponent) {
                    pawnStep++;
                    return true;
                }
            }
            if(source.getX() == destination.getX()+1 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                pawnStep++;
                return true;
            }
            if(source.getX() == destination.getX()+1 && Math.abs(source.getY() - destination.getY()) == 1 && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)) {
                pawnStep++;
                return true;
            }
        }

        else {
            if(pawnStep==0) {
                if(source.getX() == destination.getX()-2 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent && chessComponents[destination.getX()-1][destination.getY()] instanceof EmptySlotComponent) {
                    pawnStep++;
                    return true;
                }
            }
            if(source.getX() == destination.getX()-1 && source.getY() == destination.getY() && chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {
                pawnStep++;
                return true;
            }
            if(source.getX() == destination.getX()-1 && Math.abs(source.getY() - destination.getY()) == 1 && !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)) {
                pawnStep++;
                return true;
            }
        }
        return false;
    }

    public boolean canPromotion(ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source = getChessboardPoint();
        boolean a=false;
        if(chessComponents[source.getX()][source.getY()].getClickController() == null) return false;
        if (chessComponents[source.getX()][source.getY()] instanceof PawnChessComponent) {
            if (destination.getY()==source.getY()&&chessComponents[destination.getX()][destination.getY()]instanceof EmptySlotComponent)a=true;
            if (Math.abs(source.getY()-destination.getY())==1&&chessComponents[destination.getX()][destination.getY()].getChessColor()!=chessComponents[source.getX()][source.getY()].getChessColor()){
                if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent){
                }
                else a=true;
            }
            if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK) {
                if (destination.getX() == 7&&source.getX()==6&&a) {
                    ChessGameFrame.Promotion();
                    if (ChessGameFrame.s.equals("Queen")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new QueenChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.BLACK,getClickController(),size);
                        return true;
                    }
                    if (ChessGameFrame.s.equals("Rook")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new RookChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.BLACK,getClickController(),size);
                        return true;
                    }if (ChessGameFrame.s.equals("Bishop")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new BishopChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.BLACK,getClickController(),size);
                        return true;
                    }if (ChessGameFrame.s.equals("Knight")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new KnightChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.BLACK,getClickController(),size);
                        return true;
                    }

                }
            }
            if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
                if (destination.getX() == 0&&source.getX()==1&&a) {
                    ChessGameFrame.Promotion();
                    if (ChessGameFrame.s.equals("Queen")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new QueenChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.WHITE,getClickController(),size);
                        return true;
                    }
                    if (ChessGameFrame.s.equals("Rook")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new RookChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.WHITE,getClickController(),size);
                        return true;
                    }if (ChessGameFrame.s.equals("Bishop")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new BishopChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.WHITE,getClickController(),size);
                        return true;
                    }if (ChessGameFrame.s.equals("Knight")) {
                        chessComponents[source.getX()][source.getY()]=null;
                        chessComponents[source.getX()][source.getY()]=new KnightChessComponent(new ChessboardPoint(source.getX(),source.getY()),getLocation(),ChessColor.WHITE,getClickController(),size);
                        return true;
                    }

                }
            }

        }
        return false;
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

    public void setPawnStep(int step) {
        pawnStep = step;
    }

    public int getPawnStep() {
        return pawnStep;
    }
}
