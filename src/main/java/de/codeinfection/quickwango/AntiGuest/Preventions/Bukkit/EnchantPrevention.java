package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Prevents enchanting
 *
 * @author Phillip Schichtel
 */
public class EnchantPrevention extends Prevention
{
    public EnchantPrevention()
    {
        super("enchant", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to access enchanting tables!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(InventoryOpenEvent event)
    {
        if (event.getInventory().getType() == InventoryType.ENCHANTING)
        {
            if (event.getPlayer() instanceof Player)
            {
                prevent(event, (Player)event.getPlayer());
            }
        }
    }
}
