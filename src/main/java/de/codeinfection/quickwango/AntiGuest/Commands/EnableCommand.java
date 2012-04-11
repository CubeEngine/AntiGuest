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
public class EnableCommand extends AbstractCommand
{
    public EnableCommand(BaseCommand base)
    {
        super("enable", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args[0]);
            if (prevention != null)
            {
                if (!prevention.isEnabled())
                {
                    if (PreventionManager.getInstance().enablePrevention(prevention))
                    {
                        sender.sendMessage(_("preventionEnabled"));
                        if (args.length <= 1 || !("-t".equals(args[1])))
                        {
                            prevention.getConfig().set("enable", true);
                            prevention.saveConfig();
                        }
                    }
                    else
                    {
                        sender.sendMessage(_("somethingFailed"));
                    }
                }
                else
                {
                    sender.sendMessage(_("alreadyEnabled"));
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
        return super.getUsage() + " <prevention> [-t]";
    }
}
