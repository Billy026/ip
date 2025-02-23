package bob.parser;

import java.util.HashMap;

import bob.commands.CreateCommand;
import bob.commands.DeleteCommand;
import bob.commands.FindCommand;
import bob.commands.GetDueDateCommand;
import bob.commands.HelpCommand;
import bob.commands.ListCommand;
import bob.commands.MarkCommand;
import bob.exceptions.InvalidCommandException;
import bob.managers.TaskManager;

/**
 * Deals with making sense of the user command.
 */
public class Parser {
    private TaskManager taskManager;
    protected enum Actions {
        TODO, DEADLINE, EVENT, DELETE, LIST, GETDUEDATE, FIND, MARK, UNMARK, HELP
    }

    /**
     * Primary constructor of Parser.
     *
     * @param filePath path of file to save to.
     */
    public Parser(String filePath) {
        this.taskManager = new TaskManager(filePath);
    }

    /**
     * Creates and executes the relevant user command.
     *
     * @param input user commanded separated by spaces.
     * @throws InvalidCommandException when an invalid command has been inputted.
     */
    public String parseCommand(String[] input) throws InvalidCommandException {
        Actions command = this.convertToActions(input[0]);

        switch (command) {
        case TODO:
            CreateCommand todoCommand = new CreateCommand(input, "T",
                "Please give a name to the ToDo task.");
            return todoCommand.exec(this.taskManager);
        case DEADLINE:
            CreateCommand deadlineCommand = new CreateCommand(input, "D",
                    "You did not provide a date or time.\n"
                    + "    Please format your input as: deadline <task name> /by <date>.");
            return deadlineCommand.exec(this.taskManager);
        case EVENT:
            CreateCommand eventCommand = new CreateCommand(input, "E",
                    "You did not provide either a start date or an end date.\n"
                    + "    Please format your input as: event <task name> /from <date> /to <date>.");
            return eventCommand.exec(this.taskManager);
        case DELETE:
            DeleteCommand deleteCommand = new DeleteCommand(input);
            return deleteCommand.exec(this.taskManager);
        case LIST:
            ListCommand listCommand = new ListCommand(input);
            return listCommand.exec(this.taskManager);
        case GETDUEDATE:
            GetDueDateCommand getDueDateCommand = new GetDueDateCommand(input);
            return getDueDateCommand.exec(this.taskManager);
        case FIND:
            FindCommand findCommand = new FindCommand(input);
            return findCommand.exec(this.taskManager);
        case MARK:
            MarkCommand markCommand = new MarkCommand(input, true);
            return markCommand.exec(this.taskManager);
        case UNMARK:
            MarkCommand unmarkCommand = new MarkCommand(input, false);
            return unmarkCommand.exec(this.taskManager);
        case HELP:
            HelpCommand helpCommand = new HelpCommand(input);
            return helpCommand.exec(taskManager);
        default:
            return "";
        }
    }

    /**
     * Propogates displayIncomingDeadlines to taskManager.
     *
     * @return output from taskManager's displayIncomingDeadlines.
     */
    public String displayIncomingDeadlines() {
        return this.taskManager.displayIncomingDeadlines();
    }

    /**
     * Propogates getSavedListMessage to taskManager.
     *
     * @return output from taskManager's getSavedListMessage.
     */
    public String getSavedListMessage() {
        return this.taskManager.getSavedListMessage();
    }

    /**
     * Converts the front of user input to a user command.
     *
     * @param str string to convert into user command.
     * @return corresponding user command.
     * @throws InvalidCommandException when an invalid user command is inputted.
     */
    Actions convertToActions(String str) throws InvalidCommandException {
        HashMap<String, Actions> actionMap = new HashMap<>();
        actionMap.put("todo", Actions.TODO);
        actionMap.put("deadline", Actions.DEADLINE);
        actionMap.put("event", Actions.EVENT);
        actionMap.put("delete", Actions.DELETE);
        actionMap.put("getDueDate", Actions.GETDUEDATE);
        actionMap.put("list", Actions.LIST);
        actionMap.put("find", Actions.FIND);
        actionMap.put("mark", Actions.MARK);
        actionMap.put("unmark", Actions.UNMARK);
        actionMap.put("help", Actions.HELP);

        assert actionMap.size() == 10 : "There should be only 10 valid actions.";

        if (actionMap.containsKey(str)) {
            return actionMap.get(str);
        } else {
            throw new InvalidCommandException("I don't understand.");
        }
    }
}
