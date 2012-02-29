package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
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

    public ChatPrevention()
    {
        super("chat", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to chat!");
        config.set("ignoreWorldeditCUIMessages", true);

        return config;
    }

    @Override
    public void enable(final Server server, final ConfigurationSection config)
    {
        super.enable(server, config);
        this.ignoreWorldeditCUIMessages = config.getBoolean("ignoreWorldeditCUIMessages");
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
