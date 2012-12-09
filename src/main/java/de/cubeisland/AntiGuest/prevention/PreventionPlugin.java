package de.cubeisland.AntiGuest.prevention;

import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.TranslatablePlugin;
import java.io.File;

/**
 * Represents a prevention plugin
 *
 * @author Phillip Schichtel
 */
public interface PreventionPlugin extends TranslatablePlugin
{
    /**
     * Returns the folder the prevention configurations should be in
     *
     * @return the folder as a File object
     */
    public File getConfigurationFolder();

    /**
     * Returns the plugins base command
     *
     * @return the plugins base command
     */
    public BaseCommand getBaseCommand();

    /**
     * Returns the base string for the prevention permissions
     *
     * @return the base string for the permissions
     */
    public String getPermissionBase();

    /**
     * Returns whether punishments are enabled for this plugin
     *
     * @return true if punishments are enabled
     */
    public boolean allowPunishments();

    /**
     * Returns whether prevention violations can be logged
     *
     * @return true if punishments may log violations
     */
    public boolean logViolations();
}
