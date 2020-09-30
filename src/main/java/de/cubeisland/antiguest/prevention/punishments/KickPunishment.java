package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Kicks a player
 *
 * @author Phillip Schichtel
 */
public class KickPunishment implements Punishment {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("reason", "&cYou were kicked as a punishment!")));
    }
}
