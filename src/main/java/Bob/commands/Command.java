package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;

public abstract class Command {
    protected String[] inputs;

    public Command(String[] inputs) {
        this.inputs = inputs;
    }

    public abstract void exec(TaskManager taskManager) throws InvalidCommandException;
}
