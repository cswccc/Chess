import javax.swing.*;
import java.awt.*;

public class Frame_Test {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame("打字游戏"); //创建一个窗口
        jFrame.setSize(400,600);//设置窗口大小
        jFrame.setLocationRelativeTo(null);//窗口居中
        jFrame.getContentPane().setLayout(null);//无布局，记得下面添加控件时要设置它们位置和大小
        JPanel imPanel=(JPanel) jFrame.getContentPane();//注意内容面板必须强转为JPanel才可以实现下面的设置透明
        imPanel.setOpaque(false);//将内容面板设为透明
        ImageIcon icon=new ImageIcon("images/graph(1).jpg");//背景图
        JLabel label = new JLabel(icon);//往一个标签中加入图片
        label.setBounds(0, 0, jFrame.getWidth(), jFrame.getHeight());//设置标签位置大小，记得大小要和窗口一样大
        icon.setImage(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));//图片自适应标签大小
        jFrame.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE));//标签添加到层面板

        JLabel testword=new JLabel("测试");
        testword.setFont(new Font("方正舒体", Font.PLAIN, 30));//设置字体
        testword.setForeground(Color.white);//设置标签字体为白色
        testword.setBounds(jFrame.getWidth()/3,jFrame.getHeight()/3,jFrame.getWidth()/3,50);//设置标签位置和大小
        testword.setHorizontalAlignment(JLabel.CENTER);//设置标签文字水平居中
        jFrame.getContentPane().add(testword);//将一个标签添加到内容面板
        jFrame.setVisible(true);//设置窗口可见
    }
}