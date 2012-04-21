package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
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
        AntiGuest.debugMode = !AntiGuest.debugMode;
        if (AntiGuest.debugMode)
        {
            sender.sendMessage(_("debugEnabled"));
        }
        else
        {
            sender.sendMessage(_("debugDisabled"));
        }

        return true;
    }
}
