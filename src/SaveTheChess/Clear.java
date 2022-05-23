package SaveTheChess;

import java.io.File;
import java.io.FileWriter;
/*
清空存储data.txt的函数
因为每次新开一局,我们都需要把之前的清空
专门新写了一个清空类
 */
public class Clear {

    public Clear() {
    }

    public void deleteData(int type) throws Exception{
        File f;
        if(type == 0) f= new File ("src/data.txt");
        else if(type == 1) f= new File ("src/data1.txt");
        else  f= new File ("src/data2.txt");
        FileWriter fw = new FileWriter (f);
        fw.write("");
        fw.flush();
        fw.close();
    }
}
