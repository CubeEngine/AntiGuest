package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class KickPunishment implements Punishment
{
    public void punish(Player player)
    {
        player.kickPlayer("You were kicked as a punishment!");
    }
}
