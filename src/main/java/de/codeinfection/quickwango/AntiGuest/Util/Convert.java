package de.codeinfection.quickwango.AntiGuest.Util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
        if (loc == null)
        {
            return null;
        }
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Vector toVector2D(Location loc)
    {
        if (loc == null)
        {
            return null;
        }
        return new Vector(loc.getX(), loc.getZ());
    }

    public static Location toLocation(World world, Vector vector)
    {
        if (vector.getDimension() != 3)
        {
            throw new IllegalArgumentException("The vector must be of the third dimension!");
        }
        return new Location(world, vector.get(0), vector.get(1), vector.get(2));
    }

    public static Location toLocation(Player player, Vector vector)
    {
        if (vector.getDimension() != 3)
        {
            throw new IllegalArgumentException("The vector must be of the third dimension!");
        }
        return new Location(player.getWorld(), vector.get(0), vector.get(1), vector.get(2), player.getLocation().getYaw(), player.getLocation().getPitch());
    }
}
