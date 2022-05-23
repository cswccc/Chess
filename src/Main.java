import view.ChessGameFrame;
import view.MainInterface;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainInterface mainInterface = null;
            try {
                mainInterface = new MainInterface(1500,1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainInterface.setVisible(true);
        });
    }
}
