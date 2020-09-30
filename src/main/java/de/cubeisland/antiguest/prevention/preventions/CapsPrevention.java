package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents users from writing with too many capital letters
 *
 * @author Phillip Schichtel
 */
public class CapsPrevention extends Prevention {
    private double maxCapsRatio;

    public CapsPrevention(PreventionPlugin plugin) {
        super("caps", plugin);
        maxCapsRatio = 0.80;
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "Configuration info:\n" + "    max-caps-ratio: the rate of capital letters compared to the length of the message in percent (0 - 100)\n";
    }

    @Override
    public void enable() {
        super.enable();
        maxCapsRatio = Math.min(100, Math.abs(getConfig().getInt("max-caps-ratio"))) / 100D;
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration config = super.getDefaultConfig();

        config.set("max-caps-ratio", (int) (maxCapsRatio * 100));

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!can(event.getPlayer())) {
            final String message = event.getMessage();
            final int length = message.length();

            int caps = 0;
            for (int i = 0; i < message.length(); ++i)
                if (Character.isUpperCase(message.charAt(i)))
                    ++caps;

            if ((double) caps / (double) length > maxCapsRatio)
                prevent(event, event.getPlayer());
        }
    }
}
