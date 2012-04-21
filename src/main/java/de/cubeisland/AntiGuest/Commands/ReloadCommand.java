package de.cubeisland.AntiGuest.Commands;

import de.cubeisland.AntiGuest.AbstractCommand;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.BaseCommand;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * This command reloads the plugin
 *
 * @author Phillip Schichtel
 */
public class ReloadCommand extends AbstractCommand
{
    public ReloadCommand(BaseCommand base)
    {
        super("reload", base);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            final PreventionManager mgr = PreventionManager.getInstance();
            final Prevention prevention = mgr.getPrevention(args[0]);
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
            final Plugin plugin = getBase().getPlugin();
            final PluginManager pm = plugin.getServer().getPluginManager();
            pm.disablePlugin(plugin);
            pm.enablePlugin(plugin);

            sender.sendMessage(_("reloaded", getBase().getPlugin().getName()));
        }

        return true;
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [prevention]";
    }
}
