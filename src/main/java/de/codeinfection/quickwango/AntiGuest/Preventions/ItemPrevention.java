package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Phillip
 */
public class ItemPrevention extends Prevention
{
    public static final String PERMISSION_BASE = "antiguest.preventions.items.";
    private final List<Material> types;

    public ItemPrevention()
    {
        super("item", AntiGuest.getInstance());
        this.types = new ArrayList<Material>();
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to use this item!");
        config.set("items", new String[] {"diamond sword"});

        return config;
    }
    
    @Override
    public void initialize(final Server server, final ConfigurationSection config)
    {
        super.initialize(server, config);
        
        List<String> items = config.getStringList("items");
        if (items != null)
        {
            final PluginManager pm = server.getPluginManager();
            Material itemType;
            for (String itemName : items)
            {
                itemType = Material.matchMaterial(itemName);
                if (itemType != null)
                {
                    this.types.add(itemType);
                    try
                    {
                        pm.addPermission(new Permission(ItemPrevention.PERMISSION_BASE + String.valueOf(itemType.getId()), PermissionDefault.OP));
                    }
                    catch (IllegalArgumentException e)
                    {}
                }
            }
        }
    }
    
    @Override
    public boolean can(final Player player)
    {
        final Material material = player.getItemInHand().getType();
        if (this.types.contains(material))
        {
            return player.hasPermission(PERMISSION_BASE + material.getId());
        }
        return true;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.PHYSICAL)
        {
            prevent(event, event.getPlayer());
        }
    }
}
