package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import static de.codeinfection.quickwango.Translation.Translator.t;
import org.bukkit.command.CommandSender;

/**
 * This command prints a help message
 *
 * @author Phillip Schichtel
 */
public class HelpCommand extends AbstractCommand
{

    public HelpCommand(BaseCommand base)
    {
        super("help", base);
    }


    public boolean execute(CommandSender sender, String[] args)
    {
        sender.sendMessage(t("listOfCommands"));
        sender.sendMessage("");

        for (AbstractCommand command : getBase().getRegisteredCommands())
        {
            sender.sendMessage(command.getUsage());
            sender.sendMessage("    " + command.getDescription());
            sender.sendMessage("");
        }

        return true;
    }
}
