import javax.swing.*;

public class test extends JFrame{
    public test() {
        setTitle("the first");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        test a = new test();
    }
}
