package de.cubeisland.antiguest.prevention.punishments;

import java.util.Locale;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cubeisland.antiguest.prevention.Punishment;

/**
 * Poisons a player
 *
 * @author Phillip Schichtel
 */
public class PotionPunishment implements Punishment {
    @Override
    public String getName() {
        return "potion";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(config.getString("effect").toUpperCase(Locale.ENGLISH)), config.getInt("duration", 3) * 20, config.getInt("amplifier", 1)));
    }
}
