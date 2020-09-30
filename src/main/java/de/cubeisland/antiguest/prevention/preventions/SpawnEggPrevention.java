package de.cubeisland.antiguest.prevention.preventions;

import static org.bukkit.Material.BAT_SPAWN_EGG;
import static org.bukkit.Material.BEE_SPAWN_EGG;
import static org.bukkit.Material.BLAZE_SPAWN_EGG;
import static org.bukkit.Material.CAT_SPAWN_EGG;
import static org.bukkit.Material.CAVE_SPIDER_SPAWN_EGG;
import static org.bukkit.Material.CHICKEN_SPAWN_EGG;
import static org.bukkit.Material.COD_SPAWN_EGG;
import static org.bukkit.Material.COW_SPAWN_EGG;
import static org.bukkit.Material.CREEPER_SPAWN_EGG;
import static org.bukkit.Material.DOLPHIN_SPAWN_EGG;
import static org.bukkit.Material.DONKEY_SPAWN_EGG;
import static org.bukkit.Material.DROWNED_SPAWN_EGG;
import static org.bukkit.Material.ELDER_GUARDIAN_SPAWN_EGG;
import static org.bukkit.Material.ENDERMAN_SPAWN_EGG;
import static org.bukkit.Material.ENDERMITE_SPAWN_EGG;
import static org.bukkit.Material.EVOKER_SPAWN_EGG;
import static org.bukkit.Material.FOX_SPAWN_EGG;
import static org.bukkit.Material.GHAST_SPAWN_EGG;
import static org.bukkit.Material.GUARDIAN_SPAWN_EGG;
import static org.bukkit.Material.HOGLIN_SPAWN_EGG;
import static org.bukkit.Material.HORSE_SPAWN_EGG;
import static org.bukkit.Material.HUSK_SPAWN_EGG;
import static org.bukkit.Material.LLAMA_SPAWN_EGG;
import static org.bukkit.Material.MAGMA_CUBE_SPAWN_EGG;
import static org.bukkit.Material.MOOSHROOM_SPAWN_EGG;
import static org.bukkit.Material.MULE_SPAWN_EGG;
import static org.bukkit.Material.OCELOT_SPAWN_EGG;
import static org.bukkit.Material.PANDA_SPAWN_EGG;
import static org.bukkit.Material.PARROT_SPAWN_EGG;
import static org.bukkit.Material.PHANTOM_SPAWN_EGG;
import static org.bukkit.Material.PIG_SPAWN_EGG;
import static org.bukkit.Material.PIGLIN_SPAWN_EGG;
import static org.bukkit.Material.PIGLIN_BRUTE_SPAWN_EGG;
import static org.bukkit.Material.PILLAGER_SPAWN_EGG;
import static org.bukkit.Material.POLAR_BEAR_SPAWN_EGG;
import static org.bukkit.Material.PUFFERFISH_SPAWN_EGG;
import static org.bukkit.Material.RABBIT_SPAWN_EGG;
import static org.bukkit.Material.RAVAGER_SPAWN_EGG;
import static org.bukkit.Material.SALMON_SPAWN_EGG;
import static org.bukkit.Material.SHEEP_SPAWN_EGG;
import static org.bukkit.Material.SHULKER_SPAWN_EGG;
import static org.bukkit.Material.SILVERFISH_SPAWN_EGG;
import static org.bukkit.Material.SKELETON_SPAWN_EGG;
import static org.bukkit.Material.SKELETON_HORSE_SPAWN_EGG;
import static org.bukkit.Material.SLIME_SPAWN_EGG;
import static org.bukkit.Material.SPIDER_SPAWN_EGG;
import static org.bukkit.Material.SQUID_SPAWN_EGG;
import static org.bukkit.Material.STRAY_SPAWN_EGG;
import static org.bukkit.Material.STRIDER_SPAWN_EGG;
import static org.bukkit.Material.TRADER_LLAMA_SPAWN_EGG;
import static org.bukkit.Material.TROPICAL_FISH_SPAWN_EGG;
import static org.bukkit.Material.TURTLE_SPAWN_EGG;
import static org.bukkit.Material.VEX_SPAWN_EGG;
import static org.bukkit.Material.VILLAGER_SPAWN_EGG;
import static org.bukkit.Material.VINDICATOR_SPAWN_EGG;
import static org.bukkit.Material.WANDERING_TRADER_SPAWN_EGG;
import static org.bukkit.Material.WITCH_SPAWN_EGG;
import static org.bukkit.Material.WITHER_SKELETON_SPAWN_EGG;
import static org.bukkit.Material.WOLF_SPAWN_EGG;
import static org.bukkit.Material.ZOGLIN_SPAWN_EGG;
import static org.bukkit.Material.ZOMBIE_SPAWN_EGG;
import static org.bukkit.Material.ZOMBIE_HORSE_SPAWN_EGG;
import static org.bukkit.Material.ZOMBIE_VILLAGER_SPAWN_EGG;
import static org.bukkit.Material.ZOMBIFIED_PIGLIN_SPAWN_EGG;

import java.util.EnumSet;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.cubeisland.antiguest.prevention.FilteredEntityPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents spawnegg usage
 *
 * @author Phillip Schichtel
 */
public class SpawnEggPrevention extends FilteredEntityPrevention
{
    private static final EnumSet<Material> EGGS = EnumSet.of(BAT_SPAWN_EGG, BEE_SPAWN_EGG, BLAZE_SPAWN_EGG, CAT_SPAWN_EGG, CAVE_SPIDER_SPAWN_EGG, CHICKEN_SPAWN_EGG, COD_SPAWN_EGG, COW_SPAWN_EGG, CREEPER_SPAWN_EGG, DOLPHIN_SPAWN_EGG, DONKEY_SPAWN_EGG, DROWNED_SPAWN_EGG, ELDER_GUARDIAN_SPAWN_EGG, ENDERMAN_SPAWN_EGG, ENDERMITE_SPAWN_EGG, EVOKER_SPAWN_EGG, FOX_SPAWN_EGG, GHAST_SPAWN_EGG, GUARDIAN_SPAWN_EGG, HOGLIN_SPAWN_EGG, HORSE_SPAWN_EGG, HUSK_SPAWN_EGG, LLAMA_SPAWN_EGG, MAGMA_CUBE_SPAWN_EGG, MOOSHROOM_SPAWN_EGG, MULE_SPAWN_EGG, OCELOT_SPAWN_EGG, PANDA_SPAWN_EGG, PARROT_SPAWN_EGG, PHANTOM_SPAWN_EGG, PIG_SPAWN_EGG, PIGLIN_SPAWN_EGG, PIGLIN_BRUTE_SPAWN_EGG, PILLAGER_SPAWN_EGG, POLAR_BEAR_SPAWN_EGG, PUFFERFISH_SPAWN_EGG, RABBIT_SPAWN_EGG, RAVAGER_SPAWN_EGG, SALMON_SPAWN_EGG, SHEEP_SPAWN_EGG, SHULKER_SPAWN_EGG, SILVERFISH_SPAWN_EGG, SKELETON_SPAWN_EGG, SKELETON_HORSE_SPAWN_EGG, SLIME_SPAWN_EGG, SPIDER_SPAWN_EGG, SQUID_SPAWN_EGG, STRAY_SPAWN_EGG, STRIDER_SPAWN_EGG, TRADER_LLAMA_SPAWN_EGG, TROPICAL_FISH_SPAWN_EGG, TURTLE_SPAWN_EGG, VEX_SPAWN_EGG, VILLAGER_SPAWN_EGG, VINDICATOR_SPAWN_EGG, WANDERING_TRADER_SPAWN_EGG, WITCH_SPAWN_EGG, WITHER_SKELETON_SPAWN_EGG, WOLF_SPAWN_EGG, ZOGLIN_SPAWN_EGG, ZOMBIE_SPAWN_EGG, ZOMBIE_HORSE_SPAWN_EGG, ZOMBIE_VILLAGER_SPAWN_EGG, ZOMBIFIED_PIGLIN_SPAWN_EGG);

    public SpawnEggPrevention(PreventionPlugin plugin)
    {
        super("spawnegg", plugin, true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("deprecation")
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            ItemStack item = event.getItem();
            if (item != null && EGGS.contains(item.getType()))
            {
                if (checkAndPrevent(event, event.getPlayer(), EntityType.fromId(item.getData().getData())))
                {
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }
        }
    }
}
