package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.configuration.ConfigurationSection;
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

    public void punish(Player player, ConfigurationSection config)
    {
        player.getVelocity().add(new Vector(0, config.getInt("height", 50), 0));
    }
}
