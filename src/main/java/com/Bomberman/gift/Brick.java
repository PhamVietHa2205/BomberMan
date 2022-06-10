package com.Bomberman.gift;

import com.Bomberman.entities.FixedEntity;
import javafx.scene.image.Image;

public class Brick extends Gift {
    public Brick(int x, int y, Image img, FixedEntity insideObj, String nameInsideObj) {
        super(x, y, img, insideObj, nameInsideObj);
    }
}
