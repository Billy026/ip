package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.ConversionManager;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

public class MarkCommand extends Command {
    private boolean isCheck;

    public MarkCommand(String[] inputs, boolean isCheck) {
        super(inputs);
        this.isCheck = isCheck;
    }

    public void exec(TaskManager taskManager) throws InvalidCommandException {
        if (inputs.length == 0) {
            throw new InvalidCommandException("Please indicate which task to delete.");
        }
        int index = ConversionManager.convertInputToIndex(inputs[1],
                "Please indicate which task to " + ((isCheck) ? "" : "un") + "mark.");

        if (taskManager.getSize() < index) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        Task task = taskManager.markTask(index - 1, isCheck);

        if (isCheck) {
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
