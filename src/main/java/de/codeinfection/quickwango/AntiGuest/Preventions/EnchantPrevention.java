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
public class EnchantPrevention extends Prevention
{

    public EnchantPrevention()
    {
        super("enchant", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE)
            {
                prevent(event, event.getPlayer());
            }
        }
    }
}
