package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.RequiresPermission;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
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
 * Prevents the user from swearing
 *
 * @author Phillip Schichtel
 */
public class SwearPrevention extends Prevention
{
    private static final String REGEX_PREFIX = "regex:";
    private Set<BadWord> swearPatterns;

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
                "get compiled on startup, an extreme list may lag the chat for guests.\n" +
                "The words may contain usual filesystem patterns.\n" +
                "Words prefixed with 'regex:' are interpreted as a Java regular expression\n";
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
        this.swearPatterns = new THashSet<BadWord>();
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

    private BadWord compile(String string)
    {
        System.out.println("String: " + string);
        if (string.startsWith(REGEX_PREFIX))
        {
            System.out.println("Will be compiled to a regex...");
            return new RegexBadWord(Pattern.compile(string.substring(REGEX_PREFIX.length())));
        }
        else
        {
            System.out.println("Will be parsed...");
            char current;
            boolean ignoreNext = false;
            boolean inGroup = false;
            boolean isPattern = false;
            StringBuilder pattern = new StringBuilder();
            StringBuilder plain = null;
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
                        pattern.append(".*?");
                    }
                    else if (current == '?')
                    {
                        pattern.append(".?");
                    }
                    else if (current == '{')
                    {
                        inGroup = true;
                        pattern.append('(');
                    }
                    else if (inGroup && current == ',')
                    {
                        pattern.append('|');
                    }
                    else if (inGroup && current == '}')
                    {
                        inGroup = false;
                        pattern.append(')');
                    }
                    else
                    {
                        ignoreNext = true;
                        break ignore;
                    }
                    
                    isPattern = true;
                    pattern.append(Pattern.quote(plain.toString()));
                    plain = null;
                }
                else
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
                pattern.append(plain);
            }
            
            System.out.println("Final pattern: " + pattern.toString());
            
            if (isPattern)
            {
                System.out.println("Will be compiled to a regex...");
                return new RegexBadWord(Pattern.compile("\\b" + pattern.append("\\b").toString(), Pattern.CASE_INSENSITIVE));
            }
            
            System.out.println("This seems to be a static word");
            return new PlainBadWord(pattern.toString());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            final String message = event.getMessage();
            synchronized(this)
            {
                for (BadWord badword : this.swearPatterns)
                {
                    if (badword.test(message))
                    {
                        sendMessage(player);
                        punish(player);
                        event.setCancelled(true);
                        return;
                    }
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

                synchronized(this)
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
    
    private interface BadWord
    {
        public boolean test(String string);
    }
    
    private class PlainBadWord implements BadWord
    {
        private final String word;
        
        public PlainBadWord(String word)
        {
            this.word = word;
        }
        
        public boolean test(String string)
        {
            return string.contains(word);
        }
    }
    
    public class RegexBadWord implements BadWord
    {
        private final Pattern regex;
        
        public RegexBadWord(Pattern regex)
        {
            this.regex = regex;
        }

        public boolean test(String string)
        {
            return this.regex.matcher(string).find();
        }
    }
}
