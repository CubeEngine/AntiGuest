package de.cubeisland.antiguest.prevention.preventions;

import static org.bukkit.Material.ACACIA_DOOR;
import static org.bukkit.Material.ACACIA_FENCE_GATE;
import static org.bukkit.Material.BIRCH_DOOR;
import static org.bukkit.Material.BIRCH_FENCE_GATE;
import static org.bukkit.Material.CRIMSON_DOOR;
import static org.bukkit.Material.CRIMSON_FENCE_GATE;
import static org.bukkit.Material.DARK_OAK_DOOR;
import static org.bukkit.Material.DARK_OAK_FENCE_GATE;
import static org.bukkit.Material.IRON_DOOR;
import static org.bukkit.Material.JUNGLE_DOOR;
import static org.bukkit.Material.JUNGLE_FENCE_GATE;
import static org.bukkit.Material.OAK_DOOR;
import static org.bukkit.Material.OAK_FENCE_GATE;
import static org.bukkit.Material.SPRUCE_DOOR;
import static org.bukkit.Material.SPRUCE_FENCE_GATE;
import static org.bukkit.Material.WARPED_DOOR;
import static org.bukkit.Material.WARPED_FENCE_GATE;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.FilteredItemPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

import static org.bukkit.Material.*;

/**
 * Prevents door usage
 *
 * @author Phillip Schichtel
 */
public class DoorPrevention extends FilteredItemPrevention
{
    private static final EnumSet<Material> DOORS = EnumSet
            .of(IRON_DOOR, OAK_DOOR, SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, CRIMSON_DOOR, WARPED_DOOR, ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, CRIMSON_FENCE_GATE, DARK_OAK_FENCE_GATE, JUNGLE_FENCE_GATE, OAK_FENCE_GATE, SPRUCE_FENCE_GATE, WARPED_FENCE_GATE);

    public DoorPrevention(PreventionPlugin plugin)
    {
        super("door", plugin);
        setEnableByDefault(true);
    }

    @Override
    public Set<Material> decodeList(List<String> list)
    {
        Set<Material> materials = super.decodeList(list);

        EnumSet<Material> doors = EnumSet.noneOf(Material.class);
        for (Material material : materials)
        {
            if (DOORS.contains(material))
            {
                doors.add(material);
            }
        }

        return doors;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)
        {
            final Material material = event.getClickedBlock().getType();
            if (DOORS.contains(material))
            {
                checkAndPrevent(event, event.getPlayer(), material);
            }
        }
    }
}
