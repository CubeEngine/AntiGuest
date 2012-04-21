package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This commands checks whether a player can pass a prevention
 *
 * @author Phillip Schichtel
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
            sender.sendMessage(_("tooFewArguments"));
            sender.sendMessage(_("see", getBase().getLabel() + " help"));
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
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [player] <prevention>";
    }
}
