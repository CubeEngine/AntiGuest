package de.cubeisland.AntiGuest;

import static de.cubeisland.AntiGuest.AntiGuest._;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

/**
 *
 * @author CodeInfection
 */
public class PunishedPrevention extends Prevention
{
    public PunishedPrevention(String name, PreventionPlugin plugin)
    {
        super(name, plugin);
    }

    public PunishedPrevention(String name, PreventionPlugin plugin, boolean enableByDefault)
    {
        super(name, plugin, enableByDefault);
    }

    public PunishedPrevention(String name, String permission, PreventionPlugin plugin, boolean enableByDefault)
    {
        super(name, permission, plugin, enableByDefault);
    }

    public Configuration getDefaultConfiguration()
    {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("punish", false);

        ConfigurationSection punishmentSection;
        List<ConfigurationSection> punishments = new ArrayList<ConfigurationSection>(2);

        punishmentSection = new MemoryConfiguration();
        punishmentSection.set("slap.violations", 3);
        punishmentSection.set("slap.damage", 4);
        punishments.add(punishmentSection);

        punishmentSection = new MemoryConfiguration();
        punishmentSection.set("kick.violations", 3);
        punishmentSection.set("kick.reason", _("defaultKickReason"));
        punishments.add(punishmentSection);

        defaultConfig.set("punishments", punishments);

        return defaultConfig;
    }
}
