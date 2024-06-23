package com.chudnovskiy0;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundPlayer {
    public static final String SEP = System.getProperty("file.separator");
    public static final String FILE_DIR = System.getProperty("user.dir") + SEP + "sound";
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private Player player;

    public void playSound(String soundFile) {
        try {
            FileInputStream fis = new FileInputStream(FILE_DIR + SEP + soundFile);
            player = new Player(fis);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            logger.error("An error occurred while playing the sound file", e);
        }
    }
}