package de.cubeisland.libMinecraft.command;

/**
 * This exception will be handled by the base command.
 * Its message will be send to the command sender.
 *
 * @author Phillip Schichtel
 */
public class CommandException extends RuntimeException
{
    public CommandException(String message)
    {
        super(message);
    }

    public CommandException(Throwable cause)
    {
        super(cause);
    }

    public CommandException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
