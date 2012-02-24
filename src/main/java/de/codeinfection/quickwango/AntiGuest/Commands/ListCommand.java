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
        sender.sendMessage("The following preventions are active:");
        sender.sendMessage("");
        for (Prevention prevention : PreventionManager.getInstance().getPreventions())
        {
            sender.sendMessage(" - " + prevention.getName());
            sender.sendMessage("    Active: " + (prevention.isInitialized() ? "Yes" : "No"));
        }

        return true;
    }

    @Override
    public String getDescription()
    {
        return "Lists all active preventions.";
    }
}
