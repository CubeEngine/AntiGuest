package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
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
    public LavabucketPrevention(PreventionPlugin plugin)
    {
        super("lavabucket", plugin);
        setEnableByDefault(true);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() + "\nThis prevention works, even though the client shows that lava was placed!\n";
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void empty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket() == Material.LAVA_BUCKET)
        {
            checkAndPrevent(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event)
    {
        if (event.getItemStack().getType() == Material.LAVA_BUCKET)
        {
            checkAndPrevent(event, event.getPlayer());
        }
    }
}
