package de.cubeisland.AntiGuest;

import de.cubeisland.AntiGuest.commands.BasicCommands;
import de.cubeisland.AntiGuest.commands.PreventionManagementCommands;
import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionConfiguration;
import de.cubeisland.AntiGuest.prevention.PreventionManager;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import de.cubeisland.AntiGuest.prevention.preventions.AdPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.AfkPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.AnvilPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.BeaconPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.BedPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.BowPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.BreakBlockPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.BrewPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ButtonPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.CakePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.CapsPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ChangeSignPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ChatPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ChestPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.CommandPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.DamagePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.DispenserPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.DoorPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.DropPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.DropperPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.EnchantPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.FightPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.FishPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.FurnacePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.GuestLimitPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.HopperPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.HorsePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.HotbarPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.HungerPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ItemFramePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ItemPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.JukeboxPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.LavabucketPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.LeadPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.LeverPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.LinkPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.MilkingPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.MonsterPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.MovePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.NoteblockPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.PickupPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.PlaceBlockPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.PressureplatePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.RepeaterPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.ShearPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.SneakPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.SpamPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.SpawnEggPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.SwearPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.TamePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.TradingPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.TramplePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.VehiclePrevention;
import de.cubeisland.AntiGuest.prevention.preventions.WaterbucketPrevention;
import de.cubeisland.AntiGuest.prevention.preventions.WorkbenchPrevention;
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
            .registerPrevention(new HotbarPrevention(this))
            .registerPrevention(new GuestLimitPrevention(this))
            .registerPrevention(new HopperPrevention(this))
            .registerPrevention(new HungerPrevention(this))
            .registerPrevention(new ItemFramePrevention(this))
            .registerPrevention(new ItemPrevention(this))
            .registerPrevention(new JukeboxPrevention(this))
            .registerPrevention(new LavabucketPrevention(this))
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
            .registerPrevention(new VehiclePrevention(this))
            .registerPrevention(new WaterbucketPrevention(this))
            .registerPrevention(new WorkbenchPrevention(this))

            .registerPrevention(new HorsePrevention(this)) // TODO ride/access inventory
            .registerPrevention(new LeadPrevention(this))
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
        final String PREVENTIONS_KEY = "preventions";
        ConfigurationSection section = config.getConfigurationSection(PREVENTIONS_KEY);
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
            config.set(PREVENTIONS_KEY, null);
        }
    }

    private void convertPreventionConfigs()
    {
        final String MESSAGE_DELAY_KEY = "messageDelay";
        PreventionConfiguration prevConfig;
        for (Prevention prevention : PreventionManager.getInstance().getPreventions())
        {
            prevConfig = prevention.getConfig();
            if (prevConfig.contains(MESSAGE_DELAY_KEY))
            {
                prevConfig.set("throttleDelay", prevConfig.get(MESSAGE_DELAY_KEY));
                prevConfig.set(MESSAGE_DELAY_KEY, null);
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
