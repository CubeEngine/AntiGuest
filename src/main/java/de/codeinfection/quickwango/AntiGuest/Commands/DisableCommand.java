package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import static de.codeinfection.quickwango.Translation.Translator.t;
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
                    sender.sendMessage(t("preventionDisabled"));
                    if (args.length <= 1 || !("-t".equals(args[1])))
                    {
                        prevention.getConfig().set("enable", false);
                        prevention.saveConfig();
                    }
                }
                else
                {
                    sender.sendMessage(t("alreadyDisabled"));
                }
            }
            else
            {
                sender.sendMessage(t("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(t("noPrevention"));
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " <prevention> [-t]";
    }
}
