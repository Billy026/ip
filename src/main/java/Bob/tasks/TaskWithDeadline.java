package bob.tasks;

import java.time.LocalDateTime;

import bob.managers.DateManager;

/**
 * Represents a task with a due date.
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
     * Propogates isSameDay to DateManager.
     * 
     * @param date date to compare with.
     * @param withTime whether time should be considered.
     * @return if task is due the same day as date.
     */
    public boolean isSameDay(LocalDateTime date, boolean withTime) {
        return DateManager.isSameDay(this.deadline, date, withTime);
    }

    @Override
    public String toString() {
        return super.toString() + "," + this.deadline;
    }
}