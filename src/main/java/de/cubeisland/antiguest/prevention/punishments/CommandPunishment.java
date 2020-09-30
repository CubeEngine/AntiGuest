package de.cubeisland.antiguest.prevention.punishments;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import de.cubeisland.antiguest.prevention.Punishment;
import net.md_5.bungee.api.chat.BaseComponent;

public class CommandPunishment implements Punishment {
    private final CommandSender sender = new CommandPunishmentSender();

    @Override
    public String getName() {
        return "command";
    }

    @Override
    public void punish(Player player, ConfigurationSection config) {
        final Server server = player.getServer();
        final List<String> commands = config.getStringList("commands");

        for (String command : commands) {
            if (command == null)
                continue;
            command = String.valueOf(command);
            if (command.contains("%s"))
                command = String.format(command, player.getName());
            server.dispatchCommand(sender, command);
        }
    }

    private static final class CommandPunishmentSender implements CommandSender {
        
        private final Spigot spigot = new Spigot() {
            @Override
            public void sendMessage(BaseComponent component) {
                throw new UnsupportedOperationException("Cannot send a message to this plugin.");
            }
            
            @Override
            public void sendMessage(BaseComponent... components) {
                throw new UnsupportedOperationException("Cannot send a message to this plugin.");
            }
        };
        @Override
        public void sendMessage(String message) {
            throw new UnsupportedOperationException("Cannot send a message to this plugin.");
        }

        @Override
        public void sendMessage(String[] messages) {
            throw new UnsupportedOperationException("Cannot send a message to this plugin.");
        }

        @Override
        public Server getServer() {
            return Bukkit.getServer();
        }

        @Override
        public String getName() {
            return "AntiGuest";
        }

        @Override
        public boolean isPermissionSet(String name) {
            return true;
        }

        @Override
        public boolean isPermissionSet(Permission perm) {
            return true;
        }

        @Override
        public boolean hasPermission(String name) {
            return true;
        }

        @Override
        public boolean hasPermission(Permission perm) {
            return true;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
            return null;
        }

        @Override
        public void removeAttachment(PermissionAttachment attachment) {
        }

        @Override
        public void recalculatePermissions() {
        }

        @Override
        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return Collections.emptySet();
        }

        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(boolean value) {
        }

        @Override
        public Spigot spigot() {
            return spigot;
        }
    }
}
