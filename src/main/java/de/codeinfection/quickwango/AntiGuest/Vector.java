package de.codeinfection.quickwango.AntiGuest;

/**
 *
 * @author CodeInfection
 */
public class Vector
{
    private final double[] coords;

    public Vector(double... coords)
    {
        this.coords = coords;
    }

    public Vector(int... coords)
    {
        this.coords = new double[coords.length];
        for (int i = 0; i < coords.length; ++i)
        {
            this.coords[i] = (double)coords[i];
        }
    }
    
    private void validate(Vector vec)
    {
        if (!vec.isCompatible(this))
        {
            throw new IllegalArgumentException("Given vector has another dimension!");
        }
    }
    
    public boolean isCompatible(Vector other)
    {
        return (this.getDimension() == other.getDimension());
    }
    
    public int getDimension()
    {
        return this.coords.length;
    }
    
    public double get(int i)
    {
        return coords[i];
    }

    public boolean isOrthogonal(Vector other)
    {
        return (this.scalarProduct(other) == 0);
    }

    public boolean isParallel(Vector other)
    {
        validate(other);
        double quotient = this.coords[0] / other.coords[0];
        for (int i = 1; i < this.getDimension(); ++i)
        {
            if (quotient != (this.coords[i] / other.coords[i]))
            {
                return false;
            }
        }
        return true;
    }

    public double scalarProduct(Vector other)
    {
        validate(other);
        double sum = 0.0;
        for (int i = 0; i < this.getDimension(); ++i)
        {
            sum += this.get(i) * other.get(i);
        }
        return sum;
    }

    public Vector add(Vector other)
    {
        validate(other);
        double[] newCoords = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); ++i)
        {
            newCoords[i] = this.get(i) + other.get(i);
        }
        return new Vector(newCoords);
    }

    public Vector substract(Vector other)
    {
        validate(other);
        double[] newCoords = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); ++i)
        {
            newCoords[i] = this.get(i) - other.get(i);
        }
        return new Vector(newCoords);
    }

    public Vector multiply(int n)
    {
        return this.multiply((double)n);
    }
    
    public Vector multiply(double n)
    {
        double[] newCoords = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); ++i)
        {
            newCoords[i] = this.get(i) * n;
        }
        return new Vector(newCoords);
    }

    public Vector divide(int n)
    {
        return this.divide((double)n);
    }

    public Vector divide(double n)
    {
        double[] newCoords = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); ++i)
        {
            newCoords[i] = this.get(i) / n;
        }
        return new Vector(newCoords);
    }

    public double squaredLength()
    {
        double sum = 0;
        for (int i = 0; i < this.getDimension(); ++i)
        {
            sum += Math.pow(this.get(i), 2);
        }
        return sum;
    }

    public double length()
    {
        return Math.sqrt(this.squaredLength());
    }

    public Vector distanceVector(Vector other)
    {
        return other.substract(this);
    }

    public double distance(Vector other)
    {
        return this.distanceVector(other).length();
    }

    public double squaredDistance(Vector other)
    {
        return this.distanceVector(other).squaredLength();
    }

    public double crossAngle(Vector other)
    {
        return this.crossAngle(other, true);
    }

    public double crossAngle(Vector other, boolean degree)
    {
        double result = Math.acos(this.scalarProduct(other) / (this.length() * other.length()));
        if (degree)
        {
            result *= 180 / Math.PI;
        }
        return result;
    }
    
    public Vector normalize()
    {
        return this.divide(this.length());
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        sb.append(this.get(0));
        for (int i = 1; i < this.getDimension(); ++i)
        {
            sb.append("|").append(this.get(i));
        }
        return sb.append(")").toString();
    }
}
