package com.Bomberman.entities;

import com.Bomberman.controller.Rectangle;
import com.Bomberman.data.DataMapManager;
import com.Bomberman.gift.Brick;
import com.Bomberman.graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Flame {
    private String oldIMG = null;
    private boolean finished = false;
    private final int rangeFlame;
    private int rangeLeft;
    private int rangeRight;
    private int rangeTop;
    private int rangeBottom;
    protected ArrayList<Brick> brickExploded = new ArrayList<>();
    protected ArrayList<Entity> enemyExploded = new ArrayList<>();
    protected ArrayList<ImageView> flameIV = new ArrayList<>();
    protected  int x;
    protected int y;

    public Flame(int x, int y, int rangeFlame) {
        this.x = x;
        this.y = y;
        this.rangeFlame = rangeFlame;
    }

    public void flameActive() {
        this.rangeLeft = scanBarrierX(this.x, this.y, this.rangeFlame, -1);
        this.rangeRight = scanBarrierX(this.x, this.y, this.rangeFlame, 1);
        this.rangeTop = scanBarrierY(this.x, this.y, this.rangeFlame, -1);
        this.rangeBottom = scanBarrierY(this.x, this.y, this.rangeFlame, 1);
        destroyEnemyPhysics();
        destroyBrickPhysics();

        AnimationTimer timer = new AnimationTimer() {
            private long prev = 0;
            @Override
            public void handle(long now) {
                if (now - prev < 80_000_000) {
                    return;
                } else {
                    prev = now;
                    updateFlameUI();
                    if (finished) { stop(); }
                }
            }
        };
        timer.start();

    }

    public void updateFlameUI() {
        animationLayer();
    }

    private void destroyBrickUI(Image brickExp) {
        for (int i = 0; i < brickExploded.size(); i++) {
            ImageView brick = new ImageView(brickExp);
            brick.relocate(brickExploded.get(i).x * Sprite.SCALED_SIZE, brickExploded.get(i).y * Sprite.SCALED_SIZE);
            flameIV.add(brick);
        }
    }

    private void destroyBrickPhysics() {
        brickExploded.forEach(Brick::updateExist);
    }

    private void destroyEnemyPhysics() {
        for (int i = 0; i < enemyExploded.size(); i++) {
            Entity entity = enemyExploded.get(i);
            if (entity instanceof Bomber) {
                ((Bomber) entity).setExist(false);
                enemyExploded.remove(entity);
                i -= 1;
            }
            else {
                DataMapManager.amountEnemy -= 1;
                DataMapManager.point += 100;
                entity.removeEntity();
            }

        }
    }

    private void destroyEnemyUI() {
        for (Entity entity : enemyExploded) {
            ImageView enemy = null;
            if (entity instanceof Balloon) enemy = new ImageView(Sprite.balloom_dead.getFxImage());
            else if (entity instanceof Oneal) enemy = new ImageView(Sprite.oneal_dead.getFxImage());

            enemy.relocate(entity.getX(), entity.getY());
            flameIV.add(enemy);
        }
    }

    private int scanBarrierX(int xRoot, int yRoot, int range, int factor) {
        for (int i = (int)(0.5 - 0.5 * factor); i <= range; i ++) {
            int xFlame = (xRoot + factor * i) * Sprite.SCALED_SIZE;
            int yFlame = yRoot * Sprite.SCALED_SIZE;
            Rectangle flame = new Rectangle(xFlame, yFlame, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            for (int j = 0; j < DataMapManager.entities.size(); j++) {
                Entity enemyObj = DataMapManager.entities.get(j);
                Rectangle enemy = new Rectangle(enemyObj.getX(), enemyObj.getY(), enemyObj.getWidth(), enemyObj.getHeight());

                if (flame.overlapArea(enemy) != 0) {
                    enemyExploded.add(enemyObj);
                    if(i != 0 && j != 0) return Math.abs(factor * i);
                }
            }

            String key = (xRoot + factor * i) + ";" + yRoot;
            String val = DataMapManager.mapData.get(key);
            if (val.equals("brick")) {
                brickExploded.add((Brick) DataMapManager.mapGift.get(key));
                return Math.abs(factor * i) - 1;
            }
            else if (val.equals("wall")) return Math.abs(factor * i) - 1;

        }
        return Math.abs(factor * range);
    }

    private int scanBarrierY(int xRoot, int yRoot, int range, int factor) {
        for (int i = 1; i <= range; i ++) {
            int xFlame = xRoot * Sprite.SCALED_SIZE;
            int yFlame = (yRoot + factor * i) * Sprite.SCALED_SIZE;
            Rectangle flame = new Rectangle(xFlame, yFlame, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            for (int j = 0; j < DataMapManager.entities.size(); j++) {
                Entity enemyObj = DataMapManager.entities.get(j);
                Rectangle enemy = new Rectangle(enemyObj.getX(), enemyObj.getY(), enemyObj.getWidth(), enemyObj.getHeight());

                if (flame.overlapArea(enemy) != 0) {
                    enemyExploded.add(enemyObj);
                    if(j != 0) return Math.abs(factor * i);
                }
            }

            String key = xRoot  + ";" + (yRoot + factor * i);
            String val = DataMapManager.mapData.get(key);
            if (val.equals("brick")) {
                brickExploded.add((Brick) DataMapManager.mapGift.get(key));
                return Math.abs(factor * i) - 1;
            }
            else if (val.equals("wall")) return Math.abs(factor * i) - 1;
        }

        return Math.abs(factor * range);

    }

    private void addImgToLayer(Image lastLeftImg, Image lastRightImg, Image lastTopImg, Image lastBottomImg,
                               Image hori, Image ver, Image centerImg) {

        for (int i = 1; i <= rangeLeft - 1; i++) {
            ImageView horizontalLeft = new ImageView(hori);
            horizontalLeft.relocate((this.x - i) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(horizontalLeft);
        }
        for (int i = 1; i <= rangeRight - 1; i++) {
            ImageView horizontalRight = new ImageView(hori);
            horizontalRight.relocate((this.x + i) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(horizontalRight);
        }
        for (int i = 1; i <= rangeTop - 1; i++) {
            ImageView verticalTop = new ImageView(ver);
            verticalTop.relocate(this.x * Sprite.SCALED_SIZE, (this.y - i) * Sprite.SCALED_SIZE);
            flameIV.add(verticalTop);
        }
        for (int i = 1; i <= rangeBottom - 1; i++) {
            ImageView verticalBottom = new ImageView(ver);
            verticalBottom.relocate(this.x * Sprite.SCALED_SIZE, (this.y + i) * Sprite.SCALED_SIZE);
            flameIV.add(verticalBottom);
        }

        ImageView center = new ImageView(centerImg);
        center.relocate(this.x * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
        flameIV.add(center);

        if (rangeLeft == this.rangeFlame) {
            ImageView lastLeft = new ImageView(lastLeftImg);
            lastLeft.relocate((this.x - rangeLeft) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(lastLeft);
        }
        else if (rangeLeft != 0){
            ImageView horizontalLeft = new ImageView(hori);
            horizontalLeft.relocate((this.x - rangeLeft) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(horizontalLeft);
        }

        if (rangeRight == this.rangeFlame) {
            ImageView lastRight = new ImageView(lastRightImg);
            lastRight.relocate((this.x + rangeRight) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(lastRight);
        }
        else if (rangeRight != 0){
            ImageView horizontalRight = new ImageView(hori);
            horizontalRight.relocate((this.x + rangeRight) * Sprite.SCALED_SIZE, this.y * Sprite.SCALED_SIZE);
            flameIV.add(horizontalRight);
        }

        if (rangeTop == this.rangeFlame) {
            ImageView lastTop = new ImageView(lastTopImg);
            lastTop.relocate(this.x * Sprite.SCALED_SIZE, (this.y - rangeTop) * Sprite.SCALED_SIZE);
            flameIV.add(lastTop);
        }
        else if (rangeTop != 0) {
            ImageView verticalTop = new ImageView(ver);
            verticalTop.relocate(this.x * Sprite.SCALED_SIZE, (this.y - rangeTop) * Sprite.SCALED_SIZE);
            flameIV.add(verticalTop);
        }

        if (rangeBottom == this.rangeFlame) {
            ImageView lastBottom = new ImageView(lastBottomImg);
            lastBottom.relocate(this.x * Sprite.SCALED_SIZE, (this.y + rangeBottom) * Sprite.SCALED_SIZE);
            flameIV.add(lastBottom);
        }
        else if (rangeBottom != 0) {
            ImageView verticalBottom = new ImageView(ver);
            verticalBottom.relocate(this.x * Sprite.SCALED_SIZE, (this.y + rangeBottom) * Sprite.SCALED_SIZE);
            flameIV.add(verticalBottom);
        }

    }

    private void animationLayer() {
        DataMapManager.playLayer.getChildren().removeAll(flameIV);
        flameIV.clear();

        if (this.oldIMG == null) {
            Image lastLeftImg = Sprite.explosion_horizontal_left_last.getFxImage();
            Image lastRightImg = Sprite.explosion_horizontal_right_last.getFxImage();
            Image lastTopImg = Sprite.explosion_vertical_top_last.getFxImage();
            Image lastBottomImg = Sprite.explosion_vertical_down_last.getFxImage();
            Image hori = Sprite.explosion_horizontal.getFxImage();
            Image ver = Sprite.explosion_vertical.getFxImage();
            Image centerImg = Sprite.bomb_exploded.getFxImage();
            Image brickExp = Sprite.brick_exploded.getFxImage();

            this.oldIMG = "exploded";
            addImgToLayer(lastLeftImg, lastRightImg, lastTopImg, lastBottomImg, hori, ver, centerImg);
            destroyBrickUI(brickExp);
            destroyEnemyUI();
        }
        else if (this.oldIMG.equals("exploded")) {
            Image lastLeftImg = Sprite.explosion_horizontal_left_last1.getFxImage();
            Image lastRightImg = Sprite.explosion_horizontal_right_last1.getFxImage();
            Image lastTopImg = Sprite.explosion_vertical_top_last1.getFxImage();
            Image lastBottomImg = Sprite.explosion_vertical_down_last1.getFxImage();
            Image hori = Sprite.explosion_horizontal1.getFxImage();
            Image ver = Sprite.explosion_vertical1.getFxImage();
            Image centerImg = Sprite.bomb_exploded1.getFxImage();
            Image brickExp = Sprite.brick_exploded1.getFxImage();

            this.oldIMG = "exploded1";
            addImgToLayer(lastLeftImg, lastRightImg, lastTopImg, lastBottomImg, hori, ver, centerImg);
            destroyBrickUI(brickExp);
            destroyEnemyUI();
        }
        else if (this.oldIMG.equals("exploded1")) {
            Image lastLeftImg = Sprite.explosion_horizontal_left_last2.getFxImage();
            Image lastRightImg = Sprite.explosion_horizontal_right_last2.getFxImage();
            Image lastTopImg = Sprite.explosion_vertical_top_last2.getFxImage();
            Image lastBottomImg = Sprite.explosion_vertical_down_last2.getFxImage();
            Image hori = Sprite.explosion_horizontal2.getFxImage();
            Image ver = Sprite.explosion_vertical2.getFxImage();
            Image centerImg = Sprite.bomb_exploded2.getFxImage();
            Image brickExp = Sprite.brick_exploded2.getFxImage();

            this.oldIMG = "exploded2";
            addImgToLayer(lastLeftImg, lastRightImg, lastTopImg, lastBottomImg, hori, ver, centerImg);
            destroyBrickUI(brickExp);
            destroyEnemyUI();
        }
        else if (this.oldIMG.equals("exploded2")) {
            DataMapManager.playLayer.getChildren().removeAll(flameIV);
            flameIV.clear();
            this.finished = true;
            return;
        }

        DataMapManager.playLayer.getChildren().addAll(flameIV);
    }
}
