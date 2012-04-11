package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import static de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit._;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * This command lists all registered or all active preventions
 *
 * @author Phillip Schichtel
 */
public class ListCommand extends AbstractCommand
{

    public ListCommand(BaseCommand base)
    {
        super("list", base);
    }


    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0 && (args[0].equalsIgnoreCase("all") ||args[0].equals("*")))
        {
            sender.sendMessage(_("registeredPreventions"));
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                sender.sendMessage(" - " + (prevention.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + prevention.getName());
            }
            sender.sendMessage("");
        }
        else
        {
            sender.sendMessage(_("activePrevention"));
            sender.sendMessage("");
            int i = 0;
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                if (prevention.isEnabled())
                {
                    ++i;
                    sender.sendMessage(" - " + ChatColor.GREEN + prevention.getName());
                }
            }
            sender.sendMessage("");
        }

        return true;
    }
    
    @Override
    public String getUsage()
    {
        return super.getUsage() + " [all|*]";
    }
}
