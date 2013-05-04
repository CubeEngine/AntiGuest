package de.cubeisland.AntiGuest.prevention.punishments;

import de.cubeisland.AntiGuest.prevention.Punishment;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandPunishment implements Punishment
{
    private final CommandSender sender = new CommandPunishmentSender();

    public String getName()
    {
        return "command";
    }

    public void punish(Player player, ConfigurationSection config)
    {
        final Server server = player.getServer();
        final List<String> commands = config.getStringList("commands");

        for (String command : commands)
        {
            if (command == null)
            {
                continue;
            }
            command = String.valueOf(command);
            if (command.contains("%s"))
            {
                command = String.format(command, player.getName());
            }
            server.dispatchCommand(this.sender, command);
        }
    }

    private static final class CommandPunishmentSender implements CommandSender
    {
        public void sendMessage(String message)
        {}

        public void sendMessage(String[] messages)
        {}

        public Server getServer()
        {
            return Bukkit.getServer();
        }

        public String getName()
        {
            return "AntiGuest";
        }

        public boolean isPermissionSet(String name)
        {
            return true;
        }

        public boolean isPermissionSet(Permission perm)
        {
            return true;
        }

        public boolean hasPermission(String name)
        {
            return true;
        }

        public boolean hasPermission(Permission perm)
        {
            return true;
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value)
        {
            return null;
        }

        public PermissionAttachment addAttachment(Plugin plugin)
        {
            return null;
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks)
        {
            return null;
        }

        public PermissionAttachment addAttachment(Plugin plugin, int ticks)
        {
            return null;
        }

        public void removeAttachment(PermissionAttachment attachment)
        {}

        public void recalculatePermissions()
        {}

        public Set<PermissionAttachmentInfo> getEffectivePermissions()
        {
            return Collections.emptySet();
        }

        public boolean isOp()
        {
            return false;
        }

        public void setOp(boolean value)
        {}
    }
}
