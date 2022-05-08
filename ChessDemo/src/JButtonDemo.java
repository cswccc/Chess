import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JButtonDemo {
    static JFrame frame = new JFrame();
    public static void main(String[] args) {
//窗体大小
        frame.setSize(200,200);
//按钮
        JButton button =new JButton("点击我");
//在窗体上添加按钮
        frame.add(button);
//显示窗体
        frame.setVisible(true);

//添加点击事件监听器（你可以使用任何其他监听，看你想在什么情况下创建新的窗口了）
        button.addActionListener(new ActionListener(){
            //单击按钮执行的方法
            public void actionPerformed(ActionEvent e) {
                closeThis();
//创建新的窗口
                JFrame frame = new JFrame("新窗口");
//设置在屏幕的位置
                frame.setLocation(100,50);
// 窗体大小
                frame.setSize(200,200);
// 显示窗体
                frame.setVisible(true);
            }

        });
    }
    public static void closeThis(){
        frame.dispose();
    }
}