package de.cubeisland.antiguest.prevention.preventions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.cubeisland.antiguest.prevention.FilteredPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import gnu.trove.set.hash.THashSet;

/**
 * Prevents command usage
 *
 * @author Phillip Schichtel
 */
public class CommandPrevention extends FilteredPrevention<String> {
    public CommandPrevention(PreventionPlugin plugin) {
        super("command", plugin);
        setEnablePunishing(true);
        setFilterItems(new THashSet<String>(Arrays.asList("plugins", "pl", "version")));
    }

    @Override
    public Set<String> decodeList(List<String> list) {
        Set<String> commands = new THashSet<String>(list.size());
        for (String entry : list)
            commands.add(entry.trim().toLowerCase());
        return commands;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void commandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if (message.startsWith("/")) {
            message = message.substring(1).trim();
            final int spaceIndex = message.indexOf(' ');
            if (spaceIndex >= 0)
                message = message.substring(0, spaceIndex);
            if (message.length() > 0)
                checkAndPrevent(event, event.getPlayer(), message);
        }
    }
}
