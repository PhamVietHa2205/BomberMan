package com.Bomberman;

import com.Bomberman.controller.SceneController;
import com.Bomberman.data.DataMapManager;
import com.Bomberman.entities.*;
import com.Bomberman.entities.Character;
import com.Bomberman.graphics.Sprite;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.FileNotFoundException;

public class BombermanGame extends Application {
    public static Stage primaryStageGame;
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        BombermanGame.primaryStageGame = primaryStage;
        SceneController.loadMenuGame(primaryStage);

    }

    public static void renderMap() {
        DataMapManager.stillObjects.forEach(Entity::render);
        DataMapManager.entities.forEach(Character::render);
    }

    public static void updateUI() {
        for (int i = 0; i < DataMapManager.entities.size(); i++) {
            DataMapManager.entities.get(i).updateUI();
        }
    }

    public static void loadPlayStage(Stage primaryStage, Pane layer, AnimationTimer threadPlay) {
        StackPane stage = new StackPane();
        VBox root = new VBox(layer);
        stage.getChildren().add(root);
        HBox pane = new HBox();
        Text point = new Text("POINT: " + DataMapManager.point);
        point.setFill(Color.RED);
        point.setStyle("-fx-font: 24 arial;");
        pane.getChildren().add(point);
        pane.setAlignment(Pos.BASELINE_CENTER);
        stage.getChildren().clear();
        stage.getChildren().add(root);
        stage.getChildren().add(pane);
        Scene scene = new Scene(stage, DataMapManager.widthMap * Sprite.SCALED_SIZE, DataMapManager.heightMap * Sprite.SCALED_SIZE);
        scene.setOnKeyPressed(e -> {
                    HBox pane1 = new HBox();
                    Text point1 = new Text("POINT: " + DataMapManager.point);
                    point1.setFill(Color.RED);
                    point1.setStyle("-fx-font: 24 arial;");
                    pane1.getChildren().add(point1);
                    pane1.setAlignment(Pos.BASELINE_CENTER);
                    stage.getChildren().clear();
                    stage.getChildren().add(root);
                    stage.getChildren().add(pane1);
            DataMapManager.input = e.getCode();
        }
        );
        scene.setOnKeyReleased(e -> {
            DataMapManager.input1 = e.getCode();
            HBox pane1 = new HBox();
            Text point1 = new Text("POINT: " + DataMapManager.point);
            point1.setFill(Color.RED);
            point1.setStyle("-fx-font: 24 arial;");
            pane1.getChildren().add(point1);
            pane1.setAlignment(Pos.BASELINE_CENTER);
            stage.getChildren().clear();
            stage.getChildren().add(root);
            stage.getChildren().add(pane1);
        });
        AnimationTimer timer = new AnimationTimer() {
            private long startTime;
            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                super.start();
            }

            @Override
            public void stop() {
                super.stop();
                threadPlay.start();
            }

            public void handle(long now) {
                long curTime = System.currentTimeMillis();
                if (curTime - startTime >= 2000) {
                    primaryStage.setScene(scene);
                    stop();
                }
            }

        };
        timer.start();
    }

    public static void initPLayThread() {
        AnimationTimer playThread = new AnimationTimer() {
            @Override
            public void start() {
                DataMapManager.THREAD_SOUNDTRACK.loop(Clip.LOOP_CONTINUOUSLY);
                super.start();
            }

            @Override
            public void stop() {
                DataMapManager.THREAD_SOUNDTRACK.stop();
                super.stop();
                return;
            }

            private final long sleepNs = 40_000_000;
            long prevTime = 0;
            public void handle(long now) {
                if ((now - prevTime) < sleepNs) { return; }
                prevTime = now;
                updateUI();
                DataMapManager.input = null;
            }

        };
        DataMapManager.THREAD_PLAYER = playThread;
    }
}
