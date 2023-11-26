/*
package com.example.psp6newtry;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BUTTERFLY_RADIUS = 100;

    private static final int MOVEMENT_RANGE = 5;
    private ImageView butterfly;
    private Button startButton;
    private AnimationTimer animationTimer;
    private Random random;

    private double butterflyX;
    private double butterflyY;
    private double butterflyDX;
    private double butterflyDY;

    @Override
    public void start(Stage stage) throws IOException {

        Image image = new Image("D:\\strweb\\psp6newtry\\src\\main\\resources\\com\\example\\psp6newtry\\butterfly1.png");
        butterfly = new ImageView(image);
        butterfly.setFitWidth(BUTTERFLY_RADIUS);
        butterfly.setFitHeight(BUTTERFLY_RADIUS);


        startButton = new Button("Start");
        startButton.setOnAction(event -> startAnimation());


        HBox hBox = new HBox(startButton);
        Pane pane = new Pane(butterfly, hBox);

        Scene scene = new Scene(pane,  WIDTH, HEIGHT);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        random = new Random();
    }

    private void startAnimation() {
        butterflyX = WIDTH / 2;
        butterflyY = HEIGHT / 2;
        butterflyDX = randomMovement();
        butterflyDY = randomMovement();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveButterfly();
            }
        };
        animationTimer.start();
    }

    private void moveButterfly() {
        butterflyX += butterflyDX;
        butterflyY += butterflyDY;

        // Reverse direction if butterfly reaches the edge of the screen
        if (butterflyX <= 0 || butterflyX >= WIDTH - BUTTERFLY_RADIUS) {
            butterflyDX = -butterflyDX;
        }
        if (butterflyY <= 0 || butterflyY >= HEIGHT - BUTTERFLY_RADIUS) {
            butterflyDY = -butterflyDY;
        }


        butterfly.setTranslateX(butterflyX);
        butterfly.setTranslateY(butterflyY);
    }

    private double randomMovement() {
        return random.nextDouble() * MOVEMENT_RANGE - MOVEMENT_RANGE / 2;
    }


    public static void main(String[] args) {
        launch();
    }
}*/

package com.example.psp6newtry;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Random;

public class HelloApplication extends Application {

    private Thread thread;
    private Alert alert;
    private int hits = 0;
    private int misses = 0;

    private ImageView butterfly;

    private ImageView net;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final int BUTTERFLY_RADIUS = 50;
    private static final int MOVEMENT_RANGE = 70;

    private final Image image1 = new Image("D:\\strweb\\psp6newtry\\src\\main\\resources\\butterfly1.png");
    private final Image image2 = new Image("D:\\strweb\\psp6newtry\\src\\main\\resources\\butterfly2.png");


    private boolean isFirstImage = true;


    private double butterflyX;
    private double butterflyY;
    private double butterflyDX;
    private double butterflyDY;

    private Random random;

    @Override
    public void start(Stage primaryStage) {

        alert = new Alert(Alert.AlertType.ERROR);

        Button startButton = new Button("Start");

        Button stopButton = new Button("Stop");

        butterfly = new ImageView(image1);
        butterfly.setFitWidth(BUTTERFLY_RADIUS);
        butterfly.setFitHeight(BUTTERFLY_RADIUS);


        butterflyX = 20;
        butterflyY = 60;
        butterfly.setLayoutX(butterflyX);
        butterfly.setLayoutY(butterflyY);


        net = new ImageView(new Image("D:\\strweb\\psp6newtry\\src\\main\\resources\\net.png"));
        net.setFitWidth(BUTTERFLY_RADIUS * 3);
        net.setFitHeight(BUTTERFLY_RADIUS * 3);
        net.setLayoutY(-70);
        net.setLayoutX(270);

        Pane imagePane = new Pane(butterfly, net);
        imagePane.setPrefSize(WIDTH, HEIGHT);
        imagePane.setStyle(
                "-fx-background-color: rgb(171,219,227);"
        );


        startButton.setOnAction(event -> startMovement());

        stopButton.setOnAction(event -> stopMovement());

        Button catchButton = new Button("Catch");
        catchButton.setOnAction(event -> moveNet());

        VBox root = new VBox(10, startButton, stopButton, catchButton);
        root.setPadding(new Insets(10, 10, 10, 10));
        startButton.setStyle("-fx-background-color: #ffb2b3; -fx-min-width: 120; -fx-text-fill: white; -fx-font-weight: 700; -fx-font-size: 20px");
        stopButton.setStyle("-fx-background-color: rgba(78,220,167,0.77);  -fx-min-width: 120; -fx-text-fill: white;  -fx-font-weight: 700; -fx-font-size: 20px");
        catchButton.setStyle("-fx-background-color: #3d26d7;  -fx-min-width: 120; -fx-text-fill: white;  -fx-font-weight: 700; -fx-font-size: 20px");

        root.setStyle("-fx-background-color: rgba(243,236,236,0.65)");

        HBox vBox = new HBox(root, imagePane);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Thread Example");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                if (thread != null && thread.isAlive()) {
                    // Interrupt the thread to stop it
                    thread.interrupt();
                    thread = null;
                }
            }
        });
        random = new Random();
    }

    private void moveNet() {

        Thread thread = new Thread(() -> {
            int oldval = misses;
            int oldhits = hits;
            if (!hit0rMiss() && misses == oldval) {
                misses++;
            }
            if(hit0rMiss() && hits == oldhits){
                hits++;
            }
            net.setTranslateY(200);
            if (!hit0rMiss() && misses == oldval) {
                misses++;
            }
            if(hit0rMiss() && hits == oldhits){
                hits++;
            }
            try {
                if (!hit0rMiss() && misses == oldval) {
                    misses++;
                }
                if(hit0rMiss() && hits == oldhits){
                    hits++;
                }

                Thread.sleep(500);
                if (!hit0rMiss() && misses == oldval) {
                    misses++;
                }
                if(hit0rMiss() && hits == oldhits){
                    hits++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!hit0rMiss() && misses == oldval) {
                misses++;
            }
            if(hit0rMiss() && hits == oldhits){
                hits++;
            }
            net.setTranslateY(0);
        });

        thread.setDaemon(true);
        thread.start();
    }



    private boolean hit0rMiss() {
        Bounds bounds1 = net.getBoundsInParent();
        Bounds bounds2 = butterfly.getBoundsInParent();

        return bounds1.contains(bounds2);

    }


    private void startMovement() {


        butterflyDX = randomMovement();
        butterflyDY = randomMovement();


        if (thread == null || !thread.isAlive()) {

            thread = new Thread(() -> {

                while (!Thread.currentThread().isInterrupted()) {

                    moveButterfly();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();
                    }
                }
            });

            // Start the thread
            thread.start();
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("thread is already running");
            alert.show();
        }
    }

    private void stopMovement() {
        if (thread != null && thread.isAlive()) {
            // Interrupt the thread to stop it
            thread.interrupt();
            thread = null;
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Hits: " + hits + "\nMisses: " + (misses-hits));
            alert.show();
            hits = 0;
            misses = 0;
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Thread is not running");
            alert.show();
        }
    }


    private void moveButterfly() {
        butterflyX += butterflyDX;
        butterflyY += butterflyDY;

        // Reverse direction if butterfly reaches the edge of the screen
        if (butterflyX <= 10 || butterflyX >= WIDTH - BUTTERFLY_RADIUS * 2) {
            butterflyDX = -butterflyDX;
        }
        if (butterflyY <= 50 || butterflyY >= HEIGHT - BUTTERFLY_RADIUS * 2) {
            butterflyDY = -butterflyDY;
        }

        if(isFirstImage){
            butterfly.setImage(image2);
            isFirstImage = false;
        } else {
            butterfly.setImage(image1);
            isFirstImage = true;
        }

        butterfly.setLayoutX(butterflyX);
        butterfly.setLayoutY(butterflyY);
    }

    private double randomMovement() {
        return random.nextDouble() * MOVEMENT_RANGE- MOVEMENT_RANGE / 2;
    }

    public static void main(String[] args) {
        launch(args);
    }
}