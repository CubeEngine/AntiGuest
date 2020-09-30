package de.cubeisland.antiguest.commands;

import static de.cubeisland.antiguest.AntiGuest.translate;

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

                sender.sendMessage(translate("preventionsEnabled"));
            } else {
                Prevention prevention = pm.getPrevention(args.getString(0));
                if (prevention != null) {
                    if (!prevention.isEnabled()) {
                        if (PreventionManager.getInstance().enablePrevention(prevention)) {
                            sender.sendMessage(translate("preventionEnabled"));
                            if (!temporary) {
                                prevention.getConfig().set("enable", true);
                                prevention.saveConfig();
                            }
                        } else
                            sender.sendMessage(translate("somethingFailed"));
                    } else
                        sender.sendMessage(translate("alreadyEnabled"));
                } else
                    sender.sendMessage(translate("preventionNotFound"));
            }
        } else
            sender.sendMessage(translate("noPrevention"));
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

                sender.sendMessage(translate("preventionsDisabled"));
            } else {
                Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
                if (prevention != null) {
                    if (prevention.isEnabled()) {
                        PreventionManager.getInstance().disablePrevention(prevention);
                        sender.sendMessage(translate("preventionDisabled"));
                        if (!temporary) {
                            prevention.getConfig().set("enable", false);
                            prevention.saveConfig();
                        }
                    } else
                        sender.sendMessage(translate("alreadyDisabled"));
                } else
                    sender.sendMessage(translate("preventionNotFound"));
            }
        } else
            sender.sendMessage(translate("noPrevention"));
    }

    @Command(usage = "<prevention>")
    @RequiresPermission
    public void enabled(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null) {
                if (prevention.isEnabled())
                    sender.sendMessage(translate("enabled"));
                else
                    sender.sendMessage(translate("disabled"));
            } else
                sender.sendMessage(translate("preventionNotFound"));
        } else
            sender.sendMessage(translate("noPrevention"));
    }

    @Command(usage = "[-a]")
    @RequiresPermission
    public void list(CommandSender sender, CommandArgs args) {
        if (args.hasFlag("a")) {
            sender.sendMessage(translate("registeredPreventions"));
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
                sender.sendMessage(" - " + (prevention.isEnabled() ? ChatColor.BRIGHT_GREEN : ChatColor.RED) + prevention.getName());
            sender.sendMessage("");
        } else {
            sender.sendMessage(translate("activePreventions"));
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
                if (prevention.isEnabled()) {
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
            sender.sendMessage(translate("tooFewArguments"));
            sender.sendMessage(translate("see", args.getBaseLabel() + " help"));
            return;
        }

        if (player != null) {
            if (prevention != null) {
                if (prevention.can(player)) {
                    if (sender == player)
                        sender.sendMessage(translate("youtranslateableToPass"));
                    else
                        sender.sendMessage(translate("othertranslateableToPass"));
                } else if (sender == player)
                    sender.sendMessage(translate("youtranslateunableToPass"));
                else
                    sender.sendMessage(translate("othertranslateunableToPass"));
            } else
                sender.sendMessage(translate("preventionNotFound"));
        } else
            sender.sendMessage(translate("playerNotFound"));
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

                sender.sendMessage(translate("messageSet"));
            } else
                sender.sendMessage(translate("preventionNotFound"));
        } else
            sender.sendMessage(translate("tooFewArguments"));
    }
}
