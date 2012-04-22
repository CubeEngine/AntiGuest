package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import java.util.Random;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Slaps a player
 *
 * @author Phillip Schichtel
 */
public class SlapPunishment implements Punishment
{
    private final Random rand;

    public SlapPunishment()
    {
        this.rand = new Random();
    }

    public String getName()
    {
        return "slap";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.setHealth(player.getHealth() - config.getInt("damage", 3));
        player.getVelocity().add(new Vector(-rand.nextInt(5), rand.nextInt(2), -rand.nextInt(5)));
    }
}
