package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

/**
 * Prevents enchanting
 *
 * @author Phillip Schichtel
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

    private static final String DETECTION_CLASS = "net.minecraft.server.Slot";
    private static final byte SEARCH_OFFSET = 12;
    private static final byte MAX_OFFSET = SEARCH_OFFSET + 4;
    private boolean shouldBePrevented()
    {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        byte slotCalls = 0;
        for (byte i = SEARCH_OFFSET; i < MAX_OFFSET && slotCalls < 2; ++i)
        {
            if (DETECTION_CLASS.equals(stackTrace[i].getClassName()))
            {
                ++slotCalls;
            }
        }
        return (slotCalls == 2);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PrepareItemEnchantEvent event)
    {
        if (this.shouldBePrevented())
        {
            prevent(event, event.getEnchanter(), event.getItem().getType());
        }
    }
}
