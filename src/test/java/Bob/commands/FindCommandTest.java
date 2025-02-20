package bob.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidTaskOperationException;
import bob.managers.TaskManager;

public class FindCommandTest {
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
    public void exec_hasMatchingTask_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"read"});
            taskManager.addTask("T", new String[] {"not"});

            FindCommand cmd = new FindCommand(new String[] {"find", "read"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "Here are the matching tasks in your list:\n" + 
                    "1. [ ] | T | read\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_noMatchingTask_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"not"});
            taskManager.addTask("T", new String[] {"alsoNotMatching"});

            FindCommand cmd = new FindCommand(new String[] {"find", "hello"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "I can't find any matching tasks.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_noStringGiven_exceptionThrown() {
        FindCommand cmd = new FindCommand(new String[] {"find"});

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown"
        );
    }
}
