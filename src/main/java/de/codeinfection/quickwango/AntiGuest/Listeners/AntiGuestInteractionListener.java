package de.codeinfection.quickwango.AntiGuest.Listeners;

//import java.util.HashMap;
import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ItemPrevention;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestInteractionListener implements Listener
{
    private String handleRightOrLeftClick(final Material interacted, final Material inHand)
    {
        switch (interacted)
        {
            case WOODEN_DOOR:
            case IRON_DOOR:
            case IRON_DOOR_BLOCK:
            case TRAP_DOOR:
            case FENCE_GATE:
                return "door";

            case LEVER:
                return "lever";

            case STONE_BUTTON:
                return "button";

            case NOTE_BLOCK:
                return "noteblock";

            default:
                return null;
        }
    }

    private String handleRightClick(final Material interacted, final Material inHand)
    {
        switch (interacted)
        {
            case CHEST:
                return "chest";

            case WORKBENCH:
                return "workbench";

            case FURNACE:
            case BURNING_FURNACE:
                return "furnace";
                
            case DISPENSER:
                return "dispenser";

            case CAKE_BLOCK:
                return "cake";

            case BREWING_STAND:
            case CAULDRON:
                return "brew";

            case ENCHANTMENT_TABLE:
                return "enchant";

            case DIODE_BLOCK_ON:
            case DIODE_BLOCK_OFF:
                return "repeater";

            case JUKEBOX:
                return "jukebox";

            case RAILS:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            {
                switch (inHand)
                {
                    case MINECART:
                    case POWERED_MINECART:
                    case STORAGE_MINECART:
                    {
                        return "vehicle";
                    }
                    default:
                        return null;
                }
            }

            default:
            {
                switch (inHand)
                {
                    case BOAT:
                        return "vehicle";
                    default:
                        return null;
                }
            }
        }
    }

    private String handlePhysical(final Material interacted, final Material inHand)
    {
        switch (interacted)
        {
            case STONE_PLATE:
            case WOOD_PLATE:
                return "pressureplate";

            default:
                return null;
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void handleInteraction(PlayerInteractEvent event)
    {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        Action action = event.getAction();
        Material itemInHand = player.getItemInHand().getType();

        if (itemInHand != Material.AIR && action != Action.PHYSICAL)
        {
            ItemPrevention itemPrev = (ItemPrevention)AntiGuest.preventions.get("itemusage");
            if (itemPrev != null && !itemPrev.canUse(player, itemInHand))
            {
                itemPrev.sendMessage(player);
                event.setCancelled(true);
                return;
            }
        }

        Block block = event.getClickedBlock();
        if (block == null)
        {
            return;
        }
        Material interacted = block.getType();
        String prevName = null;

        AntiGuest.getInstance().debug("Player interacted with " + interacted.toString() + " holding a " + String.valueOf(itemInHand));
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)
        {
            prevName = this.handleRightOrLeftClick(interacted, itemInHand);
        }
        
        if (prevName == null && action == Action.RIGHT_CLICK_BLOCK)
        {
            prevName = this.handleRightClick(interacted, itemInHand);
        }
        
        if (prevName == null && action == Action.PHYSICAL)
        {
            prevName = this.handlePhysical(interacted, itemInHand);
        }


        Prevention prev = AntiGuest.preventions.get(prevName);
        AntiGuest.getInstance().debug("Selected prevention: " + String.valueOf(prev));
        if (prev != null)
        {
            AntiGuest.getInstance().debug("Player " + player.getName() + " is " + (prev.can(player) ? "" : "not") + " allowed to " + prev.getName());
            if (!prev.can(player))
            {
                if (prev.getMessageDelay() > 0)
                {
                    prev.sendThrottledMessage(player);
                }
                else
                {
                    prev.sendMessage(player);
                }
                event.setCancelled(true);
            }
        }
    }
}
