package org.openjfx.javaproject;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import org.openjfx.javaproject.room.Autorobot;
import org.openjfx.javaproject.common.Obstacle;

import org.openjfx.javaproject.common.ConfigParser;

import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;


import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.ui.buttons.*;


import javafx.stage.FileChooser;
import java.io.File;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;


/**
 * The main class for the Robot Simulator application.
 */
public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;
    private Pane roomPane;

    /**
     * Initializes and starts the Robot Simulator application.
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        Room room = getRoom();

        // Create a dialog for input
        roomPane = room.create();
        roomPane.setStyle("-fx-background-color: #bdc3c7;");

        for( Autorobot robot : room.getRobots()){
            roomPane.getChildren().add(robot.getShape());
            robot.getShape().setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    room.getRobots().remove(robot);
                    roomPane.getChildren().remove(robot.getShape());
                }
            });
        }
        for( Obstacle obstacle : room.getObstacles()){
            roomPane.getChildren().add(obstacle.getShape());
            obstacle.getShape().setOnMouseClicked(e -> {
                if( e.getButton() == MouseButton.SECONDARY) {
                    room.getObstacles().remove(obstacle);
                    roomPane.getChildren().remove(obstacle.getShape());
                }
            });
        }

        if(room.getControlledRobot() != null){
            roomPane.getChildren().add(room.getControlledRobot().getShape());
            roomPane.getChildren().add(room.getControlledRobot().getDirectionLine());
            room.getControlledRobot().getShape().setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    room.controlledRobot = null;
                    roomPane.getChildren().remove(room.getControlledRobot().getShape());
                    roomPane.getChildren().remove(room.getControlledRobot().getDirectionLine());
                }
            });
        }


        timer = new AnimationTimer() {
            int stepNumber = 0;
            @Override
            public void handle(long now) {
                for (Autorobot robot : room.getRobots()) {
                    robot.update(room);
                }
                if(room.isControlledRobotSet()){
                    room.controlledRobot.update(room);
                }
                stepNumber++;
            }
        };

        // Create Button Instances
        AddControlledRobotButton addControlledRobotButton = new AddControlledRobotButton(this, room, roomPane);
        AddRobotButton addRobotButton = new AddRobotButton(this, room, roomPane);
        Button addObstacleButton = new AddObstacleButton(this, room, roomPane);
        Button startButton = new StartButton(this);
        PauseButton pauseButton = new PauseButton(this);
        ConfigButton configButton = new ConfigButton(room);
        ResetButton resetButton = new ResetButton(this, room, roomPane);

        // Set Button Sizes
        addControlledRobotButton.setPrefSize(135,12);
        addObstacleButton.setPrefSize(135,12);
        addRobotButton.setPrefSize(135,12);
        startButton.setPrefSize(135,12);
        pauseButton.setPrefSize(135,12);
        configButton.setPrefSize(135,12);
        resetButton.setPrefSize(135,12);

        Region spacer = new Region();
        spacer.setMinHeight(10); // Set the height of the space you want

        // Create a new pane for top buttons
        VBox topButtonPane = new VBox(10);
        topButtonPane.setAlignment(Pos.TOP_CENTER);
        topButtonPane.getChildren().addAll(addObstacleButton, addControlledRobotButton, addRobotButton, configButton, resetButton);
        topButtonPane.setPadding(new Insets(0, 10, 10, 10));

        // Create a new pane for bottom buttons
        VBox bottomButtonPane = new VBox(10);
        bottomButtonPane.setAlignment(Pos.BOTTOM_CENTER);
        bottomButtonPane.getChildren().addAll(startButton, pauseButton);
        bottomButtonPane.setPadding(new Insets(0, 10, 0, 10));

        // Create a new BorderPane for the button layout
        BorderPane buttonLayout = new BorderPane();
        buttonLayout.setTop(topButtonPane);
        buttonLayout.setBottom(bottomButtonPane);

        // Create a main pane and add roomPane and buttonPane
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 0, 10, 10)); // Set padding for mainPane
        mainPane.setCenter(roomPane);
        mainPane.setRight(buttonLayout);

        // Main Scene
        mainPane.setStyle("-fx-background-color: #2c3e50;");
        Scene scene = new Scene(mainPane, room.getWidth() + 150 + 13, room.getHeight() + 20);


        // Key Input Listeners
        scene.setOnKeyPressed(event -> {
            if (room.isControlledRobotSet()) {
                room.controlledRobot.keyPressed(event);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (room.isControlledRobotSet()) {
                room.controlledRobot.keyReleased(event);
            }
        });

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates and configures the room for the simulation.
     * @return The configured room.
     */
    private Room getRoom() {
        return new Room(1000, 1000);
    }

    /**
     * Stops the simulation timer and resets the simulation status.
     */
    public void resetTimer(){
        timer.stop();
        isSimulationStarted = false;
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        isSimulationStarted = true;
        timer.start();
        roomPane.requestFocus();
    }

    /**
     * Checks if the simulation has started.
     * @return True if the simulation is started, false otherwise.
     */
    public boolean isSimulationStarted() {
        return isSimulationStarted;
    }

    /**
     * The main method to launch the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

