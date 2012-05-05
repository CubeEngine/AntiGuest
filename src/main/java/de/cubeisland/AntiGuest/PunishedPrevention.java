package de.cubeisland.AntiGuest;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This class represents a prevention the player gets punished from
 *
 * @author Phillip Schichtel
 */
public class PunishedPrevention extends Prevention
{
    private boolean punish;
    private TIntObjectMap<Map<Punishment, ConfigurationSection>> violationPunishmentMap;
    private TObjectIntMap<Player> playerViolationMap;
    private TObjectLongMap<Player> punishThrottleTimestamps;
    private int highestPunishmentViolation;

    public PunishedPrevention(String name, PreventionPlugin plugin)
    {
        this(name, PERMISSION_BASE + name, plugin);
    }

    public PunishedPrevention(String name, String permission, PreventionPlugin plugin)
    {
        super(name, permission, plugin);
        this.highestPunishmentViolation = 0;
    }

    @Override
    public void enable()
    {
        super.enable();

        this.punishThrottleTimestamps = new TObjectLongHashMap<Player>();
        this.violationPunishmentMap = new TIntObjectHashMap<Map<Punishment, ConfigurationSection>>();
        this.playerViolationMap = new TObjectIntHashMap<Player>();

        Configuration config = getConfig();
        this.punish = config.getBoolean("punish", this.punish);

        if (this.punish)
        {
            ConfigurationSection punishmentsSection = config.getConfigurationSection("punishments");
            if (punishmentsSection != null)
            {
                int violation;
                Map<Punishment, ConfigurationSection> punishments;
                ConfigurationSection violationSection;
                ConfigurationSection punishmentSection;
                PreventionManager pm = PreventionManager.getInstance();
                Punishment punishment;

                for (String violationString : punishmentsSection.getKeys(false))
                {
                    try
                    {
                        violation = Integer.parseInt(violationString);
                        punishments = this.violationPunishmentMap.get(violation);
                        violationSection = punishmentsSection.getConfigurationSection(violationString);
                        if (violationSection != null)
                        {
                            for (String punishmentName : violationSection.getKeys(false))
                            {
                                punishment = pm.getPunishment(punishmentName);
                                if (punishment != null)
                                {
                                    punishmentSection = violationSection.getConfigurationSection(punishmentName);
                                    if (punishmentSection != null)
                                    {
                                        if (punishments == null)
                                        {
                                            punishments = new HashMap<Punishment, ConfigurationSection>();
                                            this.violationPunishmentMap.put(violation, punishments);
                                        }
                                        punishments.put(punishment, punishmentSection);
                                    }
                                }
                            }
                        }
                    }
                    catch (NumberFormatException e)
                    {}
                }
            }
        }
    }

    @Override
    public void disable()
    {
        super.disable();
        
        this.playerViolationMap.clear();
        this.punishThrottleTimestamps.clear();
        this.violationPunishmentMap.clear();

        this.playerViolationMap = null;
        this.punishThrottleTimestamps = null;
        this.violationPunishmentMap = null;
    }

    public Configuration getDefaultConfiguration()
    {
        Configuration config = super.getDefaultConfig();

        config.set("punish", false);
        config.set("punishments.3.slap.damage", 4);
        config.set("punishments.5.kick.reason", getPlugin().getTranslation().translate("defaultKickReason"));

        return config;
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
        Long next = (long)this.punishThrottleTimestamps.get(player);
        if (next == null)
        {
            next = 0L;
        }

        final long current = System.currentTimeMillis();
        if (next < current)
        {
            this.punish(player);
            this.punishThrottleTimestamps.put(player, current + getThrottleDelay());
        }
    }
}
