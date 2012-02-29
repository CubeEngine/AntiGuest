package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
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
        super("sneak", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to sneak!");

        return config;
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
