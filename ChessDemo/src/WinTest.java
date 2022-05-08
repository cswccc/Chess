import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinTest implements ActionListener

{

    JLabel label = new JLabel("注意我会变哦！");

    public WinTest()

    {

        JFrame frame = new JFrame();

        frame.setLayout(new FlowLayout());

        JButton button = new JButton("change");

        button.addActionListener(this);

        frame.add(button);

        Font font = new Font("黑体",Font.PLAIN,40);

        label.setFont(font);

        frame.add(label);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);

    }

    public static void main(String[] args)

    {

        new WinTest();

    }

    @Override

    public void actionPerformed(ActionEvent e)

    {

        if("change".equals(e.getActionCommand()))

        {

            label.setText("啊哈哈！我会72变，啊哈哈哈哈哈哈！");

        }

    }

}