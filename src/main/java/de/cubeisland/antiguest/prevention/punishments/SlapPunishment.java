package de.cubeisland.antiguest.prevention.punishments;

import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Slaps a player
 *
 * @author Phillip Schichtel
 */
public class SlapPunishment implements Punishment {
    private final Random rand;

    public SlapPunishment() {
        rand = new Random();
    }

    @Override
    public String getName() {
        return "slap";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.damage(config.getInt("damage", 3));
        player.setVelocity(player.getVelocity().add(new Vector(-rand.nextInt(5), rand.nextInt(2), -rand.nextInt(5))));
    }
}
