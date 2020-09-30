package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Bans a player
 *
 * @author Phillip Schichtel
 */
public class LightningPunishment implements Punishment {
    @Override
    public String getName() {
        return "lightning";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.damage(config.getInt("damage", 3));
    }
}
