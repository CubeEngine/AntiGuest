package de.cubeisland.antiguest;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.cubeisland.antiguest.commands.BasicCommands;
import de.cubeisland.antiguest.commands.PreventionManagementCommands;
import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionConfiguration;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import de.cubeisland.antiguest.prevention.Punishment;
import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.Translation;
import org.reflections.Reflections;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

public class AntiGuest extends JavaPlugin implements Listener, PreventionPlugin
{
    private static Logger logger = null;

    private File preventionConfigFolder;
    private static Translation translation;
    private BaseCommand baseCommand;
    private boolean punishments;
    private boolean logViolations;

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        File dataFolder = this.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs())
        {
            logger.log(SEVERE, "Failed to create the data folder!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.preventionConfigFolder = new File(dataFolder, "preventions");

        reloadConfig();
        Configuration config = getConfig();
        config.addDefault("language", System.getProperty("user.language", "en"));
        config.options().copyDefaults(true);

        translation = Translation.get(this.getClass(), getConfig().getString("language"));
        if (translation == null)
        {
            translation = Translation.get(this.getClass(), "en");
            config.set("language", "en");
        }
        saveConfig();
        this.punishments = config.getBoolean("punishments");
        this.logViolations = config.getBoolean("log-violations");

        this.baseCommand = new BaseCommand(this, "antiguest.commands.");
        this.baseCommand.registerCommands(new BasicCommands(this)).registerCommands(new PreventionManagementCommands());

        getCommand("antiguest").setExecutor(this.baseCommand);

        PreventionManager manager = PreventionManager.getInstance();

        Reflections reflections = new Reflections(this.getClass().getPackage().getName());

        for (Class<? extends Punishment> punishmentClass : reflections.getSubTypesOf(Punishment.class))
        {
            try
            {
                manager.registerPunishment(punishmentClass.newInstance());
            }
            catch (InstantiationException e)
            {
                error("Failed to create the instance of the " + punishmentClass
                    .getSimpleName() + " (instantiation failed)!", e);
            }
            catch (IllegalAccessException e)
            {
                error("Failed to create the instance of the " + punishmentClass
                    .getSimpleName() + " (access failed)!", e);
            }
        }

        for (Class<? extends Prevention> preventionClass : reflections.getSubTypesOf(Prevention.class))
        {
            if (preventionClass.isInterface() || Modifier.isAbstract(preventionClass.getModifiers()))
            {
                continue;
            }
            try
            {
                Constructor<? extends Prevention> constructor = preventionClass.getConstructor(PreventionPlugin.class);
                constructor.setAccessible(true);
                manager.registerPrevention(constructor.newInstance(this));
            }
            catch (InstantiationException e)
            {
                error("Failed to create the instance of the " + preventionClass
                    .getSimpleName() + " (instantiation failed)!", e);
            }
            catch (IllegalAccessException e)
            {
                error("Failed to create the instance of the " + preventionClass
                    .getSimpleName() + " (access failed)!", e);
            }
            catch (NoSuchMethodException e)
            {
                error("Failed to create the instance of the " + preventionClass
                    .getSimpleName() + " (missing/invalid constructor)!", e);
            }
            catch (InvocationTargetException e)
            {
                error("Failed to create the instance of the " + preventionClass
                    .getSimpleName() + " (error in constructor)!", e);
            }
        }

        manager.enablePreventions();

        logger.info(PreventionManager.getInstance().getPreventions().size() + " Prevention(s) have been registered!");
        this.convertPreventionConfigs();
    }

    @Override
    public void onDisable()
    {
        translation = null;
        PreventionManager.getInstance().disablePreventions();
    }

    private void convertPreventionConfigs()
    {
        final String messageDelayKey = "messageDelay";
        PreventionConfiguration prevConfig;
        for (Prevention prevention : PreventionManager.getInstance().getPreventions())
        {
            prevConfig = prevention.getConfig();
            if (prevConfig.contains(messageDelayKey))
            {
                prevConfig.set("throttleDelay", prevConfig.get(messageDelayKey));
                prevConfig.set(messageDelayKey, null);
                prevention.saveConfig();
            }
        }
    }

    public File getConfigurationFolder()
    {
        return this.preventionConfigFolder;
    }

    public static void log(String msg)
    {
        logger.log(INFO, msg);
    }

    public static void error(String msg)
    {
        logger.log(SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        logger.log(SEVERE, msg, t);
    }

    public static String t(String message, Object... params)
    {
        return translation.translate(message, params);
    }

    public Translation getTranslation()
    {
        return translation;
    }

    public void setTranslation(Translation newTranslation)
    {
        translation = newTranslation;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event)
    {
        PreventionManager.getInstance().disablePreventions(event.getPlugin());
    }

    public BaseCommand getBaseCommand()
    {
        return this.baseCommand;
    }

    public String getPermissionBase()
    {
        return "antiguest.preventions.";
    }

    public boolean allowPunishments()
    {
        return this.punishments;
    }

    public boolean logViolations()
    {
        return this.logViolations;
    }
}
