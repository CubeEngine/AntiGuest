package de.codeinfection.quickwango.AntiGuest.Commands;

import de.codeinfection.quickwango.AntiGuest.AbstractCommand;
import de.codeinfection.quickwango.AntiGuest.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
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
        return "Reloads the configuration of the plugin.";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        final Plugin plugin = getBase().getPlugin();
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.disablePlugin(plugin);
        pm.enablePlugin(plugin);

        sender.sendMessage(ChatColor.GREEN + plugin.getDescription().getName() + " successfully reloaded!");

        return true;
    }

}
