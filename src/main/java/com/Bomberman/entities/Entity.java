package com.Bomberman.entities;

import com.Bomberman.data.DataMapManager;
import com.Bomberman.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    protected int x;
    protected int y;
    protected Sprite sprite;
    protected int animate = 0;
    protected Image img;
    public ImageView imageView;

    public Entity( int x, int y, Image img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.imageView = new ImageView(img);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public void render() { DataMapManager.playLayer.getChildren().add(imageView); }

    public void removeEntity() {
        DataMapManager.entities.remove(this);
        DataMapManager.playLayer.getChildren().remove(this.imageView);
    }

}
