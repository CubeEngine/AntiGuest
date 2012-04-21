package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This commands checks whether a player can pass a prevention
 *
 * @author Phillip Schichtel
 */
public class ResetCommand extends AbstractCommand implements Listener
{
    private final Set<CommandSender> requests;
    private final Server server;

    public ResetCommand(BaseCommand base)
    {
        super("reset", base);
        this.requests = new HashSet<CommandSender>();
        this.server = base.getPlugin().getServer();
        this.server.getPluginManager().registerEvents(this, base.getPlugin());
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (requests.contains(sender))
        {
            requests.remove(sender);

            PreventionManager mgr = PreventionManager.getInstance();

            for (Prevention prevention : mgr.getPreventions())
            {
                prevention.resetConfig();
            }

            sender.sendMessage(_("configsResetted"));
        }
        else
        {
            requests.add(sender);
            sender.sendMessage(_("resetRequested"));
            if (sender instanceof Player)
            {
                Player player = (Player)sender;
                this.broadcast(_("playerRequestedReset", player.getName()), player);
            }
            else
            {
                this.broadcast(_("consoleRequestedReset"), null);
            }
        }
        
        return true;
    }

    private void broadcast(String message, Player sender)
    {
        if (sender != null)
        {
            AntiGuest.log(message);
        }
        for (Player player : this.server.getOnlinePlayers())
        {
            if (player != sender && player.hasPermission(getPermission()))
            {
                player.sendMessage(message);
            }
        }
    }

    @EventHandler
    public void removeRequest(PlayerQuitEvent event)
    {
        this.requests.remove(event.getPlayer());
    }
}
