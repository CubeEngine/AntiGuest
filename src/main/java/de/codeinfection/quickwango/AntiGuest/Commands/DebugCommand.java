package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import static de.codeinfection.quickwango.Translation.Translator.t;
import org.bukkit.command.CommandSender;

/**
 * This command toggles the debug mode
 *
 * @author Phillip Schichtel
 */
public class DebugCommand extends AbstractCommand
{
    public DebugCommand(BaseCommand base)
    {
        super("debug", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        AntiGuestBukkit.debugMode = !AntiGuestBukkit.debugMode;
        if (AntiGuestBukkit.debugMode)
        {
            sender.sendMessage(t("debugEnabled"));
        }
        else
        {
            sender.sendMessage(t("debugDisabled"));
        }

        return true;
    }
}
