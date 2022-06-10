package com.Bomberman.data;

import com.Bomberman.controller.PlaySound;
import com.Bomberman.entities.Bomber;
import com.Bomberman.entities.Character;
import com.Bomberman.entities.Entity;
import com.Bomberman.entities.FixedEntity;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import javax.sound.sampled.Clip;
import java.util.List;
import java.util.Map;

public class DataMapManager {
    public static int maxLevelMap = 2;
    public static int levelMap = 1;
    public static int point = 0;
    public static ReadMapData readMapData = new ReadMapData("Level" + levelMap + ".txt");
    public static List<Character> entities = readMapData.entities;
    public static List<FixedEntity> stillObjects = readMapData.stillObjects;
    public static int widthMap = readMapData.widthMap;
    public static int heightMap = readMapData.heightMap;
    public static int amountEnemy = entities.size() - 1;
    public static Bomber player = readMapData.player;

    /** Lưu tọa độ các vật thể đang hiển thị trên layer.*/
    public static Map<String, String> mapData = readMapData.mapData;

    /**Lưu trữ tọa độ của Brick, Item.*/
    public static Map<String, FixedEntity> mapGift = readMapData.mapGift;

    public static Clip THREAD_SOUNDTRACK = PlaySound.loopPlaySound("stage.wav");
    public static Pane playLayer = new Pane();
    public static AnimationTimer THREAD_PLAYER;
    public static KeyCode input;
    public static KeyCode input1;

    public static void updateData(String pathNextMap) {
        DataMapManager.readMapData = new ReadMapData(pathNextMap);
        DataMapManager.entities = readMapData.entities;
        DataMapManager.stillObjects = readMapData.stillObjects;
        DataMapManager.levelMap = readMapData.levelMap;
        DataMapManager.widthMap = readMapData.widthMap;
        DataMapManager.heightMap = readMapData.heightMap;
        DataMapManager.amountEnemy = entities.size() - 1;
        DataMapManager.player = readMapData.player;
        DataMapManager.mapData = readMapData.mapData;
        DataMapManager.mapGift = readMapData.mapGift;
        DataMapManager.playLayer = new Pane();
    }

    public static void main(String[] args) {
        System.out.println("level :" + DataMapManager.levelMap);
        DataMapManager.updateData("Level" + levelMap + ".txt");
    }
}
