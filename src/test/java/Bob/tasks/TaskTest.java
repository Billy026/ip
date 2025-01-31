package Bob.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import Bob.exceptions.InvalidTaskOperationException;

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
            todo.check();
            deadline.check();
            event.check();

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

        try {
            todo.check();
            fail("Exception should have been thrown.");
        } catch (InvalidTaskOperationException e) {
            try {
                deadline.check();
                fail("Exception should have been thrown.");
            } catch (InvalidTaskOperationException e2) {
                try {
                    event.check();
                    fail("Exception should have been thrown.");
                } catch (InvalidTaskOperationException e3) {}
            }
        }
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
            todo.uncheck();
            deadline.uncheck();
            event.uncheck();

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

        try {
            todo.uncheck();
            fail("Exception should have been thrown.");
        } catch (InvalidTaskOperationException e) {
            try {
                deadline.uncheck();
                fail("Exception should have been thrown.");
            } catch (InvalidTaskOperationException e2) {
                try {
                    event.uncheck();
                    fail("Exception should have been thrown.");
                } catch (InvalidTaskOperationException e3) {}
            }
        }
    }

    @Test
    public void fromSaveFormat_correctFormat_correctTaskReturned() {
        try {
            Task todo = Task.fromSaveFormat("[ ] | T | todo");
            Task deadline = Task.fromSaveFormat("[ ] | D | deadline | by: 31/01/2025");
            Task event = Task.fromSaveFormat("[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");

            assertTrue(todo.isTaskType("T"));
            assertTrue(deadline.isTaskType("D"));
            assertTrue(event.isTaskType("E"));
            assertEquals(todo.toString(), "[ ] | T | todo");
            assertEquals(deadline.toString(), "[ ] | D | deadline | by: 31/01/2025");
            assertEquals(event.toString(), "[ ] | E | event | from: 31/01/2025 | to: 31/01/2025");
        } catch (IllegalArgumentException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void fromSaveFormat_incorrectFormat_exceptionThrown() {
        try {
            Task temp = Task.fromSaveFormat("[ ] | todo");
            fail("Exception should have been thrown.");
        } catch (IllegalArgumentException e) {
            try {
                Task temp2 = Task.fromSaveFormat("[ ] | J | junit");
                fail("Exception should have been thrown.");
            } catch (IllegalArgumentException e2) {}
        }
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
}
