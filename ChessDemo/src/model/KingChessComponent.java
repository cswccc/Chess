package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KingChessComponent extends ChessComponent {
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    public int KingMoveStepBlack = 0;
    public int KingMoveStepWhite = 0;

    private Image kingImage;

    public KingChessComponent(ChessboardPoint chessboardPoint, ChessColor color) {
        super(chessboardPoint,color);
    }

    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }
        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }

    private void initiateKingChessImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
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



    public ArrayList<ChessboardPoint> canReachPoints(ChessComponent[][] chessComponents) {
        ArrayList<ChessboardPoint> canReach = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (canMoveTo(chessComponents,new ChessboardPoint(i,j))){
                    canReach.add(new ChessboardPoint(i,j));
                }
            }
        }
        return canReach;
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        if (exchange(chessComponents, destination)) {
            return true;
        }
        ChessboardPoint source = getChessboardPoint();
        if(chessComponents[source.getX()][source.getY()].getChessColor() == chessComponents[destination.getX()][destination.getY()].getChessColor()) return false;
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
            boolean a = chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent || chessComponents[destination.getX()][destination.getY()].getChessColor() == ChessColor.BLACK;
            if (Math.abs(source.getX() - destination.getX()) == 1 && source.getY() == destination.getY() && a) {
                KingMoveStepWhite++;
                return true;
            }
            if (Math.abs(source.getY() - destination.getY()) == 1 && source.getX() == destination.getX() && a) {
                KingMoveStepWhite++;
                return true;
            }
            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a) {
                KingMoveStepWhite++;
                return true;

            }
        }
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK) {
            boolean a = chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent || chessComponents[destination.getX()][destination.getY()].getChessColor() == ChessColor.WHITE;
            if (Math.abs(source.getX() - destination.getX()) == 1 && source.getY() == destination.getY() && a) {
                KingMoveStepBlack++;
                return true;
            }
            if (Math.abs(source.getY() - destination.getY()) == 1 && source.getX() == destination.getX() && a) {
                KingMoveStepBlack++;
                return true;
            }
            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a) {
                KingMoveStepBlack++;
                return true;
            }
        }
        return false;
    }


    public boolean exchange(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
            if (KingMoveStepWhite == 0) {
                if (chessComponents[7][0] instanceof RookChessComponent && ((RookChessComponent)chessComponents[7][0]).getRookMoveStepWhite1() == 0) {
                    if (chessComponents[7][1] instanceof EmptySlotComponent && chessComponents[7][2] instanceof EmptySlotComponent) {
                        if (destination.getX() == 7 && destination.getY() == 1) {
                            chessComponents[7][0] = new EmptySlotComponent(new ChessboardPoint(7, 0), getLocation(), getClickController(), size);
                            chessComponents[7][2] = new RookChessComponent(new ChessboardPoint(7, 2), getLocation(), ChessColor.WHITE,getClickController(), size);
                            return true;
                        }
                        if(destination.getX() == 7 && destination.getY() == 2) return true;
                    }
                }
                if (chessComponents[7][7] instanceof RookChessComponent && ((RookChessComponent)chessComponents[7][7]).getRookMoveStepWhite2() == 0) {
                    if (chessComponents[7][4] instanceof EmptySlotComponent && chessComponents[7][5] instanceof EmptySlotComponent && chessComponents[7][6] instanceof EmptySlotComponent) {
                        if (destination.getX() == 7 && destination.getY() == 5) {
                            chessComponents[7][7] = new EmptySlotComponent(new ChessboardPoint(7, 7), getLocation(), getClickController(), size);
                            chessComponents[7][4] = new RookChessComponent(new ChessboardPoint(7, 4), getLocation(), ChessColor.WHITE,getClickController(), size);
                            return true;
                        }
                        if(destination.getX() == 7 && destination.getY() == 4) return true;
                    }
                }
            }

        }
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK) {
            if (KingMoveStepBlack == 0) {
                if (chessComponents[0][0] instanceof RookChessComponent && ((RookChessComponent)chessComponents[0][0]).getRookMoveStepBlack1() == 0) {
                    if (chessComponents[0][1] instanceof EmptySlotComponent && chessComponents[0][2] instanceof EmptySlotComponent) {
                        if (destination.getX() == 0 && destination.getY() == 1) {
                            chessComponents[0][0] = new EmptySlotComponent(new ChessboardPoint(0, 0), getLocation(), getClickController(), size);
                            chessComponents[0][2] = new RookChessComponent(new ChessboardPoint(0, 2), getLocation(), ChessColor.BLACK,getClickController(), size);
                            return true;
                        }
                        if(destination.getX() == 0 &&destination.getY() == 2) return true;
                    }
                }
                if (chessComponents[0][7] instanceof RookChessComponent && ((RookChessComponent)chessComponents[0][7]).getRookMoveStepBlack2() == 0) {
                    if (chessComponents[0][4] instanceof EmptySlotComponent && chessComponents[0][5] instanceof EmptySlotComponent && chessComponents[0][6] instanceof EmptySlotComponent) {
                        if (destination.getX() == 0 && destination.getY() == 5) {
                            chessComponents[0][7] = new EmptySlotComponent(new ChessboardPoint(0, 7), getLocation(), getClickController(), size);
                            chessComponents[0][4] = new RookChessComponent(new ChessboardPoint(0, 4), getLocation(), ChessColor.BLACK,getClickController(), size);
                            return true;
                        }
                        if(destination.getX() == 0 && destination.getY() == 4) return true;
                    }
                }
            }

        }

        return false;
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

    public int getKingMoveStepBlack() {
        return KingMoveStepBlack;
    }

    public int getKingMoveStepWhite() {
        return KingMoveStepWhite;
    }

    public void setKingMoveStepBlack(int kingMoveStepBlack) {
        KingMoveStepBlack = kingMoveStepBlack;
    }

    public void setKingMoveStepWhite(int kingMoveStepWhite) {
        KingMoveStepWhite = kingMoveStepWhite;
    }
}
