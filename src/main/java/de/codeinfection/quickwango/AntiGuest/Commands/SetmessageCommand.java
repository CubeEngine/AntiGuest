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
public class SetmessageCommand extends AbstractCommand
{
    public SetmessageCommand(BaseCommand base)
    {
        super("setmessage", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 1)
        {
            Prevention prevention = PreventionManager.getInstance().getPrevention(args[0]);
            if (prevention != null)
            {
                StringBuilder messageBuilder = new StringBuilder(args[1]);
                for (int i = 2; i < args.length; ++i)
                {
                    messageBuilder.append(' ').append(args[i]);
                }
                String message = messageBuilder.toString();
                prevention.setMessage(message);
                prevention.getConfig().set("message", message);
                prevention.saveConfig();

                sender.sendMessage(_("messageSet"));
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            sender.sendMessage(_("tooFewArguments"));
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " <prevention> <message>";
    }
}
