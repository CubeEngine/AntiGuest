package de.cubeisland.libMinecraft.math;

/**
 *
 * @author CodeInfection
 */
public class Rectangle {
    private final Vector2 corner1;
    private final Vector2 corner2;

    public Rectangle(Vector2 corner1, Vector2 corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public Vector2 getMinimumPoint() {
        return new Vector2(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y));
    }

    public Vector2 getMaximumPoint() {
        return new Vector2(Math.max(corner1.x, corner2.x), Math.max(corner1.y, corner2.y));
    }

    public boolean contains(Vector2 point) {
        Vector2 min = getMinimumPoint();
        Vector2 max = getMaximumPoint();

        return point.x >= min.x && point.x <= max.x && point.y >= min.y && point.y <= max.y;
    }
}
