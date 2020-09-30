package de.cubeisland.antiguest.prevention.preventions;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

public class LinkPrevention extends Prevention {
    private final Pattern linkRegex = Pattern.compile("(^|\\s)(https?://|www\\.)", Pattern.CASE_INSENSITIVE);

    public LinkPrevention(PreventionPlugin plugin) {
        super("link", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (linkRegex.matcher(event.getMessage()).find())
            checkAndPrevent(event, event.getPlayer());
    }
}
