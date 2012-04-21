package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 * Slaps a player
 *
 * @author Phillip Schichtel
 */
public class SlapPunishment implements Punishment
{
    public String getName()
    {
        return "slap";
    }

    public void punish(Player player)
    {
        player.setHealth(player.getHealth() - 3);
    }
}
