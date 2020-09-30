package de.cubeisland.antiguest.prevention;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;

/**
 * This class represents a prevention
 *
 * @author Phillip Schichtel
 */
public abstract class Prevention implements Listener {
    private final Thread mainThread = Thread.currentThread();
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
    private TIntObjectMap<Map<Punishment, ConfigurationSection>> violationPunishmentMap;
    private TObjectIntMap<String> playerViolationMap;
    private TObjectLongMap<String> punishThrottleTimestamps;
    private int highestPunishmentViolation;

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * allowed punishing.
     *
     * @param name   the name of the prevention
     * @param plugin the plugin
     */
    public Prevention(final String name, final PreventionPlugin plugin) {
        this(name, plugin, true);
    }

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * whether to allow punishing.
     *
     * @param name           the name of the prevention
     * @param plugin         the plugin
     * @param allowPunishing whether to allow punishing
     */
    public Prevention(final String name, final PreventionPlugin plugin, final boolean allowPunishing) {
        this(name, plugin, allowPunishing, allowPunishing);
    }

    /**
     * Initializes the prevention with its name, the corresponding plugin and
     * whether to allow punishing.
     *
     * @param name           the name of the prevention
     * @param plugin         the plugin
     * @param allowPunishing whether to allow punishing
     */
    public Prevention(final String name, final PreventionPlugin plugin, final boolean allowPunishing, final boolean allowViolationLogging) {
        this.name = name;
        permission = new Permission(plugin.getPermissionBase() + name, PermissionDefault.OP);
        this.plugin = plugin;
        this.allowPunishing = allowPunishing;
        this.allowViolationLogging = allowViolationLogging;

        loaded = false;
        setMessage(null);
        this.setThrottleDelay(0L);
        enabled = false;
        setEnableByDefault(false);
        config = null;
        highestPunishmentViolation = 0;
        setEnablePunishing(false);
        setLogViolations(true);

        messageThrottleTimestamps = null;
        logThrottleTimestamps = null;
    }

    /**
     * Returns the configuration of this prevention
     *
     * @return the config
     */
    public final PreventionConfiguration getConfig() {
        return config;
    }

    /**
     * Resets the configuration of this prevention
     *
     * @return true on success
     */
    public final boolean resetConfig() {
        config = PreventionConfiguration.get(getPlugin().getConfigurationFolder(), this, false);
        return saveConfig();
    }

    /**
     * Reloads the prevention's configuration
     *
     * @return true on success
     */
    public final boolean reloadConfig() {
        try {
            config.load();
            return true;
        } catch (InvalidConfigurationException e) {
            getPlugin().getLogger().log(SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            getPlugin().getLogger().log(SEVERE, e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * Saves the configuration of the prevention
     *
     * @return true on success
     */
    public final boolean saveConfig() {
        try {
            config.save();
            return true;
        } catch (IOException e) {
            getPlugin().getLogger().log(SEVERE, e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * Generates the default configuration of this prevention. This method should be
     * overridden for custom configs.
     *
     * @return the default config
     */
    public Configuration getDefaultConfig() {
        Configuration defaultConfig = new MemoryConfiguration();

        defaultConfig.set("enable", getEnableByDefault());
        defaultConfig.set("message", getPlugin().getTranslation().translate("message_" + name));
        if (this.getThrottleDelay() > 0)
            defaultConfig.set("throttleDelay", this.getThrottleDelay(TimeUnit.SECONDS));

        if (getAllowViolationLogging())
            defaultConfig.set("log-violations", getLogViolations());

        if (getAllowPunishing()) {
            defaultConfig.set("punish", getEnablePunishing());
            defaultConfig.set("punishments.3.slap.damage", 4);
            defaultConfig.set("punishments.5.kick.reason", plugin.getTranslation().translate("defaultKickReason"));
        }

        return defaultConfig;
    }

    /**
     * Returns a list of strings for the config header
     *
     * @return the lines
     */
    public String getConfigHeader() {
        return "This is the configuration file of the " + name + " configuration.\n";
    }

    /**
     * Loads the configuration of the prevention. this method should be called right
     * after the object got constructed.
     */
    public void load() {
        loaded = true;
        config = PreventionConfiguration.get(getPlugin().getConfigurationFolder(), this);
        if (getAllowPunishing() && getConfig().get("punishments", null) != null)
            getConfig().getDefaultSection().set("punishments", null);
        getConfig().safeSave();
    }

    /**
     * Enables the prevention. This method should be overridden for custom configs.
     */
    public void enable() {
        setMessage(getConfig().getString("message"));
        this.setThrottleDelay(getConfig().getInt("throttleDelay", 0), TimeUnit.SECONDS);

        messageThrottleTimestamps = new TObjectLongHashMap<String>();
        if (allowViolationLogging) {
            logViolations = getConfig().getBoolean("log-violations");

            if (getLogViolations())
                logThrottleTimestamps = new TObjectLongHashMap<String>();
        }

        if (getAllowPunishing()) {
            setEnablePunishing(getConfig().getBoolean("punish", getEnablePunishing()));

            if (getEnablePunishing()) {
                punishThrottleTimestamps = new TObjectLongHashMap<String>();
                violationPunishmentMap = new TIntObjectHashMap<Map<Punishment, ConfigurationSection>>();
                playerViolationMap = new TObjectIntHashMap<String>();

                ConfigurationSection punishmentsSection = getConfig().getConfigurationSection("punishments");
                if (punishmentsSection != null) {
                    int violation;
                    Map<Punishment, ConfigurationSection> punishments;
                    ConfigurationSection violationSection;
                    ConfigurationSection punishmentSection;
                    PreventionManager pm = PreventionManager.getInstance();
                    Punishment punishment;

                    for (String violationString : punishmentsSection.getKeys(false))
                        try {
                            violation = Integer.parseInt(violationString);
                            punishments = violationPunishmentMap.get(violation);
                            violationSection = punishmentsSection.getConfigurationSection(violationString);
                            if (violationSection != null)
                                for (String punishmentName : violationSection.getKeys(false)) {
                                    punishment = pm.getPunishment(punishmentName);
                                    if (punishment != null) {
                                        punishmentSection = violationSection.getConfigurationSection(punishmentName);
                                        if (punishmentSection != null) {
                                            if (punishments == null) {
                                                punishments = new ConcurrentHashMap<Punishment, ConfigurationSection>(1);
                                                violationPunishmentMap.put(violation, punishments);
                                                highestPunishmentViolation = Math.max(highestPunishmentViolation, violation);
                                            }
                                            punishments.put(punishment, punishmentSection);
                                        }
                                    }
                                }
                        } catch (NumberFormatException ignored) {
                        }
                }
            }
        }
    }

    /**
     * Disables the prevention. This method should be overridden to cleanup
     * customized preventions
     */
    public void disable() {
        messageThrottleTimestamps.clear();
        messageThrottleTimestamps = null;

        if (getAllowPunishing() && getEnablePunishing()) {
            if (playerViolationMap != null) {
                playerViolationMap.clear();
                playerViolationMap = null;
            }

            if (punishThrottleTimestamps != null) {
                punishThrottleTimestamps.clear();
                punishThrottleTimestamps = null;
            }

            if (violationPunishmentMap != null) {
                violationPunishmentMap.clear();
                violationPunishmentMap = null;
            }
        }

        if (getAllowViolationLogging() && getLogViolations() && logThrottleTimestamps != null) {
            logThrottleTimestamps.clear();
            logThrottleTimestamps = null;
        }
    }

    /**
     * Sets whether to enable this prevention by default
     *
     * @param enable true to enable it by default
     */
    public final void setEnableByDefault(boolean enable) {
        enableByDefault = enable;
    }

    /**
     * Returns whether this prevention will be enabled by default
     *
     * @return true if it will be enabled by default
     */
    public final boolean getEnableByDefault() {
        return enableByDefault;
    }

    /**
     * Returns whether this prevention is already loaded
     *
     * @return true if loaded
     */
    public final boolean isLoaded() {
        return loaded;
    }

    /**
     * Returns whether this prevention is enabled.
     *
     * @return true if this prevention is enabled
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of this prevention
     *
     * @param enable the new enabled state
     */
    public final void setEnabled(boolean enable) {
        enabled = enable;
    }

    /**
     * Returns the prevention's name
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the prevention's permission
     *
     * @return the permission
     */
    public final Permission getPermission() {
        return permission;
    }

    /**
     * Returns the plugin corresponding to this prevention
     *
     * @return the plugin
     */
    public final PreventionPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the message this prevention will send to players
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message this prevention will send to players
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = parseMessage(message);
    }

    /**
     * Returns the delay this preventions uses for throttled messages
     *
     * @return the delay
     */
    public long getThrottleDelay() {
        return throttleDelay;
    }

    public long getThrottleDelay(TimeUnit unit) {
        return unit.convert(this.getThrottleDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Sets the delay this preventions uses for throttled messages
     */
    public void setThrottleDelay(long delay) {
        throttleDelay = delay;
    }

    public void setThrottleDelay(long delay, TimeUnit unit) {
        this.setThrottleDelay(unit.toMillis(delay));
    }

    /**
     * Sets whether this prevention enables punishing
     *
     * @param enable true to enable it
     */
    public void setEnablePunishing(boolean enable) {
        enablePunishing = enable;
    }

    /**
     * Returns whether this prevention may use punishments
     *
     * @return true if punishing is allowed
     */
    public boolean getAllowPunishing() {
        return allowPunishing;
    }

    /**
     * Returns whether this prevention enables punishing
     *
     * @return true if it enables it
     */
    public boolean getEnablePunishing() {
        return enablePunishing;
    }

    /**
     * Sets whether this preventions should log violations
     *
     * @param allow true to allow it
     */
    public void setLogViolations(boolean allow) {
        logViolations = allow;
    }

    /**
     * Returns whether this preventions logs violations
     *
     * @return true if it logs violations
     */
    public boolean getLogViolations() {
        return logViolations;
    }

    /**
     * Returns whether this prevention allows to log violations
     *
     * @return true if violation logging is allowed
     */
    public boolean getAllowViolationLogging() {
        return allowViolationLogging;
    }

    protected static boolean isNPC(Entity entity) {
        return entity instanceof NPC || entity.hasMetadata("NPC");
    }

    /**
     * Checks whether a player can pass a prevention
     *
     * @param player the player
     *
     * @return true if the player can pass the prevention
     */
    public boolean can(final Player player) {
        if (isNPC(player) && !getPlugin().getConfig().getBoolean("prevent-npc"))
            return true;
        return player.hasPermission(permission);
    }

    public boolean checkAndSetThrottleTimestamp(final Player player, final TObjectLongMap<String> timestamps) {
        if (this.getThrottleDelay() > 0) {
            final long next = timestamps.get(player.getName());
            final long current = System.currentTimeMillis();
            if (next < current)
                timestamps.put(player.getName(), current + this.getThrottleDelay());
            else
                return false;
        }
        return true;
    }

    /**
     * Does the same as sendMessage(Player), except that this method throttles the
     * messages sending
     *
     * @param player hte player to send to
     */
    public void sendMessage(final Player player) {
        if (getMessage() == null)
            return;

        if (checkAndSetThrottleTimestamp(player, messageThrottleTimestamps))
            player.sendMessage(getMessage());
    }

    /**
     * This method combines prevent() and can()
     *
     * @param event  a cancellable event
     * @param player the player
     *
     * @return true if the action was prevented
     */
    public boolean checkAndPrevent(final Cancellable event, final Player player) {
        if (!can(player)) {
            prevent(event, player);
            return true;
        }
        return false;
    }

    /**
     * This methods cancels the event and handles notifications, punishing and
     * logging
     *
     * @param event  a cancellable event
     * @param player the player
     */
    public void prevent(final Cancellable event, final Player player) {
        event.setCancelled(true);
        sendMessage(player);
        punish(player);
        logViolation(player);
    }

    public synchronized void punish(final Player player) {
        if (!getPlugin().allowPunishments() || !getAllowPunishing() || !getEnablePunishing())
            return;
        int violations = playerViolationMap.get(player.getName());
        if (violations >= highestPunishmentViolation)
            violations = 0;
        playerViolationMap.put(player.getName(), ++violations);
        if (getPlugin().getConfig().getBoolean("debug", false))
            player.sendMessage("Your current violation count for '" + getName() + "': " + violations);

        Map<Punishment, ConfigurationSection> punishments = violationPunishmentMap.get(violations);
        if (punishments == null)
            return;

        if (checkAndSetThrottleTimestamp(player, punishThrottleTimestamps))
            doPunish(player, punishments);
    }

    private void doPunish(final Player player, final Map<Punishment, ConfigurationSection> punishments) {
        if (Thread.currentThread() != mainThread) {
            getPlugin().getServer().getScheduler().callSyncMethod(getPlugin(), () -> {
                doPunish(player, punishments);
                return null;
            });
            return;
        }
        for (Map.Entry<Punishment, ConfigurationSection> entry : punishments.entrySet())
            entry.getKey().punish(player, entry.getValue());
    }

    public void logViolation(Player player) {
        if (!getPlugin().logViolations() || !getAllowViolationLogging() || !getLogViolations())
            return;

        if (checkAndSetThrottleTimestamp(player, logThrottleTimestamps)) {
            String message = getPlugin().getTranslation().translate("prevention_violated", player.getName(), getName());
            getPlugin().getLogger().log(INFO, ChatColor.stripColor(message));
            getPlugin().getServer().broadcast(message, "antiguest.violation-notification");
        }
    }

    /**
     * Parses a message
     *
     * @param message the message to parse
     *
     * @return null if message is null or empty, otherwise the parsed message
     */
    public static String parseMessage(final String message) {
        if (message == null)
            return null;
        if (message.length() == 0)
            return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{name=" + name + ", permission=" + permission.toString() + ", plugin=" + plugin.toString() + "}";
    }
}
