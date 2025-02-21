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

public class MarkCommandTest {
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
    public void exec_markIncompleteTask_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"read books"});

            MarkCommand markCommand = new MarkCommand(new String[] {"mark", "1"}, true);
            String actualOutput = markCommand.exec(taskManager);

            String expectedOutput = "Nice! I've marked this task as done:\n"
                    + "[X] | T | read books\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_unmarkCompletedTask_correctOutput() {
        try {
            taskManager.addTask("T", new String[] {"read books"});

            MarkCommand markCommand = new MarkCommand(new String[] {"mark", "1"}, true);
            String markedOutput = "Nice! I've marked this task as done:\n"
                    + "[X] | T | read books\n";
            assert markCommand.exec(taskManager).equals(markedOutput);

            MarkCommand unmarkCommand = new MarkCommand(new String[] {"unmark", "1"}, false);
            String actualOutput = unmarkCommand.exec(taskManager);

            String expectedOutput = "Oh, I guess it's not done yet:\n"
                    + "[ ] | T | read books\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_markCompletedTask_exceptionThrown() {
        try {
            taskManager.addTask("T", new String[] {"read books"});

            MarkCommand markCommand = new MarkCommand(new String[] {"mark", "1"}, true);
            String markedOutput = "Nice! I've marked this task as done:\n"
                    + "[X] | T | read books\n";
            assert markCommand.exec(taskManager).equals(markedOutput);

            MarkCommand markAgainCommand = new MarkCommand(new String[] {"mark", "1"}, true);

            assertThrows(
                InvalidCommandException.class,
                () -> markAgainCommand.exec(taskManager),
                "Exception should have been thrown."
            );
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_unmarkIncompleteTask_exceptionThrown() {
        try {
            taskManager.addTask("T", new String[] {"read books"});

            MarkCommand unmarkIncompleteCommand = new MarkCommand(new String[] {"unmark", "1"}, false);

            assertThrows(
                InvalidCommandException.class,
                () -> unmarkIncompleteCommand.exec(taskManager),
                "Exception should have been thrown."
            );
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_invalidTaskIndex_exceptionThrown() {
        MarkCommand cmd = new MarkCommand(new String[] {"mark", "a"}, true);

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }

    @Test
    public void exec_taskIndexOutOfBounds_exceptionThrown() {
        MarkCommand cmd = new MarkCommand(new String[] {"mark", "1"}, true);

        assertThrows(
            InvalidCommandException.class,
            () -> cmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }

    @Test
    public void exec_noTaskIndexGiven_exceptionThrown() {
        MarkCommand markCommand = new MarkCommand(new String[] {"mark"}, true);

        assertThrows(
            InvalidCommandException.class,
            () -> markCommand.exec(taskManager),
            "Exception should have been thrown."
        );

        MarkCommand unmarkCommand = new MarkCommand(new String[] {"unmark"}, false);

        assertThrows(
            InvalidCommandException.class,
            () -> unmarkCommand.exec(taskManager),
            "Exception should have been thrown."
        );
    }
}
