package com.Bomberman.controller;

import com.Bomberman.BombermanGame;
import com.Bomberman.data.DataMapManager;
import com.Bomberman.entities.Bomber;
import com.Bomberman.entities.Entity;
import com.Bomberman.graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javax.sound.sampled.Clip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SceneController {
    public static Pane initIntroStage(String intro) {
        Pane stage = new Pane();
        BackgroundFill myBF = new BackgroundFill(Color.BLACK, new CornerRadii(0),
                new Insets(0.0,0.0,0.0,0.0));
        stage.setPrefSize(DataMapManager.widthMap * Sprite.SCALED_SIZE, DataMapManager.heightMap * Sprite.SCALED_SIZE);
        stage.setBackground(new Background(myBF));

        Text stageMap = new Text();
        stageMap.setFont(Font.font(null, FontWeight.BOLD, 30));
        stageMap.setStroke(Color.BLACK);
        stageMap.setFill(Color.WHITE);
        stageMap.setText(intro);
        stageMap.resize(100, 50);
        double posX = DataMapManager.widthMap * Sprite.SCALED_SIZE / 2.0 - 50;
        double posY = DataMapManager.heightMap * Sprite.SCALED_SIZE / 2.0 - 25;
        stageMap.relocate( posX, posY);

        stage.getChildren().add(stageMap);
        return stage;
    }

    public static void loadIntroStage(Stage primaryStage, Pane introLayer) {
        VBox root = new VBox();
        root.getChildren().add(introLayer);
        Scene scene = new Scene(root, DataMapManager.widthMap * Sprite.SCALED_SIZE, DataMapManager.heightMap * Sprite.SCALED_SIZE);
        primaryStage.setScene(scene);
    }

    public static void loadMenuGame(Stage primaryStage) throws FileNotFoundException {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1000, 500);
        InputStream input = new FileInputStream("C:\\Users\\admin\\Desktop\\Bomberman\\src\\main\\resources\\background.png");
        Image background = new Image(input);
        ImageView imageView = new ImageView(background);
        Image st = new Image(new FileInputStream("C:\\Users\\admin\\Desktop\\Bomberman\\src\\main\\resources\\startButton.png"));
        ImageView start = new ImageView(st);
        start.setFitWidth(150);
        start.setFitHeight(50);
        Image qt = new Image(new FileInputStream("C:\\Users\\admin\\Desktop\\Bomberman\\src\\main\\resources\\quitButton.png"));
        ImageView quit = new ImageView(qt);
        quit.setFitWidth(150);
        quit.setFitHeight(50);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(500);
        root.getChildren().add(imageView);
        VBox buttonBox = new VBox();
        Button startButton = new Button();
        startButton.setGraphic(start);
        startButton.relocate(100, 100);
        Button quitButton = new Button();
        quitButton.setGraphic(quit);
        buttonBox.getChildren().addAll(startButton, quitButton);
        root.getChildren().add(buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        primaryStage.setTitle("Bomberman Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        startButton.setOnMouseClicked(event -> {
            SceneController.loadIntroStage(primaryStage, SceneController.initIntroStage("STAGE " + DataMapManager.levelMap));
            primaryStage.show();

            BombermanGame.initPLayThread();
            BombermanGame.renderMap();
            BombermanGame.loadPlayStage(primaryStage, DataMapManager.playLayer, DataMapManager.THREAD_PLAYER);
        });
        quitButton.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void loadNextMapStage(Stage primaryStage) {
        DataMapManager.THREAD_SOUNDTRACK.stop();

        AnimationTimer timer = new AnimationTimer() {
            private long startTime;
            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                super.start();
                loadIntroStage(primaryStage, initIntroStage("STAGE " + DataMapManager.levelMap));
            }

            @Override
            public void stop() {
                DataMapManager.THREAD_SOUNDTRACK.loop(Clip.LOOP_CONTINUOUSLY);
                super.stop();
            }

            public void handle(long now) {
                long curTime = System.currentTimeMillis();

                if (curTime - startTime >= 2000) {
                    VBox root = new VBox(DataMapManager.playLayer);
                    Scene scene = new Scene(root, DataMapManager.widthMap * Sprite.SCALED_SIZE, DataMapManager.heightMap * Sprite.SCALED_SIZE);
                    scene.setOnKeyPressed(e -> {
                        DataMapManager.input = e.getCode();
                    });
                    primaryStage.setScene(scene);
                    DataMapManager.stillObjects.forEach(Entity::render);
                    stop();
                }
            }

        };
        timer.start();

    }
}
