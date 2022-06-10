package com.Bomberman.entities;

import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public abstract class Enemy extends Character {
    private int width = Sprite.SCALED_SIZE;
    private int height = Sprite.SCALED_SIZE;

    public Enemy(int x, int y, Image img) {
        super(x, y, img);
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    protected abstract void changeDirection();
}
