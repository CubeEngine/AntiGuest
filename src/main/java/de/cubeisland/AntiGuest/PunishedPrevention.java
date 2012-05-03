package de.cubeisland.AntiGuest;

import static de.cubeisland.AntiGuest.AntiGuest._;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class PunishedPrevention extends Prevention
{
    private boolean punish;
    private final Map<Integer, Map<Punishment, ConfigurationSection>> violationPunishmentMap;
    private final Map<Player, Integer> playerViolationMap;
    private int highestPunishmentViolation;
    private final Map<Player, Long> punishThrottleTimestamps;
    
    public PunishedPrevention(String name, PreventionPlugin plugin)
    {
        this(name, plugin, false);
    }

    public PunishedPrevention(String name, PreventionPlugin plugin, boolean enableByDefault)
    {
        this(name, PERMISSION_BASE + name, plugin, enableByDefault);
    }

    public PunishedPrevention(String name, String permission, PreventionPlugin plugin, boolean enableByDefault)
    {
        super(name, permission, plugin, enableByDefault);
        this.punishThrottleTimestamps = new HashMap<Player, Long>();
        this.violationPunishmentMap = new HashMap<Integer, Map<Punishment, ConfigurationSection>>();
        this.playerViolationMap = new HashMap<Player, Integer>();
        this.highestPunishmentViolation = 0;
    }

    @Override
    public void enable()
    {
        Configuration config = getConfig();
        this.punish = config.getBoolean("punish", this.punish);

        if (this.punish)
        {
            List punishmentSections = config.getList("punishments");
            if (punishmentSections != null)
            {
                PreventionManager prevMgr = PreventionManager.getInstance();
                Punishment punishment;
                int violations;
                Map<Punishment, ConfigurationSection> punishmentConfigMap;
                ConfigurationSection punishmentSection;
                for (Object entry : punishmentSections)
                {
                    if (entry instanceof ConfigurationSection)
                    {
                        punishmentSection = (ConfigurationSection)entry;
                        for (String key : punishmentSection.getKeys(false))
                        {
                            punishment = prevMgr.getPunishment(key);
                            if (punishment != null && punishmentSection.isConfigurationSection(key))
                            {
                                punishmentSection = punishmentSection.getConfigurationSection(key);
                                if (punishmentSection.isInt("violations"))
                                {
                                    violations = punishmentSection.getInt("violations", 0);
                                    if (violations > 0)
                                    {
                                        punishmentConfigMap = this.violationPunishmentMap.get(violations);
                                        if (punishmentConfigMap == null)
                                        {
                                            punishmentConfigMap = new HashMap<Punishment, ConfigurationSection>();
                                            this.violationPunishmentMap.put(violations, punishmentConfigMap);
                                            if (violations > this.highestPunishmentViolation)
                                            {
                                                this.highestPunishmentViolation = violations;
                                            }
                                        }
                                        punishmentConfigMap.put(punishment, punishmentSection);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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

    public void punish(Player player)
    {
        if (!this.punish)
        {
            return;
        }
        Integer violations = this.playerViolationMap.get(player);
        if (violations == null || violations >= this.highestPunishmentViolation)
        {
            violations = 0;
        }
        this.playerViolationMap.put(player, ++violations);

        Map<Punishment, ConfigurationSection> punishments = this.violationPunishmentMap.get(violations);
        if (punishments != null)
        {
            for (Map.Entry<Punishment, ConfigurationSection> entry : punishments.entrySet())
            {
                entry.getKey().punish(player, entry.getValue());
            }
        }
    }

    public void punishThrottled(Player player)
    {
        if (!this.punish)
        {
            return;
        }
        Long next = this.punishThrottleTimestamps.get(player);
        next = (next == null ? 0 : next);
        final long current = System.currentTimeMillis();

        if (next < current)
        {
            this.punish(player);
            this.punishThrottleTimestamps.put(player, current + getThrottleDelay());
        }
    }
}
