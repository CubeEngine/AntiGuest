package de.cubeisland.AntiGuest.Commands;

import com.avaje.ebean.Transaction;
import de.codeinfection.Util.Translation;
import de.cubeisland.AntiGuest.AbstractCommand;
import de.cubeisland.AntiGuest.AntiGuestBukkit;
import static de.cubeisland.AntiGuest.AntiGuestBukkit._;
import de.cubeisland.AntiGuest.BaseCommand;
import org.bukkit.command.CommandSender;

/**
 * This command toggles the debug mode
 *
 * @author Phillip Schichtel
 */
public class LanguageCommand extends AbstractCommand
{
    public LanguageCommand(BaseCommand base)
    {
        super("language", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            Translation tranlation = Translation.get(AntiGuestBukkit.class, args[0]);
            if (tranlation != null)
            {
                AntiGuestBukkit.setTranslation(tranlation);
                getBase().getPlugin().getConfig().set("language", args[0]);
                getBase().getPlugin().saveConfig();
                sender.sendMessage(_("languageLoaded", tranlation.getLanguage()));
            }
            else
            {
                sender.sendMessage(_("languageFailed", args[0]));
            }
        }
        else
        {
            sender.sendMessage(_("currentLanguage", AntiGuestBukkit.getTranslation().getLanguage()));
        }

        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [language]";
    }
}
