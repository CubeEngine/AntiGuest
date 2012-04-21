package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Prevents chatting
 *
 * @author Phillip Schichtel
 */
public class ChatPrevention extends Prevention
{
    private boolean ignoreWorldeditCUIMessages;

    public ChatPrevention(PreventionPlugin plugin)
    {
        super("chat", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("ignoreWorldeditCUIMessages", true);

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        this.ignoreWorldeditCUIMessages = getConfig().getBoolean("ignoreWorldeditCUIMessages");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerChatEvent event)
    {
        if (this.ignoreWorldeditCUIMessages || !event.getMessage().startsWith("u00a74u00a75u00a73u00a74"))
        {
            prevent(event, event.getPlayer());
        }
    }
}
