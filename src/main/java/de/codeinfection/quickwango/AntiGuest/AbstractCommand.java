package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 *
 * @author CodeInfection
 */
public abstract class AbstractCommand
{
    private final String label;
    private final BaseCommand base;
    private final Permission permission;

    public AbstractCommand(String label, BaseCommand base)
    {
        this.label = label;
        this.base = base;
        this.permission = new Permission(base.permissinBase + label, PermissionDefault.OP);
    }

    public final BaseCommand getBase()
    {
        return this.base;
    }

    public final Permission getPermission()
    {
        return this.permission;
    }

    public final String getLabel()
    {
        return this.label;
    }

    public String getUsage()
    {
        return "/" + base.getLabel() + " " + this.label;
    }

    public abstract String getDescription();

    public abstract boolean execute(CommandSender sender, String[] args);
}
