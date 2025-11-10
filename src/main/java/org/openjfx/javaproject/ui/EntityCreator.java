package org.openjfx.javaproject.ui;

import javafx.scene.layout.Pane;
import org.openjfx.javaproject.common.EntityEnum;
import org.openjfx.javaproject.common.Obstacle;
import org.openjfx.javaproject.room.*;

public class EntityCreator {
    private final Room room;
    private final Pane roomPane;

    public EntityCreator(Room room, Pane roomPane) {
        this.room = room;
        this.roomPane = roomPane;
    }

    public void createEntity(EntityEnum entityType, Position position) {
        switch (entityType) {
            case RECTANGLE_OBSTACLE:
                createRectangleObstacle(position);
                break;
            case CIRCLE_OBSTACLE:
                createCircleObstacle(position);
                break;
            case AUTO_ROBOT:
                createAutoRobot(position);
                break;
            case CONTROLLED_ROBOT:
                createControlledRobot(position);
                break;
        }
    }

    private void createRectangleObstacle(Position position) {
        Obstacle obstacle = Obstacle.create(room, position, 30, "rectangle");
        if (obstacle != null) {
            room.addObstacle(obstacle);
            roomPane.getChildren().add(obstacle.getShape());
            setupObstacleRemoval(obstacle);
        }
    }

    private void createCircleObstacle(Position position) {
        Obstacle obstacle = Obstacle.create(room, position, 30, "circle");
        if (obstacle != null) {
            room.addObstacle(obstacle);
            roomPane.getChildren().add(obstacle.getShape());
            setupObstacleRemoval(obstacle);
        }
    }

    private void createAutoRobot(Position position) {
        Autorobot robot = Autorobot.create(room, position, 0);
        if (robot != null) {
            room.addRobot(robot);
            roomPane.getChildren().add(robot.getShape());
            setupRobotRemoval(robot);
        }
    }

    private void createControlledRobot(Position position) {
        ControlledRobot robot = ControlledRobot.create(room, position, 0);
        room.addControlledRobot(robot);
        assert robot != null;
        roomPane.getChildren().add(robot.getShape());
        roomPane.getChildren().add(robot.getDirectionLine());
        setupControlledRobotRemoval(robot);
    }

    private void setupObstacleRemoval(Obstacle obstacle) {
        obstacle.getShape().setOnMouseClicked(e -> {
            if (e.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                room.getObstacles().remove(obstacle);
                roomPane.getChildren().remove(obstacle.getShape());
            }
        });
    }

    private void setupRobotRemoval(Autorobot robot) {
        robot.getShape().setOnMouseClicked(e -> {
            if (e.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                room.getRobots().remove(robot);
                roomPane.getChildren().remove(robot.getShape());
            }
        });
    }

    private void setupControlledRobotRemoval(ControlledRobot robot) {
        robot.getShape().setOnMouseClicked(e -> {
            if (e.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                room.controlledRobot = null;
                roomPane.getChildren().remove(robot.getShape());
                roomPane.getChildren().remove(robot.getDirectionLine());
            }
        });
    }
}

