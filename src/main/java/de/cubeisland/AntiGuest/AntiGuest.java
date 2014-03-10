package de.cubeisland.AntiGuest;

import de.cubeisland.AntiGuest.commands.BasicCommands;
import de.cubeisland.AntiGuest.commands.PreventionManagementCommands;
import de.cubeisland.AntiGuest.prevention.*;
import de.cubeisland.AntiGuest.prevention.preventions.*;
import de.cubeisland.AntiGuest.prevention.punishments.BanPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.BurnPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.CommandPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.DropitemPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.ExplosionPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.KickPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.KillPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.LightningPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.MessagePunishment;
import de.cubeisland.AntiGuest.prevention.punishments.PotionPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.RocketPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.SlapPunishment;
import de.cubeisland.AntiGuest.prevention.punishments.StarvationPunishment;
import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.Translation;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

public class AntiGuest extends JavaPlugin implements Listener, PreventionPlugin
{
    private static Logger logger = null;

    private        File        dataFolder;
    private        File        preventionConfigFolder;
    private static Translation translation;
    private        BaseCommand baseCommand;
    private        boolean     punishments;
    private        boolean     logViolations;

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        this.dataFolder = this.getDataFolder();
        if (!this.dataFolder.exists() && !this.dataFolder.mkdirs())
        {
            logger.log(SEVERE, "Failed to create the data folder!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
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
        this.punishments = config.getBoolean("punishments");
        this.logViolations = config.getBoolean("log-violations");

        this.baseCommand = new BaseCommand(this, "antiguest.commands.");
        this.baseCommand.registerCommands(new BasicCommands(this)).registerCommands(new PreventionManagementCommands());

        getCommand("antiguest").setExecutor(this.baseCommand);


        PreventionManager.getInstance()
            .registerPunishment(new BanPunishment())
            .registerPunishment(new BurnPunishment())
            .registerPunishment(new CommandPunishment())
            .registerPunishment(new DropitemPunishment())
            .registerPunishment(new ExplosionPunishment())
            .registerPunishment(new KickPunishment())
            .registerPunishment(new KillPunishment())
            .registerPunishment(new LightningPunishment())
            .registerPunishment(new MessagePunishment())
            .registerPunishment(new PotionPunishment())
            .registerPunishment(new RocketPunishment())
            .registerPunishment(new SlapPunishment())
            .registerPunishment(new StarvationPunishment())
            
            .registerPrevention(new AdPrevention(this))
            .registerPrevention(new AfkPrevention(this))
            .registerPrevention(new AnvilPrevention(this))
            .registerPrevention(new BeaconPrevention(this))
            .registerPrevention(new BedPrevention(this))
            .registerPrevention(new BowPrevention(this))
            .registerPrevention(new BreakBlockPrevention(this))
            .registerPrevention(new BrewPrevention(this))
            .registerPrevention(new ButtonPrevention(this))
            .registerPrevention(new CakePrevention(this))
            .registerPrevention(new CapsPrevention(this))
            .registerPrevention(new ChangeSignPrevention(this))
            .registerPrevention(new ChatPrevention(this))
            .registerPrevention(new ChestPrevention(this))
            //.registerPrevention(new CmdblockPrevention(this)) // TODO not possible yet
            .registerPrevention(new CommandPrevention(this))
            .registerPrevention(new DamagePrevention(this))
            .registerPrevention(new DispenserPrevention(this))
            .registerPrevention(new DoorPrevention(this))
            .registerPrevention(new DropperPrevention(this))
            .registerPrevention(new DropPrevention(this))
            .registerPrevention(new EnchantPrevention(this))
            .registerPrevention(new FightPrevention(this))
            .registerPrevention(new FishPrevention(this))
            .registerPrevention(new FurnacePrevention(this))
            .registerPrevention(new HorsePrevention(this)) // TODO ride/access inventory
            .registerPrevention(new HotbarPrevention(this))
            .registerPrevention(new GuestLimitPrevention(this))
            .registerPrevention(new HopperPrevention(this))
            .registerPrevention(new HungerPrevention(this))
            .registerPrevention(new ItemFramePrevention(this))
            .registerPrevention(new ItemPrevention(this))
            .registerPrevention(new JukeboxPrevention(this))
            .registerPrevention(new LavabucketPrevention(this))
            .registerPrevention(new LeadPrevention(this))
            .registerPrevention(new LeverPrevention(this))
            .registerPrevention(new LinkPrevention(this))
            .registerPrevention(new MilkingPrevention(this))
            .registerPrevention(new MonsterPrevention(this))
            .registerPrevention(new MovePrevention(this))
            .registerPrevention(new NoteblockPrevention(this))
            .registerPrevention(new PickupPrevention(this))
            .registerPrevention(new PlaceBlockPrevention(this))
            .registerPrevention(new PressureplatePrevention(this))
            .registerPrevention(new RepeaterPrevention(this))
            .registerPrevention(new ShearPrevention(this))
            .registerPrevention(new SneakPrevention(this))
            .registerPrevention(new SpamPrevention(this))
            .registerPrevention(new SpawnEggPrevention(this))
            .registerPrevention(new SwearPrevention(this))
            .registerPrevention(new TamePrevention(this))
            .registerPrevention(new TradingPrevention(this))
            .registerPrevention(new TramplePrevention(this))
            .registerPrevention(new TripwirePrevetion(this))
            .registerPrevention(new VehiclePrevention(this))
            .registerPrevention(new WaterbucketPrevention(this))
            .registerPrevention(new WorkbenchPrevention(this))

            .enablePreventions();

        logger.info(PreventionManager.getInstance().getPreventions().size() + " Prevention(s) have been registered!");
        this.convertPreventionConfigs();
    }

    @Override
    public void onDisable()
    {
        translation = null;
        PreventionManager.getInstance().disablePreventions();
    }

    private void convertConfig(FileConfiguration config)
    {
        final String preventionsKey = "preventions";
        ConfigurationSection section = config.getConfigurationSection(preventionsKey);
        PreventionManager mgr = PreventionManager.getInstance();
        if (section != null)
        {
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
                if (currentSection == null)
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
                catch (IOException ignored)
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
            config.set(preventionsKey, null);
        }
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

    public boolean allowPunishments()
    {
        return this.punishments;
    }

    public boolean logViolations()
    {
        return this.logViolations;
    }
}
