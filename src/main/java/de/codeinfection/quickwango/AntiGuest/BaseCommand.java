package de.codeinfection.quickwango.AntiGuest;

import java.util.Collection;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public class BaseCommand implements CommandExecutor
{
    public final String permissinBase;
    private final Plugin plugin;
    private final HashMap<String, AbstractCommand> subCommands;
    private final Permission parentPermission;

    private String defaultCommand;
    private String label;

    public BaseCommand(Plugin plugin)
    {
        this.plugin = plugin;
        this.permissinBase = this.plugin.getDescription().getName().toLowerCase() + ".commands.";
        this.defaultCommand = null;
        this.subCommands = new HashMap<String, AbstractCommand>();
        this.parentPermission = new Permission(permissinBase, PermissionDefault.OP);
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
    {
        this.label = label;
        if (args.length > 0)
        {
            AbstractCommand cmd = this.subCommands.get(args[0]);
            if (cmd != null)
            {
                return executeSub(sender, cmd, args);
            }
        }

        if (this.defaultCommand != null)
        {
            return executeSub(sender, this.subCommands.get(this.defaultCommand), args);
        }
        
        sender.sendMessage("Available commands:");
        for (String commandLabel : this.subCommands.keySet())
        {
            sender.sendMessage(" - " + commandLabel);
        }
        
        return true;
    }

    private boolean executeSub(CommandSender sender, AbstractCommand command, String[] args)
    {
        if (!sender.hasPermission(command.getPermission()))
        {
            sender.sendMessage(ChatColor.RED + "Permisssion denied!");
            return true;
        }

        int argc = (args.length < 1 ? 0 : args.length - 1);
        String[] subArgs = new String[argc];
        if (args.length > 1)
        {
            System.arraycopy(args, 1, subArgs, 0, argc);
        }
        return command.execute(sender, subArgs);
    }

    public BaseCommand registerSubCommand(AbstractCommand command)
    {
        this.subCommands.put(command.getLabel(), command);
        command.getPermission().addParent(this.parentPermission, true);
        return this;
    }

    public BaseCommand unregisterSubCommand(String name)
    {
        this.subCommands.remove(name);
        if (name.equals(this.defaultCommand))
        {
            this.defaultCommand = null;
        }
        return this;
    }

    public BaseCommand unregisterAllSubCommands()
    {
        this.subCommands.clear();
        return this;
    }

    public BaseCommand setDefaultCommand(String name)
    {
        if (this.subCommands.containsKey(name))
        {
            this.defaultCommand = name;
        }
        return this;
    }

    public Collection<AbstractCommand> getRegisteredCommands()
    {
        return this.subCommands.values();
    }

    public String getLabel()
    {
        return this.label;
    }

    public Plugin getPlugin()
    {
        return this.plugin;
    }
}
