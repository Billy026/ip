package Bob.commands;

import java.util.List;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

/**
 * User command to find tasks containing a string in their task names.
 */
public class FindCommand extends Command {
    /**
     * Primary constructor of FindCommand.
     * 
     * @param inputs user commanded separated by spaces.
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
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        StringBuffer buffer = new StringBuffer();

        if (this.inputs.length == 1) {
            throw new InvalidCommandException("Please provide a task name.");
        }

        // Creates string from user input
        buffer.append(this.inputs[1]);
        for (int i = 2; i < this.inputs.length; i++) {
            buffer.append(" ");
            buffer.append(this.inputs[i]);
        }

        String stringToContain = buffer.toString();
        List<Task> matchingTasks = taskManager.getMatchingTasks(stringToContain);

        // Appending matching tasks to StringBuffer
        if (!matchingTasks.isEmpty()) {
            StringBuffer outputBuffer = new StringBuffer();
            
            outputBuffer.append("Here are the matching tasks in your list:\n");
            for (int i = 1; i <= matchingTasks.size(); i++) {
                outputBuffer.append(i + ". " + matchingTasks.get(i - 1).toString() + "\n");
            }

            return outputBuffer.toString();
        } else {
            return "No matching tasks found.\n";
        }
    }
}
