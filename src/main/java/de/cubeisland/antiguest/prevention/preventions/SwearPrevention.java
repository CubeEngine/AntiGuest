package de.cubeisland.antiguest.prevention.preventions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;
import gnu.trove.set.hash.THashSet;

/**
 * Prevents the user from swearing
 *
 * @author Phillip Schichtel
 */
public class SwearPrevention extends Prevention
{
    private static final String REGEX_PREFIX = "regex:";
    private Set<Pattern> swearPatterns;
    private boolean checkCommands;

    public SwearPrevention(PreventionPlugin plugin)
    {
        super("swear", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public String getConfigHeader()
    {
        return super
            .getConfigHeader() + "\n" + "Every message, signtext or (optionally) command a guest sends will be checked for the words listed under words.\n" + "More words will result in more time to check the message. Even though the words\n" + "get compiled on startup, an extreme list may lag the chat for guests. Non-guests are unaffected!\n" + "The words may contain usual filesystem patterns.\n" + "Words prefixed with 'regex:' are interpreted as a Java regular expression\n" + "\nFilesystem patterns:\n" + " * -> any number (including none) of any character\n" + " ? -> one or none of any character\n" + " { , , } -> a group of strings of which one must match\n" + " \\ -> escape character to write the above characters as a normal character";
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("check-commands", false);
        config.set("words", new String[]{
            "hitler", "nazi", "asshole", "shit", "fuck"
        });

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        this.checkCommands = getConfig().getBoolean("check-commands");
        this.swearPatterns = new THashSet<Pattern>();
        for (String word : getConfig().getStringList("words"))
        {
            this.swearPatterns.add(this.compile(word));
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

    private Pattern compile(String string)
    {
        if (string.startsWith(REGEX_PREFIX))
        {
            return Pattern.compile(string.substring(REGEX_PREFIX.length()));
        }
        else
        {
            char current;
            boolean ignoreNext = false;
            boolean inGroup = false;
            StringBuilder pattern = new StringBuilder();
            StringBuilder plain = null;
            String replacement = null;
            for (int i = 0; i < string.length(); ++i)
            {
                current = string.charAt(i);
                ignore:
                if (!ignoreNext)
                {
                    if (current == '\\')
                    {
                        ignoreNext = true;
                    }
                    else if (current == '*')
                    {
                        replacement = ".*?";
                    }
                    else if (current == '?')
                    {
                        replacement = ".?";
                    }
                    else if (current == '{')
                    {
                        if (string.indexOf('}') <= i)
                        {
                            ignoreNext = true;
                            break ignore;
                        }
                        inGroup = true;
                        replacement = "(";
                    }
                    else if (inGroup && current == ',')
                    {
                        replacement = "|";
                    }
                    else if (inGroup && current == '}')
                    {
                        inGroup = false;
                        replacement = ")";
                    }
                    else
                    {
                        ignoreNext = true;
                        break ignore;
                    }

                    pattern.append(Pattern.quote(plain.toString()));
                    plain = null;

                    if (replacement != null)
                    {
                        pattern.append(replacement);
                        replacement = null;
                    }

                    continue;
                }
                if (ignoreNext)
                {
                    ignoreNext = false;
                }
                if (plain == null)
                {
                    plain = new StringBuilder();
                }
                plain.append(current);
            }

            if (plain != null)
            {
                pattern.append(Pattern.quote(plain.toString()));
            }

            return Pattern.compile("\\b" + pattern.append("\\b").toString(), Pattern.CASE_INSENSITIVE);
        }
    }

    private synchronized boolean containsBadword(String string)
    {
        for (Pattern badword : this.swearPatterns)
        {
            if (badword.matcher(string).find())
            {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            final String message = event.getMessage();
            if (containsBadword(message))
            {
                prevent(event, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void commandPreProcess(PlayerCommandPreprocessEvent event)
    {
        if (!this.checkCommands)
        {
            return;
        }
        final Player player = event.getPlayer();
        if (!can(player))
        {
            if (containsBadword(event.getMessage()))
            {
                prevent(event, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void signChange(SignChangeEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            for (String line : event.getLines())
            {
                if (containsBadword(line))
                {
                    prevent(event, event.getPlayer());
                    break;
                }
            }
        }
    }

    @Command
    @RequiresPermission
    public void badword(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            String word = args.getString(0);
            if (word != null)
            {
                Configuration config = getConfig();
                List<String> words = new ArrayList<String>(config.getStringList("words"));
                words.add(word);
                config.set("words", words);
                saveConfig();

                synchronized (this)
                {
                    this.swearPatterns.add(this.compile(word));
                }

                sender.sendMessage(getPlugin().getTranslation().translate("wordAdded"));
            }
            else
            {
                sender.sendMessage(getPlugin().getTranslation().translate("noWord"));
            }
        }
    }
}
