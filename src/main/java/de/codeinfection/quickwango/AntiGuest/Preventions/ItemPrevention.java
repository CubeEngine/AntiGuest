package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.Prevention;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class ItemPrevention extends Prevention
{
    public static final String PERMISSION_BASE = "antiguest.preventions.items.";

    private final List<Material> types;

    public ItemPrevention(final String message, final List<Material> types)
    {
        super("itemusage", PERMISSION_BASE + "*", message, 0);
        this.types = types;
    }

    public boolean canUse(final Player player, final Material material)
    {
        if (this.types.contains(material))
        {
            return player.hasPermission(PERMISSION_BASE + String.valueOf(material.getId()));
        }
        return true;
    }

    @Override
    public void sendThrottledMessage(final Player player)
    {
        super.sendMessage(player);
    }
}
