package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.entity.Player;

/**
 * Bans a player
 *
 * @author Phillip Schichtel
 */
public class LightningPunishment implements Punishment
{
    public String getName()
    {
        return "lightning";
    }

    public void punish(Player player)
    {
        player.getWorld().strikeLightningEffect(player.getLocation());
    }
}
