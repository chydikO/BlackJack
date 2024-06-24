package com.chudnovskiy0;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundPlayer {
    /**
     * Публичное статическое поле SEP типа String. Хранит разделитель пути файловой системы.
     */
    public static final String SEP = System.getProperty("file.separator");

    /**
     * Публичное статическое поле FILE_DIR типа String. Хранит путь к директории, где находятся звуковые файлы.
     */
    public static final String FILE_DIR = System.getProperty("user.dir") + SEP + "sound";

    /**
     * Приватное статическое поле logger типа Logger. Используется для логирования ошибок.
     */
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    /**
     * Проигрывает звуковой файл. Принимает имя звукового файла в качестве параметра.
     * Открывает файловый поток для указанного файла, создает объект Player и проигрывает звуковой файл.
     * В случае возникновения ошибки, логирует ошибку.
     * @param soundFile
     */
    public void playSound(String soundFile) {
        try {
            FileInputStream fis = new FileInputStream(FILE_DIR + SEP + soundFile);
            /**
             * Приватное поле player типа Player. Хранит объект Player, который проигрывает звуковой файл.
             */
            Player player = new Player(fis);
            player.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            logger.error("An error occurred while playing the sound file", e);
        }
    }
}