package Bob.parser;

import java.util.HashMap;

import Bob.commands.CreateCommand;
import Bob.commands.DeleteCommand;
import Bob.commands.FindCommand;
import Bob.commands.ListCommand;
import Bob.commands.MarkCommand;
import Bob.exceptions.InvalidCommandException;
import Bob.managers.TaskManager;

/**
 * Deals with making sense of the user command.
 * 
 * @param taskManager handles operations on the task list.
 * @param Actions list of valid user commands.
 */
public class Parser {
    private TaskManager taskManager;
    // List of valid user commands
    protected enum Actions {
        TODO, DEADLINE, EVENT, DELETE, LIST, FIND, MARK, UNMARK
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
        // Converts user input to a valid command
        Actions command = this.convertToActions(input[0]);

        switch (command) {
            case TODO:
                CreateCommand todoCommand = new CreateCommand(input, "T", 
                    "Please give a name to the ToDo task.");
                return todoCommand.exec(this.taskManager);
            case DEADLINE:
                CreateCommand deadlineCommand = new CreateCommand(input, "D", 
                        "You did not provide a date or time.\n" +
                        "    Please format your input as: deadline <task name> /by <date>.");
                return deadlineCommand.exec(this.taskManager);
            case EVENT:
                CreateCommand eventCommand = new CreateCommand(input, "E", 
                        "You did not provide either a start date or an end date.\n" +
                        "    Please format your input as: event <task name> /from <date> /to <date>.");
                return eventCommand.exec(this.taskManager);
            case DELETE:
                DeleteCommand deleteCommand = new DeleteCommand(input);
                return deleteCommand.exec(this.taskManager);
            case LIST:
                ListCommand listCommand = new ListCommand(input);
                return listCommand.exec(this.taskManager);
            case FIND:
                FindCommand findCommand = new FindCommand(input);
                return findCommand.exec(this.taskManager);
            case MARK:
                MarkCommand markCommand = new MarkCommand(input, true);
                return markCommand.exec(this.taskManager);
            case UNMARK:
                MarkCommand unmarkCommand = new MarkCommand(input, false);
                return unmarkCommand.exec(this.taskManager);
        }

        return ""; // Will not reach
    }

    /**
     * Propogates displayIncomingDeadlines to taskManager.
     */
    public String displayIncomingDeadlines() {
        return this.taskManager.displayIncomingDeadlines();
    }

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
    Actions convertToActions(String str) throws InvalidCommandException{
        HashMap<String, Actions> actionMap = new HashMap<>();
        actionMap.put("todo", Actions.TODO);
        actionMap.put("deadline", Actions.DEADLINE);
        actionMap.put("event", Actions.EVENT);
        actionMap.put("delete", Actions.DELETE);
        actionMap.put("list", Actions.LIST);
        actionMap.put("find", Actions.FIND);
        actionMap.put("mark", Actions.MARK);
        actionMap.put("unmark", Actions.UNMARK);

        if (actionMap.containsKey(str)) {
            return actionMap.get(str);
        } else {
            throw new InvalidCommandException("I don't understand.");
        }
    }
}
