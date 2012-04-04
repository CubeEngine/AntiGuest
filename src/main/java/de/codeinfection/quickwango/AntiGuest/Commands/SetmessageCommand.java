package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.ChatColor;
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

                sender.sendMessage(ChatColor.GREEN + "The message was successfully set!");
            }
            else
            {
                sender.sendMessage("Prevention not found!");
            }
        }
        else
        {
            sender.sendMessage("Too few arguments!");
        }
        
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " <prevention> <message>";
    }

    @Override
    public String getDescription()
    {
        return "Enables the given prevention.";
    }
}
