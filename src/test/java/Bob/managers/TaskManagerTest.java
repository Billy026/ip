package bob.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidTaskOperationException;
import bob.tasks.Task;
import javafx.util.Pair;

public class TaskManagerTest {
    private static final String shortDateFormat = "dd/MM/yyyy HH:mm";

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
    public void addTask_validTask_taskAddedSuccessfully() {
        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            this.taskManager.addTask("D", new String[]{"deadline", "31/10/2025"});
            this.taskManager.addTask("E", new String[]{"event", "31/10/2025", "31/10/2025"});

            assertEquals(this.taskManager.getTask(0).toString(), "[ ] | T | todo");
            assertEquals(this.taskManager.getTask(1).toString(),
                    "[ ] | D | deadline | by: 31/10/2025");
            assertEquals(this.taskManager.getTask(2).toString(),
                    "[ ] | E | event | from: 31/10/2025 | to: 31/10/2025");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void addTask_taskAlreadyExists_exceptionThrown() {
        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            assertEquals(this.taskManager.getTask(0).toString(), "[ ] | T | todo");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertThrows(
            InvalidTaskOperationException.class,
            () -> this.taskManager.addTask("T", new String[]{"todo"}),
            "Exception should have been thrown."
        );

        try {
            this.taskManager.markTask(0, true);
            assertEquals(this.taskManager.getTask(0).toString(), "[X] | T | todo");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertThrows(
            InvalidTaskOperationException.class,
            () -> this.taskManager.addTask("T", new String[]{"todo"}),
            "Exception should have been thrown."
        );
    }

    @Test
    public void addTask_invalidTask_exceptionThrown() {
        assertThrows(
            InvalidTaskOperationException.class,
            () -> this.taskManager.addTask("J", new String[]{"junit"}),
            "Exception should have been thrown."
        );
    }

    @Test
    public void deleteTask_validIndex_taskDeletedSuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertEquals(this.taskManager.getSize(), 1);

        this.taskManager.deleteTask(0);
        assertEquals(this.taskManager.getSize(), 0);
    }

    @Test
    public void deleteTask_invalidIndex_exceptionThrown() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        assertThrows(
            IndexOutOfBoundsException.class,
            () -> this.taskManager.deleteTask(0),
            "Exception should have been thrown."
        );
    }

    @Test
    public void markTask_markIncompleteTask_taskMarkedSuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertEquals(this.taskManager.getSize(), 1);

        try {
            this.taskManager.markTask(0, true);
            assertEquals(this.taskManager.getTask(0).toString(), "[X] | T | todo");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void markTask_unmarkCompletedTask_taskUnmarkedSuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertEquals(this.taskManager.getSize(), 1);

        try {
            this.taskManager.markTask(0, true);
            assertEquals(this.taskManager.getTask(0).toString(), "[X] | T | todo");
            this.taskManager.markTask(0, false);
            assertEquals(this.taskManager.getTask(0).toString(), "[ ] | T | todo");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void markTask_markCompletedTask_taskMarkedUnsuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            assertEquals(this.taskManager.getSize(), 1);
            this.taskManager.markTask(0, true);
            assertEquals(this.taskManager.getTask(0).toString(), "[X] | T | todo");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertThrows(
            InvalidTaskOperationException.class,
            () -> this.taskManager.markTask(0, true),
            "Exception should have been thrown."
        );
    }

    @Test
    public void markTask_unmarkIncompleteTask_taskUnmarkedUnsuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertEquals(this.taskManager.getSize(), 1);

        assertThrows(
            InvalidTaskOperationException.class,
            () -> this.taskManager.markTask(0, false),
            "Exception should have been thrown."
        );
    }

    @Test
    public void getMatchingTasks_containsMatchingTasks_correctOutput() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            this.taskManager.addTask("D", new String[]{"hello", "10/10/2025"});
            this.taskManager.addTask("E", new String[]{"toddler", "10/10/2025", "10/10/2025"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        List<Task> matchingTasks = this.taskManager.getMatchingTasks("tod");
        assertEquals(matchingTasks.size(), 2);
        assertEquals(matchingTasks.get(0).toString(), "[ ] | T | todo");
        assertEquals(matchingTasks.get(1).toString(),
                "[ ] | E | toddler | from: 10/10/2025 | to: 10/10/2025");
    }

    @Test
    public void getMatchingTasks_noMatchingTasks_correctOutput() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            this.taskManager.addTask("D", new String[]{"hello", "10/10/2025"});
            this.taskManager.addTask("E", new String[]{"toddler", "10/10/2025", "10/10/2025"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        List<Task> matchingTasks = this.taskManager.getMatchingTasks("supermegacalifricious");
        assertEquals(matchingTasks.size(), 0);
    }

    @Test
    public void displayIncomingDeadlines_bothIncomingAndNonIncoming_correctOutput() {
        // Set up tasks
        String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(shortDateFormat));
        String nextDate = LocalDateTime.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern(shortDateFormat));

        try {
            this.taskManager.addTask("D", new String[]{"deadline", currDate});
            this.taskManager.addTask("D", new String[]{"other deadline", nextDate});
            this.taskManager.addTask("E", new String[]{"event", currDate, nextDate});

            String actualOutput = this.taskManager.displayIncomingDeadlines();

            String expectedOutput = "    Here's today's incoming tasks:\n"
                    + "[ ] | D | deadline | by: " + currDate + "\n"
                    + "[ ] | E | event | from: " + currDate + " | to: " + nextDate + "\n";

            assertEquals(
                expectedOutput.trim().replace("\r\n", "\n"),
                actualOutput.toString().trim().replace("\r\n", "\n"));
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void displayIncomingDeadlines_noIncomingDeadlines_correctOutput() {
        // Set up tasks
        String nextDate = LocalDateTime.now().plusDays(1)
                .format(DateTimeFormatter.ofPattern(shortDateFormat));

        try {
            this.taskManager.addTask("D", new String[]{"other deadline", nextDate});

            String actualOutput = this.taskManager.displayIncomingDeadlines();

            String expectedOutput = "You...don't have any incoming tasks today.\n";

            assertEquals(
                expectedOutput.trim().replace("\r\n", "\n"),
                actualOutput.toString().trim().replace("\r\n", "\n"));
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void displaySameDeadines_hasSameDeadlinesWithTime_correctOutput() {
        String date1 = "20/02/2025 10:30";
        String date2 = "21/02/2025 09:30";

        try {
            this.taskManager.addTask("D", new String[]{"deadline", date1});
            this.taskManager.addTask("D", new String[]{"other deadline", date2});
            this.taskManager.addTask("E", new String[]{"event", date1, date2});

            LocalDateTime date = LocalDateTime.parse(date1, DateTimeFormatter.ofPattern(shortDateFormat));
            String actualOutput = this.taskManager.displaySameDeadlines(new Pair<>(date, true));

            String expectedOutput = "Here's the tasks due at that date:\n"
                    + "[ ] | D | deadline | by: " + date1 + "\n"
                    + "[ ] | E | event | from: " + date1 + " | to: " + date2 + "\n";

            assertEquals(
                expectedOutput.trim().replace("\r\n", "\n"),
                actualOutput.toString().trim().replace("\r\n", "\n"));
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void displaySameDeadines_hasSameDeadlinesWithoutTime_correctOutput() {
        String date1 = "20/02/2025 10:30";
        String date2 = "20/02/2025 11:30";

        try {
            this.taskManager.addTask("D", new String[]{"deadline", date1});
            this.taskManager.addTask("D", new String[]{"other deadline", date2});
            this.taskManager.addTask("E", new String[]{"event", date1, date2});

            LocalDateTime date = LocalDateTime.parse(date1, DateTimeFormatter.ofPattern(shortDateFormat));
            String actualOutput = this.taskManager.displaySameDeadlines(new Pair<>(date, false));

            String expectedOutput = "Here's the tasks due at that date:\n"
                    + "[ ] | D | deadline | by: " + date1 + "\n"
                    + "[ ] | D | other deadline | by: " + date2 + "\n"
                    + "[ ] | E | event | from: " + date1 + " | to: " + date2 + "\n";

            assertEquals(
                expectedOutput.trim().replace("\r\n", "\n"),
                actualOutput.toString().trim().replace("\r\n", "\n"));
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void displaySameDeadines_noSameDealines_correctOutput() {
        String date1 = "20/02/2025 10:30";
        String date2 = "21/02/2025 11:30";

        String notMatchingDate = "01/01/2024 00:00";

        try {
            this.taskManager.addTask("D", new String[]{"deadline", date1});
            this.taskManager.addTask("D", new String[]{"other deadline", date2});
            this.taskManager.addTask("E", new String[]{"event", date1, date2});

            LocalDateTime date = LocalDateTime.parse(notMatchingDate,
                    DateTimeFormatter.ofPattern(shortDateFormat));
            String actualOutput = this.taskManager.displaySameDeadlines(new Pair<>(date, false));

            String expectedOutput = "You...don't have any tasks due that day!\n";

            assertEquals(
                expectedOutput.trim().replace("\r\n", "\n"),
                actualOutput.toString().trim().replace("\r\n", "\n"));
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void getSavedListMessage_hasSavedTasks_correctOutput() {
        try {
            this.taskManager.addTask("T", new String[]{"todo"});
            assertEquals(this.taskManager.getSavedListMessage(),
                    "Huh, seems like you already have a saved task list.");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void getSavedListMessage_noSavedTasks_correctOutput() {
        assertEquals(this.taskManager.getSavedListMessage(), "There's...no tasks right now.");
    }
}
