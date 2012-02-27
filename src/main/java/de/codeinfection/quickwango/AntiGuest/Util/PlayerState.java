package de.codeinfection.quickwango.AntiGuest.Util;

import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 * This is just a simple class that stores player states on runtime
 *
 * @author Phillip Schichtel
 */
public class PlayerState
{
    public static final int CHATTED = 1;

    private static final HashMap<Player, Integer> states = new HashMap<Player, Integer>();

    public static boolean has(Player player, int state)
    {

        Integer savedState = states.get(player);
        if (savedState != null)
        {
            return ((savedState.intValue() & state) == state);
        }
        return false;
    }

    public static void set(Player player, int state)
    {
        Integer savedState = states.get(player);
        if (savedState == null)
        {
            states.put(player, state);
        }
        else
        {
            states.put(player, savedState | state);
        }
    }

    public static void remove(Player player, int state)
    {
        Integer savedState = states.get(player);
        if (savedState != null)
        {
            states.put(player, savedState.intValue() ^ state);
        }
    }

    public static void removePlayer(Player player)
    {
        states.remove(player);
    }
}
