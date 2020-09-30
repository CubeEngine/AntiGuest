package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Shoots a player into the skys
 *
 * @author Phillip Schichtel
 */
public class RocketPunishment implements Punishment {
    @Override
    public String getName() {
        return "rocket";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.setVelocity(player.getVelocity().add(new Vector(0, config.getInt("height", 50), 0)));
    }
}
