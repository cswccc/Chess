import view.ChessGameFrame;
import view.MainInterface;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainInterface mainInterface = new MainInterface(1500,1000);
            mainInterface.setVisible(true);
        });
    }
}
