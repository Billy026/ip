package bob.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import bob.exceptions.InvalidTaskOperationException;
import bob.storage.Storage;
import bob.tasks.Deadline;
import bob.tasks.Event;
import bob.tasks.Task;
import bob.tasks.TaskWithDeadline;
import bob.tasks.ToDo;

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
     * Primary constructor of TaskManager.
     * 
     * @param filePath path of file to save to.
     */
    public TaskManager(String filePath) {
        this.tasks = new ArrayList<>();
        this.storage = new Storage(filePath);
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
     * @param taskValues parameters of task.
     * @return created task.
     * @throws InvalidTaskOperationException if invalid task types given.
     */
    public Task addTask(String taskType, String[] taskValues) throws InvalidTaskOperationException {
        Task task = null;

        if (taskType.equals("T")) {
            task = new ToDo(taskValues[0]);
            this.tasks.add(task);
        } else if (taskType.equals("D")) {
            task = new Deadline(taskValues[0], taskValues[1]);
            this.tasks.add(task);
        } else if (taskType.equals("E")) {
            task = new Event(taskValues[0], taskValues[1], taskValues[2]);
            this.tasks.add(task);
        } else {
            throw new InvalidTaskOperationException(
                "You gave the wrong task type. I can only recognise T, D or E.");
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
    public Task markTask(int index, boolean isCheck) throws InvalidTaskOperationException {
        Task task = this.getTask(index);

        if (isCheck) {
            task.check();
        } else {
            task.uncheck();
        }

        this.storage.rewriteTaskList(this.tasks);
        return task;
    }

    /**
     * Returns a list of tasks containing stringToCheck in their names.
     * 
     * @param stringToCheck string to check for.
     * @return list of matching tasks.
     */
    public List<Task> getMatchingTasks(String stringToCheck) {
        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : this.tasks) {
            if (task.contains(stringToCheck)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    /**
     * Displays all Deadlines and Events with deadlines due today.
     */
    public String displayIncomingDeadlines() {
        List<Task> deadlineList = new ArrayList<>();
        List<Task> eventList = new ArrayList<>();

        Function<TaskWithDeadline, Boolean> isIncomingDeadline =
                (d) -> d.isTaskType("D") && d.isIncoming();
        Function<TaskWithDeadline, Boolean> isIncomingEvent = 
                (e) -> e.isTaskType("E") && e.isIncoming();

        for (Task task : this.tasks) {
            if (task.isTaskType("T")) {
                continue;
            }

            TaskWithDeadline incomingTask = (TaskWithDeadline) task;

            if (isIncomingDeadline.apply(incomingTask)) {
                deadlineList.add(incomingTask);
            } else if (isIncomingEvent.apply(incomingTask)) {
                eventList.add(incomingTask);
            }
        }

        if (!deadlineList.isEmpty() || !eventList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Here's today's incoming tasks:\n");

            for (Task task : deadlineList) {
                buffer.append(task.toString() + "\n");
            }
    
            for (Task task : eventList) {
                buffer.append(task.toString() + "\n");
            }

            return buffer.toString();
        } else {
            return "You...don't have any incoming tasks today.\n";
        }
    }

    public String getSavedListMessage() {
        if (this.tasks.isEmpty()) {
            return "There's...no tasks right now.";
        } else {
            return "Huh, seems like you already have a saved task list.";
        }
    }
}
