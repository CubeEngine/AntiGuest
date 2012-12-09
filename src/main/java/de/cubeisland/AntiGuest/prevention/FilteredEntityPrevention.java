package de.cubeisland.AntiGuest.prevention;

import org.bukkit.entity.EntityType;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a filterable Prevention related to Materials
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredEntityPrevention extends FilteredPrevention<EntityType>
{
    public FilteredEntityPrevention(final String name, final PreventionPlugin plugin)
    {
        this(name, plugin, true);
    }

    public FilteredEntityPrevention(String name, PreventionPlugin plugin, boolean allowPunishing)
    {
        this(name, plugin, allowPunishing, allowPunishing);
    }

    public FilteredEntityPrevention(String name, PreventionPlugin plugin, boolean allowPunishing, boolean allowViolationLogging)
    {
        super(name, plugin, allowPunishing, allowViolationLogging);
        setFilterItems(EnumSet.of(EntityType.CREEPER));
        setFilterMode(FilterMode.NONE);
    }

    @Override
    public List<String> encodeSet(Set<EntityType> set)
    {
        List<String> types = super.encodeSet(set);

        for (int i = 0; i < types.size(); ++i)
        {
            types.set(i, types.get(i).toLowerCase().replace('_', ' '));
        }

        return types;
    }

    @Override
    public Set<EntityType> decodeList(List<String> list)
    {
        Set<EntityType> types = EnumSet.noneOf(EntityType.class);

        for (String entry : list)
        {
            EntityType type = EntityType.fromName(entry.replace(' ', '_'));
            if (type != null)
            {
                types.add(type);
            }
        }

        return types;
    }
}
