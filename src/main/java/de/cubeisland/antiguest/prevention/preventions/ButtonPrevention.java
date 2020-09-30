package de.cubeisland.antiguest.prevention.preventions;

import static org.bukkit.Material.ACACIA_BUTTON;
import static org.bukkit.Material.BIRCH_BUTTON;
import static org.bukkit.Material.CRIMSON_BUTTON;
import static org.bukkit.Material.DARK_OAK_BUTTON;
import static org.bukkit.Material.JUNGLE_BUTTON;
import static org.bukkit.Material.OAK_BUTTON;
import static org.bukkit.Material.POLISHED_BLACKSTONE_BUTTON;
import static org.bukkit.Material.SPRUCE_BUTTON;
import static org.bukkit.Material.STONE_BUTTON;
import static org.bukkit.Material.WARPED_BUTTON;

import java.util.EnumSet;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents button usage
 *
 * @author Phillip Schichtel
 */
public class ButtonPrevention extends Prevention {

    private static final EnumSet<Material> BUTTONS = EnumSet.of(ACACIA_BUTTON, BIRCH_BUTTON, CRIMSON_BUTTON, DARK_OAK_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, POLISHED_BLACKSTONE_BUTTON, SPRUCE_BUTTON, STONE_BUTTON, WARPED_BUTTON);

    public ButtonPrevention(PreventionPlugin plugin) {
        super("button", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Material material = event.getClickedBlock().getType();
            if (BUTTONS.contains(material))
                checkAndPrevent(event, event.getPlayer());
        }
    }

}
