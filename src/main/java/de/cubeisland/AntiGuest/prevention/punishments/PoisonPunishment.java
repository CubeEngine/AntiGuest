package de.cubeisland.AntiGuest.prevention.punishments;

import de.cubeisland.AntiGuest.prevention.Punishment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Poisons a player
 *
 * @author Phillip Schichtel
 */
public class PoisonPunishment implements Punishment
{
    public String getName()
    {
        return "poison";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, config.getInt("duration", 3) * 20, config.getInt("amplifier", 1)));
    }
}
