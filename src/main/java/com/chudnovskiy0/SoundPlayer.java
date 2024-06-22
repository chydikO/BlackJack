package com.chudnovskiy0;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SoundPlayer {
    private Player player;

    public void playSound(String soundFile) {
        try {
            FileInputStream fis = new FileInputStream(soundFile);
            player = new Player(fis);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }
}