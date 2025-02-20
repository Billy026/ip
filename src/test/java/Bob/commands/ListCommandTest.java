package bob.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidTaskOperationException;
import bob.managers.TaskManager;

public class ListCommandTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("test_data/test_tasks.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void exec_containsTask_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"read books"});

            ListCommand findCommand = new ListCommand(new String[] {"list"});
            String actualOutput = findCommand.exec(taskManager);

            String expectedOutput = "Here are the tasks in your list:\n" + 
                    "1. [ ] | T | read books\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_noTasks_correctOutput() {
        try {
            ListCommand findCommand = new ListCommand(new String[] {"list"});
            String actualOutput = findCommand.exec(taskManager);

            String expectedOutput = "There are no tasks in your list.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }
}
