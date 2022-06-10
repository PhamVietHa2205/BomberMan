package com.Bomberman.gift;

import com.Bomberman.data.DataMapManager;
import com.Bomberman.entities.FixedEntity;
import javafx.scene.image.Image;

public class Gift extends FixedEntity {
    protected boolean isExist = true;
    protected FixedEntity insideObj;
    protected String nameInsideObj;


    public Gift(int x, int y, Image img, FixedEntity insideObj, String nameInsideObj) {
        super(x, y, img);
        this.insideObj = insideObj;
        this.nameInsideObj = nameInsideObj;
    }

    public void updateExist() {
        this.isExist = false;
        DataMapManager.playLayer.getChildren().remove(this.imageView);
        DataMapManager.mapData.put(this.x + ";" + this.y, this.nameInsideObj);
        DataMapManager.mapGift.put(this.x + ";" + this.y, this.insideObj);
    }
}
