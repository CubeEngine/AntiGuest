package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
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
public class AntiGuestInteractionListener extends AbstractAntiGuestListener implements Listener
{
    private static final HashMap<Player, Long> pressureTimestamps = new HashMap<Player, Long>();

    public AntiGuestInteractionListener(AntiGuest plugin)
    {
        super(plugin);
    }

    private void pressureMessage(Player player)
    {
        Long lastTime = pressureTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
        {
            sendMessage(player, "pressureplate");
            pressureTimestamps.put(player, currentTime);
        }
    }
    
    private String handleRightOrLeftClick(final Player player, final Material interacted, final Material inHand, final PlayerInteractEvent event)
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

    private String handleRightClick(final Player player, final Material interacted, final Material inHand, final PlayerInteractEvent event)
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

    private String handlePhysical(final Player player, final Material interacted, final Material inHand, final PlayerInteractEvent event)
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

    @EventHandler( event=PlayerInteractEvent.class, priority= EventPriority.LOWEST )
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

        getPlugin().debug("Player interacted with " + material.toString());

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)
        {
            prevName = this.handleRightOrLeftClick(player, itemInHand, itemInHand, event);
        }
        else if (action == Action.RIGHT_CLICK_BLOCK)
        {
            prevName = this.handleRightClick(player, material, itemInHand, event);
        }
        else if (action == Action.PHYSICAL)
        {
            prevName = this.handlePhysical(player, material, itemInHand, event);
        }


        Prevention prev = AntiGuest.preventions.get(prevName);
        if (prev != null)
        {
            if (!can(player, prev))
            {
                sendMessage(player, prev);
                event.setCancelled(true);
            }
        }
    }
}
