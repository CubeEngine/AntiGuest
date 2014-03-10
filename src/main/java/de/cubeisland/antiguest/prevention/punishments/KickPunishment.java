package de.cubeisland.antiguest.prevention.punishments;

import de.cubeisland.antiguest.prevention.Punishment;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Kicks a player
 *
 * @author Phillip Schichtel
 */
public class KickPunishment implements Punishment
{
    public String getName()
    {
        return "kick";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("reason", "&cYou were kicked as a punishment!")));
    }
}
