package de.cubeisland.antiguest.prevention;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.cubeisland.antiguest.AntiGuest;
import gnu.trove.map.hash.THashMap;

/**
 * This class manages the Prevention's.
 *
 * @author Phillip Schichtel
 */
public class PreventionManager {
    private static PreventionManager instance = null;

    private final THashMap<String, Prevention> preventions;
    private final THashMap<String, Punishment> punishments;

    private final PluginManager pm;
    private final Permission parentPermission;

    private PreventionManager() {
        pm = Bukkit.getPluginManager();
        parentPermission = new Permission("antiguest.preventions.*", PermissionDefault.OP);
        preventions = new THashMap<String, Prevention>();
        punishments = new THashMap<String, Punishment>();
        pm.addPermission(parentPermission);
    }

    /**
     * Returns the singleton instance of the PreventionManager
     *
     * @return the instance
     */
    public static PreventionManager getInstance() {
        if (instance == null)
            instance = new PreventionManager();
        return instance;
    }

    /**
     * Returns the named prevention or null if not available
     *
     * @param name the name of the prevention
     *
     * @return the prevention or null
     */
    public Prevention getPrevention(String name) {
        return preventions.get(name);
    }

    /**
     * Returns all registered preventions
     *
     * @return a collection of all preventions
     */
    public Collection<Prevention> getPreventions() {
        return new ArrayList<Prevention>(preventions.values());
    }

    /**
     * Registeres a prevention
     *
     * @param prevention the prevention to register
     *
     * @return fluent interface
     */
    public PreventionManager registerPrevention(Prevention prevention) {
        if (prevention == null)
            throw new IllegalArgumentException("prevention must not be null!");
        if (!prevention.isLoaded())
            prevention.load();
        if (!preventions.containsValue(prevention)) {
            preventions.put(prevention.getName(), prevention);
            Permission perm = prevention.getPermission();
            try {
                pm.addPermission(perm);
            } catch (IllegalArgumentException ignored) {
            }
            perm.addParent(parentPermission, true);
        }

        return this;
    }

    /**
     * Unregisteres a prevention if registered
     *
     * @param name the name of the prevention
     *
     * @return fluent interface
     */
    public PreventionManager unregisterPrevention(String name) {
        preventions.remove(name);
        return this;
    }

    public boolean enablePrevention(final String name) {
        final Prevention prevention = preventions.get(name);
        if (prevention != null)
            return this.enablePrevention(prevention);
        return false;
    }

    /**
     * Enables the named prevention if registered
     *
     * @param prevention the preventions name
     *
     * @return true if the initialization was successful
     */
    public boolean enablePrevention(final Prevention prevention) {
        if (prevention != null && !prevention.isEnabled())
            try {
                prevention.enable();
                pm.registerEvents(prevention, prevention.getPlugin());

                prevention.setEnabled(true);
                return true;
            } catch (RuntimeException e) {
                AntiGuest.error("Failed to enable the prevention '" + prevention.getName() + "'...", e);
            }
        return false;
    }

    /**
     * This method loads all registered preventions based on the given
     * ConfigurationSection and the default configuraiton. The given
     * ConfigurationSection should have a key "preventions" on top level, otherwise
     * this will fail
     *
     * @return fluent interface
     */
    public PreventionManager enablePreventions() {
        for (Prevention prevention : preventions.values())
            if (prevention.getConfig().getBoolean("enable"))
                this.enablePrevention(prevention);

        return this;
    }

    /**
     * Disables the named prevention
     *
     * @param name name of the prevention
     *
     * @return fluent interface
     */
    public PreventionManager disablePrevention(String name) {
        final Prevention prevention = preventions.get(name);
        if (prevention != null)
            this.enablePrevention(prevention);
        return this;
    }

    /**
     * Disables the named prevention
     *
     * @param prevention name of the prevention
     *
     * @return fluent interface
     */
    public PreventionManager disablePrevention(Prevention prevention) {
        if (prevention != null && prevention.isEnabled()) {
            prevention.setEnabled(false);
            HandlerList.unregisterAll(prevention);
            try {
                prevention.disable();
            } catch (RuntimeException e) {
                AntiGuest.error("Failed to disable the prevention '" + prevention.getName() + "'...", e);
            }
        }
        return this;
    }

    /**
     * Disables all preventions
     *
     * @return fluent interface
     */
    public PreventionManager disablePreventions() {
        for (Prevention prevention : preventions.values())
            this.disablePrevention(prevention);
        return this;
    }

    /**
     * Disables all preventions of a plugin
     *
     * @param plugin the plugin that registered the preventions
     *
     * @return fluent interface
     */
    public PreventionManager disablePreventions(Plugin plugin) {
        for (Prevention prevention : preventions.values())
            if (prevention.getPlugin() == plugin)
                this.disablePrevention(prevention);

        return this;
    }

    /**
     * Registeres a punishment
     *
     * @param punishment the punishment
     *
     * @return fluent interface
     */
    public PreventionManager registerPunishment(Punishment punishment) {
        if (punishment == null)
            throw new IllegalArgumentException("The punishment must not be null!");
        punishments.put(punishment.getName(), punishment);
        return this;
    }

    /**
     * Unregisteres a punishment
     *
     * @param punishment the punishment to unregister
     *
     * @return fluent interface
     */
    public PreventionManager unregisterPunishment(Punishment punishment) {
        if (punishment == null)
            throw new IllegalArgumentException("The punishment must not be null!");
        return this.unregisterPunishment(punishment.getName());
    }

    /**
     * Unregisteres a punishment by its name
     *
     * @param name the name of the punishment to unregister
     *
     * @return fluent interface
     */
    public PreventionManager unregisterPunishment(String name) {
        punishments.remove(name);
        return this;
    }

    /**
     * Returns a punishment by its name
     *
     * @param name the name of the punishment
     *
     * @return the punishment
     */
    public Punishment getPunishment(String name) {
        return punishments.get(name);
    }
}
