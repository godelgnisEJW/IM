package application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

import model.Connector;



public class Test{
	static long now = System.currentTimeMillis();
	static long lastTime = System.currentTimeMillis();
    private static int    num    = 0;

    public static void main(String[] args) throws IOException
    {
        final Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        Webcam.setDriver(new NativeWebcamDriver());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        final JFrame window = new JFrame("摄像头");
        
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)//关闭时的操作，windowClosed是关闭后的操作
            {
                webcam.close();
                window.dispose();
            }
        });
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//使用 System exit 方法退出应用程序
        final JButton button = new JButton("截图");
        window.add(panel, BorderLayout.CENTER);
        window.add(button, BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
//        byte[] buf = new byte[921600];
        webcam.addWebcamListener(new WebcamListener() {
			
			@Override
			public void webcamOpen(WebcamEvent we) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void webcamImageObtained(WebcamEvent we) {
				// TODO Auto-generated method stub
				now = System.currentTimeMillis();
				System.out.print("获取了图像------>" + (now - lastTime) + "ms\n");
				lastTime = now;
				java.nio.ByteBuffer buffer = webcam.getImageBytes();
				
//				System.out.println("position:" + buffer.position() + 
//						";capacity:" + buffer.capacity() +
//						";limit:" + buffer.limit());
//				WebcamUtils.capture(webcam, "C:\\Users\\godlegnis\\Desktop\\ptos\\" + now, ImageUtils.FORMAT_PNG);
//				921600
//				byte[] buf = new byte[buffer.capacity()];
//				buffer.get(buf);
				
				
				
			}
			
			@Override
			public void webcamDisposed(WebcamEvent we) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void webcamClosed(WebcamEvent we) {
				// TODO Auto-generated method stub
				
			}
		});
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                button.setEnabled(false);
                String fileName = "D://" + num;
                WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, "截图成功");
                        button.setEnabled(true);
                        num++;
                        return;
                    }
                });
            }
        });
    }
}
