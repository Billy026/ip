package Bob.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Bob.exceptions.InvalidTaskOperationException;

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");
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

            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void addTask_invalidTask_exceptionThrown() {
        try {
            this.taskManager.addTask("J", new String[]{"junit"});
            fail("Exception should have been thrown.");
        } catch (InvalidTaskOperationException e) {
        } finally {
            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
        }
    }

    @Test
    public void deleteTask_validTask_taskDeletedSuccessfully() {
        this.taskManager = new TaskManager("test_data/test_tasks.txt");

        try {
            this.taskManager.addTask("T", new String[]{"todo"});
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        assertEquals(this.taskManager.getSize(), 1);

        this.taskManager.deleteTask(0);
        assertEquals(this.taskManager.getSize(), 0);

        File file = new File("test_data/test_tasks.txt");
        if (file.exists()) {
            file.delete();
        }
        this.taskManager = new TaskManager("test_data/test_tasks.txt");
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
        } finally {
            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
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
        } finally {
            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
        }
    }

    @Test
    public void markTask_markCompletedTask_taskMarkedUnsuccessfully() {
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
            this.taskManager.markTask(0, true);
            fail("Exception should have been thrown.");
        } catch (InvalidTaskOperationException e) {
        } finally {
            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
        }
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

        try {
            this.taskManager.markTask(0, false);
            fail("Exception should have been thrown.");
        } catch (InvalidTaskOperationException e) {
        } finally {
            File file = new File("test_data/test_tasks.txt");
            if (file.exists()) {
                file.delete();
            }
            this.taskManager = new TaskManager("test_data/test_tasks.txt");
        }
    }
}
