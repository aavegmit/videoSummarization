//package org.wikijava.sound.playWave;

//import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.File;

/**
 * plays a wave file using PlaySound class
 * 
 * @author Manu Bhadoria
 */
public class videoPlayback {

    public static void main(String[] args) throws InterruptedException {

        // get the command line parameters
        if (args.length < 2) {
            System.err.println("Invalid number of arguments");
            return;
        }
        String videoFileName = args[0];
        String audioFileName = args[1];
        
        Thread playSoundThread = new Thread(new PlaySound(audioFileName));
        Thread playVideoThread = new Thread(new PlayVideoFile(videoFileName));
        
        playSoundThread.start();
        playVideoThread.start();

        try {
            //delay for one second
            //Thread.sleep(500000);
            playSoundThread.join();
            playVideoThread.join();
            System.out.println("Main exiting..");
        } catch (InterruptedException e) {
        }
    }
}
