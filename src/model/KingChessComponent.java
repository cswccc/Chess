package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KingChessComponent extends ChessComponent {
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    public int KingMoveStepBlack=0;
    public int KingMoveStepWhite=0;
    ArrayList<ChessboardPoint> ret = new ArrayList<>();

    private Image kingImage;

    public KingChessComponent(ChessboardPoint chessboardPoint, ChessColor color) {
        super(chessboardPoint,color);
    }

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

//        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE) {
//            boolean a=chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent||chessComponents[destination.getX()][destination.getY()].getChessColor()==ChessColor.BLACK;
//            if(Math.abs(source.getX()-destination.getX())==1 && source.getY() == destination.getY() && a){
//                KingMoveStepWhite++;
//                return true;
//            }
//            if(Math.abs(source.getY()-destination.getY())==1 && source.getX() == destination.getX() && a) {
//                KingMoveStepWhite++;
//                return true;
//            }
//            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a){
//                KingMoveStepWhite++;
//                return true;
//            }
//            if (KingMoveStepWhite==0&&RookChessComponent.RookMoveStepWhite1==0){
//                if (source.getY()==destination.getY()&&Math.abs(destination.getX()-source.getX())==2){
//
//                    return true;
//                }
//            }
//        }
//        if(chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.BLACK) {
//            boolean a=chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent||chessComponents[destination.getX()][destination.getY()].getChessColor()==ChessColor.WHITE;
//            if(Math.abs(source.getX()-destination.getX())==1 && source.getY() == destination.getY() && a){
//                KingMoveStepBlack++;
//                return true;
//            }
//            if(Math.abs(source.getY()-destination.getY())==1 && source.getX() == destination.getX() && a) {
//                KingMoveStepBlack++;
//                return true;
//            }
//            if (Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 1 && a){
//                KingMoveStepBlack++;
//                return true;
//            }
//        }
        //上面的写法有些麻烦,只要是附近的点都可以走,调用Math.abs
        int x = source.getX(), y = source.getY();
        int dx = destination.getX(), dy = destination.getY();
        if(chessComponents[x][y].getChessColor() == chessComponents[dx][dy].getChessColor()) return false;

        if(Math.abs(dx-x) <= 1 && Math.abs(dy-y) <= 1) return true;
        return false;
    }

    private boolean Victory1 (ChessComponent[][] chessComponents){
        ChessboardPoint source = getChessboardPoint();
        ArrayList<ChessboardPoint> dangerous = new ArrayList<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (chessComponents[i][j].getChessColor()!=chessComponents[source.getX()][source.getY()].getChessColor()){
                    dangerous.addAll(chessComponents[i][j].canReachPoints(chessComponents));
                }
            }
        }
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessboardPoint C = new ChessboardPoint(i,j);
                if(canMoveTo(chessComponents,C)) {
                    if (!dangerous.contains(C)){
                        ret.add(C);
                    }
                }
            }
        }
        return ret.size() != 0;
    }

    private boolean Victory2(ChessComponent[][] chessComponents){
        ChessboardPoint source = getChessboardPoint();
        if (Victory1(chessComponents)){
            for (int i=0;i<8;i++){
                for (int j=0;j<8;j++){
                    if (chessComponents[i][j].getChessColor()==chessComponents[source.getX()][source.getY()].getChessColor()){
                        ArrayList<ChessboardPoint> canHelp = chessComponents[i][j].canReachPoints(chessComponents);
                        for (int n = 0;n<canHelp.size();n++) {


                        }
                    }
                }
            }

        }
        return false;
    }

    public boolean exchange (ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source = getChessboardPoint();
        if (chessComponents[source.getX()][source.getY()].getChessColor() == ChessColor.WHITE){
            if (KingMoveStepWhite==0){
                if (RookChessComponent.getRookMoveStepWhite1()==0){
                    if (chessComponents[7][1] instanceof EmptySlotComponent&&chessComponents[7][2] instanceof EmptySlotComponent){
                        chessComponents[7][1]=chessComponents[7][3];
                        chessComponents[7][3]=new EmptySlotComponent(new ChessboardPoint(7,3),getLocation(),getClickController(),size);
                        chessComponents[7][2]=new RookChessComponent(new ChessboardPoint(7,2),getLocation(),ChessColor.WHITE,getClickController(),size);
                        chessComponents[7][0]=new EmptySlotComponent(new ChessboardPoint(7,0),getLocation(),getClickController(),size);
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
