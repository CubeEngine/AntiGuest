package de.cubeisland.antiguest.commands;

import de.cubeisland.antiguest.AntiGuest;
import static de.cubeisland.antiguest.AntiGuest._;
import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;
import gnu.trove.set.hash.THashSet;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
 */
public class BasicCommands
{
    private final AntiGuest plugin;
    private final Set<CommandSender> resetRequest;

    public BasicCommands(AntiGuest plugin)
    {
        this.plugin = plugin;
        this.resetRequest = new THashSet<CommandSender>();
    }

    @Command(usage = "[prevention]")
    @RequiresPermission
    public void reload(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            final PreventionManager mgr = PreventionManager.getInstance();
            final Prevention prevention = mgr.getPrevention(args.getString(0));
            if (prevention != null)
            {
                mgr.disablePrevention(prevention);
                prevention.reloadConfig();
                mgr.enablePrevention(prevention);
                sender.sendMessage(_("preventionReloaded", prevention.getName()));
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            final PluginManager pm = plugin.getServer().getPluginManager();
            pm.disablePlugin(plugin);
            pm.enablePlugin(plugin);

            sender.sendMessage(_("reloaded", plugin.getName()));
        }
    }

    @Command
    @RequiresPermission
    public void reset(CommandSender sender, CommandArgs args)
    {
        if (resetRequest.contains(sender))
        {
            resetRequest.remove(sender);

            PreventionManager mgr = PreventionManager.getInstance();

            for (Prevention prevention : mgr.getPreventions())
            {
                prevention.resetConfig();
            }

            sender.sendMessage(_("configsResetted"));
        }
        else
        {
            resetRequest.add(sender);
            sender.sendMessage(_("resetRequested"));
            if (sender instanceof Player)
            {
                Player player = (Player)sender;
                this.broadcastResetNotice(_("playerRequestedReset", player.getName()), player, args);
            }
            else
            {
                this.broadcastResetNotice(_("consoleRequestedReset"), null, args);
            }
        }
    }

    private void broadcastResetNotice(String message, Player sender, CommandArgs args)
    {
        if (sender != null)
        {
            AntiGuest.log(message);
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers())
        {
            if (player != sender && player.hasPermission(args.getSubCommand().getPermission()))
            {
                player.sendMessage(message);
            }
        }
    }
}
