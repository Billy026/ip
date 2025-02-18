package bob.commands;

import bob.exceptions.InvalidCommandException;
import bob.managers.ConversionManager;
import bob.managers.TaskManager;
import bob.tasks.Task;

/**
 * User command to delete a task from the list of tasks.
 */
public class DeleteCommand extends Command {
    /**
     * Primary constructor for DeleteCommand.
     * 
     * @param inputs user command separated by spaces.
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
    @Override
    public String exec(TaskManager taskManager) throws InvalidCommandException {
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
        assert task != null : "task should not be null.";
        taskManager.deleteTask(index - 1);

        return "Alright. I've removed this task:\n" +
                task.toString() + "\n" + 
                "Now you have " + taskManager.getSize() + " task" +
                (taskManager.getSize() == 1 ? "" : "s") + " in the list.";
    }
}
