package com.Bomberman.data;

import com.Bomberman.entities.*;
import com.Bomberman.entities.Character;
import com.Bomberman.gift.*;
import com.Bomberman.entities.Portal;
import com.Bomberman.graphics.Sprite;
import java.io.File;
import java.util.*;

public class ReadMapData {
    public List<Character> entities = new ArrayList<>();
    public List<FixedEntity> stillObjects = new ArrayList<>();
    public int widthMap;
    public int heightMap;
    public Map<String, String> mapData = new HashMap<>();
    public Map<String, FixedEntity> mapGift = new HashMap<>();
    public Bomber player;
    public int levelMap;

    public ReadMapData(String pathLevel) {
        readMapData(pathLevel);
    }

    private void readMapData(String pathLevel) {
        System.out.println(pathLevel + " READING MAP DATA.....");
        File file = new File("src/main/resources/levels/" + pathLevel);
        try {
            Scanner sc = new Scanner(file);
            String[] param = sc.nextLine().split(" ");

            int row = Integer.parseInt(param[1]);
            int col = Integer.parseInt(param[2]);

            this.levelMap = Integer.parseInt(param[0]);
            this.widthMap = col;
            this.heightMap = row;

            for (int i = 0; i < row; i++) {
                char[] temp = sc.nextLine().toCharArray();
                for (int j = 0; j < temp.length; j++) {
                    switch (temp[j]) {
                        case '#':
                            mapData.put(j + ";" + i, "wall");
                            stillObjects.add(new Wall(j , i, Sprite.wall.getFxImage()));
                            break;
                        case '*':
                            mapData.put(j + ";" + i, "brick");
                            FixedEntity insideBrick = new Grass(j, i, Sprite.grass.getFxImage());
                            FixedEntity brick = new Brick(j, i, Sprite.brick.getFxImage(), insideBrick, "grass");
                            stillObjects.add(insideBrick);
                            stillObjects.add(brick);
                            mapGift.put(j + ";" + i, brick);
                            break;
                        case 'x':
                            mapData.put(j + ";" + i, "brick");
                            FixedEntity belowPortal = new Grass(j, i, Sprite.grass.getFxImage());
                            FixedEntity portal = new Portal(j, i, Sprite.portal.getFxImage());
                            FixedEntity containPortal = new Brick(j, i, Sprite.brick.getFxImage(), portal, "portal");
                            stillObjects.add(belowPortal);
                            stillObjects.add(portal);
                            stillObjects.add(containPortal);
                            mapGift.put(j + ";" + i, containPortal);
                            break;
                        case 'p':
                            mapData.put(j + ";" + i, "grass");
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            player = new Bomber(j, i, Sprite.player_right.getFxImage(), "player_right");
                            entities.add(player);
                            break;
                        case '1':
                            mapData.put(j + ";" + i, "ballon");
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            entities.add(new Balloon(j, i, Sprite.balloom_left1.getFxImage()));
                            break;
                        case '2':
                            mapData.put(j + ";" + i, "oneal");
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            entities.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage()));
                            break;
                        case 'b':
                            mapData.put(j + ";" + i,"brick");
                            FixedEntity insideBomb = new Grass(j, i, Sprite.grass.getFxImage());
                            FixedEntity bomb = new BombItem(j, i, Sprite.powerup_bombs.getFxImage(), insideBomb, "grass");
                            FixedEntity cotainBomb = new Brick(j, i, Sprite.brick.getFxImage(), bomb, "bomb item");
                            stillObjects.add(insideBomb);
                            stillObjects.add(bomb);
                            stillObjects.add(cotainBomb);
                            mapGift.put(j + ";" + i, cotainBomb);
                            break;
                        case 'f':
                            mapData.put(j + ";" + i, "brick");
                            FixedEntity insideFlame = new Grass(j, i, Sprite.grass.getFxImage());
                            FixedEntity flame = new FlameItem(j, i, Sprite.powerup_flames.getFxImage(), insideFlame, "grass");
                            FixedEntity containFlame = new Brick(j, i, Sprite.brick.getFxImage(), flame, "flame item");
                            stillObjects.add(insideFlame);
                            stillObjects.add(flame);
                            stillObjects.add(containFlame);
                            mapGift.put(j + ";" + i, containFlame);
                            break;
                        case 's':
                            mapData.put(j + ";" + i, "brick");
                            FixedEntity insideSpeed = new Grass(j, i, Sprite.grass.getFxImage());
                            FixedEntity speed = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage(), insideSpeed, "grass");
                            FixedEntity containSpeed = new Brick(j, i, Sprite.brick.getFxImage(), speed, "speed item");
                            stillObjects.add(insideSpeed);
                            stillObjects.add(speed);
                            stillObjects.add(containSpeed);
                            mapGift.put(j + ";" + i, containSpeed);
                            break;
                        default:
                            mapData.put(j + ";" + i, "grass");
                            stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                    }

                }
            }
            System.out.println("DONE !!!");
        } catch (Exception e) {
            System.out.println(pathLevel + "ERROR LOAD DATA MAP !!!");
            e.printStackTrace();
        }
    }

}
