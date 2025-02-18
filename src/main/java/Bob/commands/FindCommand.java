package bob.commands;

import java.util.List;

import bob.exceptions.InvalidCommandException;
import bob.managers.TaskManager;
import bob.tasks.Task;

/**
 * User command to find tasks containing a string in their task names.
 */
public class FindCommand extends Command {
    /**
     * Primary constructor of FindCommand.
     * 
     * @param inputs user command separated by spaces.
     */
    public FindCommand(String[] inputs) {
        super(inputs);
    }

    /**
     * Returns all tasks with inputted string in their task name.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return list of tasks with matching task names.
     * @throws InvalidCommandException when no string is entered.
     */
    @Override
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        String stringToContain = getStringToContain();
        List<Task> matchingTasks = taskManager.getMatchingTasks(stringToContain);
        return getOutput(stringToContain, matchingTasks);
    }

    /**
     * Concatenates the string to check for.
     * 
     * @return string to check for.
     * @throws InvalidCommandException if there is no string input.
     */
    private String getStringToContain() throws InvalidCommandException {
        if (this.inputs.length == 1) {
            throw new InvalidCommandException("Please give me a task name.");
        }
        
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.inputs[1]);
        for (int i = 2; i < this.inputs.length; i++) {
            buffer.append(" ");
            buffer.append(this.inputs[i]);
        }

        return buffer.toString();
    }

    /**
     * Gets all matching tasks.
     * 
     * @param stringToContain string to check for.
     * @param matchingTasks list of matching tasks.
     * @return output that contains all matching tasks.
     * If no matching task is found, indicates there are none.
     */
    private String getOutput(String stringToContain, List<Task> matchingTasks) {
        if (!matchingTasks.isEmpty()) {
            StringBuffer outputBuffer = new StringBuffer();
            
            outputBuffer.append("Here are the matching tasks in your list:\n");
            for (int i = 1; i <= matchingTasks.size(); i++) {
                outputBuffer.append(i + ". " + matchingTasks.get(i - 1).toString() + "\n");
            }

            return outputBuffer.toString();
        } else {
            return "I can't find any matching tasks.\n";
        }
    }
}
