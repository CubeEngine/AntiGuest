package de.cubeisland.libMinecraft.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.cubeisland.libMinecraft.translation.TranslatablePlugin;
import de.cubeisland.libMinecraft.translation.Translation;

/**
 * This represents a base command
 *
 * @author Phillip Schichtel
 */
public class BaseCommand implements CommandExecutor {
    private final TranslatablePlugin plugin;
    private final PluginManager pm;
    private final Map<Object, Set<String>> objectCommandMap;
    private final Map<String, SubCommand> commands;
    private final Map<String, String> aliases;
    private String defaultCommand;

    private final Permission parentPermission;
    private final String permissionBase;

    public BaseCommand(TranslatablePlugin plugin, String permissionBase) {
        this(plugin, permissionBase, PermissionDefault.OP);
    }

    public BaseCommand(TranslatablePlugin plugin, String permissionBase, PermissionDefault parentDefault) {
        if (plugin == null)
            throw new IllegalArgumentException("The plugin must not be null!");
        if (permissionBase == null)
            throw new IllegalArgumentException("The permission base must not be null!");
        if (parentDefault == null)
            throw new IllegalArgumentException("The parent permission default must not be null!");
        this.plugin = plugin;
        pm = plugin.getServer().getPluginManager();
        objectCommandMap = new HashMap<Object, Set<String>>();
        commands = new HashMap<String, SubCommand>();
        aliases = new HashMap<String, String>();

        this.permissionBase = permissionBase;
        parentPermission = new Permission(permissionBase + "*", parentDefault);
        registerPermission(parentPermission);

        registerCommands(this);
        defaultCommand = "help";

    }

    private String _(String key, Object... params) {
        return plugin.getTranslation().translate(key, params);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SubCommand subCommand;
        if (args.length > 0)
            subCommand = getCommand(args[0]);
        else {
            subCommand = getCommandByName(defaultCommand);
            args = new String[] { subCommand.getName() };
        }

        if (subCommand != null) {
            Permission permission = subCommand.getPermission();
            if (permission != null && !sender.hasPermission(permission))
                sender.sendMessage(_("command_permdenied"));
            else
                try {
                    CommandArgs commandArgs = new CommandArgs(this, label, subCommand, args);
                    if (!subCommand.execute(sender, commandArgs))
                        sender.sendMessage("/" + label + " " + commandArgs.getLabel() + " " + subCommand.getUsage());
                    return true;
                } catch (CommandException e) {
                    sender.sendMessage(e.getLocalizedMessage());
                } catch (Throwable t) {
                    sender.sendMessage(_("command_internalerror"));
                    t.printStackTrace(System.err);
                }
        } else
            sender.sendMessage(_("command_notfound"));

        return true;
    }

    public BaseCommand setDefaultCommand(String name) {
        SubCommand command = getCommand(name);
        if (command != null)
            defaultCommand = command.getName();
        return this;
    }

    public SubCommand getDefaultCommand() {
        return getCommandByName(defaultCommand);
    }

    public Permission getParentPermission() {
        return parentPermission;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public final BaseCommand registerCommands(Object commandContainer) {
        if (commandContainer == null)
            throw new IllegalArgumentException("The command container must not be null!");

        try {
            Set<String> registeredCommands = new HashSet<String>();
            Command annotation;
            RequiresPermission permissionAnnotation;
            String name;
            Permission permission;

            for (Method method : commandContainer.getClass().getDeclaredMethods()) {
                annotation = method.getAnnotation(Command.class);
                if (annotation != null) {
                    name = annotation.name();
                    if ("".equals(name))
                        name = method.getName();
                    name = name.toLowerCase();
                    permission = null;
                    boolean addPermissionParent = false;
                    permissionAnnotation = method.getAnnotation(RequiresPermission.class);
                    if (permissionAnnotation != null) {
                        String permissionName = permissionAnnotation.value();
                        if (permissionName.length() == 0)
                            permissionName = permissionBase + name;

                        permission = new Permission(permissionName, permissionAnnotation.def());
                        addPermissionParent = permissionAnnotation.addParent();
                        registerPermission(permission);
                        if (addPermissionParent)
                            permission.addParent(parentPermission, true);
                    }
                    try {
                        commands.put(name, new SubCommand(commandContainer, method, name, annotation.aliases(), permission, addPermissionParent, annotation.usage()));
                        registeredCommands.add(name);

                        for (String alias : annotation.aliases())
                            aliases.put(alias, name);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }

            objectCommandMap.put(commandContainer, registeredCommands);
        } catch (Throwable t) {
        }

        return this;
    }

    public BaseCommand unregisterCommands(Object commandContainer) {
        Set<String> commandsToRemove = objectCommandMap.get(commandContainer);
        if (commandsToRemove != null)
            for (String command : commandsToRemove)
                this.unregisterCommand(command);

        return this;
    }

    public BaseCommand unregisterCommand(String name) {
        if (name == null)
            throw new IllegalArgumentException("the name must not be null!");

        commands.remove(name);
        for (Map.Entry<String, String> entry : aliases.entrySet())
            if (name.equals(entry.getValue()))
                aliases.remove(entry.getKey());
        return this;
    }

    public BaseCommand unregisterCommand(SubCommand command) {
        if (command == null)
            throw new IllegalArgumentException("the command must not be null!");
        this.unregisterCommand(command.getName());
        return this;
    }

    public Collection<SubCommand> getAllSubCommands() {
        return commands.values();
    }

    public SubCommand getCommandByName(String name) {
        if (name == null)
            return null;
        return commands.get(name.toLowerCase());
    }

    public SubCommand getCommand(String label) {
        if (label == null)
            return null;
        label = label.toLowerCase();
        String name = aliases.get(label);
        if (name != null)
            return getCommandByName(name);
        else
            return getCommandByName(label);
    }

    public BaseCommand clearCommands() {
        commands.clear();
        aliases.clear();
        objectCommandMap.clear();

        return this;
    }

    private void registerPermission(Permission permission) {
        if (permission != null)
            try {
                pm.addPermission(permission);
            } catch (IllegalArgumentException e) {
            }
    }

    @Command(name = "help")
    public void helpCommand(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            String commandName = args.getString(0);
            SubCommand command = getCommand(commandName);
            if (command != null) {
                sender.sendMessage("/" + args.getBaseLabel() + " " + args.getString(0) + " " + command.getUsage());
                sender.sendMessage("    " + _(command.getName() + "_description"));
            } else
                sender.sendMessage(_("help_cmdnotfound", args.getString(0)));
        } else {
            sender.sendMessage(_("help_listofcommands"));
            sender.sendMessage(" ");

            List<SubCommand> subCommands = new ArrayList<SubCommand>(args.getBaseCommand().getAllSubCommands());
            Collections.sort(subCommands, SubCommand.COMPARATOR);

            for (SubCommand command : subCommands) {
                if (command.getPermission() != null && !sender.hasPermission(command.getPermission()))
                    continue;
                sender.sendMessage("/" + args.getBaseLabel() + " " + command.getName() + " " + command.getUsage());
                sender.sendMessage("    " + _(command.getName() + "_description"));
                sender.sendMessage(" ");
            }
        }
    }

    @Command(name = "version")
    private void versionCommand(CommandSender sender, CommandArgs args) {
        sender.sendMessage(_("version_pluginversion", plugin.getDescription().getVersion()));
        sender.sendMessage(" ");
    }

    @Command(name = "reload")
    @RequiresPermission
    private void reloadCommand(CommandSender sender, CommandArgs args) {
        pm.disablePlugin(plugin);
        pm.enablePlugin(plugin);
        sender.sendMessage(_("reload_completed"));
    }

    @Command(name = "language", usage = "[language]")
    @RequiresPermission
    private void languageCommand(CommandSender sender, CommandArgs args) {
        if (args.size() > 0) {
            String language = args.getString(0);
            Translation tranlation = Translation.get(plugin.getClass(), language);
            if (tranlation != null) {
                plugin.setTranslation(tranlation);
                plugin.getConfig().set("language", language);
                plugin.saveConfig();
                sender.sendMessage(_("language_changed", _("language_" + tranlation.getLanguage())));
            } else
                sender.sendMessage(_("language_failed", language));
        } else
            sender.sendMessage(_("language_current", _("language_" + plugin.getTranslation().getLanguage())));
    }

    // @Command
    @RequiresPermission("libminecraft.test")
    private void test(CommandSender sender, CommandArgs args) {
        sender.sendMessage("Params:");

        for (String param : args.getParams())
            sender.sendMessage(" - '" + param + "'");
        sender.sendMessage(" ");

        sender.sendMessage("Flags:");
        for (String flag : args.getFlags())
            sender.sendMessage(" - '" + flag + "'");
    }
}
