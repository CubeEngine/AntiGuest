package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
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

        config.set("message", "&4You are not allowed to enchant!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PrepareItemEnchantEvent event)
    {
        prevent(event, event.getEnchanter(), event.getItem().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(EnchantItemEvent event)
    {
        prevent(event, event.getEnchanter(), event.getItem().getType());
    }
}
