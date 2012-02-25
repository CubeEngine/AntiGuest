package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

/**
 *
 * @author Phillip
 */
public class EnchantPrevention extends FilteredItemPrevention
{

    public EnchantPrevention()
    {
        super("enchant", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to enchant this item!");
        config.set("list", new String[] {Material.DIAMOND_SWORD.toString().toLowerCase().replace('_', ' ')});

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PrepareItemEnchantEvent event)
    {
        prevent(event, event.getEnchanter(), event.getItem().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EnchantItemEvent event)
    {
        prevent(event, event.getEnchanter(), event.getItem().getType());
    }
}
