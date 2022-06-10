package com.Bomberman.entities;

import com.Bomberman.data.DataMapManager;
import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;


public class Oneal extends Enemy {
    private int horizontalSpeed = 0;
    private int verticalSpeed = 1;

    public Oneal(int x, int y, Image img) {
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
        int expectedSpeed = (new Random().nextInt(3) + 2);
        char direction;

        char[] validDirection = findValidDirection(expectedSpeed);
        char[] orderPriority = findOrderPriority();

        direction = chooseDirection(validDirection, orderPriority);

        updateSpeed(direction, expectedSpeed);

    }

    private char[] findValidDirection(int expectedSpeed) {
        char[] validDirection = {'-', '-', '-'};

        if (this.horizontalSpeed == 0) {
            if (this.verticalSpeed > 0) validDirection[0] = 'D';
            else if (this.verticalSpeed < 0) validDirection[0] = 'U';

            boolean leftward = canMove(-expectedSpeed, 0);
            boolean rightward = canMove(expectedSpeed, 0);

            if (leftward) validDirection[1] = 'L';
            if (rightward) validDirection[2] = 'R';
        }
        else if (this.verticalSpeed == 0) {
            if (this.horizontalSpeed > 0) validDirection[0] = 'R';
            else if (this.horizontalSpeed < 0) validDirection[0] = 'L';

            boolean upward = canMove(0, -expectedSpeed);
            boolean downward = canMove(0, expectedSpeed);

            if (upward) validDirection[1] = 'U';
            if (downward) validDirection[2] = 'D';
        }

        return validDirection;
    }

    private char[] findOrderPriority() {
        int xEnemy = this.getX();
        int yEnemy = this.getY();
        int xPlayer = DataMapManager.player.getX();
        int yPlayer = DataMapManager.player.getY();
        int diffX = xEnemy - xPlayer;
        int diffY = yEnemy - yPlayer;

        char[] result = new char[2];
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0) result[0] = 'L';
            else if (diffX < 0)result[0] = 'R';

            if (diffY > 0) result[1] = 'U';
            else if (diffY < 0) result[1] = 'D';
        }
        else if (Math.abs(diffX) < Math.abs(diffY)) {
            if (diffY > 0) result[0] = 'U';
            else if (diffY < 0) result[0] = 'D';

            if (diffX > 0) result[1] = 'L';
            else if (diffX < 0) result[1] = 'R';
        }
        else if (Math.abs(diffX) == Math.abs(diffY)){
            if (diffX > 0) result[0] = 'L';
            else if (diffX < 0)result[0] = 'R';

            if (diffY > 0) result[1] = 'U';
            else if (diffY < 0)result[1] = 'D';
        }

        return result;
    }

    private char chooseDirection(char[] validDirection, char[] orderPriority) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (orderPriority[i] == validDirection[j]) { return orderPriority[i]; }
            }
        }

        return validDirection[0];
    }

    private void updateSpeed(char direction, int expectedSpeed) {
        if (direction == 'U') {
            this.horizontalSpeed = 0;
            this.verticalSpeed = -expectedSpeed;
        }
        else if (direction == 'D') {
            this.horizontalSpeed = 0;
            this.verticalSpeed = expectedSpeed;
        }
        else if (direction == 'L') {
            this.horizontalSpeed = -expectedSpeed;
            this.verticalSpeed = 0;
        }
        else if (direction == 'R') {
            this.horizontalSpeed = expectedSpeed;
            this.verticalSpeed = 0;
        }
    }
}
