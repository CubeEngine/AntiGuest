package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Drops the player players held item
 *
 * @author Phillip Schichtel
 */
public class DropitemPunishment implements Punishment
{
    public String getName()
    {
        return "dropitem";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.getWorld().dropItemNaturally(player.getLocation(), player.getItemInHand()).setPickupDelay(80);
        player.getInventory().clear(player.getInventory().getHeldItemSlot());
    }
}
