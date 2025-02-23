package bob.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidTaskOperationException;
import bob.managers.TaskManager;

public class GetDueDateCommandTest {
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
    public void exec_hasMatchingTaskWithTime_correctOutput() {
        try {
            taskManager.addTask("D", new String[] {"deadline", "01/01/2025 10:30"});
            taskManager.addTask("D", new String[] {"other deadline", "01/01/2025 11:30"});
            taskManager.addTask("E", new String[] {"event", "01/01/2025 10:30", "01/01/2025 11:30"});

            GetDueDateCommand cmd =
                    new GetDueDateCommand(new String[] {"getDueDate", "01/01/2025 10:30"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "Here's the tasks due at that date:\n"
                    + "[ ] | D | deadline | by: 01/01/2025 10:30\n"
                    + "[ ] | E | event | from: 01/01/2025 10:30 | to: 01/01/2025 11:30\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_hasMatchingTaskWithoutTime_correctOutput() {
        try {
            taskManager.addTask("D", new String[] {"deadline", "01/01/2025 10:30"});
            taskManager.addTask("D", new String[] {"other deadline", "01/01/2025 11:30"});
            taskManager.addTask("E", new String[] {"event", "01/01/2025 10:30", "01/01/2025 11:30"});

            GetDueDateCommand cmd =
                    new GetDueDateCommand(new String[] {"getDueDate", "01/01/2025"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "Here's the tasks due at that date:\n"
                    + "[ ] | D | deadline | by: 01/01/2025 10:30\n"
                    + "[ ] | D | other deadline | by: 01/01/2025 11:30\n"
                    + "[ ] | E | event | from: 01/01/2025 10:30 | to: 01/01/2025 11:30\n";

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
            taskManager.addTask("D", new String[] {"deadline", "01/01/2025 10:30"});
            taskManager.addTask("D", new String[] {"other deadline", "01/01/2025 11:30"});
            taskManager.addTask("E", new String[] {"event", "01/01/2025 10:30", "01/01/2025 11:30"});

            GetDueDateCommand cmd =
                    new GetDueDateCommand(new String[] {"getDueDate", "02/01/2025"});
            String actualOutput = cmd.exec(taskManager);

            String expectedOutput = "You...don't have any tasks due that day!";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_invalidDate_exceptionThrown() {
        try {
            taskManager.addTask("D", new String[] {"deadline", "01/01/2025 10:30"});
            taskManager.addTask("D", new String[] {"other deadline", "01/01/2025 11:30"});
            taskManager.addTask("E", new String[] {"event", "01/01/2025 10:30", "01/01/2025 11:30"});

            GetDueDateCommand notADate =
                    new GetDueDateCommand(new String[] {"getDueDate", "a"});

            assertThrows(
                InvalidCommandException.class,
                () -> notADate.exec(taskManager),
                "Exception should have been thrown."
            );

            GetDueDateCommand invalidDate =
                    new GetDueDateCommand(new String[] {"getDueDate", "80/80/2080"});

            assertThrows(
                InvalidCommandException.class,
                () -> invalidDate.exec(taskManager),
                "Exception should have been thrown."
            );
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }
}
