package de.cubeisland.AntiGuest.prevention.punishments;

import de.cubeisland.AntiGuest.prevention.Punishment;
import de.cubeisland.libMinecraft.ChatColor;
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
