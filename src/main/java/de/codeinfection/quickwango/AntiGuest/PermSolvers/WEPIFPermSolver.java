package de.codeinfection.quickwango.AntiGuest.PermSolvers;

import com.sk89q.wepif.PermissionsResolverManager;
import de.codeinfection.quickwango.AntiGuest.PermSolver;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class WEPIFPermSolver implements PermSolver
{
    private static final PermissionsResolverManager solver = PermissionsResolverManager.getInstance();

    public boolean hasPermission(Player player, String permission)
    {
        return solver.hasPermission(player, permission);
    }
}
