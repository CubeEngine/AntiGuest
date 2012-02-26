package de.codeinfection.quickwango.AntiGuest.Util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public final class Convert
{
    private Convert()
    {}

    public static Vector3D toVector(Location loc)
    {
        if (loc == null)
        {
            return null;
        }
        return new Vector3D(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Vector2D toVector2D(Location loc)
    {
        if (loc == null)
        {
            return null;
        }
        return new Vector2D(loc.getX(), loc.getZ());
    }

    public static Location toLocation(World world, Vector3D vector)
    {
        return new Location(world, vector.x, vector.y, vector.z);
    }

    public static Location toLocation(Player player, Vector3D vector)
    {
        return toLocation(player, vector);
    }
}
