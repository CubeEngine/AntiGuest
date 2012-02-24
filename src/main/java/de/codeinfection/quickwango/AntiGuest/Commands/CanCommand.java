package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class CanCommand extends AbstractCommand
{
    public CanCommand(BaseCommand base)
    {
        super("can", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        Player player;
        Prevention prevention;
        if (args.length == 1 && (sender instanceof Player))
        {
            player = (Player)sender;
            prevention = PreventionManager.getInstance().getPrevention(args[0]);
        }
        else if (args.length > 1)
        {
            player = Bukkit.getPlayer(args[0]);
            prevention = PreventionManager.getInstance().getPrevention(args[1]);
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_RED + "Too few arguments given!");
            sender.sendMessage(ChatColor.DARK_RED + "See /" + getBase().getLabel() + " help");
            return true;
        }

        if (player != null)
        {
            if (prevention != null)
            {
                if (prevention.can(player))
                {
                    if (sender == player)
                    {
                        sender.sendMessage(ChatColor.GREEN + "You are able to pass this prevention!");
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.GREEN + "The player is able to pass this prevention!");
                    }
                }
                else
                {
                    if (sender == player)
                    {
                        sender.sendMessage(ChatColor.RED + "You unable able to pass this prevention!");
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "The player is unable to pass this prevention!");
                    }
                }
            }
            else
            {
                sender.sendMessage(ChatColor.DARK_RED + "Prevention not found!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_RED + "Player not found!");
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return "/" + getBase().getLabel() + " " + this.getLabel() + " [player] <prevention>";
    }

    @Override
    public String getDescription()
    {
        return "Checks whether a player can pass a specific prevention.";
    }
}
