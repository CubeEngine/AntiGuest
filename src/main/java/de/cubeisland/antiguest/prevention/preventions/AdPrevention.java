package de.cubeisland.antiguest.prevention.preventions;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

public class AdPrevention extends Prevention {
    private final Pattern ipv4Regex = Pattern.compile("(^|\\s)\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}($|\\s)");
    private final Pattern hostRegex = Pattern.compile("(^|\\s)[\\w\\d-]+\\.[a-z]{2,4}($|\\s)", Pattern.CASE_INSENSITIVE);

    public AdPrevention(PreventionPlugin plugin) {
        super("ad", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public void enable() {
        super.enable();
        PreventionManager.getInstance().enablePrevention("link");
    }

    @Override
    public void disable() {
        super.disable();
        final PreventionManager pm = PreventionManager.getInstance();
        final Prevention link = pm.getPrevention("link");
        if (link != null && !link.getConfig().getBoolean("enable", false))
            pm.disablePrevention(link);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!can(event.getPlayer())) {
            final String msg = event.getMessage();
            if (hostRegex.matcher(msg).find() || ipv4Regex.matcher(msg).find())
                prevent(event, event.getPlayer());
        }
    }
}
