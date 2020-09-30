package de.cubeisland.antiguest.commands;

import static de.cubeisland.antiguest.AntiGuest._;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.libMinecraft.ChatColor;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;

/**
 * @author CodeInfection
 */
public class PreventionManagementCommands {
    private final PreventionManager pm;

    public PreventionManagementCommands() {
        pm = PreventionManager.getInstance();
    }

    @Command(usage = "<prevention|*> [-t]")
    @RequiresPermission
    public void enable(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            boolean temporary = args.hasFlag("t");
            if ("*".equals(args.getString(0))) {
                for (Prevention prevention : pm.getPreventions()) {
                    pm.enablePrevention(prevention);
                    if (!temporary) {
                        prevention.getConfig().set("enable", true);
                        prevention.saveConfig();
                    }
                }

                sender.sendMessage(_("preventionsEnabled"));
            } else {
                Prevention prevention = pm.getPrevention(args.getString(0));
                if (prevention != null) {
                    if (!prevention.isEnabled()) {
                        if (PreventionManager.getInstance().enablePrevention(prevention)) {
                            sender.sendMessage(_("preventionEnabled"));
                            if (!temporary) {
                                prevention.getConfig().set("enable", true);
                                prevention.saveConfig();
                            }
                        } else
                            sender.sendMessage(_("somethingFailed"));
                    } else
                        sender.sendMessage(_("alreadyEnabled"));
                } else
                    sender.sendMessage(_("preventionNotFound"));
            }
        } else
            sender.sendMessage(_("noPrevention"));
    }

    @Command(usage = "<prevention> [-t]")
    @RequiresPermission
    public void disable(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            boolean temporary = args.hasFlag("t");
            if ("*".equals(args.getString(0))) {
                for (Prevention prevention : pm.getPreventions()) {
                    pm.disablePrevention(prevention);
                    if (!temporary) {
                        prevention.getConfig().set("enable", false);
                        prevention.saveConfig();
                    }
                }

                sender.sendMessage(_("preventionsDisabled"));
            } else {
                Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
                if (prevention != null) {
                    if (prevention.isEnabled()) {
                        PreventionManager.getInstance().disablePrevention(prevention);
                        sender.sendMessage(_("preventionDisabled"));
                        if (!temporary) {
                            prevention.getConfig().set("enable", false);
                            prevention.saveConfig();
                        }
                    } else
                        sender.sendMessage(_("alreadyDisabled"));
                } else
                    sender.sendMessage(_("preventionNotFound"));
            }
        } else
            sender.sendMessage(_("noPrevention"));
    }

    @Command(usage = "<prevention>")
    @RequiresPermission
    public void enabled(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null) {
                if (prevention.isEnabled())
                    sender.sendMessage(_("enabled"));
                else
                    sender.sendMessage(_("disabled"));
            } else
                sender.sendMessage(_("preventionNotFound"));
        } else
            sender.sendMessage(_("noPrevention"));
    }

    @Command(usage = "[-a]")
    @RequiresPermission
    public void list(CommandSender sender, CommandArgs args) {
        if (args.hasFlag("a")) {
            sender.sendMessage(_("registeredPreventions"));
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
                sender.sendMessage(" - " + (prevention.isEnabled() ? ChatColor.BRIGHT_GREEN : ChatColor.RED) + prevention.getName());
            sender.sendMessage("");
        } else {
            sender.sendMessage(_("activePreventions"));
            sender.sendMessage("");
            int i = 0;
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
                if (prevention.isEnabled()) {
                    ++i;
                    sender.sendMessage(" - " + ChatColor.BRIGHT_GREEN + prevention.getName());
                }
            sender.sendMessage("");
        }
    }

    @Command(usage = "[player] <prevention>")
    @RequiresPermission
    public void can(CommandSender sender, CommandArgs args) {
        Player player;
        Prevention prevention;
        if (args.size() > 1) {
            player = args.getBaseCommand().getPlugin().getServer().getPlayer(args.getString(0));
            prevention = pm.getPrevention(args.getString(1));
        } else if (args.size() > 0 && sender instanceof Player) {
            player = (Player) sender;
            prevention = pm.getPrevention(args.getString(0));
        } else {
            sender.sendMessage(_("tooFewArguments"));
            sender.sendMessage(_("see", args.getBaseLabel() + " help"));
            return;
        }

        if (player != null) {
            if (prevention != null) {
                if (prevention.can(player)) {
                    if (sender == player)
                        sender.sendMessage(_("you_ableToPass"));
                    else
                        sender.sendMessage(_("other_ableToPass"));
                } else if (sender == player)
                    sender.sendMessage(_("you_unableToPass"));
                else
                    sender.sendMessage(_("other_unableToPass"));
            } else
                sender.sendMessage(_("preventionNotFound"));
        } else
            sender.sendMessage(_("playerNotFound"));
    }

    @Command(usage = "<prevention> <message>")
    @RequiresPermission
    public void setMessage(CommandSender sender, CommandArgs args) {
        if (args.size() > 1) {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null) {
                String message = args.getString(1);
                prevention.setMessage(message);
                prevention.getConfig().set("message", message);
                prevention.saveConfig();

                sender.sendMessage(_("messageSet"));
            } else
                sender.sendMessage(_("preventionNotFound"));
        } else
            sender.sendMessage(_("tooFewArguments"));
    }
}
