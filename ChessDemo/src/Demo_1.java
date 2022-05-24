import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//动态效果
public class Demo_1 extends JFrame{
    //背景
    private BufferedImage bufferedImage;
    //窗体大小
    private int width = 1000;
    private int height = 1000;
    //照片数组
    private BufferedImage[] images = new BufferedImage[4];
    //要绘制动态图片中的那张
    private BufferedImage image;
    //背景音乐
    private File bgm;
    //播放音乐类对象
    private Demo_2 demo_2 = new Demo_2();

    //初始化
    {
        if(bufferedImage == null){
            //                bufferedImage = ImageIO.read(new File("images/backGround(1).jpg"));
//                for(int i = 1;i < images.length + 1;i ++)
//                    images[i - 1] = ImageIO.read(new File("images/backGround(1)" + i + ".png"));

        }
        bgm = new File("BGM/The King's Arrival - Dominik Hauser.wav");
    }

    public Demo_1(){
        super("动态测试");
        //设置窗口
        setSize(1000,1000);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //启动线程
        MyThread myThread = new MyThread();
        myThread.start();

        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
        button.setLocation(100, 500 / 10 + 70);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        //播放背景音乐
        demo_2.playMusic(bgm);
    }

    //双缓冲绘制解决图片闪烁问题
    @Override
    public void paint(Graphics g) {
        Image image = this.createImage(width,height);
        Graphics gImage = image.getGraphics();
        gImage.setColor(gImage.getColor());
        gImage.fillRect(0,0,width,height);
        super.paint(gImage);

        //绘制背景
        gImage.drawImage(bufferedImage,0 ,0 ,null );
        //绘制动态图片
        gImage.drawImage(this.image,0 ,100 ,null );

        //最后绘制缓冲后的图片
        g.drawImage(image,0 ,0 , null);
    }

    private int num = 0;//images数组内图片索引
    //线程内部类
    private class MyThread extends Thread{
        @Override
        public void run() {
            while(true) {
                if(num <= 3) {
                    image = images[num ++];
                }
                else
                    num = 0;
                repaint();
                try {
                    sleep(100);//每隔100毫秒重绘一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Demo_1();
    }
}