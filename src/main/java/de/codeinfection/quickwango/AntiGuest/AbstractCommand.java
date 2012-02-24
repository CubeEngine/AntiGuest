package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.command.CommandSender;

/**
 *
 * @author CodeInfection
 */
public abstract class AbstractCommand
{
    private final String label;
    private final BaseCommand base;

    public AbstractCommand(String label, BaseCommand base)
    {
        this.label = label;
        this.base = base;
    }

    public final BaseCommand getBase()
    {
        return this.base;
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
