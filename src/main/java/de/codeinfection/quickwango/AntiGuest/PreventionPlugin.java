package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public interface PreventionPlugin extends Plugin
{
    public File getConfigurationFolder();
}
