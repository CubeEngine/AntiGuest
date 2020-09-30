package de.cubeisland.libMinecraft.math;

/**
 * This class represents a 3D vector
 *
 * @author Phillip Schichtel
 */
public class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    public Vector3(final int x, final int y, final int z) {
        this((double) x, (double) y, (double) z);
    }

    public Vector3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isOrthogonal(Vector3 other) {
        return dot(other) == 0.0;
    }

    public boolean isParallel(Vector3 other) {
        return x / other.x == y / other.y;
    }

    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 add(double n) {
        return new Vector3(x + n, y + n, z + n);
    }

    public Vector3 substract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 substract(double n) {
        return new Vector3(x - n, y - n, z - n);
    }

    public Vector3 multiply(int n) {
        return this.multiply((double) n);
    }

    public Vector3 multiply(double n) {
        return new Vector3(x * n, y * n, z * n);
    }

    public Vector3 divide(int n) {
        return this.multiply((double) n);
    }

    public Vector3 divide(double n) {
        return new Vector3(x / n, y / n, z / n);
    }

    public double squaredLength() {
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
    }

    public double length() {
        return Math.sqrt(squaredLength());
    }

    public Vector3 distanceVector(Vector3 other) {
        return other.substract(this);
    }

    public double squaredDistance(Vector3 other) {
        return distanceVector(other).squaredLength();
    }

    public double distance(Vector3 other) {
        return distanceVector(other).length();
    }

    public double crossAngle(Vector3 other) {
        return this.crossAngle(other, true);
    }

    public double crossAngle(Vector3 other, boolean degree) {
        double result = Math.acos(dot(other) / (length() * other.length()));
        if (degree)
            result *= 180 / Math.PI;
        return result;
    }

    public Vector3 normalize() {
        return this.divide(length());
    }

    public Vector3 midpoint(Vector3 other) {
        return this.add(other.substract(this).divide(2));
    }

    public Vector2 project2D() {
        return new Vector2(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Vector3))
            return false;

        Vector3 other = (Vector3) o;

        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(x) ^ Double.doubleToLongBits(x) >>> 32);
        hash = 59 * hash + (int) (Double.doubleToLongBits(y) ^ Double.doubleToLongBits(y) >>> 32);
        hash = 59 * hash + (int) (Double.doubleToLongBits(z) ^ Double.doubleToLongBits(z) >>> 32);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + "|" + y + "|" + z + ")";
    }
}
