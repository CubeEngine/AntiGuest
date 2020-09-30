package de.cubeisland.antiguest;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

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
import org.reflections.Reflections;

import de.cubeisland.antiguest.commands.BasicCommands;
import de.cubeisland.antiguest.commands.PreventionManagementCommands;
import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionConfiguration;
import de.cubeisland.antiguest.prevention.PreventionManager;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import de.cubeisland.antiguest.prevention.Punishment;
import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.Translation;

public class AntiGuest extends JavaPlugin implements Listener, PreventionPlugin {
    private static Logger logger = null;

    private File preventionConfigFolder;
    private static Translation translation;
    private BaseCommand baseCommand;
    private boolean punishments;
    private boolean logViolations;

    @Override
    public void onEnable() {
        logger = getLogger();
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            logger.log(SEVERE, "Failed to create the data folder!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        preventionConfigFolder = new File(dataFolder, "preventions");

        reloadConfig();
        Configuration config = getConfig();
        config.addDefault("language", System.getProperty("user.language", "en"));
        config.options().copyDefaults(true);

        translation = Translation.get(this.getClass(), getConfig().getString("language"));
        if (translation == null) {
            translation = Translation.get(this.getClass(), "en");
            config.set("language", "en");
        }
        saveConfig();
        punishments = config.getBoolean("punishments");
        logViolations = config.getBoolean("log-violations");

        baseCommand = new BaseCommand(this, "antiguest.commands.");
        baseCommand.registerCommands(new BasicCommands(this)).registerCommands(new PreventionManagementCommands());

        getCommand("antiguest").setExecutor(baseCommand);

        PreventionManager manager = PreventionManager.getInstance();

        Reflections reflections = new Reflections(this.getClass().getPackage().getName());

        for (Class<? extends Punishment> punishmentClass : reflections.getSubTypesOf(Punishment.class))
            try {
                manager.registerPunishment(punishmentClass.newInstance());
            } catch (InstantiationException e) {
                error("Failed to create the instance of the " + punishmentClass.getSimpleName() + " (instantiation failed)!", e);
            } catch (IllegalAccessException e) {
                error("Failed to create the instance of the " + punishmentClass.getSimpleName() + " (access failed)!", e);
            }

        for (Class<? extends Prevention> preventionClass : reflections.getSubTypesOf(Prevention.class)) {
            if (preventionClass.isInterface() || Modifier.isAbstract(preventionClass.getModifiers()))
                continue;
            try {
                Constructor<? extends Prevention> constructor = preventionClass.getConstructor(PreventionPlugin.class);
                constructor.setAccessible(true);
                manager.registerPrevention(constructor.newInstance(this));
            } catch (InstantiationException e) {
                error("Failed to create the instance of the " + preventionClass.getSimpleName() + " (instantiation failed)!", e);
            } catch (IllegalAccessException e) {
                error("Failed to create the instance of the " + preventionClass.getSimpleName() + " (access failed)!", e);
            } catch (NoSuchMethodException e) {
                error("Failed to create the instance of the " + preventionClass.getSimpleName() + " (missing/invalid constructor)!", e);
            } catch (InvocationTargetException e) {
                error("Failed to create the instance of the " + preventionClass.getSimpleName() + " (error in constructor)!", e);
            }
        }

        manager.enablePreventions();

        logger.info(PreventionManager.getInstance().getPreventions().size() + " Prevention(s) have been registered!");
        convertPreventionConfigs();
    }

    @Override
    public void onDisable() {
        translation = null;
        PreventionManager.getInstance().disablePreventions();
    }

    private void convertPreventionConfigs() {
        final String messageDelayKey = "messageDelay";
        PreventionConfiguration prevConfig;
        for (Prevention prevention : PreventionManager.getInstance().getPreventions()) {
            prevConfig = prevention.getConfig();
            if (prevConfig.contains(messageDelayKey)) {
                prevConfig.set("throttleDelay", prevConfig.get(messageDelayKey));
                prevConfig.set(messageDelayKey, null);
                prevention.saveConfig();
            }
        }
    }

    @Override
    public File getConfigurationFolder() {
        return preventionConfigFolder;
    }

    public static void log(String msg) {
        logger.log(INFO, msg);
    }

    public static void error(String msg) {
        logger.log(SEVERE, msg);
    }

    public static void error(String msg, Throwable t) {
        logger.log(SEVERE, msg, t);
    }

    public static String _(String message, Object... params) {
        return translation.translate(message, params);
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Translation newTranslation) {
        translation = newTranslation;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        PreventionManager.getInstance().disablePreventions(event.getPlugin());
    }

    @Override
    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    @Override
    public String getPermissionBase() {
        return "antiguest.preventions.";
    }

    @Override
    public boolean allowPunishments() {
        return punishments;
    }

    @Override
    public boolean logViolations() {
        return logViolations;
    }
}
