package de.codeinfection.quickwango.AntiGuest.Util;

/**
 *
 * @author CodeInfection
 */
public class Vector2D
{
    public final double x;
    public final double y;

    public Vector2D(int x, int y)
    {
        this((double)x, (double)y);
    }

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isOrthogonal(Vector2D other)
    {
        return (this.dotProduct(other) == 0.0);
    }

    public boolean isParallel(Vector2D other)
    {
        return (this.x / other.x == this.y / other.y);
    }

    private double dotProduct(Vector2D other)
    {
        return (this.x * other.x + this.y * other.y);
    }

    public Vector2D add(Vector2D other)
    {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D substract(Vector2D other)
    {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D multiply(int n)
    {
        return this.multiply((double)n);
    }

    public Vector2D multiply(double n)
    {
        return new Vector2D(this.x * n, this.y * n);
    }

    public Vector2D divide(int n)
    {
        return this.multiply((double)n);
    }

    public Vector2D divide(double n)
    {
        return new Vector2D(this.x / n, this.y / n);
    }

    public double squaredLength()
    {
        return (Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double length()
    {
        return Math.sqrt(this.squaredLength());
    }

    public Vector2D distanceVector(Vector2D other)
    {
        return other.substract(this);
    }

    public double squaredDistance(Vector2D other)
    {
        return this.distanceVector(other).squaredLength();
    }

    public double distance(Vector2D other)
    {
        return this.distanceVector(other).length();
    }

    public double crossAngle(Vector2D other)
    {
        return this.crossAngle(other, true);
    }

    public double crossAngle(Vector2D other, boolean degree)
    {
        double result = Math.acos(this.dotProduct(other) / (this.length() * other.length()));
        if (degree)
        {
            result *= 180 / Math.PI;
        }
        return result;
    }

    public Vector2D normalize()
    {
        return this.divide(this.length());
    }

    public Vector2D midpoint(Vector2D other)
    {
        return this.add(other.substract(this).divide(2));
    }

    @Override
    public String toString()
    {
        return "(" + this.x + "|" + this.y + ")";
    }
}
