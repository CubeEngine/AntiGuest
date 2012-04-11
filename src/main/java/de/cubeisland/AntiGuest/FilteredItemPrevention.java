package de.cubeisland.AntiGuest;

import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This class represents a filterable Prevention related to Materials
 *
 * @author Phillip Schichtel
 * @TODO reduce code duplication in prevent and preventThrottled
 */
public abstract class FilteredItemPrevention extends FilteredPrevention
{
    private final boolean ignoreBlocks;

    public FilteredItemPrevention(final String name, final PreventionPlugin plugin)
    {
        this(name, plugin, false);
    }

    public FilteredItemPrevention(final String name, final PreventionPlugin plugin, boolean ignoreBlocks)
    {
        this(name, plugin, false, ignoreBlocks);
    }

    public FilteredItemPrevention(final String name, final PreventionPlugin plugin, boolean enableByDefault, boolean ignoreBlocks)
    {
        super(name, plugin, enableByDefault);
        this.ignoreBlocks = ignoreBlocks;
    }

    public FilteredItemPrevention(String name, String permission, PreventionPlugin plugin, boolean enableByDefault)
    {
        this(name, permission, plugin, enableByDefault, false);
    }

    public FilteredItemPrevention(String name, String permission, PreventionPlugin plugin, boolean enableByDefault, boolean ignoreBlocks)
    {
        super(name, permission, plugin, enableByDefault);
        this.ignoreBlocks = ignoreBlocks;
    }

    /**
     * This method changes the default value if the "list" entry
     *
     * @return the default config
     */
    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("list", new String[] {Material.DIRT.toString().toLowerCase()});

        return config;
    }

    /**
     * This method parses the string list to Material instances
     *
     * @param server a Server instance
     * @param config the config of this prevention
     */
    @Override
    public void enable()
    {
        super.enable();

        // normalize the items
        HashSet<Object> newItems = new HashSet<Object>(this.filterItems.size());
        for (Object item : this.filterItems)
        {
            Material material = Material.matchMaterial(String.valueOf(item));
            if (material != null)
            {
                newItems.add(material);
            }
        }
        this.filterItems = newItems;
    }

    /**
     * This method checks whether the player can do the subaction
     *
     * @param player the player
     * @param material the material
     * @return true if he can
     */
    public boolean can(final Player player, final Material material)
    {
        if (this.ignoreBlocks && material.getId() <= 256)
        {
            return true;
        }
        else
        {
            return super.can(player, material);
        }
    }

    /**
     * Prevents the action if the player can't pass it
     *
     * @param event a cancellable event
     * @param player the player
     * @param material the material
     * @return true if the action was prevented
     */
    public boolean prevent(final Cancellable event, final Player player, final Material material)
    {
        if (!this.can(player, material))
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
     * @param material the item representing the subaction
     * @return true if the action was prevented
     */
    public boolean preventThrottled(final Cancellable event, final Player player, final Material material)
    {
        if (!this.can(player, material))
        {
            event.setCancelled(true);
            this.sendThrottledMessage(player);
            return true;
        }
        return false;
    }
}
