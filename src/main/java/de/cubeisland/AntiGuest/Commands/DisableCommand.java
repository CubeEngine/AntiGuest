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
public class DisableCommand extends AbstractCommand
{
    public DisableCommand(BaseCommand base)
    {
        super("disable", base);
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
                    PreventionManager.getInstance().disablePrevention(prevention);
                    sender.sendMessage(_("preventionDisabled"));
                    if (args.length <= 1 || !("-t".equals(args[1])))
                    {
                        prevention.getConfig().set("enable", false);
                        prevention.saveConfig();
                    }
                }
                else
                {
                    sender.sendMessage(_("alreadyDisabled"));
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
