package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author CodeInfection
 */
public class PreventionConfiguration extends YamlConfiguration
{
    private final File file;

    private PreventionConfiguration(File file)
    {
        this.file = file;
    }

    public static PreventionConfiguration loadConfig(File dir, Prevention prevention)
    {
        final PreventionConfiguration config = new PreventionConfiguration(new File(dir, prevention.getName() + ".yml"));
        try
        {
            config.load();
            config.options().copyDefaults(true);
            config.setDefaults(prevention.getDefaultConfig());
        }
        catch (FileNotFoundException e)
        {}
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
        catch (InvalidConfigurationException e)
        {
            e.printStackTrace(System.err);
        }
        return config;
    }

    public void load() throws FileNotFoundException, IOException, InvalidConfigurationException
    {
        this.load(this.file);
    }


    public void save() throws IOException
    {
        this.save(file);
    }
}
