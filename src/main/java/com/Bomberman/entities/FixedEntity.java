package com.Bomberman.entities;

import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public abstract class FixedEntity extends Entity {
    private int width = Sprite.SCALED_SIZE;
    private int height = Sprite.SCALED_SIZE;

    public FixedEntity(int x, int y, Image img) {
        super(x, y, img);
        this.imageView.relocate(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
