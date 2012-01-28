package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public interface PermSolver
{
    public boolean hasPermission(Player player, String permission);
}
