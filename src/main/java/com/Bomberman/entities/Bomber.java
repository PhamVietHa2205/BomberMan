package com.Bomberman.entities;

import com.Bomberman.BombermanGame;
import com.Bomberman.controller.Rectangle;
import com.Bomberman.controller.SceneController;
import com.Bomberman.data.DataMapManager;
import com.Bomberman.data.ReadMapData;
import com.Bomberman.gift.BombItem;
import com.Bomberman.gift.FlameItem;
import com.Bomberman.gift.SpeedItem;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import com.Bomberman.graphics.Sprite;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.Port;
import javafx.scene.text.Text;
import java.awt.*;
import java.util.ArrayList;

public class Bomber extends Character {
    private String oldIMG = null;
    private String oldPlayerDead = null;
    private boolean isExist = true;
    private int speed = 5;
    private int width = 24;
    private int height = 32;
    private int amountBomb = 1;
    private int rangeFlame = 2;
    private int input;
    private boolean move;
    private ArrayList<Bomb> bombActive = new ArrayList<>();

    public void setExist(boolean exist) {
        this.isExist = exist;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Bomber(int x, int y, Image img, String oldIMG) {
        super(x, y, img);
        this.oldIMG = oldIMG;
    }

    @Override
    public void updateUI() {
        this.input = processInput(DataMapManager.input);

        if (this.input != -1 && isExist) {
            processPutBomb(this.input);
            updatePhysics();
            processPlayerMoveUI(this.input);
        } else processPlayerDelay(DataMapManager.input1);
        DataMapManager.playLayer.getChildren().remove(this.imageView);
        this.imageView = new ImageView(this.img);
        this.imageView.relocate(x, y);
        DataMapManager.playLayer.getChildren().add(this.imageView);
        processGift();
        processCollisionEnemy();
        checkAmountBombActive();

        if (!this.isExist) {
            processPlayerDied();
        }
    }

    /**
     * kiểm tra hướng di chuyển và xử lý. Gọi hàm processMove.
     */
    @Override
    public void updatePhysics() {
        if (this.input == 1) {
            processMove(-speed, 0);
        } else if (this.input == 2) {
            processMove(speed, 0);
        } else if (this.input == 3) {
            processMove(0, -speed);
        } else if (this.input == 4) {
            processMove(0, speed);
        }
        animate = (animate + 1) % 500;
    }

    /**
     * xử lý di chuyển của bomber.
     *
     * @param disX khoảng cách bomber di chuyển sang phải nếu disX > 0 và sang trái nếu disX < 0.
     * @param disY khoảng cách bomber di chuyển xuống nếu disY > 0 và lên trên nếu disY < 0.
     */
    public void processMove(int disX, int disY) {
        if (disX < 0) {
            int X = this.x / Sprite.SCALED_SIZE;
            int Y = this.y / Sprite.SCALED_SIZE;

            Rectangle entity = new Rectangle(this.x + disX, this.y, this.width, this.height);
            Rectangle stillUp = new Rectangle((X - 1) * Sprite.SCALED_SIZE, Y * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            Rectangle stillDown = new Rectangle((X - 1) * Sprite.SCALED_SIZE, (Y + 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            int s1 = entity.overlapArea(stillUp);
            int s2 = entity.overlapArea(stillDown);
            boolean t1 = canMove(X - 1, Y);
            boolean t2 = canMove(X - 1, Y + 1);

            if (s1 == s2 && s1 == 0) {
                this.x += disX;
            } else if (t1 && t2) {
                this.x += disX;
            } else if ((!t1 && t2) && (s1 < s2)) {
                this.x += disX;
                if (s1 != 0) this.y = (Y + 1) * Sprite.SCALED_SIZE;
            } else if ((t1 && !t2) && (s1 > s2)) {
                this.x += disX;
                if (s2 != 0) this.y = (Y + 1) * Sprite.SCALED_SIZE - this.height;
            } else if (!t1 && !t2) {
                this.x = X * Sprite.SCALED_SIZE;
            }

        } else if (disX > 0) {
            int X;
            int Y = this.y / Sprite.SCALED_SIZE;
            if ((this.x + this.width) % Sprite.SCALED_SIZE == 0) {
                X = (this.x + this.width) / Sprite.SCALED_SIZE - 1;
            } else {
                X = (this.x + this.width) / Sprite.SCALED_SIZE;
            }

            Rectangle entity = new Rectangle(this.x + disX, this.y, this.width, this.height);
            Rectangle stillUp = new Rectangle((X + 1) * Sprite.SCALED_SIZE, Y * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            Rectangle stillDown = new Rectangle((X + 1) * Sprite.SCALED_SIZE, (Y + 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            int s1 = entity.overlapArea(stillUp);
            int s2 = entity.overlapArea(stillDown);
            boolean t1 = canMove(X + 1, Y);
            boolean t2 = canMove(X + 1, Y + 1);

            if (s1 == s2 && s1 == 0) {
                this.x += disX;
            } else if (t1 && t2) {
                this.x += disX;
            } else if ((!t1 && t2) && (s1 < s2)) {
                this.x += disX;
                if (s1 != 0) this.y = (Y + 1) * Sprite.SCALED_SIZE;
            } else if ((t1 && !t2) && (s1 > s2)) {
                this.x += disX;
                if (s2 != 0) this.y = (Y + 1) * Sprite.SCALED_SIZE - this.height;
            } else if (!t1 && !t2) {
                this.x = (X + 1) * Sprite.SCALED_SIZE - this.width;
            }
        } else if (disY < 0) {
            int X = this.x / Sprite.SCALED_SIZE;
            int Y = this.y / Sprite.SCALED_SIZE;

            Rectangle entity = new Rectangle(this.x, this.y + disY, this.width, this.height);
            Rectangle stillLeft = new Rectangle(X * Sprite.SCALED_SIZE, (Y - 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            Rectangle stillRight = new Rectangle((X + 1) * Sprite.SCALED_SIZE, (Y - 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            int s1 = entity.overlapArea(stillLeft);
            int s2 = entity.overlapArea(stillRight);
            boolean t1 = canMove(X, Y - 1);
            boolean t2 = canMove(X + 1, Y - 1);

            if (s1 == 0 && s2 == 0) {
                this.y += disY;
            } else if (t1 && t2) {
                this.y += disY;
            } else if ((!t1 && t2) && s1 < s2) {
                this.y += disY;
                if (s1 != 0) this.x = (X + 1) * Sprite.SCALED_SIZE;
            } else if ((t1 && !t2) && s1 > s2) {
                this.y += disY;
                if (s2 != 0) this.x = (X + 1) * Sprite.SCALED_SIZE - this.width;

            } else if (!t1 && !t2) {
                this.y = Y * Sprite.SCALED_SIZE;
            }
        } else if (disY > 0) {
            int X = this.x / Sprite.SCALED_SIZE;
            int Y;
            if ((this.y + this.height) % Sprite.SCALED_SIZE == 0) {
                Y = (this.y + this.height) / Sprite.SCALED_SIZE - 1;
            } else {
                Y = (this.y + this.height) / Sprite.SCALED_SIZE;
            }

            Rectangle entity = new Rectangle(this.x, this.y + disY, this.width, this.height);
            Rectangle stillLeft = new Rectangle(X * Sprite.SCALED_SIZE, (Y + 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            Rectangle stillRight = new Rectangle((X + 1) * Sprite.SCALED_SIZE, (Y + 1) * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);

            int s1 = entity.overlapArea(stillLeft);
            int s2 = entity.overlapArea(stillRight);
            boolean t1 = canMove(X, Y + 1);
            boolean t2 = canMove(X + 1, Y + 1);

            if (s1 == s2 && s1 == 0) {
                this.y += disY;
            } else if (t1 && t2) {
                this.y += disY;
            } else if ((!t1 && t2) && (s1 < s2)) {
                this.y += disY;
                if (s1 != 0) this.x = (X + 1) * Sprite.SCALED_SIZE;
            } else if ((t1 && !t2) && (s1 > s2)) {
                this.y += disY;
                if (s2 != 0) this.x = (X + 1) * Sprite.SCALED_SIZE - this.width;
            } else if (!t1 && !t2) {
                this.y = (Y + 1) * Sprite.SCALED_SIZE - this.height;
            }
        }
    }

    /**
     * xử lý ăn item.
     */
    public void processGift() {
        int unitX;
        int unitY;

        double numX = Math.round((this.x * 10.0) / Sprite.SCALED_SIZE) / 10.0;
        double numY = Math.round((this.y * 10.0) / Sprite.SCALED_SIZE) / 10.0;

        if (numX - (int) numX > 0.5) unitX = (int) numX + 1;
        else unitX = (int) numX;
        if (numY - (int) numY > 0.5) unitY = (int) numY + 1;
        else unitY = (int) numY;

        String response = DataMapManager.mapData.get(unitX + ";" + unitY);

        if (response.equals("flame item")) {
            this.rangeFlame += 1;
            ((FlameItem) DataMapManager.mapGift.get(unitX + ";" + unitY)).updateExist();
        } else if (response.equals("speed item")) {
            this.speed += 2;
            ((SpeedItem) DataMapManager.mapGift.get(unitX + ";" + unitY)).updateExist();
        } else if (response.equals("bomb item")) {
            this.amountBomb += 1;
            ((BombItem) DataMapManager.mapGift.get(unitX + ";" + unitY)).updateExist();
        } else if (response.equals("portal") && DataMapManager.amountEnemy == 0) {
            if (DataMapManager.levelMap < DataMapManager.maxLevelMap) {
                System.out.println("next map");
                DataMapManager.updateData("Level" + (DataMapManager.levelMap + 1) + ".txt");
                SceneController.loadNextMapStage(BombermanGame.primaryStageGame);
            }
            else {
                Stage primaryStage = BombermanGame.primaryStageGame;
                DataMapManager.THREAD_PLAYER.stop();
                SceneController.loadIntroStage(primaryStage, SceneController.initIntroStage("YOU WIN!"));
                primaryStage.show();
            }
        }
    }

    /**
     * xử lý đặt bom
     *
     * @param req nếu là 0 thì tiến hành đặt bom.
     */
    public void processPutBomb(int req) {
        if (req != 0) return;
        if (bombActive.size() >= amountBomb) return;
        int unitX;
        int unitY;

        double numX = Math.round((this.x * 10.0) / Sprite.SCALED_SIZE) / 10.0;
        double numY = Math.round((this.y * 10.0) / Sprite.SCALED_SIZE) / 10.0;

        if (numX - (int) numX > 0.5) unitX = (int) numX + 1;
        else unitX = (int) numX;
        if (numY - (int) numY > 0.5) unitY = (int) numY + 1;
        else unitY = (int) numY;

        Bomb bomb = new Bomb(unitX, unitY, Sprite.bomb.getFxImage(), this.rangeFlame);
        DataMapManager.mapData.put(unitX + ";" + unitY, "bomb");
        bomb.activeBomb();
        bombActive.add(bomb);
    }

    /**
     * xử lý số lượng đặt bom.
     */
    public void checkAmountBombActive() {
        bombActive.removeIf(bomb -> bomb.isExploded);
    }

    /**
     * @param keyCode đầu vào là 1 phím do người dùng nhấn xuống
     * @return 0 là phím space để đặt bom, 1 là phím trái hoặc A,
     * 2 là phím phải hoặc D, 3 là phím lên hoặc W, 4 là phím xuống hoặc S.
     */
    public int processInput(KeyCode keyCode) {
        if (keyCode == null) return -1;
        if (keyCode == KeyCode.SPACE) return 0;
        else if ((keyCode == KeyCode.LEFT || keyCode == KeyCode.A)) {
            return 1;
        } else if ((keyCode == KeyCode.RIGHT || keyCode == KeyCode.D)) {
            return 2;
        } else if ((keyCode == KeyCode.UP || keyCode == KeyCode.W)) {
            return 3;
        } else if ((keyCode == KeyCode.DOWN || keyCode == KeyCode.S)) {
            return 4;
        }

        return -1;
    }

    /**
     * cập nhật lại hình ảnh bomber mỗi lúc di chuyển.
     *
     * @param req yêu cầu về hướng di chuyển.
     */
    public void processPlayerMoveUI(int req) {
        switch (req) {
            case 1:
                this.img = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                break;
            case 2:
                this.img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                break;
            case 3:
                this.img = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                break;
            case 4:
                this.img = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                break;
            default:
                this.img = Sprite.player_right.getFxImage();
                break;
        }
    }

    /**
     * xử lí delay
     */
    public void processPlayerDelay(KeyCode keyCode) {
        if (keyCode == KeyCode.LEFT || keyCode == KeyCode.A)
            this.img = Sprite.player_left.getFxImage();
        else if (keyCode == KeyCode.RIGHT || keyCode == keyCode.D)
            this.img = Sprite.player_right.getFxImage();
        else if (keyCode == KeyCode.UP || keyCode == KeyCode.W)
            this.img = Sprite.player_up.getFxImage();
        else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.S)
            this.img = Sprite.player_down.getFxImage();
    }

    /**
     * kiểm tra xem có thể đi qua ô tọa độ(x, y) không ?
     *
     * @param x tọa độ điểm x trên hệ tọa độ đơn vị.
     * @param y tọa độ điểm y trên hệ tọa độ đơn vị.
     * @return true nếu có thể đi qua, false nếu không đi qua được.
     */
    public boolean canMove(int x, int y) {
        String respone = DataMapManager.mapData.get(x + ";" + y);

        return !respone.equals("wall") && !respone.equals("brick") && !respone.equals("bomb");
    }

    /**
     * xử lý khi người chơi va chạm với enemy.
     */
    public void processCollisionEnemy() {
        Rectangle recPlayer = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        for (int i = 1; i < DataMapManager.entities.size(); i++) {
            Enemy obj = (Enemy) DataMapManager.entities.get(i);
            Rectangle recEnemy = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());

            if (recEnemy.overlapArea(recPlayer) != 0) {
                this.isExist = false;
                break;
            }
        }

    }

    /**
     * xử lý UI và âm thanh người chơi chết
     */
    public void processPlayerDied() {
        if (oldPlayerDead == null) {
            this.img = Sprite.player_dead1.getFxImage();
            oldPlayerDead = "player_dead1";
        } else if (oldPlayerDead.equals("player_dead1")) {
            this.img = Sprite.player_dead2.getFxImage();
            oldPlayerDead = "player_dead2";
        } else if (oldPlayerDead.equals("player_dead2")) {
            this.img = Sprite.player_dead3.getFxImage();
            oldPlayerDead = "player_dead3";
        } else if (oldPlayerDead.equals("player_dead3")) {
            this.removeEntity();
            DataMapManager.THREAD_PLAYER.stop();
            Stage primaryStage = BombermanGame.primaryStageGame;
            SceneController.loadIntroStage(primaryStage, SceneController.initIntroStage("GAME OVER!"));
            primaryStage.show();
        }
    }
    public Image getImg() {
        return this.img;
    }
}
