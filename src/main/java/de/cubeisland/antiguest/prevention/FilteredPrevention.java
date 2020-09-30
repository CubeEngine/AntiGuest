package de.cubeisland.antiguest.prevention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import gnu.trove.set.hash.THashSet;

/**
 * This base class represents a prevention that can be filtered. That means it
 * is able to checkAndPrevent only a subset of actions based an a whitelist or
 * blacklist
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredPrevention<T> extends Prevention {
    private Set<T> filterItems;
    private FilterMode filterMode;

    public FilteredPrevention(String name, PreventionPlugin plugin) {
        this(name, plugin, true);
    }

    public FilteredPrevention(String name, PreventionPlugin plugin, boolean allowPunishing) {
        this(name, plugin, allowPunishing, allowPunishing);
    }

    public FilteredPrevention(String name, PreventionPlugin plugin, boolean allowPunishing, boolean allowViolationLogging) {
        super(name, plugin, allowPunishing, allowViolationLogging);
        this.filterMode = FilterMode.BLACKLIST;
        this.filterItems = new THashSet<T>(0);
    }

    /**
     * Returns the mode this prevention filters in
     *
     * @return the filter mode
     */
    public FilterMode getFilterMode() {
        return this.filterMode;
    }

    /**
     * Sets the mode this prevention filters in
     *
     * @param mode the mode to filter in
     */
    public void setFilterMode(FilterMode mode) {
        this.filterMode = mode;
    }

    /**
     * Sets the filter items
     *
     * @param filterItems the filter items
     */
    public void setFilterItems(Set<T> filterItems) {
        this.filterItems = filterItems;
    }

    /**
     * Returns the filter items
     *
     * @return the filter items
     */
    public Set<T> getFilterItems() {
        return this.filterItems;
    }

    /**
     * This method adds the entries "mode" and "list" to the default config
     *
     * @return the default config
     */
    @Override
    public Configuration getDefaultConfig() {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("mode", this.filterMode.getName());
        defaultConfig.set("list", encodeSet(this.filterItems));

        return defaultConfig;
    }

    /**
     * Encodes the type specific set into a string list
     *
     * @param set the set
     *
     * @return the string list
     */
    public List<String> encodeSet(Set<T> set) {
        List<String> stringList = new ArrayList<String>(set.size());

        for (T entry : set)
            stringList.add(String.valueOf(entry));

        return stringList;
    }

    /**
     * Decodes a string list into the type specific item set
     *
     * @param list the list
     *
     * @return the set
     */
    public abstract Set<T> decodeList(List<String> list);

    @Override
    public void enable() {
        super.enable();

        this.filterMode = FilterMode.getByAlias(getConfig().getString("mode"));
        List<String> items = getConfig().getStringList("list");
        if (items != null)
            setFilterItems(decodeList(items));
    }

    /**
     * This method checks whether the player can do the subaction
     *
     * @param player the player
     * @param item   the item representing the subaction
     *
     * @return true if he can
     */
    public boolean can(final Player player, final T item) {
        if (!can(player))
            switch (getFilterMode()) {
                case NONE:
                    return false;
                case WHITELIST:
                    return getFilterItems().contains(item);
                case BLACKLIST:
                    return !getFilterItems().contains(item);
            }
        return true;
    }

    /**
     * Prevents the action if the player can't pass it
     *
     * @param event  a cancellable event
     * @param player the player
     * @param item   the item representing the subaction
     *
     * @return true if the action was prevented
     */
    public boolean checkAndPrevent(final Cancellable event, final Player player, final T item) {
        if (!this.can(player, item)) {
            prevent(event, player);
            return true;
        }
        return false;
    }

    /**
     * Represents the modes of a filtered prevention
     */
    public enum FilterMode {
        NONE(
                "none", "-1", "nolist", "all"
        ),
        WHITELIST(
                "whitelist", "0", "white"
        ),
        BLACKLIST(
                "blacklist", "1", "black"
        );

        private static final HashMap<String, FilterMode> ALIAS_MAP = new HashMap<String, FilterMode>(values().length);

        private final String name;
        private final String[] aliases;

        FilterMode(String name, String... aliases) {
            this.name = name;
            this.aliases = aliases;
        }

        public String getName() {
            return name;
        }

        public String[] getAliases() {
            return aliases;
        }

        public static FilterMode getByAlias(String alias) {
            return ALIAS_MAP.get(alias.toLowerCase());
        }

        static {
            for (FilterMode mode : values()) {
                ALIAS_MAP.put(mode.getName(), mode);
                for (String alias : mode.getAliases())
                    ALIAS_MAP.put(alias.toLowerCase(), mode);
            }
        }
    }
}
