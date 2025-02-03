package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;

public class ListCommand extends Command {
    public ListCommand(String[] inputs) {
        super(inputs);
    }

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
