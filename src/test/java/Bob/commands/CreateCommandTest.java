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
import bob.managers.TaskManager;

public class CreateCommandTest {
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
    public void exec_validInput_correctOutput() {
        CreateCommand todoCmd = new CreateCommand(new String[] {"todo", "read"}, "T",
                "Exception thrown.");

        try {
            String actualOutput = todoCmd.exec(taskManager);

            String expectedOutput = "Sure. I've added this task:\n"
                    + "[ ] | T | read\n"
                    + "Now you have 1 task in the list.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        CreateCommand deadlineCmd = new CreateCommand(
                new String[] {"deadline", "assignment", "/by", "01/01/2025", "10:30"},
                "D", "Exception thrown.");

        try {
            String actualOutput = deadlineCmd.exec(taskManager);

            String expectedOutput = "Sure. I've added this task:\n"
                    + "[ ] | D | assignment | by: 01/01/2025 10:30\n"
                    + "Now you have 2 tasks in the list.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        CreateCommand eventCmd = new CreateCommand(
                new String[] {"event", "meeting", "/from", "01/01/2025", "10:00", "/to", "01/01/2025", "11:00"},
                "E", "Exception thrown.");

        try {
            String actualOutput = eventCmd.exec(taskManager);

            String expectedOutput = "Sure. I've added this task:\n"
                    + "[ ] | E | meeting | from: 01/01/2025 10:00 | to: 01/01/2025 11:00\n"
                    + "Now you have 3 tasks in the list.\n";

            assertEquals(actualOutput, expectedOutput);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void exec_insufficientParametersGiven_exceptionThrown() {
        CreateCommand todoCmd = new CreateCommand(new String[] {"todo"}, "T",
                "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> todoCmd.exec(taskManager),
            "Exception should have been thrown."
        );

        CreateCommand deadlineCmd = new CreateCommand(
            new String[] {"deadline", "assignment", "/by"},
            "D", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> deadlineCmd.exec(taskManager),
            "Exception should have been thrown."
        );

        CreateCommand eventCmd = new CreateCommand(
            new String[] {"event", "meeting", "/from", "01/01/2025", "10:00", "/by", "01/01/2025", "11:00"},
            "E", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> eventCmd.exec(taskManager),
            "Exception should have been thrown."
        );
    }

    @Test
    public void exec_invalidDatesGiven_exceptionThrown() {
        CreateCommand alphabeticDeadline = new CreateCommand(
            new String[] {"deadline", "assignment", "/by", "a"},
            "D", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> alphabeticDeadline.exec(taskManager),
            "Exception should have been thrown."
        );

        CreateCommand invalidDate = new CreateCommand(
            new String[] {"deadline", "assignment", "/by", "80/80/2080", "00:00"},
            "D", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> invalidDate.exec(taskManager),
            "Exception should have been thrown."
        );

        CreateCommand eventWithoutStart = new CreateCommand(
            new String[] {"event", "meeting", "/from", "/to", "01/01/2025", "11:00"},
            "E", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> eventWithoutStart.exec(taskManager),
            "Exception should have been thrown."
        );

        CreateCommand eventWithoutEnd = new CreateCommand(
            new String[] {"event", "meeting", "/from", "01/01/2025", "10:00", "/to"},
            "E", "Exception thrown.");

        assertThrows(
            InvalidCommandException.class,
            () -> eventWithoutEnd.exec(taskManager),
            "Exception should have been thrown."
        );
    }
}
