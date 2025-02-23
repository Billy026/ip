package bob.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bob.tasks.Task;
import bob.tasks.ToDo;

public class StorageTest {
    private Storage storage;

    @BeforeEach
    public void setUp() {
        this.storage = new Storage(Paths.get("test_data", "test_tasks.txt").toString());
    }

    @AfterEach
    public void tearDown() {
        File file = new File(Paths.get("test_data", "test_tasks.txt").toString());
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void saveTask_validTask_taskIsSaved() throws IOException {
        ToDo todo = new ToDo("Test Task");

        this.storage.saveTask(todo);

        File file = new File("test_data/test_tasks.txt");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertEquals("[ ] | T | Test Task", reader.readLine());
        }
    }

    @Test
    public void loadTasks_existingFile_tasksAreLoaded() throws IOException {
        List<Task> loadedTasks = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test_data/test_tasks.txt"))) {
            writer.write("[ ] | T | Task 1");
            writer.newLine();
            writer.write("[ ] | T | Task 2");
            writer.newLine();
        }

        this.storage.loadTasks(loadedTasks::add);

        assertEquals(2, loadedTasks.size());
        assertEquals("[ ] | T | Task 1", loadedTasks.get(0).toString());
        assertEquals("[ ] | T | Task 2", loadedTasks.get(1).toString());
    }

    @Test
    public void loadTasks_noFile_printsNoSavedTasks() {
        File file = new File("test_data/test_tasks.txt");
        if (file.exists()) {
            file.delete();
        }

        List<Task> loadedTasks = new ArrayList<>();
        this.storage.loadTasks(loadedTasks::add);

        assertTrue(loadedTasks.isEmpty());
    }

    @Test
    public void rewriteTaskList_validTasks_fileContainsCorrectData() throws IOException {
        List<Task> tasks = List.of(new ToDo("Task A"), new ToDo("Task B"));

        this.storage.rewriteTaskList(tasks);

        File file = new File("test_data/test_tasks.txt");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertEquals("[ ] | T | Task A", reader.readLine());
            assertEquals("[ ] | T | Task B", reader.readLine());
        }
    }

    @Test
    public void saveTask_handlesIoException() {
        String invalidPath = "/invalid/path/test_data/test_tasks.txt";
        Storage faultyStorage = new Storage(invalidPath);
        ToDo todo = new ToDo("Invalid Task");

        assertDoesNotThrow(() -> faultyStorage.saveTask(todo));
    }
}
