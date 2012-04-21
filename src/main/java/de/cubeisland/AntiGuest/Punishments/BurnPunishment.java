package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class BurnPunishment implements Punishment
{
    public void punish(Player player)
    {
        player.setFireTicks(80);
    }
}
