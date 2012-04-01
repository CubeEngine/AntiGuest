package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.block.BrewingStand;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Prevents brewing
 *
 * @author Phillip Schichtel
 */
public class BrewPrevention extends Prevention
{
    public BrewPrevention()
    {
        super("brew", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to access brewing stands!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(InventoryOpenEvent event)
    {
        if (event.getInventory() != null && event.getInventory().getHolder() instanceof BrewingStand)
        {
            if (event.getPlayer() instanceof Player)
            {
                prevent(event, (Player)event.getPlayer());
            }
        }
    }
}
