package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * This commands checks whether a prevention is enabled
 *
 * @author Phillip Schichtel
 */
public class DisableCommand extends AbstractCommand
{
    public DisableCommand(BaseCommand base)
    {
        super("disable", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args[0]);
            if (prevention != null)
            {
                if (prevention.isEnabled())
                {
                    PreventionManager.getInstance().disablePrevention(args[0]);
                    sender.sendMessage(ChatColor.GREEN + "This prevention should now be disabled!");
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "This prevention is already disabled!");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "The given prevention is not registered!");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You didn't specify a prevention!");
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return "/" + getBase().getLabel() + " " + this.getLabel() + " <prevention>";
    }

    @Override
    public String getDescription()
    {
        return "Disables the given prevention.";
    }
}
