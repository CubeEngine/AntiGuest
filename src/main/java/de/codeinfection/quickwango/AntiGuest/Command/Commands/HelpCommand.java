package de.codeinfection.quickwango.AntiGuest.Command.Commands;

import de.codeinfection.quickwango.AntiGuest.Command.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.Command.BaseCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author CodeInfection
 */
public class HelpCommand extends AbstractCommand
{

    public HelpCommand(BaseCommand base)
    {
        super("help", base);
    }


    public boolean execute(CommandSender sender, String[] args)
    {
        sender.sendMessage("Here is a list of the available commands and their usage:");
        sender.sendMessage("");

        for (AbstractCommand command : getBase().getRegisteredCommands())
        {
            sender.sendMessage(command.getUsage());
            sender.sendMessage("    " + command.getDescription());
            sender.sendMessage("");
        }

        return true;
    }

    @Override
    public String getDescription()
    {
        return "Prints this help message.";
    }
}
