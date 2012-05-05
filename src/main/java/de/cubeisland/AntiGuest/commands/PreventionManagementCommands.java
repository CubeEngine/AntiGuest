package de.cubeisland.AntiGuest.commands;

import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionManager;
import de.cubeisland.libMinecraft.ChatColor;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class PreventionManagementCommands
{
    private final PreventionManager pm;

    public PreventionManagementCommands()
    {
        this.pm = PreventionManager.getInstance();
    }

    @Command(desc = "Disables all preventions", usage = "[-t]")
    @CommandPermission
    public void disableAll(CommandSender sender, CommandArgs args)
    {
        boolean temporary = args.hasFlag("t");
        for (Prevention prevention : pm.getPreventions())
        {
            pm.disablePrevention(prevention);
            if (!temporary)
            {
                prevention.getConfig().set("enable", false);
                prevention.saveConfig();
            }
        }

        sender.sendMessage(_("preventionsDisabled"));
    }

    @Command(desc = "Enables all preventions", usage = "[-t]")
    @CommandPermission
    public void enableAll(CommandSender sender, CommandArgs args)
    {
        boolean temporary = args.hasFlag("t");
        for (Prevention prevention : pm.getPreventions())
        {
            pm.enablePrevention(prevention);
            if (!temporary)
            {
                prevention.getConfig().set("enable", true);
                prevention.saveConfig();
            }
        }

        sender.sendMessage(_("preventionsEnabled"));
    }

    @Command(desc = "Enables a prevention", usage = "<prevention> [-t]")
    @CommandPermission
    public void enable(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            Prevention prevention = this.pm.getPrevention(args.getString(0));
            if (prevention != null)
            {
                if (!prevention.isEnabled())
                {
                    if (PreventionManager.getInstance().enablePrevention(prevention))
                    {
                        sender.sendMessage(_("preventionEnabled"));
                        if (!args.hasFlag("t"))
                        {
                            prevention.getConfig().set("enable", true);
                            prevention.saveConfig();
                        }
                    }
                    else
                    {
                        sender.sendMessage(_("somethingFailed"));
                    }
                }
                else
                {
                    sender.sendMessage(_("alreadyEnabled"));
                }
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("noPrevention"));
        }
    }

    @Command(desc = "Disables a prevention", usage = "<prevention> [-t]")
    @CommandPermission
    public void disable(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null)
            {
                if (prevention.isEnabled())
                {
                    PreventionManager.getInstance().disablePrevention(prevention);
                    sender.sendMessage(_("preventionDisabled"));
                    if (!args.hasFlag("t"))
                    {
                        prevention.getConfig().set("enable", false);
                        prevention.saveConfig();
                    }
                }
                else
                {
                    sender.sendMessage(_("alreadyDisabled"));
                }
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("noPrevention"));
        }
    }

    @Command(desc = "Checks whether the prevention is enabled", usage = "<prevention>")
    @CommandPermission
    public void enabled(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null)
            {
                if (prevention.isEnabled())
                {
                    sender.sendMessage(_("enabled"));
                }
                else
                {
                    sender.sendMessage(_("disabled"));
                }
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("noPrevention"));
        }
    }

    @Command(desc = "Lists the preventions", usage = "[-a]")
    @CommandPermission
    public void list(CommandSender sender, CommandArgs args)
    {
        if (args.hasFlag("a"))
        {
            sender.sendMessage(_("registeredPreventions"));
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                sender.sendMessage(" - " + (prevention.isEnabled() ? ChatColor.BRIGHT_GREEN : ChatColor.RED) + prevention.getName());
            }
            sender.sendMessage("");
        }
        else
        {
            sender.sendMessage(_("activePreventions"));
            sender.sendMessage("");
            int i = 0;
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                if (prevention.isEnabled())
                {
                    ++i;
                    sender.sendMessage(" - " + ChatColor.BRIGHT_GREEN + prevention.getName());
                }
            }
            sender.sendMessage("");
        }
    }

    @Command(desc = "Checks whether a player can pass the prevention", usage = "[player] <prevention>")
    @CommandPermission
    public void can(CommandSender sender, CommandArgs args)
    {
        Player player;
        Prevention prevention;
        if (args.size() > 1)
        {
            player = args.getBaseCommand().getPlugin().getServer().getPlayer(args.getString(0));
            prevention = this.pm.getPrevention(args.getString(1));
        }
        else if (args.size() > 0 && sender instanceof Player)
        {
            player = (Player)sender;
            prevention = this.pm.getPrevention(args.getString(0));
        }
        else
        {
            sender.sendMessage(_("tooFewArguments"));
            sender.sendMessage(_("see", args.getBaseLabel() + " help"));
            return;
        }

        if (player != null)
        {
            if (prevention != null)
            {
                if (prevention.can(player))
                {
                    if (sender == player)
                    {
                        sender.sendMessage(_("you_ableToPass"));
                    }
                    else
                    {
                        sender.sendMessage(_("other_ableToPass"));
                    }
                }
                else
                {
                    if (sender == player)
                    {
                        sender.sendMessage(_("you_unableToPass"));
                    }
                    else
                    {
                        sender.sendMessage(_("other_unableToPass"));
                    }
                }
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("playerNotFound"));
        }
    }

    @Command(desc = "Sets the message of a prevention", usage = "<prevention> <message>")
    @CommandPermission
    public void setMessage(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 1)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args.getString(0));
            if (prevention != null)
            {
                String message = args.getString(1);
                prevention.setMessage(message);
                prevention.getConfig().set("message", message);
                prevention.saveConfig();

                sender.sendMessage(_("messageSet"));
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("tooFewArguments"));
        }
    }
}
