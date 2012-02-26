package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public abstract class FilteredPrevention extends Prevention
{
    protected Set<String> filterItems;
    private Mode mode;
    
    public FilteredPrevention(String name, Plugin plugin)
    {
        super(name, plugin);
    }

    public FilteredPrevention(String name, String permission, Plugin plugin)
    {
        super(name, permission, plugin);
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection defaultConfig = super.getDefaultConfig();

        defaultConfig.set("mode", "blacklist");
        defaultConfig.set("list", new String[] {"example"});

        return defaultConfig;
    }

    @Override
    public void initialize(final Server server, final ConfigurationSection config)
    {
        super.initialize(server, config);

        this.mode = Mode.getByAlias(config.getString("mode"));
        this.filterItems = new HashSet<String>(config.getStringList("list"));
    }

    public boolean can(final Player player, final String item)
    {
        if (!can(player))
        {
            final boolean isListed = this.filterItems.contains(item);

            //AntiGuest.debug("Filter mode: " + this.mode.name());

            switch (this.mode)
            {
                case NONE:
                    return false;
                case WHITELIST:
                    return isListed;
                case BLACKLIST:
                    return !isListed;
            }
        }
        return true;
    }

    public boolean prevent(final Cancellable event, final Player player, final String item)
    {
        if (!this.can(player, item))
        {
            event.setCancelled(true);
            this.sendMessage(player);
            return true;
        }
        return false;
    }
    
    public boolean preventThrottled(final Cancellable event, final Player player, final String item)
    {
        if (!this.can(player, item))
        {
            event.setCancelled(true);
            this.sendThrottledMessage(player);
            return true;
        }
        return false;
    }

    public Mode getMode()
    {
        return this.mode;
    }

    public enum Mode
    {
        NONE("-1", "none", "nolist", "all"),
        WHITELIST("0", "white", "whitelist", "positivlist"),
        BLACKLIST("1", "black", "blacklist", "negativlist");

        private static final HashMap<String, Mode> ALIAS_MAP = new HashMap<String, Mode>();

        private final String[] aliases;
        
        Mode(String... aliases)
        {
            this.aliases = aliases;
        }

        public String[] getAliases()
        {
            return this.aliases;
        }

        public static Mode getByAlias(String alias)
        {
            return ALIAS_MAP.get(alias.toLowerCase());
        }

        static
        {
            for (Mode mode : values())
            {
                for (String alias : mode.getAliases())
                {
                    ALIAS_MAP.put(alias.toLowerCase(), mode);
                }
            }
        }
    }
}
