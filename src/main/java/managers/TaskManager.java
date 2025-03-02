package managers;

import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidTaskOperationException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.TaskWithDeadline;
import tasks.ToDo;

/**
 * Contains list of tasks and operations on it.
 * 
 * @param tasks list of tasks.
 * @param storage stores and loads tasks from hard disk.
 */
public class TaskManager {
    private List<Task> tasks;
    private Storage storage;

    /**
     * Primary constructor.
     */
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.storage = new Storage();
        this.storage.loadTasks((Task t) -> this.tasks.add(t));
    }

    /**
     * Returns size of the list of tasks.
     * 
     * @return size of list of tasks.
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Adds a task based on taskType and params. Also saves task into hard disk.
     * 
     * @param taskType type of task.
     * @param params parameters of task.
     * @return created task.
     * @throws InvalidTaskOperationException if invalid task types given.
     */
    public Task addTask(String taskType, String[] params) throws InvalidTaskOperationException {
        Task task = null;

        if (taskType.equals("T")) {
            task = new ToDo(params[0]);
            this.tasks.add(task);
        } else if (taskType.equals("D")) {
            task = new Deadline(params[0], params[1]);
            this.tasks.add(task);
        } else if (taskType.equals("E")) {
            task = new Event(params[0], params[1], params[2]);
            this.tasks.add(task);
        } else {
            throw new InvalidTaskOperationException(
                "Invalid task type. The valid task types are: T, D, E.");
        }
        
        this.storage.saveTask(task);
        return task;
    }

    /**
     * Returns task at given index.
     * 
     * @param index index of requested task.
     * @return task at index.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Removes task at given index from list of tasks.
     * 
     * @param index index of task to remove.
     */
    public void deleteTask(int index) {
        this.tasks.remove(index);
        this.storage.rewriteTaskList(this.tasks);
    }

    /**
     * Either marks or unmarks a task.
     * When mark == true, mark task. Else unmark task.
     * 
     * @param index index of task to edit.
     * @param mark whether to mark or unmark task.
     * @return edited task.
     * @throws InvalidTaskOperationException if invalid index given.
     */
    public Task markTask(int index, boolean mark) throws InvalidTaskOperationException {
        Task task = this.getTask(index);

        if (mark) {
            task.check();
        } else {
            task.uncheck();
        }

        this.storage.rewriteTaskList(this.tasks);
        return task;
    }

    /**
     * Displays all Deadlines and Events with deadlines due today.
     */
    public void displayIncomingDeadlines() {
        List<Task> deadlineList = new ArrayList<>();
        List<Task> eventList = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isTaskType("D")) {
                TaskWithDeadline incomingTask = (TaskWithDeadline) task;
                if (incomingTask.isIncoming()) {
                    deadlineList.add(incomingTask);
                }
            } else if (task.isTaskType("E")) {
                TaskWithDeadline incomingTask = (TaskWithDeadline) task;
                if (incomingTask.isIncoming()) {
                    eventList.add(incomingTask);
                }
            }
        }

        if (!deadlineList.isEmpty() || !eventList.isEmpty()) {
            System.out.println("    Today's incoming tasks:");

            for (Task task : deadlineList) {
                System.out.println("    " + task.toString());
            }
    
            for (Task task : eventList) {
                System.out.println("    " + task.toString());
            }
        } else {
            System.out.println("    You have no incoming tasks today.");
        }
    }
}