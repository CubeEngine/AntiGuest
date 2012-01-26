package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.Location;

/**
 *
 * @author CodeInfection
 */
public class Convert
{
    private Convert()
    {}

    public static Vector toVector(Location loc)
    {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Vector toVector2D(Location loc)
    {
        return new Vector(loc.getX(), loc.getZ());
    }
}
