package de.cubeisland.antiguest.prevention.preventions;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import de.cubeisland.antiguest.prevention.FilteredPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents users from trading
 *
 * @author Phillip Schichtel
 */
public class TradingPrevention extends FilteredPrevention<Profession> {
    public TradingPrevention(PreventionPlugin plugin) {
        super("trading", plugin);
        setFilterMode(FilterMode.NONE);
        setFilterItems(EnumSet.allOf(Profession.class));
    }

    @Override
    public Set<Profession> decodeList(List<String> list) {
        Set<Profession> professions = EnumSet.noneOf(Profession.class);

        for (String name : list)
            professions.add(Profession.valueOf(name.trim().toUpperCase(Locale.ENGLISH)));

        return professions;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager && !can(event.getPlayer()))
            checkAndPrevent(event, event.getPlayer(), ((Villager) event.getRightClicked()).getProfession());
    }
}
