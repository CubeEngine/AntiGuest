package de.cubeisland.AntiGuest.prevention;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This class represents a filterable Prevention related to Materials
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredItemPrevention extends FilteredPrevention<Material>
{
    private boolean ignoreBlocks = false;

    public FilteredItemPrevention(final String name, final PreventionPlugin plugin)
    {
        this(name, PERMISSION_BASE + name, plugin);
    }

    public FilteredItemPrevention(String name, String permission, PreventionPlugin plugin)
    {
        super(name, permission, plugin);
        this.filterItems = EnumSet.of(Material.DIRT);
    }

    /**
     * Sets whether this prevention should ignore blocks
     *
     * @param ignore true to ignore blocks
     */
    public final void setIgnoreBlocks(boolean ignore)
    {
        this.ignoreBlocks = ignore;
    }

    /**
     * Returns whether this prevention ignores blocks
     *
     * @return true if it ignores blocks
     */
    public final boolean getIgnoreBlocks()
    {
        return this.ignoreBlocks;
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

        ArrayList<String> materials = new ArrayList<String>(this.filterItems.size());
        for (Material material : this.filterItems)
        {
            materials.add(material.toString().toLowerCase().replace('_', ' '));
        }
        
        config.set("list", materials);

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
        Set<Material> newItems = EnumSet.noneOf(Material.class);
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
    @Override
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
    @Override
    public boolean prevent(final Cancellable event, final Player player, final Material material)
    {
        if (!this.can(player, material))
        {
            event.setCancelled(true);
            sendMessage(player);
            return true;
        }
        return false;
    }
}
