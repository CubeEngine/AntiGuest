package de.codeinfection.quickwango.AntiGuest.Util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * This is a util class to convert from and to Bukkit objects
 *
 * @author Phillip Schichtel
 */
public final class Convert
{
    private Convert()
    {}

    /**
     * Converts a Location to a Vector3D
     *
     * @param loc the Location
     * @return the Vector3D
     */
    public static Vector3D toVector(Location loc)
    {
        if (loc == null)
        {
            return null;
        }
        return new Vector3D(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Converts a Location to a Vector2D
     *
     * @param loc the Location
     * @return the Vector2D
     */
    public static Vector2D toVector2D(Location loc)
    {
        if (loc == null)
        {
            return null;
        }
        return new Vector2D(loc.getX(), loc.getZ());
    }

    /**
     * Converts a World and a Vector3D to a Location
     *
     * @param world the World
     * @param vector the Vector3D
     * @return the Location
     */
    public static Location toLocation(World world, Vector3D vector)
    {
        if (vector == null)
        {
            return null;
        }
        return new Location(world, vector.x, vector.y, vector.z);
    }

    /**
     * Converts a Player and a Vector3D to a Location
     *
     * @param player the Player
     * @param vector the Vector3D
     * @return the Location
     */
    public static Location toLocation(Player player, Vector3D vector)
    {
        if (player == null)
        {
            return null;
        }
        return toLocation(player.getWorld(), vector);
    }
}
