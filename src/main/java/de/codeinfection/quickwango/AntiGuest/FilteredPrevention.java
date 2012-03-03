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
 * This base class represents a prevention that can be filtered.
 * That means it is able to prevent only a subset of actions based an a whitelist or blacklist
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredPrevention extends Prevention
{
    protected Set<Object> filterItems;
    private Mode mode;
    
    public FilteredPrevention(String name, Plugin plugin)
    {
        super(name, plugin);
    }

    public FilteredPrevention(String name, String permission, Plugin plugin)
    {
        super(name, permission, plugin);
    }

    /**
     * This method adds the entries "mode" and "list" to the default config
     *
     * @return the default config
     */
    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection defaultConfig = super.getDefaultConfig();

        defaultConfig.set("mode", "blacklist");
        defaultConfig.set("list", new String[] {"example"});

        return defaultConfig;
    }

    /**
     * This method reads the additional entries "mode" and "list"
     * 
     * @param server
     * @param config
     */
    @Override
    public void enable(final Server server, final ConfigurationSection config)
    {
        super.enable(server, config);

        this.mode = Mode.getByAlias(config.getString("mode"));
        this.filterItems = new HashSet<Object>(config.getList("list"));
    }

    /**
     * This method checks whether the player can do the subaction
     *
     * @param player the player
     * @param item the item representing the subaction
     * @return true if he can
     */
    public boolean can(final Player player, final Object item)
    {
        if (!can(player))
        {
            //AntiGuest.debug("Filter mode: " + this.mode.name());

            switch (this.mode)
            {
                case NONE:
                    return false;
                case WHITELIST:
                    return this.filterItems.contains(item);
                case BLACKLIST:
                    return !this.filterItems.contains(item);
            }
        }
        return true;
    }

    /**
     * Prevents the action if the player can't pass it
     * 
     * @param event a cancellable event
     * @param player the player
     * @param item the item representing the subaction
     * @return true if the action was prevented
     */
    public boolean prevent(final Cancellable event, final Player player, final Object item)
    {
        if (!this.can(player, item))
        {
            event.setCancelled(true);
            this.sendMessage(player);
            return true;
        }
        return false;
    }

    /**
     * The same as prevent(Cancellable, Player, Object) except that it is throttled
     *
     * @param event a cancellable event
     * @param player the player
     * @param item the item representing the subaction
     * @return true if the action was prevented
     */
    public boolean preventThrottled(final Cancellable event, final Player player, final Object item)
    {
        if (!this.can(player, item))
        {
            event.setCancelled(true);
            this.sendThrottledMessage(player);
            return true;
        }
        return false;
    }

    /**
     * Returns the mode this prevention currently uses
     *
     * @return the mode
     */
    public Mode getMode()
    {
        return this.mode;
    }

    /**
     * Represents the modes of a filtered prevention
     */
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
