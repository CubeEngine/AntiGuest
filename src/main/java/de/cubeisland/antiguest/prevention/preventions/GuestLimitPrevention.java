package de.cubeisland.antiguest.prevention.preventions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import gnu.trove.map.hash.THashMap;

/**
 * This prevention limits the number of guests that can join the server
 *
 * @author Phillip Schichtel
 */
public class GuestLimitPrevention extends Prevention {
    private Map<String, String> kickMessages;
    private int minimumPlayers;
    private int guestLimit;
    private boolean kickGuests;
    private Server server;

    public GuestLimitPrevention(PreventionPlugin plugin) {
        super("guestlimit", plugin, false);
        server = plugin.getServer();
    }

    @Override
    public void enable() {
        super.enable();
        kickMessages = new THashMap<String, String>();

        minimumPlayers = getConfig().getInt("minimumPlayers");
        guestLimit = getConfig().getInt("guestLimit");
        kickGuests = getConfig().getBoolean("kickGuests");
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "\n" + "Configuration info:\n" + "    minimumPlayer: the number of players on the server to enable the guest limit\n" + "    guestLimit: the number of guests that can join the server\n" + "    kickGuests: whether to kick a guest from hte full server if a member wants to join\n";
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("minimumPlayers", Math.round(getPlugin().getServer().getMaxPlayers() * .7));
        defaultConfig.set("guestLimit", Math.round(getPlugin().getServer().getMaxPlayers() * .3));
        defaultConfig.set("kickGuests", false);

        return defaultConfig;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            event.allow();
            kickMessages.put(event.getPlayer().getName(), event.getKickMessage());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final List<Player> guests = getGuests();
        final int onlinePlayers = server.getOnlinePlayers().size() - 1;
        final boolean serverIsFull = onlinePlayers >= server.getMaxPlayers();

        // guest ?
        if (!can(player)) {
            final boolean tooManyGuests = guests.size() >= guestLimit && onlinePlayers > minimumPlayers;

            // server full or too many guests?
            if (serverIsFull || tooManyGuests)
                player.kickPlayer(kickMessages.remove(player.getName()));
        } else {
            final boolean hasKickableGuests = guests.size() > 0 && kickGuests;

            // server full and can kick a guest?
            if (serverIsFull)
                // can kick a guest?
                if (hasKickableGuests)
                    guests.get(0).kickPlayer(getMessage());
                else
                    player.kickPlayer(kickMessages.remove(player.getName()));
        }
    }

    private List<Player> getGuests() {
        List<Player> guests = new ArrayList<Player>();

        for (Player player : getPlugin().getServer().getOnlinePlayers())
            if (!can(player))
                guests.add(player);

        return guests;
    }
}
