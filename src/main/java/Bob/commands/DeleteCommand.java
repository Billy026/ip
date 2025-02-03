package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.ConversionManager;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

public class DeleteCommand extends Command {
    public DeleteCommand(String[] inputs) {
        super(inputs);
    }

    public void exec(TaskManager taskManager) throws InvalidCommandException {
        // Convert task number to int
        if (inputs.length == 1) {
            throw new InvalidCommandException("Please indicate which task to delete.");
        }
        int index = ConversionManager.convertInputToIndex(
                inputs[1], "Please provide a valid task number.");
        
        if (taskManager.getSize() < index) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        // Delete task
        Task task = taskManager.getTask(index - 1);
        taskManager.deleteTask(index - 1);

        System.out.println(
                "    Alright. I've removed this task:\n" +
                "      " + task.toString() + "\n" + 
                "    Now you have " + taskManager.getSize() + " task" +
                ((taskManager.getSize() == 1) ? "" : "s") + " in the list.");
    }
}
