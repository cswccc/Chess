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
/*
保存函数
将棋盘信息输出成字符串存储到data.txt
你也看到了我前面所有的棋子都加了一个ToString方法
具体字符代表的棋子可以参考Assignment5
大写是黑旗
小写是白棋
 */
public class Save {
    private ArrayList<String> data = new ArrayList<>();

    public Save(List<Chessboard> chessboards) {//收集棋盘信息,所有走过的棋盘都要保存,因为存储的信息后面还要能悔棋的.
        StringBuilder s = new StringBuilder();

        for(Chessboard chessboard : chessboards) {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) s.append(chessboard.getChessComponents()[i][j]);
            }
        }
        data.add(String.valueOf(s));
    }

    public void SaveTheData() throws Exception {//将棋盘信息输出到data.txt
        try {
            Clear clear = new Clear();//存储之前清空一下
            clear.deleteData();
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
