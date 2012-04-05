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
                    sender.sendMessage(t("enabled"));
                }
                else
                {
                    sender.sendMessage(t("disabled"));
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
        return super.getUsage() + " <prevention>";
    }
}
