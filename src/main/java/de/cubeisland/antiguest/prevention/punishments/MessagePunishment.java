package de.cubeisland.antiguest.prevention.punishments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Sends a awkward message from the user
 *
 * @author Phillip Schichtel
 */
public class MessagePunishment implements Punishment {
    @Override
    public String getName() {
        return "message";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        if (config.contains("message"))
            player.chat(config.getString("message"));
    }
}
