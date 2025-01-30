package Bob.tasks;

import Bob.managers.DateManager;

/**
 * Tasks with a deadline.
 * 
 * @param deadline date to be completed by.
 */
public abstract class TaskWithDeadline extends Task {
    private String deadline;

    /**
     * Constructor for newly added tasks with a deadline.
     * 
     * @param taskName name of task.
     * @param taskType type of task.
     * @param deadline date to be completed by.
     */
    public TaskWithDeadline(String taskName, String taskType, String deadline) {
        super(taskName, taskType);
        this.deadline = deadline;
    }

    /**
     * Constructor for tasks with a deadline loaded from save file.
     * 
     * @param taskName name of task.
     * @param taskType type of task.
     * @param deadline date to be completed by.
     * @param isCompleted completion status of task.
     */
    public TaskWithDeadline(String taskName, String taskType, String deadline, boolean isCompleted) {
        super(taskName, taskType, isCompleted);
        this.deadline = deadline;
    }

    /**
     * Returns whether task is due today.
     * 
     * @return if task is due today.
     */
    public boolean isIncoming() {
        return DateManager.isSameDay(this.deadline);
    }

    @Override
    public String toString() {
        return super.toString() + "," + this.deadline;
    }
}