package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents sneaking (the player still ducks, but the player's name above the
 * head stays visible as of Bukkit 1.1-R5-SNAPSHOT)
 *
 * @author Phillip Schichtel
 */
public class SneakPrevention extends Prevention {
    public SneakPrevention(PreventionPlugin plugin) {
        super("sneak", plugin);
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "\nThis prevention doesn't checkAndPrevent crouching!\n";
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void toggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (event.isSneaking())
            checkAndPrevent(event, player);
    }
}
