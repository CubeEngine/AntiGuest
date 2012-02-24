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
public class RepeaterPrevention extends Prevention
{

    public RepeaterPrevention()
    {
        super("repeater", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.DIODE_BLOCK_ON || material == Material.DIODE_BLOCK_OFF)
            {
                prevent(event, event.getPlayer());
            }
        }
    }
}
