package de.cubeisland.antiguest.prevention;

import java.io.File;

import de.cubeisland.libMinecraft.command.BaseCommand;
import de.cubeisland.libMinecraft.translation.TranslatablePlugin;

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
    File getConfigurationFolder();

    /**
     * Returns the plugins base command
     *
     * @return the plugins base command
     */
    BaseCommand getBaseCommand();

    /**
     * Returns the base string for the prevention permissions
     *
     * @return the base string for the permissions
     */
    String getPermissionBase();

    /**
     * Returns whether punishments are enabled for this plugin
     *
     * @return true if punishments are enabled
     */
    boolean allowPunishments();

    /**
     * Returns whether prevention violations can be logged
     *
     * @return true if punishments may log violations
     */
    boolean logViolations();
}
