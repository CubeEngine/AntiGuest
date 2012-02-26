package de.codeinfection.quickwango.AntiGuest.Util;

/**
 *
 * @author CodeInfection
 */
public class Vector3D
{
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(final int x, final int y, final int z)
    {
        this((double)x, (double)y, (double)z);
    }

    public Vector3D(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isOrthogonal(Vector3D other)
    {
        return (this.dotProduct(other) == 0.0);
    }

    public boolean isParallel(Vector3D other)
    {
        return (this.x / other.x == this.y / other.y);
    }

    private double dotProduct(Vector3D other)
    {
        return (this.x * other.x + this.y * other.y);
    }

    public Vector3D add(Vector3D other)
    {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3D substract(Vector3D other)
    {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3D multiply(int n)
    {
        return this.multiply((double)n);
    }

    public Vector3D multiply(double n)
    {
        return new Vector3D(this.x * n, this.y * n, this.z * n);
    }

    public Vector3D divide(int n)
    {
        return this.multiply((double)n);
    }

    public Vector3D divide(double n)
    {
        return new Vector3D(this.x / n, this.y / n, this.z / n);
    }

    public double squaredLength()
    {
        return (Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double length()
    {
        return Math.sqrt(this.squaredLength());
    }

    public Vector3D distanceVector(Vector3D other)
    {
        return other.substract(this);
    }

    public double squaredDistance(Vector3D other)
    {
        return this.distanceVector(other).squaredLength();
    }

    public double distance(Vector3D other)
    {
        return this.distanceVector(other).length();
    }

    public double crossAngle(Vector3D other)
    {
        return this.crossAngle(other, true);
    }

    public double crossAngle(Vector3D other, boolean degree)
    {
        double result = Math.acos(this.dotProduct(other) / (this.length() * other.length()));
        if (degree)
        {
            result *= 180 / Math.PI;
        }
        return result;
    }

    public Vector3D normalize()
    {
        return this.divide(this.length());
    }

    public Vector3D midpoint(Vector3D other)
    {
        return this.add(other.substract(this).divide(2));
    }

    public Vector2D project2D()
    {
        return new Vector2D(this.x, this.y);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + "|" + this.y  + "|" + this.z + ")";
    }
}
