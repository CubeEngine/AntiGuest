package de.cubeisland.AntiGuest.prevention;

import de.cubeisland.libMinecraft.ChatColor;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.concurrent.TimeUnit;

import static java.util.logging.Level.INFO;

/**
 * This class represents a prevention
 *
 * @author Phillip Schichtel
 */
public abstract class Prevention implements Listener
{
    private static final PunishmentProcedure PUNISHMENT_PROCEDURE = new PunishmentProcedure();

    private final String name;
    private final Permission permission;
    private final PreventionPlugin plugin;
    private final boolean allowPunishing;
    private final boolean allowViolationLogging;

    private boolean loaded;
    private String message;
    private long throttleDelay;
    private boolean enabled;
    private boolean enableByDefault;
    private PreventionConfiguration config;
    private TObjectLongMap<String> messageThrottleTimestamps;
    private boolean logViolations;
    private TObjectLongMap<String> logThrottleTimestamps;

    private boolean enablePunishing;
    private TIntObjectMap<THashMap<Punishment, ConfigurationSection>> violationPunishmentMap;
    private TObjectIntMap<String> playerViolationMap;
    private TObjectLongMap<String> punishThrottleTimestamps;
    private int highestPunishmentViolation;

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * allowed punishing.
     *
     * @param name the name of the prevention
     * @param plugin the plugin
     */
    public Prevention(final String name, final PreventionPlugin plugin)
    {
        this(name, plugin, true);
    }

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * whether to allow punishing.
     *
     * @param name the name of the prevention
     * @param plugin the plugin
     * @param allowPunishing whether to allow punishing
     */
    public Prevention(final String name, final PreventionPlugin plugin, final boolean allowPunishing)
    {
        this(name, plugin, allowPunishing, allowPunishing);
    }

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * whether to allow punishing.
     *
     * @param name the name of the prevention
     * @param plugin the plugin
     * @param allowPunishing whether to allow punishing
     */
    public Prevention(final String name, final PreventionPlugin plugin, final boolean allowPunishing, final boolean allowViolationLogging)
    {
        this.name = name;
        this.permission = new Permission(plugin.getPermissionBase() + name, PermissionDefault.OP);
        this.plugin = plugin;
        this.allowPunishing = allowPunishing;
        this.allowViolationLogging = allowViolationLogging;

        this.loaded = false;
        this.message = null;
        this.throttleDelay = 0;
        this.enabled = false;
        this.enableByDefault = false;
        this.config = null;
        this.highestPunishmentViolation = 0;
        this.enablePunishing = false;
        this.logViolations = true;
    }

    /**
     * Returns the configuration of this prevention
     *
     * @return the config
     */
    public final PreventionConfiguration getConfig()
    {
        return this.config;
    }

    /**
     * Resets the configuration of this prevention
     *
     * @return true on success
     */
    public final boolean resetConfig()
    {
        this.config = PreventionConfiguration.get(getPlugin().getConfigurationFolder(), this, false);
        return saveConfig();
    }

    /**
     * Reloads the prevention's configuration
     *
     * @return true on success
     */
    public final boolean reloadConfig()
    {
        try
        {
            this.config.load();
            return true;
        }
        catch (Throwable e)
        {
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Saves the configuration of the prevention
     *
     * @return true on success
     */
    public final boolean saveConfig()
    {
        try
        {
            this.config.save();
            return true;
        }
        catch (Throwable e)
        {
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Generates the default configuration of this prevention.
     * This method should be overridden for custom configs.
     *
     * @return the default config
     */
    public Configuration getDefaultConfig()
    {
        Configuration defaultConfig = new MemoryConfiguration();

        defaultConfig.set("enable", this.getEnableByDefault());
        defaultConfig.set("message", this.getPlugin().getTranslation().translate("message_" + this.name));
        if (this.getThrottleDelay() > 0)
        {
            defaultConfig.set("throttleDelay", getThrottleDelay(TimeUnit.SECONDS));
        }

        if (this.getAllowViolationLogging())
        {
            defaultConfig.set("log-violations", this.getLogViolations());
        }

        if (this.getAllowPunishing())
        {
            defaultConfig.set("punish", this.getEnablePunishing());
            defaultConfig.set("punishments.3.slap.damage", 4);
            defaultConfig.set("punishments.5.kick.reason", this.plugin.getTranslation().translate("defaultKickReason"));
        }

        return defaultConfig;
    }

    /**
     * Returns a list of strings for the config header
     *
     * @return the lines
     */
    public String getConfigHeader()
    {
        return "This is the configuration file of the " + this.name + " configuration.\n";
    }

    /**
     * Loads the configuration of the prevention.
     * this method should be called right after the object got constructed.
     */
    public void load()
    {
        this.loaded = true;
        this.config = PreventionConfiguration.get(this.plugin.getConfigurationFolder(), this);
        if (this.getAllowPunishing() && this.config.get("punishments", null) != null)
        {
            this.config.getDefaultSection().set("punishments", null);
        }
        this.config.safeSave();
    }

    /**
     * Enables the prevention.
     * This method should be overridden for custom configs.
     */
    public void enable()
    {
        this.messageThrottleTimestamps = new TObjectLongHashMap<String>();
        this.throttleDelay = config.getInt("throttleDelay", 0) * 1000;
        this.setMessage(config.getString("message"));
        if (this.allowViolationLogging)
        {
            this.logViolations = config.getBoolean("log-violations");

            if (this.getLogViolations())
            {
                this.logThrottleTimestamps = new TObjectLongHashMap<String>();
            }
        }

        if (this.getAllowPunishing())
        {
            this.enablePunishing = config.getBoolean("punish", this.enablePunishing);

            if (this.getEnablePunishing())
            {
                this.punishThrottleTimestamps = new TObjectLongHashMap<String>();
                this.violationPunishmentMap = new TIntObjectHashMap<THashMap<Punishment, ConfigurationSection>>();
                this.playerViolationMap = new TObjectIntHashMap<String>();

                ConfigurationSection punishmentsSection = config.getConfigurationSection("punishments");
                if (punishmentsSection != null)
                {
                    int violation;
                    THashMap<Punishment, ConfigurationSection> punishments;
                    ConfigurationSection violationSection;
                    ConfigurationSection punishmentSection;
                    PreventionManager pm = PreventionManager.getInstance();
                    Punishment punishment;

                    for (String violationString : punishmentsSection.getKeys(false))
                    {
                        try
                        {
                            violation = Integer.parseInt(violationString);
                            punishments = this.violationPunishmentMap.get(violation);
                            violationSection = punishmentsSection.getConfigurationSection(violationString);
                            if (violationSection != null)
                            {
                                for (String punishmentName : violationSection.getKeys(false))
                                {
                                    punishment = pm.getPunishment(punishmentName);
                                    if (punishment != null)
                                    {
                                        punishmentSection = violationSection.getConfigurationSection(punishmentName);
                                        if (punishmentSection != null)
                                        {
                                            if (punishments == null)
                                            {
                                                punishments = new THashMap<Punishment, ConfigurationSection>();
                                                this.violationPunishmentMap.put(violation, punishments);
                                                this.highestPunishmentViolation = Math.max(this.highestPunishmentViolation, violation);
                                            }
                                            punishments.put(punishment, punishmentSection);
                                        }
                                    }
                                }
                            }
                        }
                        catch (NumberFormatException e)
                        {}
                    }
                }
            }
        }
    }

    /**
     * Disables the prevention.
     * This method should be overridden to cleanup customized preventions
     */
    public void disable()
    {
        this.messageThrottleTimestamps.clear();
        this.messageThrottleTimestamps = null;

        if (this.getAllowPunishing() && this.getEnablePunishing())
        {
            this.playerViolationMap.clear();
            this.playerViolationMap = null;

            this.punishThrottleTimestamps.clear();
            this.punishThrottleTimestamps = null;
            
            this.violationPunishmentMap.clear();
            this.violationPunishmentMap = null;
        }

        if (this.getAllowViolationLogging() && this.getLogViolations())
        {
            this.logThrottleTimestamps.clear();
            this.logThrottleTimestamps = null;
        }
    }

    /**
     * Sets whether to enable this prevention by default
     *
     * @param enable true to enable it by default
     */
    public final void setEnableByDefault(boolean enable)
    {
        this.enableByDefault = enable;
    }

    /**
     * Returns whether this prevention will be enabled by default
     *
     * @return true if it will be enabled by default
     */
    public final boolean getEnableByDefault()
    {
        return this.enableByDefault;
    }

    /**
     * Returns whether this prevention is already loaded
     *
     * @return true if loaded
     */
    public final boolean isLoaded()
    {
        return this.loaded;
    }

    /**
     * Returns whether this prevention is enabled.
     *
     * @return true if this prevention is enabled
     */
    public final boolean isEnabled()
    {
        return this.enabled;
    }

    /**
     * Sets the enabled state of this prevention
     *
     * @param enable
     */
    public final void setEnabled(boolean enable)
    {
        this.enabled = enable;
    }

    /**
     * Returns the prevention's name
     * 
     * @return the name
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Returns the prevention's permission
     *
     * @return the permission
     */
    public final Permission getPermission()
    {
        return this.permission;
    }

    /**
     * Returns the plugin corresponding to this prevention
     *
     * @return the plugin
     */
    public final PreventionPlugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * Returns the message this prevention will send to players
     *
     * @return the message
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Sets the message this prevention will send to players
     *
     * @param message the new message
     */
    public void setMessage(String message)
    {
        this.message = parseMessage(message);
    }

    /**
     * Returns the delay this preventions uses for throttled messages
     *
     * @return the delay
     */
    public long getThrottleDelay()
    {
        return this.throttleDelay;
    }

    public long getThrottleDelay(TimeUnit unit)
    {
        return unit.convert(this.getThrottleDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Sets the delay this preventions uses for throttled messages
     *
     * @return the delay
     */
    public void setThrottleDelay(long delay)
    {
        this.throttleDelay = delay;
    }

    public void setThrottleDelay(long delay, TimeUnit unit)
    {
        this.setThrottleDelay(unit.toMillis(delay));
    }

    /**
     * Sets whether this prevention enables punishing
     * 
     * @param enable true to enable it
     */
    public void setEnablePunishing(boolean enable)
    {
        this.enablePunishing = enable;
    }

    /**
     * Returns whether this prevention may use punishments
     *
     * @return true if punishing is allowed
     */
    public boolean getAllowPunishing()
    {
        return this.allowPunishing;
    }

    /**
     * Returns whether this prevention enables punishing
     *
     * @return true if it enables it
     */
    public boolean getEnablePunishing()
    {
        return this.enablePunishing;
    }

    /**
     * Sets whether this preventions should log violations
     *
     * @param allow true to allow it
     */
    public void setLogViolations(boolean allow)
    {
        this.logViolations = allow;
    }

    /**
     * Returns whether this preventions logs violations
     *
     * @return true if it logs violations
     */
    public boolean getLogViolations()
    {
        return this.logViolations;
    }

    /**
     * Returns whether this prevention allows to log violations
     *
     * @return true if violation logging is allowed
     */
    public boolean getAllowViolationLogging()
    {
        return this.allowViolationLogging;
    }

    /**
     * Checks whether a player can pass a prevention
     *
     * @param player the player
     * @return true if the player can pass the prevention
     */
    public boolean can(final Player player)
    {
        return player.hasPermission(this.permission);
    }

    public boolean checkAndSetThrottleTimestamp(final Player player, final TObjectLongMap<String> timestamps)
    {
        if (this.getThrottleDelay() > 0)
        {
            final long next = timestamps.get(player.getName());
            final long current = System.currentTimeMillis();
            if (next < current)
            {
                timestamps.put(player.getName(), current + this.getThrottleDelay());
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Does the same as sendMessage(Player), except that this method throttles the messages sending
     * 
     * @param player hte player to send to
     */
    public void sendMessage(final Player player)
    {
        if (this.getMessage() == null)
        {
            return;
        }

        if (this.checkAndSetThrottleTimestamp(player, this.messageThrottleTimestamps))
        {
            player.sendMessage(this.getMessage());
        }
    }

    /**
     * This method combines prevent() and can()
     *
     * @param event a cancellable event
     * @param player the player
     * @return true if the action was prevented
     */
    public boolean checkAndPrevent(final Cancellable event, final Player player)
    {
        if (!this.can(player))
        {
            prevent(event, player);
            return true;
        }
        return false;
    }

    /**
     * This methods cancels the event and handles notifications, punishing and logging
     *
     * @param event a cancellable event
     * @param player the player
     */
    public void prevent(final Cancellable event, final Player player)
    {
        event.setCancelled(true);
        sendMessage(player);
        punish(player);
        logViolation(player);
    }

    public synchronized void punish(final Player player)
    {
        if (!this.getPlugin().allowPunishments() || !this.getAllowPunishing() || !this.getEnablePunishing())
        {
            return;
        }
        int violations = this.playerViolationMap.get(player.getName());
        if (violations >= this.highestPunishmentViolation)
        {
            violations = 0;
        }
        this.playerViolationMap.put(player.getName(), ++violations);

        THashMap<Punishment, ConfigurationSection> punishments = this.violationPunishmentMap.get(violations);
        if (punishments == null)
        {
            return;
        }

        if (this.checkAndSetThrottleTimestamp(player, this.punishThrottleTimestamps))
        {
            PUNISHMENT_PROCEDURE.player = player;
            punishments.forEachEntry(PUNISHMENT_PROCEDURE);
            PUNISHMENT_PROCEDURE.player = null;
        }
    }

    public void logViolation(Player player)
    {
        if (!this.getPlugin().logViolations() || !this.getAllowViolationLogging() || !this.getLogViolations())
        {
            return;
        }

        if (this.checkAndSetThrottleTimestamp(player, this.logThrottleTimestamps))
        {
            String message = this.getPlugin().getTranslation().translate("prevention_violated", player.getName(), this.getName());
            this.getPlugin().getLogger().log(INFO, ChatColor.stripColors(message));
            this.getPlugin().getServer().broadcast(message, "antiguest.violation-notification");
        }
    }

    /**
     * Parses a message
     *
     * @param message the message to parse
     * @return null if message is null or empty, otherwise the parsed message
     */
    public static String parseMessage(final String message)
    {
        if (message == null)
        {
            return null;
        }
        if (message.length() == 0)
        {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{name=" + this.name + ", permission=" + this.permission.toString() + ", plugin=" + this.plugin.toString() + "}";
    }

    private static final class PunishmentProcedure implements TObjectObjectProcedure<Punishment, ConfigurationSection>
    {
        public Player player;

        public boolean execute(Punishment punishment, ConfigurationSection config)
        {
            punishment.punish(this.player, config);
            return true;
        }
    }
}

