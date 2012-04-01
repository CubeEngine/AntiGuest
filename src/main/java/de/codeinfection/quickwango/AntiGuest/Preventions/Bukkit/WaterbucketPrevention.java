package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * Prevents water bucket usage
 *
 * @author Phillip Schichtel
 */
public class WaterbucketPrevention extends Prevention
{
    public WaterbucketPrevention()
    {
        super("waterbucket", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to use water buckets!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void empty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket() == Material.WATER_BUCKET)
        {
            prevent(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event)
    {
        if (event.getItemStack().getType() == Material.WATER_BUCKET)
        {
            prevent(event, event.getPlayer());
        }
    }
}
