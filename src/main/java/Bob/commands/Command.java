package bob.commands;

import bob.exceptions.InvalidCommandException;
import bob.managers.TaskManager;

/**
 * Valid command inputted by the user.
 * 
 * @param inputs user commanded separated by spaces.
 */
public abstract class Command {
    protected String[] inputs;

    /**
     * Primary constructor of Command.
     * 
     * @param inputs user command separated by spaces.
     */
    public Command(String[] inputs) {
        this.inputs = inputs;
    }

    /**
     * Executes the intended behaviour of the command.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return output of command.
     * @throws InvalidCommandException when some part of the command is invalid.
     */
    public abstract String exec(TaskManager taskManager) throws InvalidCommandException;
}
