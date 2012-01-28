package de.codeinfection.quickwango.AntiGuest.PermSolvers;

import de.codeinfection.quickwango.AntiGuest.PermSolver;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class DefaultPermSolver implements PermSolver
{
    public boolean hasPermission(Player player, String permission)
    {
        return player.hasPermission(permission);
    }
}
