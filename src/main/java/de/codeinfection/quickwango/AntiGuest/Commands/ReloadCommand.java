package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.PreventionManager;
import org.bukkit.ChatColor;
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
    public String getDescription()
    {
        return "Reloads the configuration of the plugin or a specific prevention.";
    }

    @Override
    public String getUsage()
    {
        return super.getUsage() + " [prevention]";
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
                sender.sendMessage(ChatColor.GREEN + "The prevention " + prevention.getName() + " was successfully reloaded!");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "The given preventions is not available!");
            }
        }
        else
        {
            final Plugin plugin = getBase().getPlugin();
            final PluginManager pm = plugin.getServer().getPluginManager();
            pm.disablePlugin(plugin);
            pm.enablePlugin(plugin);

            sender.sendMessage(ChatColor.GREEN + plugin.getDescription().getName() + " successfully reloaded!");
        }

        return true;
    }
}
