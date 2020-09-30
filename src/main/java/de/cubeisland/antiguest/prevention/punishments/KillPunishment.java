package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Kills a player
 *
 * @author Phillip Schichtel
 */
public class KillPunishment implements Punishment {
    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.setHealth(0);
    }
}
