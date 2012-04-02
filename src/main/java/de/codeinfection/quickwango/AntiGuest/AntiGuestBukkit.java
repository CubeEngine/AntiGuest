package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.quickwango.AntiGuest.Commands.*;
import de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        getConfig().options().copyDefaults(true);
        debugMode = getConfig().getBoolean("debug");
        saveConfig();

        PreventionManager.getInstance().enablePreventions();

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event)
    {
        PreventionManager.getInstance().disablePreventions(event.getPlugin());
    }
}
