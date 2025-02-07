package bob.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeadlineTest {
    @Test
    public void toString_defaultConstructor_correctOutput() {
        Deadline deadline = new Deadline("new task", "31/01/2025");
        assertEquals(deadline.toString(), "[ ] | D | new task | by: 31/01/2025");
    }

    @Test
    public void toString_isCompleted_correctOutput() {
        Deadline deadline = new Deadline("completed task", "31/01/2025", true);
        assertEquals(deadline.toString(), "[X] | D | completed task | by: 31/01/2025");
    }

    @Test
    public void toString_isNotCompleted_correctOutput() {
        Deadline deadline = new Deadline("incomplete task", "31/01/2025", false);
        assertEquals(deadline.toString(), "[ ] | D | incomplete task | by: 31/01/2025");
    }
}
