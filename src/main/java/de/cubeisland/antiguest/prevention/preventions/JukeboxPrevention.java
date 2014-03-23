package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents jukebox usage
 *
 * @author Phillip Schichtel
 */
public class JukeboxPrevention extends Prevention
{
    public JukeboxPrevention(PreventionPlugin plugin)
    {
        super("jukebox", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.JUKEBOX)
            {
                checkAndPrevent(event, event.getPlayer());
            }
        }
    }
}
