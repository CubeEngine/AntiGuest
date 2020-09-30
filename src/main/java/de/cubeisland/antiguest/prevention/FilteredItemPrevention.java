package de.cubeisland.antiguest.prevention;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * This class represents a filterable Prevention related to Materials
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredItemPrevention extends FilteredPrevention<Material> {
    private boolean ignoreBlocks = false;

    public FilteredItemPrevention(final String name, final PreventionPlugin plugin) {
        this(name, plugin, true);
    }

    public FilteredItemPrevention(String name, PreventionPlugin plugin, boolean allowPunishing) {
        this(name, plugin, allowPunishing, allowPunishing);
    }

    public FilteredItemPrevention(String name, PreventionPlugin plugin, boolean allowPunishing, boolean allowViolationLogging) {
        super(name, plugin, allowPunishing, allowViolationLogging);
        setFilterItems(EnumSet.of(Material.DIRT));
        setFilterMode(FilterMode.NONE);
    }

    /**
     * Sets whether this prevention should ignore blocks
     *
     * @param ignore true to ignore blocks
     */
    public final void setIgnoreBlocks(boolean ignore) {
        ignoreBlocks = ignore;
    }

    /**
     * Returns whether this prevention ignores blocks
     *
     * @return true if it ignores blocks
     */
    public final boolean getIgnoreBlocks() {
        return ignoreBlocks;
    }

    @Override
    public List<String> encodeSet(Set<Material> set) {
        List<String> materials = super.encodeSet(set);

        for (int i = 0; i < materials.size(); ++i)
            materials.set(i, materials.get(i).toLowerCase().replace('_', ' '));

        return materials;
    }

    @Override
    public Set<Material> decodeList(List<String> list) {
        Set<Material> materials = EnumSet.noneOf(Material.class);

        for (String entry : list) {
            Material material = Material.matchMaterial(entry);
            if (material != null)
                materials.add(material);
        }

        return materials;
    }

    /**
     * This method checks whether the player can do the subaction
     *
     * @param player   the player
     * @param material the material
     *
     * @return true if he can
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean can(final Player player, final Material material) {
        if (ignoreBlocks && material.getId() <= 256)
            return true;
        else
            return super.can(player, material);
    }
}
