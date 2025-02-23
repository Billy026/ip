package bob.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidCommandException;
import bob.managers.TaskManager;

public class HelpCommandTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        this.taskManager = new TaskManager(Paths.get("test_data", "test_tasks.txt").toString());
    }

    @AfterEach
    public void cleanUp() {
        File file = new File(Paths.get("test_data", "test_tasks.txt").toString());
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void exec_correctOutput() {
        HelpCommand cmd = new HelpCommand(new String[] {"help"});
        try {
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "Sure, here is what you can do:\n"
                    + "\n"
                    + "ToDo\n"
                    + "________________\n"
                    + "todo <task name>\n"
                    + "________________\n"
                    + "Creates a to do task. A to do task only has a task name, with no deadlines.\n"
                    + "\n"
                    + "Deadline\n"
                    + "__________________________________________________________\n"
                    + "deadline <task name> /by <due date: e.g. dd/MM/yyyy hh:mm>\n"
                    + "__________________________________________________________\n"
                    + "Creates a deadline task. A deadline task has a task name and one deadline.\n"
                    + "\n"
                    + "Event\n"
                    + "___________________________________________________________________________________"
                    + "______________\n"
                    + "event <task name> /from <start date: e.g. dd/MM/yyyy hh:mm> /to <end date: e.g."
                    + " dd/MM/yyyy hh:mm>\n"
                    + "___________________________________________________________________________________"
                    + "______________\n"
                    + "Creates an event task. An event task has a task name, a start date and an end date.\n"
                    + "\n"
                    + "Delete\n"
                    + "___________________\n"
                    + "delete <task index>\n"
                    + "___________________\n"
                    + "Deletes a task by its index.\n"
                    + "\n"
                    + "Find\n"
                    + "___________\n"
                    + "find <name>\n"
                    + "___________\n"
                    + "Lists down all tasks containing <name>.\n"
                    + "\n"
                    + "Get due date\n"
                    + "_____________________\n"
                    + "getDueDate <due date>\n"
                    + "_____________________\n"
                    + "Lists down all tasks with the specified due date (if inputted due date does not have"
                    + " time, time will not be accounted for when getting matching tasks).\n"
                    + "\n"
                    + "List\n"
                    + "____\n"
                    + "list\n"
                    + "____\n"
                    + "Lists all existing tasks.\n"
                    + "\n"
                    + "Mark\n"
                    + "_________________\n"
                    + "mark <task index>\n"
                    + "_________________\n"
                    + "Marks a task at the given index as completed.\n"
                    + "\n"
                    + "Unmark\n"
                    + "___________________\n"
                    + "unmark <task index>\n"
                    + "___________________\n"
                    + "Marks a task at the given index as incomplete.\n"
                    + "\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }
}
