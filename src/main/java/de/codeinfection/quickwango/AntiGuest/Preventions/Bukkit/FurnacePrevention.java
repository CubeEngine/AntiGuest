package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.FurnaceInventory;

/**
 * Prevents furnace access
 *
 * @author Phillip Schichtel
 */
public class FurnacePrevention extends Prevention
{
    public FurnacePrevention()
    {
        super("furnace", AntiGuestBukkit.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to access furnaces!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(InventoryOpenEvent event)
    {
        AntiGuestBukkit.log("InventoryOpenEvent triggered!");
        AntiGuestBukkit.log("Name: " + event.getInventory().getName());
        AntiGuestBukkit.log("Title: " + event.getInventory().getTitle());
        AntiGuestBukkit.log("Inventory: " + event.getInventory().getClass().getName());
        AntiGuestBukkit.log("Holder: " + (event.getInventory().getHolder() != null ? event.getInventory().getHolder().getClass().getName() : "null"));
        if (event.getInventory() instanceof FurnaceInventory)
        {
            if (event.getPlayer() instanceof Player)
            {
                prevent(event, (Player)event.getPlayer());
            }
        }
    }
}
