package controller;


import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;
    private ChessGameFrame chessGameFrame;

    public ClickController(Chessboard chessboard, ChessGameFrame chessGameFrame) {
        this.chessboard = chessboard;
        this.chessGameFrame = chessGameFrame;
    }

    private void addBGM(ChessComponent chessComponent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais;

        if(chessComponent instanceof PawnChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Pawn.wav"));
        }
        else if(chessComponent instanceof KingChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/King.WAV"));
        }
        else if(chessComponent instanceof BishopChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Bishop.wav"));
        }
        else if(chessComponent instanceof QueenChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Queen.wav"));
        }
        else if(chessComponent instanceof RookChessComponent) {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Rook.wav"));
        }
        else {
            ais = AudioSystem.getAudioInputStream(new File("BGM/Knight.wav"));
        }

        AudioFormat aif = ais.getFormat();

        final SourceDataLine sdl;

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);

        sdl = (SourceDataLine) AudioSystem.getLine(info);
        sdl.open(aif);
        sdl.start();

        FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);

        double value = 2;
        float db = (float) (Math.log(value == 0.0f ? 0.0001f : value) / Math.log(10.0) * 20.0);

        fc.setValue(db);

        int nByte = 0;
        final int SIZE= 1024*64;
        byte[] buffer = new byte[SIZE];
        int n=5;

        while(n-- != 0) {
            nByte = ais.read(buffer,0,SIZE);
            sdl.write(buffer,0,nByte);
        }
        sdl.stop();

        ais.close();
        sdl.close();
    }

    private void addBGM2(ChessComponent chessComponent) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("BGM/setChess.wav"));;

        AudioFormat aif = ais.getFormat();

        final SourceDataLine sdl;

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);

        sdl = (SourceDataLine) AudioSystem.getLine(info);
        sdl.open(aif);
        sdl.start();

        FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);

        double value = 2;
        float db = (float) (Math.log(value == 0.0f ? 0.0001f : value) / Math.log(10.0) * 20.0);

        fc.setValue(db);

        int nByte = 0;
        final int SIZE= 1024*64;
        byte[] buffer = new byte[SIZE];
        int n=2;

        while(n-- != 0) {
            nByte = ais.read(buffer,0,SIZE);
            sdl.write(buffer,0,nByte);
        }
        sdl.stop();

        ais.close();
        sdl.close();
    }

    public void onClick(ChessComponent chessComponent) throws Exception {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();
                addBGM(chessComponent);
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                addBGM2(chessComponent);
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
