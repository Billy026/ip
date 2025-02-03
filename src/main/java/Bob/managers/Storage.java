package Bob.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.List;

import Bob.tasks.Task;

/**
 * Handles storing and loading of tasks into hard disk.
 * 
 * @param filePath path of file to save to.
 */
public class Storage {
    // File path for saving tasks
    private String filePath;

    /**
     * Primary constructor of Storage.
     * 
     * @param filePath path of file to save to.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves a task to data file.
     * 
     * @param newTask task to save.
     */
    public void saveTask(Task newTask) {
        File file = new File(this.filePath);
        file.getParentFile().mkdirs(); // Ensures parent directory exists

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(newTask.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("    There was a problem saving the task: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from data file into task list.
     * 
     * @param saveToTaskList task adding function in TaskManager.
     */
    public void loadTasks(Consumer<Task> saveToTaskList) {
        File file = new File(this.filePath);
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    saveToTaskList.accept(Task.getFromSaveFormat(line));
                } catch (Exception e) { // Handle corrupted task
                    System.err.println("    There was a problem loading the task: " + e.getMessage());
                }
            }
            System.out.println("    Saved task list found.");
        } catch (FileNotFoundException e) {
            System.out.println("    No saved task list found.");
        } catch (IOException e) {
            System.err.println("    There was a problem loading the file: " + e.getMessage());
        }
    }

    /**
     * Rewrites the task list to the data file.
     * 
     * @param tasks list of tasks.
     */
    public void rewriteTaskList(List<Task> tasks) {
        File file = new File(this.filePath);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("    There was a problem saving the task: " + e.getMessage());
        }
    }
}
