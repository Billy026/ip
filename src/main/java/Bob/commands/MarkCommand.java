package bob.commands;

import bob.exceptions.InvalidCommandException;
import bob.managers.ConversionManager;
import bob.managers.TaskManager;
import bob.tasks.Task;

/**
 * User command to mark or unmark a task.
 * 
 * @param isMark whether the task should be marked or unmarked.
 */
public class MarkCommand extends Command {
    private boolean isMark;

    /**
     * Primary constructor of MarkCommand.
     * 
     * @param inputs user commanded separated by spaces.
     * @param isMark whether the task should be marked or unmarked.
     */
    public MarkCommand(String[] inputs, boolean isMark) {
        super(inputs);
        this.isMark = isMark;
    }

    /**
     * Marks or unmarks the task as given by inputs.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return marked/unmarked task.
     * @throws InvalidCommandException if invalid task number given.
     */
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        // Get and ensure valid task number inputted
        if (this.inputs.length == 1) {
            throw new InvalidCommandException("Please tell me which task to delete.");
        }
        int index = ConversionManager.convertInputToIndex(this.inputs[1],
                "Please tell me which task to " + ((this.isMark) ? "" : "un") + "mark.");

        if (taskManager.getSize() < index) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        Task task = taskManager.markTask(index - 1, this.isMark);

        if (this.isMark) {
            return "Nice! I've marked this task as done:\n" +
                    task.toString() + "\n";
        } else {
            return "Oh, I guess it's not done yet:\n" +
                    task.toString() + "\n";
        }
    }
}
