package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
import de.cubeisland.libMinecraft.command.Command;
import de.cubeisland.libMinecraft.command.CommandArgs;
import de.cubeisland.libMinecraft.command.CommandPermission;
import de.cubeisland.libMinecraft.translation.Translation;
import gnu.trove.set.hash.THashSet;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
 */
public class BasicCommands
{
    private final AntiGuest plugin;
    private final Set<CommandSender> resetRequest;

    public BasicCommands(AntiGuest plugin)
    {
        this.plugin = plugin;
        this.resetRequest = new THashSet<CommandSender>();
    }

    @Command(desc = "Reloads a single or all preventions", usage = "[prevention]")
    @CommandPermission
    public void reload(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            final PreventionManager mgr = PreventionManager.getInstance();
            final Prevention prevention = mgr.getPrevention(args.getString(0));
            if (prevention != null)
            {
                mgr.disablePrevention(prevention);
                prevention.reloadConfig();
                mgr.enablePrevention(prevention);
                sender.sendMessage(_("preventionReloaded", prevention.getName()));
            }
            else
            {
                sender.sendMessage(_("preventionNotFound"));
            }
        }
        else
        {
            final PluginManager pm = plugin.getServer().getPluginManager();
            pm.disablePlugin(plugin);
            pm.enablePlugin(plugin);

            sender.sendMessage(_("reloaded", plugin.getName()));
        }
    }

    @Command(desc = "Resets all prevention configurations")
    @CommandPermission
    public void reset(CommandSender sender, CommandArgs args)
    {
        if (resetRequest.contains(sender))
        {
            resetRequest.remove(sender);

            PreventionManager mgr = PreventionManager.getInstance();

            for (Prevention prevention : mgr.getPreventions())
            {
                prevention.resetConfig();
            }

            sender.sendMessage(_("configsResetted"));
        }
        else
        {
            resetRequest.add(sender);
            sender.sendMessage(_("resetRequested"));
            if (sender instanceof Player)
            {
                Player player = (Player)sender;
                this.broadcastResetNotice(_("playerRequestedReset", player.getName()), player, args);
            }
            else
            {
                this.broadcastResetNotice(_("consoleRequestedReset"), null, args);
            }
        }
    }

    private void broadcastResetNotice(String message, Player sender, CommandArgs args)
    {
        if (sender != null)
        {
            AntiGuest.log(message);
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers())
        {
            if (player != sender && player.hasPermission(args.getSubCommand().getPermission()))
            {
                player.sendMessage(message);
            }
        }
    }

    @Command(desc = "Sets or gets the language", usage = "[language]")
    @CommandPermission
    public void language(CommandSender sender, CommandArgs args)
    {
        if (args.size() > 0)
        {
            String language = args.getString(0);
            Translation tranlation = Translation.get(AntiGuest.class, language);
            if (tranlation != null)
            {
                plugin.setTranslation(tranlation);
                plugin.getConfig().set("language", language);
                plugin.saveConfig();
                sender.sendMessage(_("languageLoaded", tranlation.getLanguage()));
            }
            else
            {
                sender.sendMessage(_("languageFailed", language));
            }
        }
        else
        {
            sender.sendMessage(_("currentLanguage", plugin.getTranslation().getLanguage()));
        }
    }
}
