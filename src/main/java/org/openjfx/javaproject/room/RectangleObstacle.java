package org.openjfx.javaproject.room;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.openjfx.javaproject.common.Obstacle;

public class RectangleObstacle extends Obstacle {
    private final Rectangle rectangle;

    /**
     * Constructs a RectangleObstacle with the specified position and size.
     *
     * @param position The position of the center of the rectangle.
     * @param size The size (width and height) of the rectangle.
     */
    public RectangleObstacle(Position position, double size) {
        super(position);
        size *= 2;
        // Adjust the position so the rectangle is created from the middle
        rectangle = new Rectangle(position.getX() - size / 2, position.getY() - size / 2, size, size);
        rectangle.setFill(Color.GRAY);
    }

    /**
     * Retrieves the JavaFX Rectangle representing this obstacle's shape.
     *
     * @return The JavaFX Rectangle.
     */
    @Override
    public Rectangle getShape() {
        return rectangle;
    }

    /**
     * Retrieves the size of this obstacle.
     *
     * @return The size of the obstacle.
     */
    @Override
    public double getSize() { //TODO: return correct value, not
        return rectangle.getHeight();
    }

    /**
     * Retrieves the type of this obstacle.
     *
     * @return The type of the obstacle.
     */
    @Override
    public String getType() {
        return "rectangle";
    }

    /**
     * Checks if a circle collides with this RectangleObstacle.
     *
     * @param x The x-coordinate of the circle's center.
     * @param y The y-coordinate of the circle's center.
     * @param radius The radius of the circle.
     * @return True if the circle collides with this obstacle, false otherwise.
     */
    @Override
    public boolean checkCollision(double x, double y, double radius) {
        double halfSize = getSize() / 2;
        double left = position.getX() - halfSize - radius;
        double right = position.getX() + halfSize + radius;
        double top = position.getY() - halfSize - radius;
        double bottom = position.getY() + halfSize + radius;
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    /**
     * Calculates the angle from a given position to the nearest point on this RectangleObstacle.
     *
     * @param fromX The x-coordinate of the position to calculate from.
     * @param fromY The y-coordinate of the position to calculate from.
     * @return The angle in radians to the nearest point on the obstacle.
     */
    @Override
    public double calculateAngleTo(double fromX, double fromY) {
        double halfSize = getSize() / 2;
        double left = position.getX() - halfSize;
        double right = position.getX() + halfSize;
        double top = position.getY() - halfSize;
        double bottom = position.getY() + halfSize;

        double nearestX = Math.max(left, Math.min(fromX, right));
        double nearestY = Math.max(top, Math.min(fromY, bottom));

        double dx = nearestX - fromX;
        double dy = nearestY - fromY;
        return Math.atan2(dy, dx);
    }


}
