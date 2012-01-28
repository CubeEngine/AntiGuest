package de.codeinfection.quickwango.AntiGuest.Command;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.PermSolver;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class BaseCommand implements CommandExecutor
{
    private static final String PERMISSONS_BASE = "antiguest.commands.";

    private String defaultCommand;
    private HashMap<String, AbstractCommand> subCommands;
    private final PermSolver permSolver;

    private String label;

    public BaseCommand()
    {
        this.defaultCommand = null;
        this.permSolver = AntiGuest.getInstance().getPermSolver();
        this.subCommands = new HashMap<String, AbstractCommand>();
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
        if (sender instanceof Player)
        {
            Player player = (Player)sender;
            if (!this.permSolver.hasPermission(player, PERMISSONS_BASE + "*") &&
                !this.permSolver.hasPermission(player, PERMISSONS_BASE + command.getLabel()))
            {
                sender.sendMessage(ChatColor.RED + "Permisssion denied!");
                return true;
            }
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

    public PermSolver getPermSolver()
    {
        return this.permSolver;
    }

    public String getLabel()
    {
        return this.label;
    }
}
