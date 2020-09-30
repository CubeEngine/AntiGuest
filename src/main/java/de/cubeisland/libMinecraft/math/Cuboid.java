package de.cubeisland.libMinecraft.math;

/**
 *
 * @author CodeInfection
 */
public class Cuboid {
    private final Vector3 corner1;
    private final Vector3 corner2;

    public Cuboid(Vector3 corner1, Vector3 corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public Vector3 getMinimumPoint() {
        return new Vector3(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y), Math.min(corner1.z, corner2.z));
    }

    public Vector3 getMaximumPoint() {
        return new Vector3(Math.max(corner1.x, corner2.x), Math.max(corner1.y, corner2.y), Math.max(corner1.z, corner2.z));
    }

    public boolean contains(Vector3 point) {
        Vector3 min = getMinimumPoint();
        Vector3 max = getMaximumPoint();

        return point.x >= min.x && point.x <= max.x && point.y >= min.y && point.y <= max.y && point.z >= min.z && point.z <= max.z;
    }
}
