package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.ConversionManager;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

/**
 * User command to delete a task from the list of tasks.
 */
public class DeleteCommand extends Command {
    /**
     * Primary constructor for DeleteCommand.
     * 
     * @param inputs user commanded separated by spaces.
     */
    public DeleteCommand(String[] inputs) {
        super(inputs);
    }

    /**
     * Deletes the task with the given index from the list of tasks.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return deleted task.
     * @throws InvalidCommandException if task index is invalid.
     */
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        // Convert task number to int
        if (this.inputs.length == 1) {
            throw new InvalidCommandException("Please tell me which task to delete.");
        }
        int index = ConversionManager.convertInputToIndex(
                this.inputs[1], "Please give me a valid task number.");
        
        if (taskManager.getSize() < index) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        // Delete task
        Task task = taskManager.getTask(index - 1);
        taskManager.deleteTask(index - 1);

        return "Alright. I've removed this task:\n" +
                task.toString() + "\n" + 
                "Now you have " + taskManager.getSize() + " task" +
                ((taskManager.getSize() == 1) ? "" : "s") + " in the list.";
    }
}
