package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents sign changing
 *
 * @author Phillip Schichtel
 */
public class ChangeSignPrevention extends Prevention
{
    public ChangeSignPrevention(PreventionPlugin plugin)
    {
        super("changesign", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void change(SignChangeEvent event)
    {
        checkAndPrevent(event, event.getPlayer());
    }
}
