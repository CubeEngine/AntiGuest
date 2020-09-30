package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Bans a player
 *
 * @author Phillip Schichtel
 */
public class BanPunishment implements Punishment {
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        Bukkit.getServer().getBanList(Type.NAME).addBan(player.getUniqueId().toString(), ChatColor.translateAlternateColorCodes('&', config.getString("reason", "&cYou were banned as a punishment!")), null, "AntiGuest");
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("reason", "&cYou were banned as a punishment!")));
    }
}
