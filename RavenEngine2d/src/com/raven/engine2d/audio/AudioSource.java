package com.raven.engine2d.audio;

import com.raven.engine2d.GameProperties;

import javax.sound.sampled.*;
import java.io.File;
import java.sql.SQLSyntaxErrorException;

public class AudioSource {

    private AudioFormat audioFormat;
    private int size;
    private byte[] audioBytes;
    private DataLine.Info dataLineInfo;

    public AudioSource(String name) {
        try {
            File f = new File(GameProperties.getMainDirectory() + File.separator + "audio" + File.separator + name);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
            audioFormat = audioInputStream.getFormat();
            size = (int) (audioFormat.getFrameSize() * audioInputStream.getFrameLength());
            audioBytes = new byte[size];
            dataLineInfo = new DataLine.Info(Clip.class, audioFormat, size);

            audioInputStream.read(audioBytes, 0, size);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Clip getAudioClip() {
        try {
            Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
            clip.open(audioFormat, audioBytes, 0, size);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                }

                System.out.println(event.getLine().isOpen());
                clip.setFramePosition(0);
                    clip.start();
            });

            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
