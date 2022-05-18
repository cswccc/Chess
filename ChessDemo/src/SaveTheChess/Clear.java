package SaveTheChess;

import java.io.File;
import java.io.FileWriter;

public class Clear {

    public Clear() {
    }

    public void deleteData() throws Exception{
        File f = new File ("src/data.txt");
        FileWriter fw = new FileWriter (f);
        fw.write("");
        fw.flush();
        fw.close();
    }
}
