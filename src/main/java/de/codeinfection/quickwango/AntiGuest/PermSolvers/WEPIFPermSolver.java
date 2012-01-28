package de.codeinfection.quickwango.AntiGuest.PermSolvers;

import com.sk89q.wepif.PermissionsResolverManager;
import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.PermSolver;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class WEPIFPermSolver implements PermSolver
{
    private static final AntiGuest plugin = AntiGuest.getInstance();
    private static final PermissionsResolverManager solver = PermissionsResolverManager.getInstance();

    public boolean hasPermission(Player player, String permission)
    {
        plugin.debug("Permission to solve: " + permission);
        final boolean result = solver.hasPermission(player, permission);
        plugin.debug("Result: " + String.valueOf(result));
        return result;
    }
}
