package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import de.cubeisland.libMinecraft.StringUtils;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author CodeInfection
 */
public class SwearPrevention extends Prevention
{
    private Set<Pattern> swearPatterns;

    public SwearPrevention(PreventionPlugin plugin)
    {
        super("swear", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() + "\n" +
                "Every message a guest sends will be checked for the words listed under words.\n" +
                "More words will result in more time to check the message. Even though the words\n" +
                "get compiled on startup, an extreme list may lag the chat for guests.\n";
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
        this.swearPatterns = Collections.synchronizedSet(new THashSet<Pattern>());
        for (String word : getConfig().getStringList("words"))
        {
            if (word.startsWith("regex:"))
            {
                this.swearPatterns.add(Pattern.compile(word.substring(6)));
            }
            else
            {
                this.swearPatterns.add(compile(word));
            }
        }

        getPlugin().getBaseCommand().registerCommands(this);
    }

    @Override
    public void disable()
    {
        super.disable();
        this.swearPatterns.clear();
        this.swearPatterns = null;
        getPlugin().getBaseCommand().unregisterCommands(this);
    }

    private static Pattern compile(String string)
    {
        String[] parts = StringUtils.explode("*", string, false);
        string = "\\b" + Pattern.quote(parts[0]);
        for (int i = 1; i < parts.length; ++i)
        {
            string += ".*?" + Pattern.quote(parts[i]);
        }
        string += "\\b";
        
        return Pattern.compile(string, Pattern.CASE_INSENSITIVE);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event)
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
                    punish(player);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @Command
    @RequiresPermission
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
            
            this.swearPatterns.add(compile(word));

            sender.sendMessage(getPlugin().getTranslation().translate("wordAdded"));
        }
        else
        {
            sender.sendMessage(getPlugin().getTranslation().translate("noWord"));
        }
    }
}
