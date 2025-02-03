package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.ConversionManager;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

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
     * @throws InvalidCommandException if invalid task number given.
     */
    public void exec(TaskManager taskManager) throws InvalidCommandException {
        // Get and ensure valid task number inputted
        if (this.inputs.length == 1) {
            throw new InvalidCommandException("Please indicate which task to delete.");
        }
        int index = ConversionManager.convertInputToIndex(this.inputs[1],
                "Please indicate which task to " + ((this.isMark) ? "" : "un") + "mark.");

        if (taskManager.getSize() < index) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        Task task = taskManager.markTask(index - 1, this.isMark);

        if (this.isMark) {
            System.out.println(
                "    Nice! I've marked this task as done:\n" +
                "      " + task.toString());
        } else {
            System.out.println(
                    "    Oh, I guess it's not done yet:\n" +
                    "      " + task.toString());
        }
    }
}
