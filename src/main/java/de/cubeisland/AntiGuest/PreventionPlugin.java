package de.cubeisland.AntiGuest;

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
}
