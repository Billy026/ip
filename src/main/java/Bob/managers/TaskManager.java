package bob.managers;

import java.time.LocalDateTime;
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
        } else if (taskType.equals(deadlineShortFormat)) {
            task = new Deadline(taskValues[0], taskValues[1]);
        } else if (taskType.equals(eventShortFormat)) {
            task = new Event(taskValues[0], taskValues[1], taskValues[2]);
        } else {
            throw new InvalidTaskOperationException(
                "You gave the wrong task type. I can only recognise T, D or E.");
        }

        assert task != null : "Task should not be null.";

        checkForExistingTasks(task);
        
        this.tasks.add(task);
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
        assert task != null : "Task should not be null.";

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

        this.tasks.stream()
                  .filter((t) -> t.contains(stringToCheck))
                  .forEach((t) -> matchingTasks.add(t));

        return matchingTasks;
    }

    /**
     * Displays all Deadlines and Events with deadlines due today.
     */
    public String displayIncomingDeadlines() {
        Function<TaskWithDeadline, Boolean> isIncomingDeadline =
                (d) -> d.isTaskType(deadlineShortFormat) && d.isSameDay(LocalDateTime.now(), false);
        Function<TaskWithDeadline, Boolean> isIncomingEvent = 
                (e) -> e.isTaskType(eventShortFormat) && e.isSameDay(LocalDateTime.now(), false);
        
        Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> incomingLists =
                getFilteredLists(isIncomingDeadline, isIncomingEvent);
        return concatenateTasks(incomingLists, "Here's today's incoming tasks:\n",
                "You...don't have any incoming tasks today.\n");
    }

    /**
     * Displays all Deadlines and Events with deadlines the same as the given date.
     * 
     * @param pair pair of date to compare with and whether time should be considered.
     * @return output to be displayed.
     */
    public String displaySameDeadlines(Pair<LocalDateTime, Boolean> pair) {
        Function<TaskWithDeadline, Boolean> isSameDeadline =
                (d) -> d.isTaskType(deadlineShortFormat) && d.isSameDay(pair.getKey(), pair.getValue());
        Function<TaskWithDeadline, Boolean> isSameEvent = 
                (e) -> e.isTaskType(eventShortFormat) && e.isSameDay(pair.getKey(), pair.getValue());
        
        Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> lists =
                getFilteredLists(isSameDeadline, isSameEvent);
        return concatenateTasks(lists, "Here's the tasks due at that date:\n",
                "You...don't have any tasks due that day!");
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
     * Gets the lists with matching due dates.
     * 
     * @param deadlineFunction function to filter deadlines by.
     * @param eventFunction function to filter events by.
     * @return list of matching deadlines and list of matching events.
     */
    private Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> getFilteredLists(
            Function<TaskWithDeadline, Boolean> deadlineFunction,
            Function<TaskWithDeadline, Boolean> eventFunction) {
        List<TaskWithDeadline> deadlineList = new ArrayList<>();
        List<TaskWithDeadline> eventList = new ArrayList<>();

        for (Task task : this.tasks) {
            if (task.isTaskType(todoShortFormat)) {
                continue;
            }

            TaskWithDeadline filteredTask = (TaskWithDeadline) task;

            if (deadlineFunction.apply(filteredTask)) {
                deadlineList.add(filteredTask);
            } else if (eventFunction.apply(filteredTask)) {
                eventList.add(filteredTask);
            }
        }

        return new Pair<List<TaskWithDeadline>, List<TaskWithDeadline>>(deadlineList, eventList);
    }

    /**
     * Concatenates all matching tasks to produce output message.
     * 
     * @param list pair of lists of matching tasks
     * @param headingString first string to display if matching tasks found.
     * @param emptyString string to display if no matching tasks found.
     * @return output with all matching tasks.
     * If there are no matching tasks, indicate that there are no matching tasks.
     */
    private String concatenateTasks(Pair<List<TaskWithDeadline>, List<TaskWithDeadline>> list,
            String headingString, String emptyString) {
        List<TaskWithDeadline> deadlineList = list.getKey();
        List<TaskWithDeadline> eventList = list.getValue();

        if (!deadlineList.isEmpty() || !eventList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(headingString);

            deadlineList.stream()
                        .forEach((t) -> buffer.append(t.toString() + "\n"));
    
            eventList.stream()
                     .forEach((t) -> buffer.append(t.toString() + "\n"));

            return buffer.toString();
        } else {
            return emptyString;
        }
    }

    /**
     * Checks if task already exists in the list of tasks.
     * 
     * @param task task to check.
     * @throws InvalidTaskOperationException if task already exists.
     */
    private void checkForExistingTasks(Task task) throws InvalidTaskOperationException {
        for (Task t : tasks) {
            if (t.toString().substring(2).equals(task.toString().substring(2))) {
                throw new InvalidTaskOperationException("Sorry, that task already exists.");
            }
        }
    }
}
