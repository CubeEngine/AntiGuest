package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.FilteredPrevention;
import java.util.HashSet;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author CodeInfection
 */
public class DamagePrevention extends FilteredPrevention
{
    public DamagePrevention()
    {
        super("damage", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&2AntiGuest protected you from damage!");
        config.set("mode", "none");
        config.set("list", new String[]{"lava"});

        return config;
    }

    @Override
    public void initialize(final Server server, final ConfigurationSection config)
    {
        super.initialize(server, config);
        HashSet<String> newList = new HashSet<String>();
        for (String item : this.filterItems)
        {
            item = item.trim().replace(" ", "_").toUpperCase();
            try
            {
                newList.add(DamageCause.valueOf(item).toString());
            }
            catch (IllegalArgumentException e)
            {}
        }
        this.filterItems = newList;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityDamageEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            prevent(event, (Player)entity, event.getCause().name());
        }
    }
}
