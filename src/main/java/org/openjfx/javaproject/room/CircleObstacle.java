package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.openjfx.javaproject.common.Obstacle;

/**
 * Represents a circular obstacle in a room.
 */
public class CircleObstacle extends Obstacle {
    private final Circle circle;

    /**
     * Constructs a CircleObstacle with the specified position and radius.
     *
     * @param position The position of the center of the circle.
     * @param radius The radius of the circle.
     */
    public CircleObstacle(Position position, double radius) {
        super(position);
        circle = new Circle(position.getX(), position.getY(), radius);
        circle.setFill(Color.GRAY);
    }

    /**
     * Retrieves the graphical representation of the CircleObstacle.
     *
     * @return The Circle representing the CircleObstacle.
     */
    @Override
    public Circle getShape() {
        return circle;
    }

    /**
     * Retrieves the size of the CircleObstacle, which is its radius.
     *
     * @return The radius of the CircleObstacle.
     */
    @Override
    public double getSize() {
        return circle.getRadius();
    }

    /**
     * Retrieves the type of the obstacle.
     *
     * @return The type of the obstacle, which is "circle".
     */
    @Override
    public String getType() {
        return "circle";
    }

    /**
     * Checks if a circle collides with this CircleObstacle.
     *
     * @param x The x-coordinate of the circle's center.
     * @param y The y-coordinate of the circle's center.
     * @param radius The radius of the circle.
     * @return True if the circle collides with this CircleObstacle, false otherwise.
     */
    @Override
    public boolean checkCollision(double x, double y, double radius) {
        double dx = x - position.getX();
        double dy = y - position.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (radius + getSize());
    }

    /**
     * Calculates the angle from a given point to the center of this CircleObstacle.
     *
     * @param fromX The x-coordinate of the starting point.
     * @param fromY The y-coordinate of the starting point.
     * @return The angle in radians from the starting point to the center of the CircleObstacle.
     */
    @Override
    public double calculateAngleTo(double fromX, double fromY) {
        double dx = position.getX() - fromX;
        double dy = position.getY() - fromY;
        return Math.atan2(dy, dx);
    }


}
