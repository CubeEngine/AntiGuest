package de.cubeisland.AntiGuest.prevention;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Represents a punishment
 *
 * @author Phillip Schichtel
 */
public interface Punishment
{
    /**
     * Returns the name of the prevention
     *
     * @return the name
     */
    String getName();

    /**
     * Punishes a player
     *
     * @param player the player to punish
     */
    void punish(Player player, ConfigurationSection config);
}
