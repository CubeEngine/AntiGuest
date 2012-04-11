package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import static de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit._;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.command.CommandSender;

/**
 * This commands checks whether a prevention is enabled
 *
 * @author Phillip Schichtel
 */
public class DisableallCommand extends AbstractCommand
{
    public DisableallCommand(BaseCommand base)
    {
        super("disableall", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        PreventionManager pm = PreventionManager.getInstance();

        for (Prevention prevention : pm.getPreventions())
        {
            pm.disablePrevention(prevention);
            prevention.getConfig().set("enable", false);
            prevention.saveConfig();
        }

        sender.sendMessage(_("preventionsDisabled"));

        return true;
    }
}
