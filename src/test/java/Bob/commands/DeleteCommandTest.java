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

public class DeleteCommandTest {
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
    public void exec_validTaskIndex_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"read"});
            DeleteCommand cmd = new DeleteCommand(new String[] {"delete", "1"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "Alright. I've removed this task:\n" +
                "[ ] | T | read\n" + 
                "Now you have 0 task in the list.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_invalidTaskIndex_exceptionThrown() {
        DeleteCommand cmd = new DeleteCommand(new String[] {"delete", "a"});

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }

    @Test
    public void exec_taskIndexOutOfBounds_exceptionThrown() {
        DeleteCommand cmd = new DeleteCommand(new String[] {"delete", "1"});

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }

    @Test
    public void exec_noTaskIndexGiven_exceptionThrown() {
        DeleteCommand cmd = new DeleteCommand(new String[] {"delete"});

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }
}
