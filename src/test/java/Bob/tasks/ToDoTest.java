package bob.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ToDoTest {
    @Test
    public void toString_defaultConstructor_correctOutput() {
        ToDo todo = new ToDo("new task");
        assertEquals(todo.toString(), "[ ] | T | new task");
    }

    @Test
    public void toString_isCompleted_correctOutput() {
        ToDo todo = new ToDo("completed task", true);
        assertEquals(todo.toString(), "[X] | T | completed task");
    }

    @Test
    public void toString_isNotCompleted_correctOutput() {
        ToDo todo = new ToDo("incomplete task", false);
        assertEquals(todo.toString(), "[ ] | T | incomplete task");
    }
}
