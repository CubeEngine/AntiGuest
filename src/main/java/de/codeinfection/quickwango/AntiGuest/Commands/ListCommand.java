package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.command.CommandSender;

/**
 *
 * @author CodeInfection
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
            sender.sendMessage("The following preventions are registered:");
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                sender.sendMessage(" - " + prevention.getName());
            }
        }
        else
        {
            sender.sendMessage("The following preventions are active:");
            sender.sendMessage("");
            for (Prevention prevention : PreventionManager.getInstance().getPreventions())
            {
                if (prevention.isEnabled())
                {
                    sender.sendMessage(" - " + prevention.getName());
                }
            }
        }

        return true;
    }

    @Override
    public String getDescription()
    {
        return "Lists all active or registered preventions.";
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [all|*]";
    }
}
