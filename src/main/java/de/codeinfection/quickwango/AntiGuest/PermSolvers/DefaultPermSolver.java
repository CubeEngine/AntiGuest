package de.codeinfection.quickwango.AntiGuest.PermSolvers;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.PermSolver;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class DefaultPermSolver implements PermSolver
{
    private static final AntiGuest plugin = AntiGuest.getInstance();

    public boolean hasPermission(Player player, String permission)
    {
        plugin.debug("Permission to solve: " + permission);
        final boolean result = player.hasPermission(permission);
        plugin.debug("Result: " + String.valueOf(result));
        return result;
    }
}
