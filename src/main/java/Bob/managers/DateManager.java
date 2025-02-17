package bob.managers;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

import bob.exceptions.InvalidDateFormatException;

/**
 * Manages date operations.
 */
public class DateManager {
    private static final String spaceSeparator = " ";
    private static final String slashSeparator = "/";
    private static final String colonSeparator = ":";
    private static final String leadingZero = "0";
    private static final String midnight = " 00:00";
    private static final String currCenturyLead = "20";
    private static final String prevCenturyLead = "19";

    private static final int minutesInHour = 60;
    private static final int hoursInDay = 24;
    private static final int daysInWeek = 7;
    private static final int monthsInYear = 12;
    private static final int yearLongForm = 4;
    private static final int yearShortForm = 2;

    private static final int daysInFebruary = 29;
    private static final int daysInShorterMonth = 30;
    private static final int daysInLongerMonth = 31;

    private static final String longDateFormat = "dd MMMM yyyy HH:mm";
    private static final String shortDateFormat = "dd/MM/yyyy HH:mm";

    private static final String invalidDateErrorMessage =
            "Invalid date format. Example format: dd/MM/yyyy or dd MMMM yyyy HH:mm (in 24 hour format).";

    /**
     * Converts the format of a date string to a standardised format.
     * 
     * @param dateString string to change format of.
     * @return changed format.
     * @throws InvalidDateFormatException if the string does not match any valid format.
     */
    public static String normaliseDateFormat(String dateString) throws InvalidDateFormatException {
        String dayOfWeek = checkForDayOfWeek(dateString);
        if (dayOfWeek != "") {
            return dayOfWeek;
        }

        String[] dateParts = dateString.split("[ ,/\\-:]");
        if (dateParts.length != 3 && dateParts.length != 5) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }

        if (dateParts.length == 3) {
            dateParts = new String[] {dateParts[0], dateParts[1], dateParts[2], leadingZero, leadingZero};
        }

        dateParts = swapIfTimeInFront(dateParts);
        dateParts = swapIfYearInFront(dateParts);

        String day = normaliseDayFormat(dateParts);
        String month = normaliseMonthFormat(dateParts);
        String year = normaliseYearFormat(dateParts);
        String hour = addLeadingZeroes(dateParts[3], hoursInDay);
        String minute = addLeadingZeroes(dateParts[4], minutesInHour);
        
        return convertToCorrectFormat(day, month, year, hour, minute);
    }

    /**
     * Checks and returns the correct date from a valid day of week.
     * 
     * @param dateString string to change format of.
     * @return correct date from a valid day of week.
     * If no valid day of week is found, return "".
     */
    private static String checkForDayOfWeek(String dateString) {
        HashMap<String, Integer> dayMap = new HashMap<>();
        dayMap.put("mon", 1);
        dayMap.put("monday", 1);
        dayMap.put("tue", 2);
        dayMap.put("tuesday", 2);
        dayMap.put("wed", 3);
        dayMap.put("wednesday", 3);
        dayMap.put("thu", 4);
        dayMap.put("thursday", 4);
        dayMap.put("fri", 5);
        dayMap.put("friday", 5);
        dayMap.put("sat", 6);
        dayMap.put("saturday", 6);
        dayMap.put("sun", 7);
        dayMap.put("sunday", 7);

        assert dayMap.size() == 14 :
                "There should be only 14 possible day formats (excluding capitalisation).";

        if (dayMap.containsKey(dateString.toLowerCase())) {
            int targetInt = dayMap.get(dateString.toLowerCase());
            int currInt = dayMap.get(LocalDate.now().getDayOfWeek().name().toLowerCase());

            // If target day of week is behind the current day of week
            if (targetInt < currInt) {
                targetInt += daysInWeek;
            }

            String[] dateParts = LocalDate.now().plusDays(targetInt - currInt)
                    .toString().split("[/ ,-]");
            String tempForSwitching = dateParts[0];
            dateParts[0] = dateParts[2];
            dateParts[2] = tempForSwitching;

            return dateParts[0] + slashSeparator + dateParts[1] + slashSeparator + dateParts[2] + midnight;
        } else {
            return "";
        }
    }

    /**
     * Checks if the first two strings best fit time or date, and swaps if it is time.
     * 
     * @param dateParts array of day, month, year, hour and minute.
     * @return array with day, month, year, hour, minute in that order.
     * @throws InvalidDateFormatException if invalid date format given.
     */
    private static String[] swapIfTimeInFront(String[] dateParts) throws InvalidDateFormatException {
        String[] alphabeticParts = searchForAlphabeticMonth(dateParts);

        if (alphabeticParts.length != 0) {
            return alphabeticParts;
        }

        boolean[] dateOrTimeFirst = checkDateOrTimeFirst(dateParts);
        return swapParts(dateParts, dateOrTimeFirst);
    }

    /**
     * Swaps the day and year indexes if year is at index 0.
     * 
     * @param dateParts array of day, month, year, hour and minute.
     * @return array of day, month, year, hour, minute in that order.
     */
    private static String[] swapIfYearInFront(String[] dateParts) throws InvalidDateFormatException {
        boolean isFirstLength4 = dateParts[0].length() == yearLongForm;
        boolean isMoreThanMaxDay = Integer.parseInt(dateParts[0]) > getMaximumDays(dateParts[1]);

        if (isFirstLength4 || isMoreThanMaxDay) {
            return new String[] {dateParts[2], dateParts[1], dateParts[0]};       
        } else {
            return dateParts;
        }
    }

    /**
     * Returns the day with a length of 2.
     * If the length of the day is 1, adds a leading zero.
     * 
     * @param dateParts array of day, month, year, hour, minute in that order.
     * @return day with a length of 2.
     * @throws InvalidDateFormatException if the length of day is more than 2, or the day format is invalid.
     */
    private static String normaliseDayFormat(String[] dateParts) throws InvalidDateFormatException {
        if (dateParts[0].matches("\\d+")) {
            int day = Integer.parseInt(dateParts[0]);
            if (day <= 0 || day > getMaximumDays(dateParts[1])) {
                throw new InvalidDateFormatException(invalidDateErrorMessage);
            }

            if (dateParts[0].length() == 1) {
                return leadingZero + dateParts[0];
            } else if (dateParts[0].length() == 2) {
                return dateParts[0];
            } else {
                throw new InvalidDateFormatException(invalidDateErrorMessage);
            }
        } else {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
    }

    /**
     * Returns the month from a valid format to the standardized format.
     * 
     * @param dateParts array of day, month, year, hour, minute in that order.
     * @return month in a standardized format.
     * @throws InvalidDateFormatException if month is in an invalid format.
     */
    private static String normaliseMonthFormat(String[] dateParts) throws InvalidDateFormatException {
        if (dateParts[1].matches("\\d+")) {
            return convertNumericMonth(dateParts);
        } else if (dateParts[1].matches("[a-zA-Z]+")) {
            return convertAlphabeticMonth(dateParts);
        } else {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
    }

    /**
     * Returns year with a length of 4.
     * If the length of year is 2, converts year to a length of 4.
     * 
     * @param dateParts array of day, month, year, hour, minute in that order.
     * @return year with a length of 4.
     * @throws InvalidDateFormatException if year is in an invalid format.
     */
    private static String normaliseYearFormat(String[] dateParts) throws InvalidDateFormatException {
        if (!dateParts[2].matches("\\d+")) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }

        if (dateParts[2].length() == yearLongForm) {
            return dateParts[2];
        } else if (dateParts[2].length() == yearShortForm) {
            if (Integer.parseInt(dateParts[2]) <= LocalDate.now().getYear() % 100) {
                return currCenturyLead + dateParts[2];
            } else {
                return prevCenturyLead + dateParts[2];
            }
        } else {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
    }

    /**
     * Adds a leading zero to the string if it is length of 1.
     * 
     * @param str string to add leading zero to if needed.
     * @param max maximum value the string can be.
     * @return valid value with length of 2.
     * @throws InvalidDateFormatException if str is not numeric or within valid range.
     */
    private static String addLeadingZeroes(String str, int max) throws InvalidDateFormatException {
        if (str.matches("\\d+") || Integer.parseInt(str) >= max) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
        
        int length = str.length();

        if (length == 1) {
            return leadingZero + str;
        } else if (length == 2) {
            return str;
        } else {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
    }

    /**
     * Sorts the array into the correct format based on if and where there is an alphabetic month.
     * 
     * @param dateParts array of day, month, year, hour, minute.
     * @return sorted array if alphabetic month found.
     * If there is no alphabetic month, returns an empty array.
     */
    private static String[] searchForAlphabeticMonth(String[] dateParts) {
        int index = -1;

        for (int i = 0; i < dateParts.length; i++) {
            if (dateParts[i].matches("[a-zA-Z]+")) {
                index = i;
            }
        }

        if (index == 1) {
            return dateParts;
        } else if (index == 3) {
            return new String[] {dateParts[2], dateParts[3], dateParts[4], dateParts[0], dateParts[1]};
        } else {
            return new String[0];
        }
    }

    /**
     * Checks if the first two string values represent date or time.
     * 
     * @param dateParts array of day, month, year, hour, minute.
     * @return boolean of whether the values represent date or time.
     * @throws InvalidDateFormatException if the array of strings are not numeric.
     */
    private static boolean[] checkDateOrTimeFirst(String[] dateParts) throws InvalidDateFormatException {
        int[] numericParts = ConversionManager.convertToNumerics(dateParts, invalidDateErrorMessage);

        Function<Integer, Boolean> checkIfValidMinute = m -> m >= 0 && m < minutesInHour;
        Function<Integer, Boolean> checkIfValidHour = h -> h >= 0 && h < hoursInDay;
        Function<Integer, Boolean> checkIfValidMonth = m -> m > 0 && m <= monthsInYear;

        boolean isDateFirst = checkIfValidMinute.apply(numericParts[4])
                && checkIfValidHour.apply(numericParts[3]) && checkIfValidMonth.apply(numericParts[1]);
        boolean isTimeFirst = checkIfValidMinute.apply(numericParts[1])
                && checkIfValidHour.apply(numericParts[0]) && checkIfValidMonth.apply(numericParts[3]);

        return new boolean[] {isDateFirst, isTimeFirst};
    }

    /**
     * Swaps values in the string of array if time is before date.
     * 
     * @param dateParts array of day, month, year, hour, minute.
     * @param firsts boolean of whether the values represent date or time.
     * @return array of day, month, year, hour, minute in that order.
     * @throws InvalidDateFormatException if the first two strings are neither date nor time.
     */
    private static String[] swapParts(String[] dateParts, boolean[] firsts) throws InvalidDateFormatException {
        BiFunction<Integer, Integer, Boolean> checkIfValidDay = (d, m) -> d > 0 && d <= m;

        if (!firsts[0] && !firsts[1]) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }

        int index = (firsts[0]) ? 1 : 3;
        int day = (firsts[0]) ? Integer.parseInt(dateParts[0]) : Integer.parseInt(dateParts[2]);

        if (!checkIfValidDay.apply(day, getMaximumDays(dateParts[index]))) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }

        if (firsts[0]) {
            return dateParts;
        } else {
            assert firsts[1] == true;
            return new String[] {dateParts[2], dateParts[3], dateParts[4], dateParts[0], dateParts[1]};
        }
    }

    /**
     * Returns numeric month with a length of 2.
     * If the length of numeric month is 1, adds a leading zero.
     * 
     * @param dateParts array of day, month, year, hour, minute in that order.
     * @return month with a length of 2.
     * @throws InvalidDateFormatException if month is before day.
     */
    private static String convertNumericMonth(String[] dateParts) throws InvalidDateFormatException {
        int month = Integer.parseInt(dateParts[1]);
        if (month > monthsInYear) {
            throw new InvalidDateFormatException(
                "Sorry, we have not implemented MM/dd/yy format yet.");
        } else if (month <= 0) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        } else if (dateParts[1].length() == 1) {
            return leadingZero + dateParts[1];
        } else {
            return dateParts[1];
        }
    }

    /**
     * Standardizes alphabetic month to a full alphabetic format.
     * 
     * @param dateParts array of day, month, year, hour, minute in that order.
     * @return month in a full alphabetic format.
     * @throws InvalidDateFormatException if month is in an invalid format.
     */
    private static String convertAlphabeticMonth(String[] dateParts) throws InvalidDateFormatException {
        HashMap<String, String> monthMap = new HashMap<>();
            monthMap.put("jan", "January");
            monthMap.put("feb", "February");
            monthMap.put("mar", "March");
            monthMap.put("apr", "April");
            monthMap.put("may", "May");
            monthMap.put("jun", "June");
            monthMap.put("jul", "July");
            monthMap.put("aug", "August");
            monthMap.put("sep", "September");
            monthMap.put("oct", "October");
            monthMap.put("nov", "November");
            monthMap.put("dec", "December");

            assert monthMap.size() == 12 :
                "There should be only 12 possible short month formats (excluding capitalisation).";

            // Set to check for long form of month
            HashSet<String> monthSet = new HashSet<>();
            monthSet.add("january");
            monthSet.add("february");
            monthSet.add("march");
            monthSet.add("april");
            monthSet.add("may");
            monthSet.add("june");
            monthSet.add("july");
            monthSet.add("august");
            monthSet.add("september");
            monthSet.add("october");
            monthSet.add("november");
            monthSet.add("december");

            assert monthSet.size() == 12 :
                "There should be only 12 possible long month formats (excluding capitalisation).";

            if (dateParts[1].length() == 3 && monthMap.containsKey(dateParts[1].toLowerCase())) {
                return capitaliseString(monthMap.get(dateParts[1].toLowerCase()));
            } else if (monthSet.contains(dateParts[1].toLowerCase())) {
                return capitaliseString(dateParts[1]);
            } else {
                throw new InvalidDateFormatException(invalidDateErrorMessage);
            }
    }

    /**
     * Gets the maximum number of days in the given month.
     * 
     * @param month month to get the maximum number of days from.
     * @return maximum number of days.
     * @throws InvalidDateFormatException if month is not numeric.
     */
    private static int getMaximumDays(String month) throws InvalidDateFormatException {
        final int february = 2;

        try {
            int numericMonth = Integer.parseInt(month);

            if (numericMonth == february) {
                return daysInFebruary;
            } else if (isMonth30Days(numericMonth)) {
                return daysInShorterMonth;
            } else {
                return daysInLongerMonth;
            }
        } catch (NumberFormatException e) {
            throw new InvalidDateFormatException(invalidDateErrorMessage);
        }
    }

    /**
     * Checks if the month has a maximum of 30 days.
     * 
     * @param month month to check.
     * @return whether the month has a maximum of 30 days.
     */
    private static boolean isMonth30Days(int month) {
        final int[] monthsWith20Days = {4, 6, 9, 11};

        for (Integer m : monthsWith20Days) {
            if (month == m) {
                return true;
            }
        }

        return false;
    }

    /**
     * Combines day, month and year to a standardized format.
     * Format is based on whether month is numeric or alphabetic.
     * 
     * @param day day to combine.
     * @param month month to combine.
     * Determines the standardized format.
     * @param year year to combine.
     * @param hour hour to combine.
     * @param minute minute to combine.
     * @return date and time in a standardized format.
     */
    private static String convertToCorrectFormat(String day, String month, String year,
            String hour, String minute) {
        if (month.matches("\\d+")) {
            return day + slashSeparator + month + slashSeparator + year + spaceSeparator + hour
                    + colonSeparator + minute;
        } else {
            return day + spaceSeparator + month + spaceSeparator + year + spaceSeparator + hour
                    + colonSeparator + minute;
        }
    }

    /**
     * Checks if a deadline is due today.
     * 
     * @param deadline deadline to check.
     * @return whether deadline is due today.
     */
    public static boolean isSameDay(String deadline) {
        if (deadline.contains(slashSeparator)) {
            LocalDate targetDate =
                    LocalDate.parse(deadline, DateTimeFormatter.ofPattern(shortDateFormat));
            return targetDate.equals(LocalDate.now());
        } else if (deadline.contains(spaceSeparator)) {
            LocalDate targetDate =
                    LocalDate.parse(deadline, DateTimeFormatter.ofPattern(longDateFormat));
            return targetDate.equals(LocalDate.now());
        } else {
            return false;
        }
    }

    public boolean isSameTime(String time) {
        return true;
    }

    /**
     * Capitalises the first letter of a string.
     * 
     * @param str string to capitalise.
     * @return capitalised string.
     */
    private static String capitaliseString(String str) {
        return str.substring(0, 1).toUpperCase() +
                str.substring(1).toLowerCase();
    }
}
