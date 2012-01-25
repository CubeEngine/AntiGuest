package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.Location;

/**
 *
 * @author CodeInfection
 */
public class Vector2
{
    public final double x;
    public final double z;

    public Vector2(Location location)
    {
        this(location.getX(), location.getZ());
    }

    public Vector2(int n)
    {
        this(n, n);
    }

    public Vector2(double n)
    {
        this(n, n);
    }

    public Vector2(int x, int z)
    {
        this((double)x, (double)z);
    }

    public Vector2(double x, double z)
    {
        this.x = x;
        this.z = z;
    }

    public boolean isOrthogonal(Vector2 other)
    {
        return (this.scalarProduct(other) == 0);
    }

    public boolean isParallel(Vector2 other)
    {
        return (this.x / other.x == this.z / other.z);
    }

    public double scalarProduct(Vector2 other)
    {
        return (this.x * other.x + this.z * other.z);
    }

    public Vector2 add(Vector2 other)
    {
        return new Vector2(this.x + other.x, this.z + other.z);
    }

    public Vector2 substract(Vector2 other)
    {
        return new Vector2(this.x - other.x, this.z - other.z);
    }

    public double squaredLength()
    {
        return (Math.pow(this.x, 2) + Math.pow(this.z, 2));
    }

    public double length()
    {
        return Math.sqrt(this.squaredLength());
    }

    public Vector2 distanceVector(Vector2 other)
    {
        return other.substract(this);
    }

    public double distance(Vector2 other)
    {
        return this.distanceVector(other).length();
    }

    public double squaredDistance(Vector2 other)
    {
        return this.distanceVector(other).squaredLength();
    }

    public double crossAngle(Vector2 other)
    {
        return Math.cos(this.scalarProduct(other) / (this.length() * other.length()));
    }
}
