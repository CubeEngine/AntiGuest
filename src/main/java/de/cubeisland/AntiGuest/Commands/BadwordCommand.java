package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

/**
 * This commands adds badwords to the swear prevention
 *
 * @author Phillip Schichtel
 */
public class BadwordCommand extends AbstractCommand
{
    public BadwordCommand(BaseCommand base)
    {
        super("badword", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        Prevention swearPrevention = PreventionManager.getInstance().getPrevention("swear");
        if (swearPrevention != null)
        {
            if (args.length > 0)
            {
                Configuration config = swearPrevention.getConfig();
                List<String> words = new ArrayList<String>(config.getStringList("words"));
                words.add(args[0]);
                config.set("words", words);
                swearPrevention.saveConfig();
                if (swearPrevention.isEnabled())
                {
                    swearPrevention.disable();
                    swearPrevention.enable();
                }

                sender.sendMessage(_("wordAdded"));
            }
            else
            {
                sender.sendMessage(_("noWord"));
            }
        }
        else
        {
            sender.sendMessage(_("swearPrevNotFound"));
        }
        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " <badword>";
    }
}
