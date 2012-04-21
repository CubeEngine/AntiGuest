package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class KillPunishment implements Punishment
{
    public void punish(Player player)
    {
        player.setHealth(0);
    }
}
