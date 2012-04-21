package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class BanPunishment implements Punishment
{
    public void punish(Player player)
    {
        player.setBanned(true);
        player.kickPlayer("You got banned as a punishment!");
    }
}
