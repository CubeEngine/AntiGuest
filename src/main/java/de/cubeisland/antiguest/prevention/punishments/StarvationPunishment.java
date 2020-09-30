package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Lets a user starve
 *
 * @author Phillip Schichtel
 */
public class StarvationPunishment implements Punishment {
    @Override
    public String getName() {
        return "starvation";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.setSaturation(0);
    }
}
