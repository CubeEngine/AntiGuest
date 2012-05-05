package de.cubeisland.AntiGuest.prevention;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.procedure.TObjectObjectProcedure;
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
    private final static PunishmentProcedure PUNISHMENT_PROCEDURE = new PunishmentProcedure();
    private boolean punish;
    private TIntObjectMap<THashMap<Punishment, ConfigurationSection>> violationPunishmentMap;
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
        this.violationPunishmentMap = new TIntObjectHashMap<THashMap<Punishment, ConfigurationSection>>();
        this.playerViolationMap = new TObjectIntHashMap<Player>();

        Configuration config = getConfig();
        this.punish = config.getBoolean("punish", this.punish);

        if (this.punish)
        {
            ConfigurationSection punishmentsSection = config.getConfigurationSection("punishments");
            if (punishmentsSection != null)
            {
                int violation;
                THashMap<Punishment, ConfigurationSection> punishments;
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
                                            punishments = new THashMap<Punishment, ConfigurationSection>();
                                            this.violationPunishmentMap.put(violation, punishments);
                                            this.highestPunishmentViolation = Math.max(this.highestPunishmentViolation, violation);
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

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("punish", false);
        config.set("punishments.3.slap.damage", 4);
        config.set("punishments.5.kick.reason", getPlugin().getTranslation().translate("defaultKickReason"));

        return config;
    }

    public void punish(final Player player)
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

        THashMap<Punishment, ConfigurationSection> punishments = this.violationPunishmentMap.get(violations);
        if (punishments != null)
        {
            PUNISHMENT_PROCEDURE.player = player;
            punishments.forEachEntry(PUNISHMENT_PROCEDURE);
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

    private static final class PunishmentProcedure implements TObjectObjectProcedure<Punishment, ConfigurationSection>
    {
        public Player player;

        public boolean execute(Punishment punishment, ConfigurationSection config)
        {
            punishment.punish(this.player, config);
            return true;
        }
    }
}
