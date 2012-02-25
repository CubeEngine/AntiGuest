package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Phillip
 */
public class ItemPrevention extends FilteredItemPrevention
{
    public ItemPrevention()
    {
        super("item", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to use this item!");
        config.set("list", new String[] {Material.DIAMOND_SWORD.toString().toLowerCase().replace('_', ' ')});

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.PHYSICAL)
        {
            final ItemStack itemInHand = event.getItem();
            if (itemInHand != null)
            {
                this.prevent(event, event.getPlayer(), event.getItem().getType());
            }
        }
    }
}
