package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 *
 * @author Phillip
 */
public class MonsterPrevention extends Prevention
{

    public MonsterPrevention()
    {
        super("monster", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(EntityTargetEvent event)
    {
        final Entity target = event.getTarget();
        if (target instanceof Player)
        {
            preventThrottled(event, (Player)target);
        }
    }
}
