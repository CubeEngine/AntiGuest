package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Kills a player
 *
 * @author Phillip Schichtel
 */
public class KillPunishment implements Punishment
{
    public String getName()
    {
        return "kill";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.setHealth(0);
    }
}
