package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.Prevention;
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
    public SneakPrevention()
    {
        super("sneak", AntiGuestBukkit.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerToggleSneakEvent event)
    {
        final Player player = event.getPlayer();
        if (event.isSneaking())
        {
            if (!can(player))
            {
                if (!player.getGameMode().equals(GameMode.CREATIVE))
                {
                    sendMessage(player);
                }
                event.setCancelled(true);
            }
        }
    }
}