package bob.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidDateFormatException;
import bob.exceptions.InvalidTaskOperationException;

public class DateManagerTest {
    private static final String shortDateFormat = "dd/MM/yyyy HH:mm";
    private static final String longDateFormat = "dd MMMM yyyy HH:mm";

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
            return LocalDate.now().plusDays(i - currInt).atStartOfDay()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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
    public void normaliseDateFormat_ddMMyyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("31/10/2025"),
                    "31/10/2025 00:00");
            assertEquals(DateManager.normaliseDateFormat("10/10/2026 10:30"),
                    "10/10/2026 10:30");
            assertEquals(DateManager.normaliseDateFormat("21:45 01/01/2025"),
                    "01/01/2025 21:45");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_ddMMMMyyyy_correctOutput() {
        try {
            assertEquals(
                    DateManager.normaliseDateFormat("31 January 2025"),
                            "31 January 2025 00:00");
            assertEquals(
                    DateManager.normaliseDateFormat("10 october 2026 10:30"),
                            "10 October 2026 10:30");
            assertEquals(
                    DateManager.normaliseDateFormat("21:45 01 nOveMBer 2025"),
                            "01 November 2025 21:45");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_dMMyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("8/10/25"),
                    "08/10/2025 00:00");
            assertEquals(DateManager.normaliseDateFormat("7/10/98 10:30"),
                    "07/10/1998 10:30");
            assertEquals(DateManager.normaliseDateFormat("21:45 1/01/76"),
                    "01/01/1976 21:45");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_dMMyyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("8/10/2025"),
                    "08/10/2025 00:00");
            assertEquals(DateManager.normaliseDateFormat("7/10/2026 10:30"),
                    "07/10/2026 10:30");
            assertEquals(DateManager.normaliseDateFormat("21:45 1/01/2025"),
                    "01/01/2025 21:45");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_dMMMyyyy_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("31 JAN 2025"),
                    "31 January 2025 00:00");
            assertEquals(DateManager.normaliseDateFormat("10 oct 2026 10:30"),
                    "10 October 2026 10:30");
            assertEquals(DateManager.normaliseDateFormat("21:45 01 nOv 2025"),
                    "01 November 2025 21:45");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_yearFirst_correctOutput() {
        try {
            assertEquals(DateManager.normaliseDateFormat("2025-8-31"),
                    "31/08/2025 00:00");
            assertEquals(DateManager.normaliseDateFormat("32 10 10 10:30"),
                    "10/10/1932 10:30");
            assertEquals(
                DateManager.normaliseDateFormat("21:30 1998 October 25"),
                        "25 October 1998 21:30");
        } catch (InvalidDateFormatException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void normaliseDateFormat_invalidDate_exceptionThrown() {
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("hi"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("31/31/31/31 10:30"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("31/101/2025 10:30"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("10 hi 2026 10:30"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("01/01/202 10:30"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("01/01/20 25:30"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidDateFormatException.class,
            () -> DateManager.normaliseDateFormat("01/01/20 00:60"),
            "Exception should have been thrown."
        );
    }

    @Test
    public void isSameDay_shortFormatWithTime_correctBooleansReturned() {
        String matchingDate = "01/01/2025 10:30";
        String differentTime = "01/01/2025 11:00";
        String differentDate = "02/01/2025 11:00";
        LocalDateTime date = LocalDateTime.parse(matchingDate, DateTimeFormatter.ofPattern(shortDateFormat));

        assertTrue(DateManager.isSameDay(matchingDate, date, true));
        assertFalse(DateManager.isSameDay(differentTime, date, true));
        assertFalse(DateManager.isSameDay(differentDate, date, true));
    }

    @Test
    public void isSameDay_shortFormatWithoutTime_correctBooleansReturned() {
        String matchingDate = "01/01/2025 10:30";
        String differentTime = "01/01/2025 11:00";
        String differentDate = "02/01/2025 11:00";
        LocalDateTime date = LocalDateTime.parse(matchingDate, DateTimeFormatter.ofPattern(shortDateFormat));

        assertTrue(DateManager.isSameDay(matchingDate, date, false));
        assertTrue(DateManager.isSameDay(differentTime, date, false));
        assertFalse(DateManager.isSameDay(differentDate, date, false));
    }

    @Test
    public void isSameDay_longFormatWithTime_correctBooleansReturned() {
        String matchingDate = "01 January 2025 10:30";
        String differentTime = "01 January 2025 11:00";
        String differentDate = "02 January 2025 11:00";
        LocalDateTime date = LocalDateTime.parse(matchingDate, DateTimeFormatter.ofPattern(longDateFormat));

        assertTrue(DateManager.isSameDay(matchingDate, date, true));
        assertFalse(DateManager.isSameDay(differentTime, date, true));
        assertFalse(DateManager.isSameDay(differentDate, date, true));
    }

    @Test
    public void isSameDay_longFormatWithoutTime_correctBooleansReturned() {
        String matchingDate = "01 January 2025 10:30";
        String differentTime = "01 January 2025 11:00";
        String differentDate = "02 January 2025 11:00";
        LocalDateTime date = LocalDateTime.parse(matchingDate, DateTimeFormatter.ofPattern(longDateFormat));

        assertTrue(DateManager.isSameDay(matchingDate, date, false));
        assertTrue(DateManager.isSameDay(differentTime, date, false));
        assertFalse(DateManager.isSameDay(differentDate, date, false));
    }

    @Test
    public void checkForValidEventDates_validEventDates_returnedSuccessfully() {
        String[] inputPartsShortDate = new String[] {"task", "01/01/2025 10:00", "01/01/2025 11:00"};
        String[] inputPartsLongDate = new String[] {"task", "01 May 2025 10:00", "01 May 2025 11:00"};

        try {
            DateManager.checkForValidEventDates(inputPartsShortDate, true);
            DateManager.checkForValidEventDates(inputPartsLongDate, true);
        } catch (InvalidTaskOperationException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void checkForValidEventDates_invalidEventDates_exceptionThrown() {
        String[] inputPartsShortDate = new String[] {"task", "01/01/2025 11:00", "01/01/2025 10:00"};
        String[] inputPartsLongDate = new String[] {"task", "01 May 2025 11:00", "01 May 2025 11:00"};

        assertThrows(
            InvalidTaskOperationException.class,
            () -> DateManager.checkForValidEventDates(inputPartsShortDate, true),
            "Exception should have been thrown."
        );

        assertThrows(
            InvalidTaskOperationException.class,
            () -> DateManager.checkForValidEventDates(inputPartsLongDate, true),
            "Exception should have been thrown."
        );
    }
}
