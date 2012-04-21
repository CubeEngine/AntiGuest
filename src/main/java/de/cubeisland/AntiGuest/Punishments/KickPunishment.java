package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 * Kicks a player
 *
 * @author Phillip Schichtel
 */
public class KickPunishment implements Punishment
{
    public String getName()
    {
        return "kick";
    }

    public void punish(Player player)
    {
        player.kickPlayer("You were kicked as a punishment!");
    }
}
