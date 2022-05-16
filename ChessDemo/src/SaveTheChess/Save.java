package SaveTheChess;

import view.Chessboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Save {
    private ArrayList<String> data = new ArrayList<>();

    public Save(List<Chessboard> chessboards) {
        StringBuilder s = new StringBuilder();

        for(Chessboard chessboard : chessboards) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) s.append(chessboard.getChessComponents()[i][j]);
            }
        }
        data.add(String.valueOf(s));
    }

    private void deleteData() throws Exception{
        File f = new File ("src/data.txt");
        FileWriter fw = new FileWriter (f);
        fw.write("");
        fw.flush();
        fw.close();
    }

    public void SaveTheData() throws Exception {
        try {
            deleteData();
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data.txt"));

            for(String s : data) {
                int len = s.length();
                for(int i = 0 ; i < len; i++) writer.write(s.charAt(i));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
