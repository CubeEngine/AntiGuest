package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.libMinecraft.Translation;
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
            Translation tranlation = Translation.get(AntiGuest.class, args[0]);
            if (tranlation != null)
            {
                AntiGuest.setTranslation(tranlation);
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
            sender.sendMessage(_("currentLanguage", AntiGuest.getTranslation().getLanguage()));
        }

        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [language]";
    }
}
