/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manu Bhadoria
 */
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.Object;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class PlayVideoFile implements Runnable {

    public int SecofPlay;
    public String videoFileName;

    public PlayVideoFile(int seconds) {
        this.SecofPlay = seconds;
    }

    public PlayVideoFile(String videoFileName) {
        System.out.println("PlayVideoFile Thread created....");
        this.SecofPlay = 600;
        this.videoFileName = videoFileName;
    }

    @Override
    public void run() {
        System.out.println("PlayVideoFile Thread created RUN....");
        int Height = 240, Width = 320;
        BufferedImage Bimage = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);

        ImageIcon ic = new ImageIcon(Bimage);
        JLabel label = new JLabel(ic);

        byte r_pix[] = new byte[Height * Width];
        byte g_pix[] = new byte[Height * Width];
        byte b_pix[] = new byte[Height * Width];

        try {

            File file = new File(this.videoFileName);
            long numFrames = file.length() / (Height * Width * 3);
            //System.out.println("File Lenght: "+file.length()+"Number of frames is :" + numFrames);
            FileInputStream fis = new FileInputStream(file);

            int icount, jcount;

            JFrame f = new JFrame("576_Final_Project_Aaveg_Manu");
            f.setSize(320, 240);

            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            for (int k = 0; k < numFrames; k++) {
                
                long time = System.nanoTime();
                
                fis.read(r_pix, 0, Height * Width);
                fis.read(g_pix, 0, Height * Width);
                fis.read(b_pix, 0, Height * Width);
                for (icount = 0; icount < Height; icount++) {
                    for (jcount = 0; jcount < Width; jcount++) {
                        int pix = (int) ((r_pix[icount * Width + jcount] << 16) + (g_pix[icount * Width + jcount] << 8) + b_pix[icount * Width + jcount]);
                        Bimage.setRGB(jcount, icount, pix);
                    }
                }
                f.add(label, BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
                label.repaint();
                time = System.nanoTime() - time;
                long miliTime = time/1000000;
                int nanoTime = (int)time%1000000;
                miliTime = 41 - miliTime;
                nanoTime = 666667 - nanoTime;
                if(miliTime < 0 )
                    miliTime = 0;
                if(nanoTime < 0)
                    nanoTime = 0;
                //System.out.println("This much time it takes: "+ time +"miliTime: "+miliTime+"nanoTime: "+nanoTime);
                Thread.currentThread().sleep(miliTime, nanoTime);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(PlayVideoFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException: " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
        System.out.println("Video Thread Exiting...");
    }
}