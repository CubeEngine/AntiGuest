package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
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
