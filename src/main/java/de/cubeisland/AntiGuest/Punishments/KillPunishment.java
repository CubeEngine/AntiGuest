package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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
