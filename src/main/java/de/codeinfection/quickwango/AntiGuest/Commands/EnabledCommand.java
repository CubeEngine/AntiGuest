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
public class EnabledCommand extends AbstractCommand
{
    public EnabledCommand(BaseCommand base)
    {
        super("enabled", base);
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
                    sender.sendMessage(ChatColor.GREEN + "This prevention is enabled!");
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "This prevention is disabled!");
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
        return super.getUsage() + " <prevention>";
    }

    @Override
    public String getDescription()
    {
        return "Checks whether the given prevention is enabled.";
    }
}
