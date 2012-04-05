package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Prevents sign changing
 *
 * @author Phillip Schichtel
 */
public class ChangesignPrevention extends Prevention
{
    public ChangesignPrevention()
    {
        super("changesign", AntiGuestBukkit.getInstance(), true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(SignChangeEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
