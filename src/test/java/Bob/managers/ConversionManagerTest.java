package bob.managers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidDateFormatException;

public class ConversionManagerTest {
    @Test
    public void convertInputToIndex_validInput_returnsCorrectOutput() throws InvalidCommandException {
        int expectedIndex = 123;
        int actualIndex = ConversionManager.convertInputToIndex("123", "Invalid input!");

        assertEquals(actualIndex, expectedIndex);
    }

    @Test
    public void convertInputToIndex_invalidInput_exceptionThrown() {
        assertThrows(
            InvalidCommandException.class,
            () -> ConversionManager.convertInputToIndex("abc", "Invalid input!"),
            "Exception should have been thrown."
        );
        assertThrows(
            InvalidCommandException.class,
            () -> ConversionManager.convertInputToIndex("1@3", "Invalid input!"),
            "Exception should have been thrown."
        );
    }

    @Test
    public void convertToNumerics_validInput_returnsCorrectOutput() throws InvalidDateFormatException {
        String[] validInput = new String[] {"24", "10", "2025"};

        int[] expectedOutput = new int[] {24, 10, 2025};
        int[] actualOutput = ConversionManager.convertToNumerics(validInput, "Invalid date format!");

        assertArrayEquals(actualOutput, expectedOutput);
    }

    @Test
    public void convertToNumerics_invalidInput_exceptionThrown() {
        String[] invalidInput = {"24", "October", "2025"};

        assertThrows(
            InvalidDateFormatException.class,
            () -> ConversionManager.convertToNumerics(invalidInput, "Invalid date format!"),
            "Exception should have been thrown."
        );
    }
}
