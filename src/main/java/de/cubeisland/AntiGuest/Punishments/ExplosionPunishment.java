package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 * Creates an explosion on the player's position
 *
 * @author Phillip Schichtel
 */
public class ExplosionPunishment implements Punishment
{
    public String getName()
    {
        return "explosion";
    }

    public void punish(Player player)
    {
        player.getWorld().createExplosion(player.getLocation(), 0);
    }
}
