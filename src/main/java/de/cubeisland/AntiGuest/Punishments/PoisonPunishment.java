package de.cubeisland.AntiGuest.Punishments;

import de.cubeisland.AntiGuest.Punishment;
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

    public void punish(Player player)
    {
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 1));
    }
}
