package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.quickwango.AntiGuest.Commands.*;
import de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuestBukkit extends JavaPlugin implements Listener
{
    private static AntiGuestBukkit instance = null;

    private static Logger logger = null;
    public static boolean debugMode = false;
    
    private Server server;
    private PluginManager pm;
    private Configuration config;
    private File dataFolder;

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
        logger = this.getLogger();
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();

        this.reloadConfig();

        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        debugMode = this.config.getBoolean("debug");

        try
        {
            Class.forName("org.bukkit.event.inventory.InventoryOpenEvent");
        }
        catch (ClassNotFoundException e)
        {
            AntiGuestBukkit.error("AntiGuest detected that your CraftBukkit version is too old.");
            AntiGuestBukkit.error("You should at least use CraftBukkit 1.1-R5 !");
            AntiGuestBukkit.error("I will now disable my self!");
            this.pm.disablePlugin(this);
            return;
        }

        PreventionManager.getInstance().enablePreventions(this.config.getConfigurationSection("preventions"));

        if (!this.config.getKeys(false).isEmpty())
        {
            this.saveConfig();
        }

        BaseCommand baseCommand = new BaseCommand(this);
        baseCommand.registerSubCommand(new EnabledCommand(baseCommand))
                   .registerSubCommand(new EnableCommand(baseCommand))
                   .registerSubCommand(new DisableCommand(baseCommand))
                   .registerSubCommand(new ListCommand(baseCommand))
                   .registerSubCommand(new CanCommand(baseCommand))
                   .registerSubCommand(new DebugCommand(baseCommand))
                   .registerSubCommand(new HelpCommand(baseCommand))
                   .registerSubCommand(new ReloadCommand(baseCommand))
                   .setDefaultCommand("help");
        
        this.getCommand("antiguest").setExecutor(baseCommand);

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable()
    {
        PreventionManager.getInstance().disablePreventions();
        log(this.getDescription().getVersion() + " disabled");
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event)
    {
        PreventionManager.getInstance().disablePreventions(event.getPlugin());
    }
}
