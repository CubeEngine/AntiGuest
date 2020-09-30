package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Creates an explosion on the player's position
 *
 * @author Phillip Schichtel
 */
public class ExplosionPunishment implements Punishment {
    @Override
    public String getName() {
        return "explosion";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.getWorld().createExplosion(player.getLocation(), 0);
        player.damage(config.getInt("damage", 3));
    }
}
