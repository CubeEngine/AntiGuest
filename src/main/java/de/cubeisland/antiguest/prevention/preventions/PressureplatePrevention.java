package de.cubeisland.antiguest.prevention.preventions;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Material.ACACIA_PRESSURE_PLATE;
import static org.bukkit.Material.BIRCH_PRESSURE_PLATE;
import static org.bukkit.Material.CRIMSON_PRESSURE_PLATE;
import static org.bukkit.Material.DARK_OAK_PRESSURE_PLATE;
import static org.bukkit.Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
import static org.bukkit.Material.JUNGLE_PRESSURE_PLATE;
import static org.bukkit.Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
import static org.bukkit.Material.OAK_PRESSURE_PLATE;
import static org.bukkit.Material.POLISHED_BLACKSTONE_PRESSURE_PLATE;
import static org.bukkit.Material.SPRUCE_PRESSURE_PLATE;
import static org.bukkit.Material.STONE_PRESSURE_PLATE;
import static org.bukkit.Material.WARPED_PRESSURE_PLATE;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

import static org.bukkit.Material.*;

/**
 * Prevents pressure plate usage
 *
 * @author Phillip Schichtel
 */
public class PressureplatePrevention extends Prevention
{
    private static final EnumSet<Material> PRESSURE_PLATES = EnumSet.of(ACACIA_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, CRIMSON_PRESSURE_PLATE, DARK_OAK_PRESSURE_PLATE, HEAVY_WEIGHTED_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, LIGHT_WEIGHTED_PRESSURE_PLATE, OAK_PRESSURE_PLATE, POLISHED_BLACKSTONE_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, STONE_PRESSURE_PLATE, WARPED_PRESSURE_PLATE);

    public PressureplatePrevention(PreventionPlugin plugin)
    {
        super("pressureplate", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void pressure(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
        {
            final Material material = event.getClickedBlock().getType();
            if (PRESSURE_PLATES.contains(material))
            {
                checkAndPrevent(event, event.getPlayer());
            }
        }
    }
}
