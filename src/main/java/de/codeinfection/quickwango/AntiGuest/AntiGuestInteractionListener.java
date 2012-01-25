package de.codeinfection.quickwango.AntiGuest;

//import java.util.HashMap;
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
//    private static final HashMap<Player, Long> pressureTimestamps = new HashMap<Player, Long>();

//    private void pressureMessage(Player player)
//    {
//        Long lastTime = pressureTimestamps.get(player);
//        long currentTime = System.currentTimeMillis();
//        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
//        {
//            sendMessage(player, "pressureplate");
//            pressureTimestamps.put(player, currentTime);
//        }
//    }
    
    private String handleRightOrLeftClick(final Material interacted, final Material inHand)
    {
        switch (interacted)
        {
            case WOODEN_DOOR:
            case IRON_DOOR:
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
        final Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        if (block == null)
        {
            return;
        }
        Material material = block.getType();
        Material itemInHand = player.getItemInHand().getType();
        String prevName = null;

        AntiGuest.getInstance().debug("Player interacted with " + material.toString() + " holding a " + String.valueOf(itemInHand));
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)
        {
            prevName = this.handleRightOrLeftClick(itemInHand, itemInHand);
        }
        else if (action == Action.RIGHT_CLICK_BLOCK)
        {
            prevName = this.handleRightClick(material, itemInHand);
        }
        else if (action == Action.PHYSICAL)
        {
            prevName = this.handlePhysical(material, itemInHand);
        }


        Prevention prev = AntiGuest.preventions.get(prevName);
        if (prev != null)
        {
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
