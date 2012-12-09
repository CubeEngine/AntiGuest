package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

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
