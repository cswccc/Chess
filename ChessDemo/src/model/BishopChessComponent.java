package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BishopChessComponent extends ChessComponent {


    private static Image Bishop_WHITE;
    private static Image Bishop_BLACK;

    private Image bishopImage;

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController clickController, int size) {
        super(chessboardPoint, location, color, clickController, size);
        initiateBishopImage(color);
    }

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if(color == ChessColor.WHITE) {
                bishopImage = Bishop_WHITE;
            }
            else if(color == ChessColor.BLACK) {
                bishopImage = Bishop_BLACK;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if(chessComponents[source.getX()][source.getY()].getChessColor() == chessComponents[destination.getX()][destination.getY()].getChessColor()) return false;
        //我自己加的一句话,如果走的目的地和当前棋子颜色相同,不能走,返回false
        //所有的棋子都添加了
        int flag = 0;
        if ((source.getX() + source.getY() == destination.getX() +destination.getY())) {
            flag = 1;
        }
        else if((source.getX() - source.getY() == destination.getX() - destination.getY())) {
            flag = 1;
        }
        if(flag == 0) return false;

        else {
            int col = destination.getY();
            for(int row = destination.getX(); row != source.getX();) {
                if(row < source.getX()) row++;
                else row --;

                if(col < source.getY()) col++;
                else col--;
                if((row != source.getX()) && !(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void loadResource() throws IOException {
        if(Bishop_WHITE == null) {
            Bishop_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }
        if(Bishop_BLACK == null) {
            Bishop_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    @Override
    public String toString() {
        return getChessColor() == ChessColor.BLACK ? "B" : "b";
    }
}
