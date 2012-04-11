package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.Util.Translation;
import de.codeinfection.quickwango.AntiGuest.Commands.*;
import de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuestBukkit extends JavaPlugin implements Listener, PreventionPlugin
{
    private static AntiGuestBukkit instance = null;

    private static Logger logger = null;
    public static boolean debugMode = false;
    
    private File dataFolder;
    private File preventionConfigFolder;
    private static Translation translation;

    public AntiGuestBukkit()
    {
        instance = this;
    }

    public static AntiGuestBukkit getInstance()
    {
        return instance;
    }
    
    @Override
    public void onLoad()
    {
        logger = this.getLogger();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();
        this.preventionConfigFolder = new File(this.dataFolder, "preventions");
        PreventionManager.getInstance()
            .initialize(this)
            .registerPrevention(new AfkPrevention())
            .registerPrevention(new BedPrevention())
            .registerPrevention(new BowPrevention())
            .registerPrevention(new BreakblockPrevention())
            .registerPrevention(new BrewPrevention())
            .registerPrevention(new ButtonPrevention())
            .registerPrevention(new CakePrevention())
            .registerPrevention(new ChangesignPrevention())
            .registerPrevention(new ChatPrevention())
            .registerPrevention(new ChestPrevention())
            .registerPrevention(new CommandPrevention())
            .registerPrevention(new DamagePrevention())
            .registerPrevention(new DispenserPrevention())
            .registerPrevention(new DoorPrevention())
            .registerPrevention(new DropPrevention())
            .registerPrevention(new EnchantPrevention())
            .registerPrevention(new FightPrevention())
            .registerPrevention(new FishPrevention())
            .registerPrevention(new FurnacePrevention())
            .registerPrevention(new HungerPrevention())
            .registerPrevention(new ItemPrevention())
            .registerPrevention(new JukeboxPrevention())
            .registerPrevention(new LavabucketPrevention())
            .registerPrevention(new LeverPrevention())
            .registerPrevention(new MilkingPrevention())
            .registerPrevention(new MonsterPrevention())
            .registerPrevention(new MovePrevention())
            .registerPrevention(new NoteblockPrevention())
            .registerPrevention(new PickupPrevention())
            .registerPrevention(new PlaceblockPrevention())
            .registerPrevention(new PressureplatePrevention())
            .registerPrevention(new RepeaterPrevention())
            .registerPrevention(new ShearPrevention())
            .registerPrevention(new SneakPrevention())
            .registerPrevention(new SpamPrevention())
            .registerPrevention(new SwearPrevention())
            .registerPrevention(new TamePrevention())
            .registerPrevention(new VehiclePrevention())
            .registerPrevention(new WaterbucketPrevention())
            .registerPrevention(new WorkbenchPrevention());
    }

    @Override
    public void onEnable()
    {
        reloadConfig();
        Configuration config = getConfig();
        this.convertConfig((FileConfiguration)config);
        config.addDefault("language", System.getProperty("user.language", "en"));
        config.options().copyDefaults(true);
        debugMode = getConfig().getBoolean("debug");
        translation = Translation.get(this.getClass(), getConfig().getString("language"));
        if (translation == null)
        {
            translation = Translation.get(this.getClass(), "en");
        }
        saveConfig();

        PreventionManager.getInstance().enablePreventions();

        BaseCommand baseCommand = new BaseCommand(this);
        baseCommand.registerSubCommand(new EnabledCommand(baseCommand))
                   .registerSubCommand(new EnableCommand(baseCommand))
                   .registerSubCommand(new EnableallCommand(baseCommand))
                   .registerSubCommand(new DisableCommand(baseCommand))
                   .registerSubCommand(new DisableallCommand(baseCommand))
                   .registerSubCommand(new SetmessageCommand(baseCommand))
                   .registerSubCommand(new ListCommand(baseCommand))
                   .registerSubCommand(new CanCommand(baseCommand))
                   .registerSubCommand(new DebugCommand(baseCommand))
                   .registerSubCommand(new HelpCommand(baseCommand))
                   .registerSubCommand(new ReloadCommand(baseCommand))
                   .registerSubCommand(new LanguageCommand(baseCommand))
                   .registerSubCommand(new ResetCommand(baseCommand))
                   .setDefaultCommand("help");
        
        this.getCommand("antiguest").setExecutor(baseCommand);
    }

    @Override
    public void onDisable()
    {
        translation = null;
        PreventionManager.getInstance().disablePreventions();
    }

    private void convertConfig(FileConfiguration config)
    {
        final String PREVENTIONS_KEY = "preventions";
        ConfigurationSection section = config.getConfigurationSection(PREVENTIONS_KEY);
        if (section != null)
        {
            PreventionManager mgr = PreventionManager.getInstance();
            Prevention currentPrevention;
            PreventionConfiguration preventionConfig;
            ConfigurationSection currentSection;
            for (String key : section.getKeys(false))
            {
                currentPrevention = mgr.getPrevention(key);
                if (currentPrevention == null)
                {
                    continue;
                }
                currentSection = section.getConfigurationSection(key);
                if (currentPrevention == null)
                {
                    continue;
                }
                preventionConfig = currentPrevention.getConfig();

                for (Map.Entry<String, Object> entry : currentSection.getValues(true).entrySet())
                {
                    preventionConfig.set(entry.getKey(), entry.getValue());
                }
                try
                {
                    preventionConfig.save();
                }
                catch (IOException e)
                {}
            }
            try
            {
                config.save(new File(this.dataFolder, "config.yml.old"));
            }
            catch (IOException e)
            {
                error("Failed to write the old configuration file", e);
            }
            config.set(PREVENTIONS_KEY, null);
        }
    }

    public File getConfigurationFolder()
    {
        return this.preventionConfigFolder;
    }

    public static void log(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        logger.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }

    public static String _(String message, Object... params)
    {
        return translation.translate(message, params);
    }

    public static Translation getTranslation()
    {
        return translation;
    }

    public static void setTranslation(Translation newTranslation)
    {
        translation = newTranslation;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event)
    {
        PreventionManager.getInstance().disablePreventions(event.getPlugin());
    }
}
