package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Phillip
 */
public class PressureplatePrevention extends Prevention
{

    public PressureplatePrevention()
    {
        super("pressureplate", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
        {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.STONE_PLATE || material == Material.WOOD_PLATE)
            {
                prevent(event, event.getPlayer());
            }
        }
    }
}
