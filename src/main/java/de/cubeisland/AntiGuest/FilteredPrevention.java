package de.cubeisland.AntiGuest;

import gnu.trove.set.hash.THashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This base class represents a prevention that can be filtered.
 * That means it is able to prevent only a subset of actions based an a whitelist or blacklist
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredPrevention<T extends Object> extends PunishedPrevention
{
    protected Set<T> filterItems;
    private FilterMode filterMode;
    
    public FilteredPrevention(String name, PreventionPlugin plugin)
    {
        this(name, PERMISSION_BASE + name, plugin);
    }

    public FilteredPrevention(String name, String permission, PreventionPlugin plugin)
    {
        super(name, permission, plugin);
        this.filterMode = FilterMode.BLACKLIST;
    }

    /**
     * Returns the mode this prevention filters in
     *
     * @return the filter mode
     */
    public FilterMode getFilterMode()
    {
        return this.filterMode;
    }

    /**
     * Sets the mode this prevention filters in
     *
     * @param mode the mode to filter in
     */
    public void setFilterMode(FilterMode mode)
    {
        this.filterMode = mode;
    }

    /**
     * This method adds the entries "mode" and "list" to the default config
     *
     * @return the default config
     */
    @Override
    public Configuration getDefaultConfig()
    {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("mode", this.filterMode.getName());
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
    public void enable()
    {
        super.enable();

        this.filterMode = FilterMode.getByAlias(getConfig().getString("mode"));
        List<?> items = getConfig().getList("list");
        if (items == null)
        {
            this.filterItems = new THashSet();
        }
        else
        {
            this.filterItems = new THashSet(items);
        }
    }

    /**
     * This method checks whether the player can do the subaction
     *
     * @param player the player
     * @param item the item representing the subaction
     * @return true if he can
     */
    public boolean can(final Player player, final T item)
    {
        if (!can(player))
        {
            //AntiGuest.debug("Filter mode: " + this.mode.name());

            switch (this.filterMode)
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
    public boolean prevent(final Cancellable event, final Player player, final T item)
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
     * Returns the mode this prevention currently uses
     *
     * @return the mode
     */
    public FilterMode getMode()
    {
        return this.filterMode;
    }

    /**
     * Represents the modes of a filtered prevention
     */
    public enum FilterMode
    {
        NONE("none", "-1", "nolist", "all"),
        WHITELIST("whitelist", "0", "white", "positivlist"),
        BLACKLIST("blacklist", "1", "black", "negativlist");

        private static final HashMap<String, FilterMode> ALIAS_MAP = new HashMap<String, FilterMode>(values().length);

        private final String name;
        private final String[] aliases;
        
        FilterMode(String name, String... aliases)
        {
            this.name = name;
            this.aliases = aliases;
        }

        public String getName()
        {
            return this.name;
        }

        public String[] getAliases()
        {
            return this.aliases;
        }

        public static FilterMode getByAlias(String alias)
        {
            return ALIAS_MAP.get(alias.toLowerCase());
        }

        static
        {
            for (FilterMode mode : values())
            {
                ALIAS_MAP.put(mode.getName(), mode);
                for (String alias : mode.getAliases())
                {
                    ALIAS_MAP.put(alias.toLowerCase(), mode);
                }
            }
        }
    }
}
