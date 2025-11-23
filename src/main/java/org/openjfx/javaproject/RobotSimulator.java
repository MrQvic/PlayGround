package org.openjfx.javaproject;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import org.openjfx.javaproject.common.EntityEnum;
import org.openjfx.javaproject.room.Autorobot;

import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;

import org.openjfx.javaproject.room.Position;
import org.openjfx.javaproject.room.Room;
import org.openjfx.javaproject.ui.EntityCreator;
import org.openjfx.javaproject.ui.buttons.*;

/**
 * The main class for the Robot Simulator application.
 */
public class RobotSimulator extends Application {
    private AnimationTimer timer;
    private boolean isSimulationStarted = false;
    private Pane roomPane;

    private final ButtonSelection buttonSelection = new ButtonSelection();
    private EntityCreator entityCreator;


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

        entityCreator = new EntityCreator(room, roomPane);

        roomPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && buttonSelection.getMode() != EntityEnum.NONE) {
                entityCreator.createEntity(buttonSelection.getMode(), new Position(e.getX(), e.getY()));
            }
        });

        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
                lastUpdate = now;

                for (Autorobot robot : room.getRobots()) {
                    robot.update(room, deltaTime);
                }
                if (room.isControlledRobotSet()) {
                    room.controlledRobot.update(room, deltaTime);
                }
            }
        };


        ToggleGroup selectionGroup = new ToggleGroup();

        ToggleButton rectangleObstacleButton = new ToggleButton("Rectangle Obstacle");
        ToggleButton circleObstacleButton = new ToggleButton("Circle Obstacle");
        ToggleButton autoRobotButton = new ToggleButton("Auto Robot");
        ToggleButton controlledRobotButton = new ToggleButton("Controlled Robot");
        ToggleButton noneButton = new ToggleButton("None");

        rectangleObstacleButton.setToggleGroup(selectionGroup);
        circleObstacleButton.setToggleGroup(selectionGroup);
        autoRobotButton.setToggleGroup(selectionGroup);
        controlledRobotButton.setToggleGroup(selectionGroup);
        noneButton.setToggleGroup(selectionGroup);


        rectangleObstacleButton.setOnAction(e -> buttonSelection.setMode(EntityEnum.RECTANGLE_OBSTACLE));
        circleObstacleButton.setOnAction(e -> buttonSelection.setMode(EntityEnum.CIRCLE_OBSTACLE));
        autoRobotButton.setOnAction(e -> buttonSelection.setMode(EntityEnum.AUTO_ROBOT));
        controlledRobotButton.setOnAction(e -> buttonSelection.setMode(EntityEnum.CONTROLLED_ROBOT));
        noneButton.setOnAction(e -> buttonSelection.setMode(EntityEnum.NONE));


        rectangleObstacleButton.setPrefSize(135, 12);
        circleObstacleButton.setPrefSize(135, 12);
        autoRobotButton.setPrefSize(135, 12);
        controlledRobotButton.setPrefSize(135, 12);
        noneButton.setPrefSize(135, 12);

        noneButton.setSelected(true);

        // Create Button Instances
        Button startButton = new StartButton(this);
        PauseButton pauseButton = new PauseButton(this);
        ConfigButton configButton = new ConfigButton(room);
        ResetButton resetButton = new ResetButton(this, room, roomPane);

        // Set Button Sizes
        startButton.setPrefSize(135,12);
        pauseButton.setPrefSize(135,12);
        configButton.setPrefSize(135,12);
        resetButton.setPrefSize(135,12);

        Region spacer = new Region();
        spacer.setMinHeight(10); // Set the height of the space you want

        // Create a new pane for top buttons
        VBox topButtonPane = new VBox(10);
        topButtonPane.setAlignment(Pos.TOP_CENTER);
        topButtonPane.setPadding(new Insets(0, 10, 10, 10));
        topButtonPane.getChildren().clear();
        topButtonPane.getChildren().addAll(
                rectangleObstacleButton,
                circleObstacleButton,
                autoRobotButton,
                controlledRobotButton,
                noneButton,
                configButton,
                resetButton
        );

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
     * Configures and returns the Room instance for the simulation.
     * @return The configured room.
     */
    private Room getRoom() {
        return new Room(500, 500);
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

