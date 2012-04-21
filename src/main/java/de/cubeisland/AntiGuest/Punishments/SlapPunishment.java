package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class SlapPunishment implements Punishment
{
    public void punish(Player player)
    {
        player.setHealth(player.getHealth() - 3);
    }
}
