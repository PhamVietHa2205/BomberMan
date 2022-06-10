package com.Bomberman.entities;

import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public abstract class Character extends Entity {
    public Character(int x, int y, Image img) {
        super(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE, img);
        this.imageView.relocate(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    public abstract void updateUI();

    public abstract void updatePhysics();
}
