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
     * @throws InvalidCommandException not possible, as there are no possible invalid inputs here.
     */
    public void exec(TaskManager taskManager) throws InvalidCommandException {
        if (taskManager.getSize() != 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 1; i <= taskManager.getSize(); i++) {
                System.out.println("    " + i + ". " + taskManager.getTask(i - 1).toString());
            }
        } else {
            System.out.println("There are currently no tasks in your list.");
        }
    }
}
