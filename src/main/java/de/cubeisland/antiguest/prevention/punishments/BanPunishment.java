package de.cubeisland.antiguest.prevention.punishments;

import de.cubeisland.antiguest.prevention.Punishment;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Bans a player
 *
 * @author Phillip Schichtel
 */
public class BanPunishment implements Punishment
{
    public String getName()
    {
        return "ban";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.setBanned(true);
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("reason", "&cYou were banned as a punishment!")));
    }
}
