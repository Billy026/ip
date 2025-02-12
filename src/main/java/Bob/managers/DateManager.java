package bob.managers;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import bob.exceptions.InvalidDateFormatException;

/**
 * Manages date operations.
 */
public class DateManager {
    private static final String spaceSeparator = " ";
    private static final String slashSeparator = "/";
    private static final String leadingZero = "0";
    private static final String currCenturyLead = "20";
    private static final String prevCenturyLead = "19";

    private static final int daysInWeek = 7;
    private static final int daysInMonth = 31;
    private static final int monthsInYear = 12;
    private static final int yearLongForm = 4;
    private static final int yearShortForm = 2;

    private static final String longDateFormat = "dd MMMM yyyy";
    private static final String shortDateFormat = "dd/MM/yyyy";

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

        String[] dateParts = dateString.split("[/ ,-]");
        if (dateParts.length != 3) {
            throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
        }

        dateParts = swapIfYearInFront(dateParts);
        String day = normaliseDayFormat(dateParts);
        String month = normaliseMonthFormat(dateParts);
        String year = normaliseYearFormat(dateParts);
        
        return convertToCorrectFormat(day, month, year);
    }

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

        if (dayMap.containsKey(dateString.toLowerCase())) {
            int targetInt = dayMap.get(dateString.toLowerCase());
            int currInt = dayMap.get(LocalDate.now().getDayOfWeek().name().toLowerCase());

            if (targetInt < currInt) {
                targetInt += daysInWeek;
            }

            String[] dateParts = LocalDate.now().plusDays(targetInt - currInt)
                    .toString().split("[/ ,-]");
            String tempForSwitching = dateParts[0];
            dateParts[0] = dateParts[2];
            dateParts[2] = tempForSwitching;

            return dateParts[0] + slashSeparator + dateParts[1] + slashSeparator + dateParts[2];
        } else {
            return "";
        }
    }

    private static String[] swapIfYearInFront(String[] dateParts) {
        if (dateParts[0].length() == yearLongForm || Integer.parseInt(dateParts[0]) > daysInMonth) {
            return new String[] {dateParts[2], dateParts[1], dateParts[0]};       
        } else {
            return dateParts;
        }
    }

    private static String normaliseDayFormat(String[] dateParts) throws InvalidDateFormatException {
        if (dateParts[0].matches("\\d+")) {
            if (dateParts[0].length() == 1) {
                return leadingZero + dateParts[0];
            } else {
                return dateParts[0];
            }
        } else {
            throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
        }
    }

    private static String normaliseMonthFormat(String[] dateParts) throws InvalidDateFormatException {
        if (dateParts[1].matches("\\d+")) {
            return convertNumericMonth(dateParts);
        } else if (dateParts[1].matches("[a-zA-Z]+")) {
            return convertAlphabeticMonth(dateParts);
        } else {
            throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
        }
    }

    private static String convertNumericMonth(String[] dateParts) throws InvalidDateFormatException {
        if (Integer.parseInt(dateParts[1]) > monthsInYear) {
            throw new InvalidDateFormatException(
                "Sorry, we have not implemented MM/dd/yy format yet.");
        }

        if (dateParts[1].length() == 1) {
            return leadingZero + dateParts[1];
        } else {
            return dateParts[1];
        }
    }

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

            if (dateParts[1].length() == 3 && monthMap.containsKey(dateParts[1].toLowerCase())) {
                return capitaliseString(monthMap.get(dateParts[1].toLowerCase()));
            } else if (monthSet.contains(dateParts[1].toLowerCase())) {
                return capitaliseString(dateParts[1]);
            } else {
                throw new InvalidDateFormatException(
                        "Invalid date format. Example format: dd/MM/yyyy.");
            }
    }

    private static String normaliseYearFormat(String[] dateParts) throws InvalidDateFormatException {
        if (!dateParts[2].matches("\\d+")) {
            throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
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
            throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
        }
    }

    private static String convertToCorrectFormat(String day, String month, String year) {
        if (month.matches("\\d+")) {
            return day + slashSeparator + month + slashSeparator + year;
        } else {
            return day + spaceSeparator + month + spaceSeparator + year;
        }
    }

    /**
     * Checks if a deadline is due today.
     * 
     * @param deadline deadline to check.
     * @return whether deadline is due today.
     */
    public static boolean isSameDay(String deadline) {
        if (deadline.contains(spaceSeparator)) {
            LocalDate targetDate =
                    LocalDate.parse(deadline, DateTimeFormatter.ofPattern(longDateFormat));
            return targetDate.equals(LocalDate.now());
        } else if (deadline.contains(slashSeparator)) {
            LocalDate targetDate =
                    LocalDate.parse(deadline, DateTimeFormatter.ofPattern(shortDateFormat));
            return targetDate.equals(LocalDate.now());
        } else {
            return false;
        }
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
