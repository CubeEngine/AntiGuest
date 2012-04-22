package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents pressureplate usage
 *
 * @author Phillip Schichtel
 */
public class PressureplatePrevention extends Prevention
{
    public PressureplatePrevention(PreventionPlugin plugin)
    {
        super("pressureplate", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
        {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.STONE_PLATE || material == Material.WOOD_PLATE)
            {
                preventThrottled(event, event.getPlayer());
            }
        }
    }
}
