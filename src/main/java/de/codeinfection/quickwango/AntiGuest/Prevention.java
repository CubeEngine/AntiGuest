package de.codeinfection.quickwango.AntiGuest;

/**
 *
 * @author CodeInfection
 */
public class Prevention
{
    public final String name;
    public final String permission;
    public String message;

    public Prevention(final String name, final String message)
    {
        this(name, name, message);
    }

    public Prevention(final String name, final String permission, final String message)
    {
        this.name = name;
        this.permission = "antiguest.preventions." + permission.toLowerCase();
        this.message = message;
    }
}
