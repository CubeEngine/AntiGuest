package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Prevents picking up items
 *
 * @author Phillip Schichtel
 */
public class PickupPrevention extends FilteredItemPrevention
{
    public PickupPrevention()
    {
        super("pickup", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to pickup this item!");
        config.set("messageDelay", 3);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerPickupItemEvent event)
    {
        preventThrottled(event, event.getPlayer(), event.getItem().getItemStack().getType());
    }
}
