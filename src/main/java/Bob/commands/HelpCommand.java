package bob.commands;

import bob.exceptions.InvalidCommandException;
import bob.managers.TaskManager;

/**
 * User command to list all commands.
 */
public class HelpCommand extends Command {
    private static int maxDashLength = 56;

    /**
     * Primary constructor for HelpCommand.
     *
     * @param inputs user command separated by spaces.
     */
    public HelpCommand(String[] inputs) {
        super(inputs);
    }

    /**
     * Gives the name, format and a description of all avaliable user commands.
     */
    @Override
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Sure, here is what you can do:\n\n");

        buffer.append(createFunctionParagraph(
            "ToDo",
            "todo <task name>",
            "Creates a to do task. A to do task only has a task name, with no deadlines."
        ));

        buffer.append(createFunctionParagraph(
            "Deadline",
            "deadline <task name> /by <due date: e.g. dd/MM/yyyy hh:mm>",
            "Creates a deadline task. A deadline task has a task name and one deadline."
        ));

        buffer.append(createFunctionParagraph(
            "Event",
            "event <task name> /from <start date: e.g. dd/MM/yyyy hh:mm> "
                     + "/to <end date: e.g. dd/MM/yyyy hh:mm>",
            "Creates an event task. An event task has a task name, a start date and an end date."
        ));

        buffer.append(createFunctionParagraph(
            "Delete",
            "delete <task index>",
            "Deletes a task by its index."
        ));

        buffer.append(createFunctionParagraph(
            "Find",
            "find <name>",
            "Lists down all tasks containing <name>."
        ));

        buffer.append(createFunctionParagraph(
            "Get due date",
            "getDueDate <due date>",
            "Lists down all tasks with the specified due date (if inputted due date does not"
                    + " have time, time will not be accounted for when getting matching tasks)."
        ));

        buffer.append(createFunctionParagraph(
            "List",
            "list",
            "Lists all existing tasks."
        ));

        buffer.append(createFunctionParagraph(
            "Mark",
            "mark <task index>",
            "Marks a task at the given index as completed."
        ));

        buffer.append(createFunctionParagraph(
            "Unmark",
            "unmark <task index>",
            "Marks a task at the given index as incomplete."
        ));

        return buffer.toString();
    }

    /**
     * Creates a paragraph with the title, format and description of the command.
     *
     * @param title name of command.
     * @param format format of user input for command.
     * @param description description of command.
     * @return paragraph with the 3 details combined.
     */
    private String createFunctionParagraph(String title, String format, String description) {
        String underline = getUnderline(title, format);

        String formattedTitle = title + "\n" + underline;
        String formattedFormat = format + "\n" + underline;
        return formattedTitle + formattedFormat + description + "\n" + "\n";
    }

    /**
     * Creates an underline that spans the length of the title or format, whichever is longer.
     *
     * @param title name of command.
     * @param format format of user input for command.
     * @return underline.
     */
    private String getUnderline(String title, String format) {
        int length = Math.min(Math.max(title.length(), format.length()), maxDashLength);
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            buffer.append("_");
        }
        buffer.append("\n");

        return buffer.toString();
    }
}
