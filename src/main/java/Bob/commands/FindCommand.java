package Bob.commands;

import java.util.List;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

public class FindCommand extends Command {
    public FindCommand(String[] inputs) {
        super(inputs);
    }

    public void exec(TaskManager taskManager) throws InvalidCommandException {
        StringBuffer buffer = new StringBuffer();

        if (inputs.length == 1) {
            throw new InvalidCommandException("Please provide a task name.");
        }

        // Creates string from user input
        buffer.append(inputs[1]);
        for (int i = 2; i < inputs.length; i++) {
            buffer.append(" ");
            buffer.append(inputs[i]);
        }

        String taskName = buffer.toString();
        List<Task> matchingTasks = taskManager.getMatchingTasks(taskName);

        // Print matching tasks
        if (!matchingTasks.isEmpty()) {
            System.out.println("    Here are the matching tasks in your list:");
            for (int i = 1; i <= matchingTasks.size(); i++) {
                System.out.println("    " + i + ". " + matchingTasks.get(i - 1).toString());
            }
        } else {
            System.out.println("    No matching tasks found.");
        }
    }
}
