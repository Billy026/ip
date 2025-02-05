package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;

/**
 * User command to list all tasks in the list.
 */
public class ListCommand extends Command {
    /**
     * Primary constructor of ListCommand.
     * 
     * @param inputs user commanded separated by spaces.
     */
    public ListCommand(String[] inputs) {
        super(inputs);
    }

    /**
     * Lists all tasks currently in the list.
     * If no tasks are found, indicates this to the user.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return list of tasks.
     * @throws InvalidCommandException not possible, as there are no possible invalid inputs here.
     */
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        if (taskManager.getSize() != 0) {
            StringBuffer buffer = new StringBuffer();

            buffer.append("Here are the tasks in your list:\n");
            for (int i = 1; i <= taskManager.getSize(); i++) {
                buffer.append(i + ". " + taskManager.getTask(i - 1).toString() + "\n");
            }

            return buffer.toString();
        } else {
            return "There are currently no tasks in your list.\n";
        }
    }
}
