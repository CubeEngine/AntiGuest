package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.quickwango.AntiGuest.Command.BaseCommand;
import de.codeinfection.quickwango.AntiGuest.Command.Commands.CanCommand;
import de.codeinfection.quickwango.AntiGuest.Command.Commands.DebugCommand;
import de.codeinfection.quickwango.AntiGuest.Command.Commands.HelpCommand;
import de.codeinfection.quickwango.AntiGuest.Command.Commands.ListCommand;
import de.codeinfection.quickwango.AntiGuest.Listeners.AntiGuestBlockListener;
import de.codeinfection.quickwango.AntiGuest.Listeners.AntiGuestInteractionListener;
import de.codeinfection.quickwango.AntiGuest.Listeners.AntiGuestMovementListener;
import de.codeinfection.quickwango.AntiGuest.Listeners.AntiGuestPlayerListener;
import de.codeinfection.quickwango.AntiGuest.Listeners.AntiGuestVehicleListener;
import de.codeinfection.quickwango.AntiGuest.Preventions.ActionPrevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ItemPrevention;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin
{
    private static AntiGuest instance = null;
    public static final Map<String, Prevention> preventions = new HashMap<String, Prevention>();

    private Logger logger = null;
    public static boolean debugMode = false;
    
    private PluginManager pm;
    private Configuration config;
    private File dataFolder;

    public AntiGuest()
    {
        instance = this;
    }

    public static AntiGuest getInstance()
    {
        return instance;
    }

    public void onEnable()
    {
        this.logger = this.getLogger();
        this.pm = this.getServer().getPluginManager();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();

        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();

        this.loadConfig();

        BaseCommand baseCommand = new BaseCommand();
        baseCommand.registerSubCommand(new ListCommand(baseCommand))
                   .registerSubCommand(new CanCommand(baseCommand))
                   .registerSubCommand(new DebugCommand(baseCommand))
                   .registerSubCommand(new HelpCommand(baseCommand))
                   .setDefaultCommand("help");
        
        this.getCommand("antiguest").setExecutor(baseCommand);

        try
        {
            this.pm.registerEvents(new AntiGuestBlockListener(), this);
            debug("block listener registered!");
            this.pm.registerEvents(new AntiGuestPlayerListener(), this);
            debug("player listener registered!");
            this.pm.registerEvents(new AntiGuestVehicleListener(), this);
            debug("vehicle listener registered!");
            this.pm.registerEvents(new AntiGuestMovementListener(), this);
            debug("movement listener registered!");
            this.pm.registerEvents(new AntiGuestInteractionListener(), this);
            debug("interaction listener registered!");
        }
        catch (Throwable t)
        {
            error("Caught an exception while registering events:");
            error(t.getLocalizedMessage(), t);
            return;
        }

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    public void onDisable()
    {
        log(this.getDescription().getVersion() + " disabled");
    }

    private void loadConfig()
    {
        debugMode = this.config.getBoolean("debug");

        this.loadActionPreventions(this.config.getConfigurationSection("preventions.actions"));
        this.loadItemPrevention(this.config.getConfigurationSection("preventions.items"));
    }

    private void loadActionPreventions(ConfigurationSection config)
    {
        if (config != null)
        {
            ActionPrevention prevention;
            String message;
            Integer messageDelay;
            for (String key : config.getKeys(false))
            {
                ConfigurationSection section = config.getConfigurationSection(key);

                if (section.getBoolean("enable", false))
                {
                    messageDelay = section.getInt("messageDelay", 0) * 1000;
                    message = parseChatColors(section.getString("message", ""));
                    prevention = new ActionPrevention(key, message, messageDelay, section);
                    preventions.put(prevention.getName(), prevention);
                    debug("Loaded prevention: " + prevention.getName());
                    try
                    {
                        this.pm.addPermission(new Permission(prevention.getPermission(), PermissionDefault.OP));
                    }
                    catch (IllegalArgumentException e)
                    {}
                }
            }
        }
    }

    private void loadItemPrevention(ConfigurationSection config)
    {
        if (config != null)
        {
            if (config.getBoolean("enable"))
            {
                String message = parseChatColors(config.getString("message"));
                List<String> items = config.getStringList("items");
                List<Material> types = new ArrayList<Material>(items.size());
                if (items != null)
                {
                    Material itemType;
                    for (String itemName : items)
                    {
                        itemType = Material.matchMaterial(itemName);
                        if (itemType != null)
                        {
                            types.add(itemType);
                            debug("Added " + itemType.toString() + " to the prevented items");
                            try
                            {
                                this.pm.addPermission(new Permission(ItemPrevention.PERMISSION_BASE + String.valueOf(itemType.getId()), PermissionDefault.OP));
                            }
                            catch (IllegalArgumentException e)
                            {}
                        }
                    }

                    ItemPrevention prevention = new ItemPrevention(message, types);
                    preventions.put(prevention.getName(), prevention);
                }
            }
        }
    }

    private static String parseChatColors(String string)
    {
        if (string != null)
        {
            string = string.replaceAll("&([a-f0-9])", "\u00A7$1");
        }
        return string;
    }

    public void log(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    public void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable t)
    {
        logger.log(Level.SEVERE, msg, t);
    }

    public void debug(String msg)
    {
        if (debugMode)
        {
            this.log("[debug] " + msg);
        }
    }
}
