package de.cubeisland.antiguest.commands;

import static de.cubeisland.antiguest.AntiGuest.translate;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.cubeisland.antiguest.AntiGuest;
import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;
import gnu.trove.set.hash.THashSet;

/**
 * @author CodeInfection
 */
public class BasicCommands {
    private final AntiGuest plugin;
    private final Set<CommandSender> resetRequest;

    public BasicCommands(AntiGuest plugin) {
        this.plugin = plugin;
        resetRequest = new THashSet<CommandSender>();
    }

    @Command(usage = "[prevention]")
    @RequiresPermission
    public void reload(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            final PreventionManager mgr = PreventionManager.getInstance();
            final Prevention prevention = mgr.getPrevention(args.getString(0));
            if (prevention != null) {
                mgr.disablePrevention(prevention);
                prevention.reloadConfig();
                mgr.enablePrevention(prevention);
                sender.sendMessage(translate("preventionReloaded", prevention.getName()));
            } else
                sender.sendMessage(translate("preventionNotFound"));
        } else {
            final PluginManager pm = plugin.getServer().getPluginManager();
            pm.disablePlugin(plugin);
            pm.enablePlugin(plugin);

            sender.sendMessage(translate("reloaded", plugin.getName()));
        }
    }

    @Command
    @RequiresPermission
    public void reset(CommandSender sender, CommandArgs args) {
        if (resetRequest.contains(sender)) {
            resetRequest.remove(sender);

            PreventionManager mgr = PreventionManager.getInstance();

            for (Prevention prevention : mgr.getPreventions())
                prevention.resetConfig();

            sender.sendMessage(translate("configsResetted"));
        } else {
            resetRequest.add(sender);
            sender.sendMessage(translate("resetRequested"));
            if (sender instanceof Player) {
                Player player = (Player) sender;
                broadcastResetNotice(translate("playerRequestedReset", player.getName()), player, args);
            } else
                broadcastResetNotice(translate("consoleRequestedReset"), null, args);
        }
    }

    private void broadcastResetNotice(String message, Player sender, CommandArgs args) {
        if (sender != null)
            AntiGuest.log(message);
        for (Player player : plugin.getServer().getOnlinePlayers())
            if (player != sender && player.hasPermission(args.getSubCommand().getPermission()))
                player.sendMessage(message);
    }
}
