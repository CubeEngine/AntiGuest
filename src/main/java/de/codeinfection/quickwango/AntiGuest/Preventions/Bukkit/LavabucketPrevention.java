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
 * Prevents lava bucket usage
 *
 * @author Phillip Schichtel
 */
public class LavabucketPrevention extends Prevention
{
    public LavabucketPrevention()
    {
        super("lavabucket", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to use lava buckets!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void empty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket() == Material.LAVA_BUCKET)
        {
            prevent(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event)
    {
        if (event.getItemStack().getType() == Material.LAVA_BUCKET)
        {
            prevent(event, event.getPlayer());
        }
    }
}
