package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.quickwango.AntiGuest.Commands.CanCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.DebugCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.HelpCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.ListCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.ReloadCommand;
import de.codeinfection.quickwango.AntiGuest.Preventions.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin
{
    private static AntiGuest instance = null;

    private static Logger logger = null;
    public static boolean debugMode = false;
    
    private Server server;
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
    
    @Override
    public void onLoad()
    {
        PreventionManager.getInstance()
            .initialize(this)
            .registerPrevention(new BedPrevention())
            .registerPrevention(new BreakblockPrevention())
            .registerPrevention(new PlaceblockPrevention())
            .registerPrevention(new BrewPrevention())
            .registerPrevention(new ButtonPrevention())
            .registerPrevention(new CakePrevention())
            .registerPrevention(new ChatPrevention())
            .registerPrevention(new ChestPrevention())
            .registerPrevention(new CommandPrevention())
            .registerPrevention(new DispenserPrevention())
            .registerPrevention(new DoorPrevention())
            .registerPrevention(new DropPrevention())
            .registerPrevention(new EnchantPrevention())
            .registerPrevention(new FishPrevention())
            .registerPrevention(new FurnacePrevention())
            .registerPrevention(new HungerPrevention())
            .registerPrevention(new ItemPrevention())
            .registerPrevention(new JukeboxPrevention())
            .registerPrevention(new LavabucketPrevention())
            .registerPrevention(new LeverPrevention())
            .registerPrevention(new MonsterPrevention())
            .registerPrevention(new MovePrevention())
            .registerPrevention(new NoteblockPrevention())
            .registerPrevention(new PickupPrevention())
            .registerPrevention(new PressureplatePrevention())
            .registerPrevention(new PvpPrevention())
            .registerPrevention(new RepeaterPrevention())
            .registerPrevention(new SneakPrevention())
            .registerPrevention(new SpamPrevention())
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

        PreventionManager.getInstance().loadPreventions(this.config);

        if (!this.config.getValues(false).isEmpty())
        {
            this.saveConfig();
        }

        BaseCommand baseCommand = new BaseCommand(this);
        baseCommand.registerSubCommand(new ListCommand(baseCommand))
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
}
