package com.Bomberman.entities;

import com.Bomberman.data.DataMapManager;
import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Balloon extends Enemy {
    private int horizontalSpeed = 2;
    private int verticalSpeed = 0;

    public Balloon(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void render() {
        imageView.relocate(x, y);
        DataMapManager.playLayer.getChildren().add(imageView);
    }

    @Override
    public void updateUI() {
        updatePhysics();

        DataMapManager.playLayer.getChildren().remove(this.imageView);
        this.imageView = new ImageView(this.img);
        this.imageView.relocate(x, y);
        DataMapManager.playLayer.getChildren().add(this.imageView);
    }

    public void updatePhysics() {
        boolean collision = canMove(this.horizontalSpeed, this.verticalSpeed);
        int changeXEnemy = this.getX() % Sprite.SCALED_SIZE;
        int changeYEnemy = this.getY() % Sprite.SCALED_SIZE;

        if (!collision) {
            this.horizontalSpeed = -1 * this.horizontalSpeed;
            this.verticalSpeed = -1 * this.verticalSpeed;
        }
        if (changeXEnemy == 0 && changeYEnemy == 0) {
            changeDirection();
        }
        this.x += horizontalSpeed;
        this.y += verticalSpeed;
    }

    private boolean canMove(int horSpeed, int verSpeed) {
        if ((this.getX() + horSpeed) % Sprite.SCALED_SIZE == 0
                && (this.getY() + verSpeed) % Sprite.SCALED_SIZE == 0) return true;

        int unitX = (this.getX() + horSpeed) / Sprite.SCALED_SIZE;
        int unitY = (this.getY() + verSpeed) / Sprite.SCALED_SIZE;

        if (verSpeed == 0) {
            String res1 = DataMapManager.mapData.get(unitX + ";" + unitY);
            String res2 = DataMapManager.mapData.get((unitX + 1 ) + ";" + unitY);

            return !res1.equals("wall") && !res1.equals("brick") && !res1.equals("bomb")
                    && !res2.equals("wall") && !res2.equals("brick") && !res2.equals("bomb");
        }
        else if (horSpeed == 0) {
            String res1 = DataMapManager.mapData.get(unitX + ";" + unitY);
            String res2 = DataMapManager.mapData.get(unitX + ";" + (unitY + 1));

            return !res1.equals("wall") && !res1.equals("brick") && !res1.equals("bomb")
                    && !res2.equals("wall") && !res2.equals("brick") && !res2.equals("bomb");
        }
        return false;
    }

    @Override
    protected void changeDirection() {
        if (new Random().nextInt(2) == 0) return;

        int expectedSpeed = 2;
        int direction = -1;

        if (this.horizontalSpeed == 0) {
            boolean leftward = canMove(-expectedSpeed, 0);
            boolean rightward = canMove(expectedSpeed, 0);

            if (leftward && rightward) {
               direction = new Random().nextInt(2);
            }
            else if (leftward) direction = 0;
            else if (rightward) direction = 1;

            if (direction == 0) {
                this.horizontalSpeed = -expectedSpeed;
                this.verticalSpeed = 0;
            }
            else if (direction == 1){
                this.horizontalSpeed = expectedSpeed;
                this.verticalSpeed = 0;
            }
        }
        else if (this.verticalSpeed == 0) {
            boolean upward = canMove(0, -expectedSpeed);
            boolean downward = canMove(0, expectedSpeed);

            if (upward && downward) {
                direction = new Random().nextInt(2);
            }
            else if (upward) direction = 0;
            else if (downward) direction = 1;

            if (direction == 0) {
                this.horizontalSpeed = 0;
                this.verticalSpeed = -expectedSpeed;
            }
            else if (direction == 1){
                this.horizontalSpeed = 0;
                this.verticalSpeed = expectedSpeed;
            }
        }
    }

}
