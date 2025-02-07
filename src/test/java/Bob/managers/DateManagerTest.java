package bob.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidDateFormatException;

public class DateManagerTest {
    @Test
    public void normaliseDateFormat_weekOfDay_correctOutput() {
        HashMap<String, Integer> dayMap = new HashMap<>();
        dayMap.put("monday", 1);
        dayMap.put("tuesday", 2);
        dayMap.put("wednesday", 3);
        dayMap.put("thursday", 4);
        dayMap.put("friday", 5);
        dayMap.put("saturday", 6);
        dayMap.put("sunday", 7);

        int currInt = dayMap.get(LocalDate.now().getDayOfWeek().name().toLowerCase());
        Function<Integer, String> c = (i) -> {
                    if (i < currInt) {
                        i += 7;
                    }
                    return LocalDate.now().plusDays(i - currInt)
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }; 

        try {
            assertEquals(DateManager.normaliseDateFormat("Monday"), c.apply(1));
            assertEquals(DateManager.normaliseDateFormat("tuesday"), c.apply(2));
            assertEquals(DateManager.normaliseDateFormat("WEDNESDAY"), c.apply(3));
            assertEquals(DateManager.normaliseDateFormat("Thu"), c.apply(4));
            assertEquals(DateManager.normaliseDateFormat("fri"), c.apply(5));
            assertEquals(DateManager.normaliseDateFormat("SAT"), c.apply(6));
            assertEquals(DateManager.normaliseDateFormat("sUn"), c.apply(7));
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_dd_MM_yyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("31/10/2025"), "31/10/2025");
            assertEquals(DateManager.normaliseDateFormat("10/10/2026"), "10/10/2026");
            assertEquals(DateManager.normaliseDateFormat("01/01/2025"), "01/01/2025");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_dd_MMMM_yyyy_correctOutput() {
        try {
            assertEquals(
                    DateManager.normaliseDateFormat("31 January 2025"), "31 January 2025");
            assertEquals(
                    DateManager.normaliseDateFormat("10 october 2026"), "10 October 2026");
            assertEquals(
                    DateManager.normaliseDateFormat("01 nOveMBer 2025"), "01 November 2025");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_d_MM_yy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("8/10/25"), "08/10/2025");
            assertEquals(DateManager.normaliseDateFormat("7/10/98"), "07/10/1998");
            assertEquals(DateManager.normaliseDateFormat("1/01/76"), "01/01/1976");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_d_MM_yyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("8/10/2025"), "08/10/2025");
            assertEquals(DateManager.normaliseDateFormat("7/10/2026"), "07/10/2026");
            assertEquals(DateManager.normaliseDateFormat("1/01/2025"), "01/01/2025");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_d_MMM_yyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("31 JAN 2025"), "31 January 2025");
            assertEquals(DateManager.normaliseDateFormat("10 oct 2026"), "10 October 2026");
            assertEquals(DateManager.normaliseDateFormat("01 nOv 2025"), "01 November 2025");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_yearFirst_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("2025-8-31"), "31/08/2025");
            assertEquals(DateManager.normaliseDateFormat("32 10 10"), "10/10/1932");
            assertEquals(
                DateManager.normaliseDateFormat("1998 October 25"), "25 October 1998");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_monthFirstDate_exceptionThrown() {
        try {
            DateManager.normaliseDateFormat("10/31/2025");
            fail("Exception should have been thrown.");
        } catch (InvalidDateFormatException e) {}
    }

    @Test
    public void normaliseDateFormat_invalidDate_exceptionThrown() {
        try {
            DateManager.normaliseDateFormat("hi");
            fail("Exception should have been thrown.");
        } catch (InvalidDateFormatException e) {
            try {
                DateManager.normaliseDateFormat("31/31/31/31");
                fail("Exception should have been thrown.");
            } catch (InvalidDateFormatException e2) {
                try {
                    DateManager.normaliseDateFormat("31/101/2025");
                    fail("Exception should have been thrown.");
                } catch (InvalidDateFormatException e3) {
                    try {
                        DateManager.normaliseDateFormat("10 hi 2026");
                        fail("Exception should have been thrown.");
                    } catch (InvalidDateFormatException e4) {
                        try {
                            DateManager.normaliseDateFormat("01/01/202");
                            fail("Exception should have been thrown.");
                        } catch (InvalidDateFormatException e5) {}
                    }
                }
            }
        }
    }
    
    // isSameDay tested with isIncoming in TaskWithDeadline
}
