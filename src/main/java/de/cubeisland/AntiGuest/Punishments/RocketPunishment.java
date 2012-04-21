package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Shoots a player into the skys
 *
 * @author Phillip Schichtel
 */
public class RocketPunishment implements Punishment
{
    public String getName()
    {
        return "rockets";
    }

    public void punish(Player player)
    {
        player.getVelocity().add(new Vector(0, 50, 0));
    }
}
