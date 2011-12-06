import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl;
import javax.swing.text.Position;

/**
 * 
 * <Replace this with a short description of the class.>
 * 
 * @author Manu Bhadoria
 */
public class PlaySound implements Runnable {

    private File audioFile;
    private Position curPosition;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    enum Position {

        LEFT, RIGHT, NORMAL
    };

    /**
     * CONSTRUCTOR
     */
    //public PlaySound(InputStream waveStream) {
    public PlaySound(String audioFileName) {
        this.audioFile = new File(audioFileName);
        curPosition = Position.NORMAL;
    }

    @Override
    public void run() {

        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(this.audioFile);
        } catch (UnsupportedAudioFileException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Obtain the information about the AudioInputStream
        AudioFormat audioFormat = audioInputStream.getFormat();
        Info info = new Info(SourceDataLine.class, audioFormat);

        // opens the audio channel
        SourceDataLine dataLine = null;
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
        } catch (LineUnavailableException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (dataLine.isControlSupported(FloatControl.Type.PAN)) {
            FloatControl pan = (FloatControl) dataLine.getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT) {
                pan.setValue(1.0f);
            } else if (curPosition == Position.LEFT) {
                pan.setValue(-1.0f);
            }
        }

        // Starts the music :P
        dataLine.start();
        int readBytes = 0;
        byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];

        try {
            while (readBytes != -1) {
                readBytes = audioInputStream.read(audioBuffer, 0,
                        audioBuffer.length);
		try{
		    while(PlayVideoFile.videoStarted == false){
			Thread.currentThread().sleep(100,0) ;
		    }
		} catch (Exception e){
		}
                if (readBytes >= 0) {
                    dataLine.write(audioBuffer, 0, readBytes);
                }
            }
        } catch (IOException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            // plays what's left and and closes the audioChannel
            dataLine.drain();
            dataLine.close();
        }

    }
}
