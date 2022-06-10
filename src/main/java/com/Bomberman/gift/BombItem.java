package com.Bomberman.gift;

import com.Bomberman.entities.FixedEntity;
import javafx.scene.image.Image;

public class BombItem extends Gift {
    public BombItem(int x, int y, Image img, FixedEntity insideObj, String nameInsideObj) {
        super(x, y, img, insideObj, nameInsideObj);
    }
}
