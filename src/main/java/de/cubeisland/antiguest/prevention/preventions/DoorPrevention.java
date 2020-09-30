package de.cubeisland.antiguest.prevention.preventions;

import static org.bukkit.Material.FENCE_GATE;
import static org.bukkit.Material.IRON_DOOR;
import static org.bukkit.Material.IRON_DOOR_BLOCK;
import static org.bukkit.Material.TRAP_DOOR;
import static org.bukkit.Material.WOODEN_DOOR;

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

/**
 * Prevents door usage
 *
 * @author Phillip Schichtel
 */
public class DoorPrevention extends FilteredItemPrevention {
    private static final EnumSet<Material> DOORS = EnumSet.of(WOODEN_DOOR, IRON_DOOR, IRON_DOOR_BLOCK, TRAP_DOOR, FENCE_GATE);

    public DoorPrevention(PreventionPlugin plugin) {
        super("door", plugin);
        setEnableByDefault(true);
    }

    @Override
    public Set<Material> decodeList(List<String> list) {
        Set<Material> materials = super.decodeList(list);

        EnumSet<Material> doors = EnumSet.noneOf(Material.class);
        for (Material material : materials)
            if (DOORS.contains(material))
                doors.add(material);

        return doors;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            final Material material = event.getClickedBlock().getType();
            if (DOORS.contains(material))
                checkAndPrevent(event, event.getPlayer(), material);
        }
    }
}
