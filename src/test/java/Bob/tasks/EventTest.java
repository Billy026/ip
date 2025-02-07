package bob.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EventTest {
    @Test
    public void toString_defaultConstructor_correctOutput() {
        Event event = new Event("new task", "31/01/2025", "31/01/2025");
        assertEquals(event.toString(), "[ ] | E | new task | from: 31/01/2025 | to: 31/01/2025");
    }

    @Test
    public void toString_isCompleted_correctOutput() {
        Event event = new Event(
                "completed task", "31/01/2025", "31/01/2025", true);
        assertEquals(event.toString(), "[X] | E | completed task | from: 31/01/2025 | to: 31/01/2025");
    }

    @Test
    public void toString_isNotCompleted_correctOutput() {
        Event event = new Event(
                "incomplete task", "31/01/2025", "31/01/2025", false);
        assertEquals(event.toString(), "[ ] | E | incomplete task | from: 31/01/2025 | to: 31/01/2025");
    }
}
