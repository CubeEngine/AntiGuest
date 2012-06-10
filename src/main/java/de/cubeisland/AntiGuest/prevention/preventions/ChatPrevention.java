package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
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
    public ChatPrevention(PreventionPlugin plugin)
    {
        super("chat", plugin);
        setEnablePunishing(false);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() + "\n" +
                "Configuration info:\n" +
                "    ignoreWorldeditCUIMessages: this option makes AntiGuest ignore the handshake messages of the WordEditCUI mod\n";
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("ignoreWorldeditCUIMessages", true);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chat(PlayerChatEvent event)
    {
        if (getConfig().getBoolean("ignoreWorldeditCUIMessages") || !event.getMessage().startsWith("u00a74u00a75u00a73u00a74"))
        {
            prevent(event, event.getPlayer());
        }
    }
}