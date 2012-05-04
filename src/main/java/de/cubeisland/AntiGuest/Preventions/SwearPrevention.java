package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author CodeInfection
 */
public class SwearPrevention extends Prevention
{
    private List<Pattern> swearPatterns;

    public SwearPrevention(PreventionPlugin plugin)
    {
        super("swear", plugin);
        this.setEnableByDefault(true);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("words", new String[] {
            "hitler",
            "nazi",
            "asshole",
            "shit",
            "fuck"
        });

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        this.swearPatterns = new ArrayList<Pattern>();
        for (String word : getConfig().getStringList("words"))
        {
            this.swearPatterns.add(compile(word));
        }

        ((AntiGuest)getPlugin()).getBaseCommand().registerCommands(this);
    }

    @Override
    public void disable()
    {
        super.disable();
        this.swearPatterns.clear();
        this.swearPatterns = null;
        ((AntiGuest)getPlugin()).getBaseCommand().unregisterCommands(this);
    }

    private static Pattern compile(String string)
    {
        return Pattern.compile("\\b" + Pattern.quote(string) + "\\b", Pattern.CASE_INSENSITIVE);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handle(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            final String message = event.getMessage();
            for (Pattern regex : this.swearPatterns)
            {
                if (regex.matcher(message).find())
                {
                    sendMessage(player);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @Command(desc = "Adds a badword to the swear prevention")
    public void badword(CommandSender sender, CommandArgs args)
    {
        String word = args.getString(0);
        if (word != null)
        {
            Configuration config = getConfig();
            List<String> words = new ArrayList<String>(config.getStringList("words"));
            words.add(word);
            config.set("words", words);
            saveConfig();
            disable();
            enable();

            sender.sendMessage(_("wordAdded"));
        }
        else
        {
            sender.sendMessage(_("noWord"));
        }
    }
}
