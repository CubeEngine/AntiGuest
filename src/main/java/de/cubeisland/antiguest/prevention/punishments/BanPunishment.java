package de.cubeisland.antiguest.prevention.punishments;

import de.cubeisland.antiguest.AntiGuest;
import org.bukkit.BanList.Type;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

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
        String reason = ChatColor.translateAlternateColorCodes('&', config
                .getString("reason", "&cYou were banned as a punishment!"));
        player.getServer()
                .getBanList(Type.NAME)
                .addBan(player.getUniqueId().toString(), reason, null, AntiGuest.class.getSimpleName());
        player.kickPlayer(reason);
    }
}
