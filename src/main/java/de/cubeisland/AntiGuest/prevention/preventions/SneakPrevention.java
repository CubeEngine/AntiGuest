package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * Prevents sneaking (the player still ducks, but the player's name above the head stays visible as of Bukkit 1.1-R5-SNAPSHOT)
 *
 * @author Phillip Schichtel
 */
public class SneakPrevention extends Prevention
{
    public SneakPrevention(PreventionPlugin plugin)
    {
        super("sneak", plugin);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() + "\nThis prevention doesn't prevent crouching!\n";
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void toggleSneak(PlayerToggleSneakEvent event)
    {
        final Player player = event.getPlayer();
        if (event.isSneaking())
        {
            if (!can(player))
            {
                if (!player.getGameMode().equals(GameMode.CREATIVE))
                {
                    sendMessage(player);
                    punish(player);
                }
                event.setCancelled(true);
            }
        }
    }
}
