package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 *
 * @author Phillip
 */
public class DropPrevention extends FilteredItemPrevention
{
    public DropPrevention()
    {
        super("drop", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to drop items!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerDropItemEvent event)
    {
        prevent(event, event.getPlayer(), event.getItemDrop().getItemStack().getType());
    }
}
