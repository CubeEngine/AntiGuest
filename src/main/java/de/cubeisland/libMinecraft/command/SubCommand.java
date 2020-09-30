package de.cubeisland.libMinecraft.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * This represents an sub command. You usually don't use this class directly.
 *
 * @author Phillip Schichtel
 */
public class SubCommand {
    public static final Comparator<SubCommand> COMPARATOR = new SubCommandComparator();
    private final Object commandContainer;
    private final Method method;

    private final String name;
    private final String[] aliases;
    private final Permission permission;
    private final boolean addPermissionParent;
    private final String usage;

    protected SubCommand(Object commandContainer, Method method, String name, String[] aliases, Permission permission, boolean addPermissionParent, String usage) {
        if (commandContainer == null)
            throw new IllegalArgumentException("The command container must not be null!");
        if (method == null)
            throw new IllegalArgumentException("The method must not be null!");
        if (name == null)
            throw new IllegalArgumentException("The name must not be null!");
        if (aliases == null)
            throw new IllegalArgumentException("The aliases must not be null!");

        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 2 || paramTypes[0] != CommandSender.class || paramTypes[1] != CommandArgs.class)
            throw new IllegalArgumentException("The methods signature is invalid!");

        this.commandContainer = commandContainer;
        this.method = method;
        this.method.setAccessible(true);

        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.addPermissionParent = addPermissionParent;
        this.usage = usage;
    }

    public boolean execute(CommandSender sender, CommandArgs args) throws Throwable {
        try {
            Object result = method.invoke(commandContainer, sender, args);
            if (result instanceof Boolean)
                return ((Boolean) result).booleanValue();
            return true;
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof CommandException)
                throw (CommandException) t;
            else
                throw t;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Permission getPermission() {
        return permission;
    }

    public boolean addPermissionParent() {
        return addPermissionParent;
    }

    public String getUsage() {
        return usage;
    }

    private static final class SubCommandComparator implements Comparator<SubCommand> {
        @Override
        public int compare(SubCommand o1, SubCommand o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    @Override
    public String toString() {
        return "SubCommand(name=" + name + ",aliases=" + aliases + ")";
    }
}
