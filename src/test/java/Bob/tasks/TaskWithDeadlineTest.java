package Bob.tasks;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TaskWithDeadlineTest {
    @Test
    public void isIncoming_isSameDay_trueReturned() {
        String currDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Deadline deadlineDueToday = new Deadline("deadline", currDate);
        Event eventDueToday = new Event("event", currDate, currDate);

        assertTrue(deadlineDueToday.isIncoming());
        assertTrue(eventDueToday.isIncoming());
    }

    @Test
    public void isIncoming_isNotSameDay_falseReturned() {
        String nextDate = LocalDate.now().plusDays(1).
                format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Deadline deadlineDueToday = new Deadline("deadline", nextDate);
        Event eventDueToday = new Event("event", nextDate, nextDate);

        assertFalse(deadlineDueToday.isIncoming());
        assertFalse(eventDueToday.isIncoming());
    }
}
