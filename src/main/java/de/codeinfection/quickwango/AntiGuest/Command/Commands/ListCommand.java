package de.codeinfection.quickwango.AntiGuest.Command.Commands;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Command.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.Command.BaseCommand;
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
        for (String prevName : AntiGuest.preventions.keySet())
        {
            sender.sendMessage(" - " + prevName);
        }

        return true;
    }

    @Override
    public String getDescription()
    {
        return "Lists all active preventions.";
    }
}
