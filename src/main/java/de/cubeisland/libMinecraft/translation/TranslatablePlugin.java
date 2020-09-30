package de.cubeisland.libMinecraft.translation;

import org.bukkit.plugin.Plugin;

/**
 * Represents a translated plugin
 *
 * @author Phillip Schichtel
 */
public interface TranslatablePlugin extends Plugin
{
    public Translation getTranslation();
    public void setTranslation(Translation translation);
}
