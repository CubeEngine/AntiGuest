package de.cubeisland.AntiGuest;

import de.cubeisland.AntiGuest.commands.BasicCommands;
import de.cubeisland.AntiGuest.commands.PreventionManagementCommands;
import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionConfiguration;
import de.cubeisland.AntiGuest.prevention.PreventionManager;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import de.cubeisland.AntiGuest.prevention.preventions.*;
import de.cubeisland.AntiGuest.prevention.punishments.*;
import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.Translation;
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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin implements Listener, PreventionPlugin
{
    private static Logger logger = null;
    private static final String PERMISSION_BASE = "antiguest.commands.";
    
    private File dataFolder;
    private File preventionConfigFolder;
    private static Translation translation;
    private BaseCommand baseCommand;

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();
        this.preventionConfigFolder = new File(this.dataFolder, "preventions");
        
        reloadConfig();
        Configuration config = getConfig();
        this.convertConfig((FileConfiguration)config);
        config.addDefault("language", System.getProperty("user.language", "en"));
        config.options().copyDefaults(true);
        
        translation = Translation.get(this.getClass(), getConfig().getString("language"));
        if (translation == null)
        {
            translation = Translation.get(this.getClass(), "en");
            config.set("language", "en");
        }
        saveConfig();

        this.baseCommand = new BaseCommand(this, new Permission(PERMISSION_BASE + "*", PermissionDefault.OP), PERMISSION_BASE);
        this.baseCommand.registerCommands(new BasicCommands(this))
                        .registerCommands(new PreventionManagementCommands());

        getCommand("antiguest").setExecutor(baseCommand);


        PreventionManager.getInstance()
            .registerPunishment(new BanPunishment())
            .registerPunishment(new BurnPunishment())
            .registerPunishment(new DropitemPunishment())
            .registerPunishment(new ExplosionPunishment())
            .registerPunishment(new KickPunishment())
            .registerPunishment(new KillPunishment())
            .registerPunishment(new LightningPunishment())
            .registerPunishment(new MessagePunishment())
            .registerPunishment(new PoisonPunishment())
            .registerPunishment(new RocketPunishment())
            .registerPunishment(new SlapPunishment())
            .registerPunishment(new StarvationPunishment())
            
            .registerPrevention(new AfkPrevention(this))
            .registerPrevention(new BedPrevention(this))
            .registerPrevention(new BowPrevention(this))
            .registerPrevention(new BreakblockPrevention(this))
            .registerPrevention(new BrewPrevention(this))
            .registerPrevention(new ButtonPrevention(this))
            .registerPrevention(new CakePrevention(this))
            .registerPrevention(new ChangesignPrevention(this))
            .registerPrevention(new ChatPrevention(this))
            .registerPrevention(new ChestPrevention(this))
            .registerPrevention(new CommandPrevention(this))
            .registerPrevention(new DamagePrevention(this))
            .registerPrevention(new DispenserPrevention(this))
            .registerPrevention(new DoorPrevention(this))
            .registerPrevention(new DropPrevention(this))
            .registerPrevention(new EnchantPrevention(this))
            .registerPrevention(new FightPrevention(this))
            .registerPrevention(new FishPrevention(this))
            .registerPrevention(new FurnacePrevention(this))
            .registerPrevention(new HungerPrevention(this))
            .registerPrevention(new ItemPrevention(this))
            .registerPrevention(new JukeboxPrevention(this))
            .registerPrevention(new LavabucketPrevention(this))
            .registerPrevention(new LeverPrevention(this))
            .registerPrevention(new MilkingPrevention(this))
            .registerPrevention(new MonsterPrevention(this))
            .registerPrevention(new MovePrevention(this))
            .registerPrevention(new NoteblockPrevention(this))
            .registerPrevention(new PickupPrevention(this))
            .registerPrevention(new PlaceblockPrevention(this))
            .registerPrevention(new PressureplatePrevention(this))
            .registerPrevention(new RepeaterPrevention(this))
            .registerPrevention(new ShearPrevention(this))
            .registerPrevention(new SneakPrevention(this))
            .registerPrevention(new SpamPrevention(this))
            .registerPrevention(new SwearPrevention(this))
            .registerPrevention(new TamePrevention(this))
            .registerPrevention(new VehiclePrevention(this))
            .registerPrevention(new WaterbucketPrevention(this))
            .registerPrevention(new WorkbenchPrevention(this))
            .enablePreventions();
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

    public static String _(String message, Object... params)
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
}
