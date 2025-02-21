package bob.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidTaskOperationException;

public class TaskTest {
    @Test
    public void check_incompleteTask_taskMarked() {
        ToDo todo = new ToDo("todo", false);
        Deadline deadline = new Deadline("deadline", "31/01/2025", false);
        Event event = new Event("event", "31/01/2025", "31/01/2025", false);

        assertEquals(todo.toString(), "[ ] | T | todo");
        assertEquals(deadline.toString(), "[ ] | D | deadline | by: 31/01/2025");
        assertEquals(event.toString(), "[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");

        try {
            todo.markTask();
            deadline.markTask();
            event.markTask();

            assertEquals(todo.toString(), "[X] | T | todo");
            assertEquals(deadline.toString(), "[X] | D | deadline | by: 31/01/2025");
            assertEquals(event.toString(), "[X] | E | event | from: 31/01/2025 | to: 31/01/2025");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void check_completedTask_exceptionThrown() {
        ToDo todo = new ToDo("todo", true);
        Deadline deadline = new Deadline("deadline", "31/01/2025", true);
        Event event = new Event("event", "31/01/2025", "31/01/2025", true);

        assertEquals(todo.toString(), "[X] | T | todo");
        assertEquals(deadline.toString(), "[X] | D | deadline | by: 31/01/2025");
        assertEquals(event.toString(), "[X] | E | event | from: 31/01/2025 | to: 31/01/2025");

        assertThrows(
            InvalidTaskOperationException.class,
            () -> todo.markTask(),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidTaskOperationException.class,
            () -> deadline.markTask(),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidTaskOperationException.class,
            () -> event.markTask(),
            "Exception should have been thrown."
        );
    }

    @Test
    public void uncheck_completedTask_taskMarked() {
        ToDo todo = new ToDo("todo", true);
        Deadline deadline = new Deadline("deadline", "31/01/2025", true);
        Event event = new Event("event", "31/01/2025", "31/01/2025", true);

        assertEquals(todo.toString(), "[X] | T | todo");
        assertEquals(deadline.toString(), "[X] | D | deadline | by: 31/01/2025");
        assertEquals(event.toString(), "[X] | E | event | from: 31/01/2025 | to: 31/01/2025");

        try {
            todo.unmarkTask();
            deadline.unmarkTask();
            event.unmarkTask();

            assertEquals(todo.toString(), "[ ] | T | todo");
            assertEquals(deadline.toString(), "[ ] | D | deadline | by: 31/01/2025");
            assertEquals(event.toString(), "[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void uncheck_incompleteTask_exceptionThrown() {
        ToDo todo = new ToDo("todo", false);
        Deadline deadline = new Deadline("deadline", "31/01/2025", false);
        Event event = new Event("event", "31/01/2025", "31/01/2025", false);

        assertEquals(todo.toString(), "[ ] | T | todo");
        assertEquals(deadline.toString(), "[ ] | D | deadline | by: 31/01/2025");
        assertEquals(event.toString(), "[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");

        assertThrows(
            InvalidTaskOperationException.class,
            () -> todo.unmarkTask(),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidTaskOperationException.class,
            () -> deadline.unmarkTask(),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidTaskOperationException.class,
            () -> event.unmarkTask(),
            "Exception should have been thrown."
        );
    }

    @Test
    public void getFromSaveFormat_correctFormat_correctTaskReturned() {
        try {
            Task todo = Task.getFromSaveFormat("[ ] | T | todo");
            Task deadline = Task.getFromSaveFormat("[ ] | D | deadline | by: 31 January 2025");
            Task event = Task.getFromSaveFormat("[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");

            assertTrue(todo.isTaskType("T"));
            assertTrue(deadline.isTaskType("D"));
            assertTrue(event.isTaskType("E"));
            assertEquals(todo.toString(), "[ ] | T | todo");
            assertEquals(deadline.toString(), "[ ] | D | deadline | by: 31 January 2025");
            assertEquals(event.toString(), "[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");
        } catch (IllegalArgumentException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void getFromSaveFormat_incorrectFormat_exceptionThrown() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Task.getFromSaveFormat("[ ] | todo"),
            "Exception should have been thrown."
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> Task.getFromSaveFormat("[ ] | J | junit"),
            "Exception should have been thrown."
        );
    }

    @Test
    public void isTaskType_correctTaskType_trueReturned() {
        ToDo todo = new ToDo("todo");
        Deadline deadline = new Deadline("deadline", "31/01/2025");
        Event event = new Event("event", "31/01/2025", "31/01/2025");

        assertTrue(todo.isTaskType("T"));
        assertTrue(deadline.isTaskType("D"));
        assertTrue(event.isTaskType("E"));
    }

    @Test
    public void isTaskType_incorrectTaskType_falseReturned() {
        ToDo todo = new ToDo("todo");
        Deadline deadline = new Deadline("deadline", "31/01/2025");
        Event event = new Event("event", "31/01/2025", "31/01/2025");

        assertFalse(todo.isTaskType("D"));
        assertFalse(todo.isTaskType("E"));
        assertFalse(deadline.isTaskType("T"));
        assertFalse(deadline.isTaskType("E"));
        assertFalse(event.isTaskType("T"));
        assertFalse(event.isTaskType("D"));
    }

    @Test
    public void contains_containsString_trueReturned() {
        ToDo todo = new ToDo("todo");
        Deadline deadline = new Deadline("deadline", "31/01/2025");
        Event event = new Event("event", "31/01/2025", "31/01/2025");

        assertTrue(todo.contains("tod"));
        assertTrue(deadline.contains("line"));
        assertTrue(event.contains("vent"));
    }

    @Test
    public void contains_doesNotContainString_falseReturned() {
        ToDo todo = new ToDo("todo");
        Deadline deadline = new Deadline("deadline", "31/01/2025");
        Event event = new Event("event", "31/01/2025", "31/01/2025");

        assertFalse(todo.contains("TOD"));
        assertFalse(deadline.contains("Line"));
        assertFalse(event.contains("hi"));
    }
}
