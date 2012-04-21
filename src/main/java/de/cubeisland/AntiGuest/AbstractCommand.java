package de.cubeisland.AntiGuest;

import static de.cubeisland.AntiGuest.AntiGuest._;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * This class represents a command that can be attached to a BaseCommand
 *
 * @author Phillip Schichtel
 */
public abstract class AbstractCommand
{
    private final String label;
    private final BaseCommand base;
    private final Permission permission;

    /**
     * Initializes this command with its name and base command
     *
     * @param label the name/label
     * @param base the base command
     */
    public AbstractCommand(String label, BaseCommand base)
    {
        this.label = label;
        this.base = base;
        this.permission = new Permission(base.permissinBase + label, PermissionDefault.OP);
    }

    /**
     * Returns the base command
     *
     * @return the base command
     */
    public final BaseCommand getBase()
    {
        return this.base;
    }

    /**
     * Returns the permission needed to run this command
     *
     * @return the permission
     */
    public final Permission getPermission()
    {
        return this.permission;
    }

    /**
     * Returns the label of this command
     *
     * @return the label
     */
    public final String getLabel()
    {
        return this.label;
    }

    /**
     * Returns the usage string of this command
     *
     * @return the usage string
     */
    public String getUsage()
    {
        return "/" + base.getLabel() + " " + this.label;
    }

    /**
     * Returns a short description of this command
     *
     * @return the description
     */
    public String getDescription()
    {
        return _(this.label + "_description");
    }

    /**
     * This method executes this command
     *
     * @param sender a command sender
     * @param args the arguments
     * @return true if the command succeeded
     */
    public abstract boolean execute(CommandSender sender, String[] args);
}
