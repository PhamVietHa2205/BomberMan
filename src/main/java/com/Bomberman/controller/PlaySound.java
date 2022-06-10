package com.Bomberman.controller;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlaySound {
    public static Clip loopPlaySound(String nameSound){
        Clip clip = null;
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(PlaySound.class.getResource("/sound/" + nameSound));
            clip = AudioSystem.getClip();
            clip.open(audio);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return clip;
    }

    public static void playSound(String filePath) {
        InputStream music;
        try {
            music = new FileInputStream(new File("src/main/resources/sound/" + filePath));
            AudioStream audio = new AudioStream(music);
            AudioPlayer.player.start(audio);

        } catch (Exception e) {
            System.out.println("not here");
        }
    }
}