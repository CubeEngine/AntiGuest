package de.codeinfection.quickwango.AntiGuest;

import de.codeinfection.quickwango.AntiGuest.Commands.CanCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.DebugCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.HelpCommand;
import de.codeinfection.quickwango.AntiGuest.Commands.ListCommand;
import de.codeinfection.quickwango.AntiGuest.Preventions.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin
{
    private static AntiGuest instance = null;
    public static final Map<String, Prevention> preventions = new HashMap<String, Prevention>();

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

        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();

        this.loadPreventions();

        BaseCommand baseCommand = new BaseCommand();
        baseCommand.registerSubCommand(new ListCommand(baseCommand))
                   .registerSubCommand(new CanCommand(baseCommand))
                   .registerSubCommand(new DebugCommand(baseCommand))
                   .registerSubCommand(new HelpCommand(baseCommand))
                   .setDefaultCommand("help");
        
        this.getCommand("antiguest").setExecutor(baseCommand);

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable()
    {
        log(this.getDescription().getVersion() + " disabled");
    }

    private void loadPreventions()
    {
        debugMode = this.config.getBoolean("debug");

        ConfigurationSection preventionsSection = this.config.getConfigurationSection("preventions.actions");
        if (preventionsSection != null)
        {
            final PreventionManager prevMgr = PreventionManager.getInstance();
            ConfigurationSection currentSection;
            for (String prevention : preventionsSection.getKeys(false))
            {
                currentSection = preventionsSection.getConfigurationSection(prevention);
                if (currentSection.getBoolean("enable", false))
                {
                    prevMgr.initializePrevention(prevention, this.server, currentSection);
                }
            }
        }
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
