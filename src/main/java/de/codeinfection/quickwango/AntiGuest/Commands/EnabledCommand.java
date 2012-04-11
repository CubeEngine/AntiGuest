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
                    sender.sendMessage(_("enabled"));
                }
                else
                {
                    sender.sendMessage(_("disabled"));
                }
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("noPrevention"));
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " <prevention>";
    }
}
