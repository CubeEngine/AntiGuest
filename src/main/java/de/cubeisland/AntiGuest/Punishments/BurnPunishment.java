package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Sets a player on fire
 *
 * @author Phillip Schichtel
 */
public class BurnPunishment implements Punishment
{
    public String getName()
    {
        return "burn";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.setFireTicks(config.getInt("duration", 3) * 20);
    }
}
