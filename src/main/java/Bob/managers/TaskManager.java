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
import javafx.util.Pair;

/**
 * Represents the storage and management of the list of tasks.
 * 
 * @param tasks list of tasks.
 * @param storage stores and loads tasks from hard disk.
 */
public class TaskManager {
    private static final String todoShortFormat = "T";
    private static final String deadlineShortFormat = "D";
    private static final String eventShortFormat = "E";

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

        if (taskType.equals(todoShortFormat)) {
            task = new ToDo(taskValues[0]);
            this.tasks.add(task);
        } else if (taskType.equals(deadlineShortFormat)) {
            task = new Deadline(taskValues[0], taskValues[1]);
            this.tasks.add(task);
        } else if (taskType.equals(eventShortFormat)) {
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
     * When mark is true, mark task. Else unmark task.
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
        Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> incomingLists = getIncomingLists();
        System.out.println(incomingLists.getKey());
        return concatenateIncomingTasks(incomingLists);
    }

    /**
     * Returns output depending on whether the list of tasks is empty.
     * 
     * @return Output depending on whether the list of tasks is empty.
     */
    public String getSavedListMessage() {
        if (this.tasks.isEmpty()) {
            return "There's...no tasks right now.";
        } else {
            return "Huh, seems like you already have a saved task list.";
        }
    }

    /**
     * Gets the lists with incoming due dates.
     * 
     * @return list of incoming deadlines and list of incoming events.
     */
    private Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> getIncomingLists() {
        List<TaskWithDeadline> deadlineList = new ArrayList<>();
        List<TaskWithDeadline> eventList = new ArrayList<>();

        Function<TaskWithDeadline, Boolean> isIncomingDeadline =
                (d) -> d.isTaskType(deadlineShortFormat) && d.isIncoming();
        Function<TaskWithDeadline, Boolean> isIncomingEvent = 
                (e) -> e.isTaskType(eventShortFormat) && e.isIncoming();

        for (Task task : this.tasks) {
            if (task.isTaskType(todoShortFormat)) {
                continue;
            }

            TaskWithDeadline incomingTask = (TaskWithDeadline) task;

            if (isIncomingDeadline.apply(incomingTask)) {
                deadlineList.add(incomingTask);
            } else if (isIncomingEvent.apply(incomingTask)) {
                eventList.add(incomingTask);
            }
        }

        return new Pair<List<TaskWithDeadline>, List<TaskWithDeadline>>(deadlineList, eventList);
    }

    /**
     * Concatenates all incoming tasks to produce output message.
     * 
     * @param incomingList pair of lists of incoming tasks
     * @return output with all incoming tasks.
     * If there are no incoming tasks, indicate that there are no incoming tasks.
     */
    private String concatenateIncomingTasks(Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> incomingList) {
        List<TaskWithDeadline> deadlineList = incomingList.getKey();
        List<TaskWithDeadline> eventList = incomingList.getValue();

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
}
