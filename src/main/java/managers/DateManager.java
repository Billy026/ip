package managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceptions.InvalidDateFormatException;

/**
 * Manages date operations.
 */
public class DateManager {
    /**
     * Detects the format of a date string.
     * 
     * @param dateString string to detect format of
     * @return detected format
     * @throws InvalidDateFormatException If the string does not match any known format
     */
    public static String detectDateFormat(String dateString) throws InvalidDateFormatException {
        List<String> formats = new ArrayList<>();

        // Initialise formats
        // Formats with time

        // Informal
        formats.add("ddd hh:mm");
        formats.add("dddd hh:mm");
        formats.add("ddd hh.mm");
        formats.add("dddd hh.mm");

        // With delimeter " "
        formats.add("hh:mm d MMM yy");
        formats.add("hh:mm d MMM yyyy");
        formats.add("hh:mm d MMMM yy");
        formats.add("hh:mm d MMMM yyyy");
        formats.add("hh:mm dd MMM yy");
        formats.add("hh:mm dd MMM yyyy");
        formats.add("hh:mm dd MMMM yy");
        formats.add("hh:mm dd MMMM yyyy");
        formats.add("hh.mm d MMM yy");
        formats.add("hh.mm d MMM yyyy");
        formats.add("hh.mm d MMMM yy");
        formats.add("hh.mm d MMMM yyyy");
        formats.add("hh.mm dd MMM yy");
        formats.add("hh.mm dd MMM yyyy");
        formats.add("hh.mm dd MMMM yy");
        formats.add("hh.mm dd MMMM yyyy");

        // With delimeter ","
        formats.add("hh:mm, d MMM, yyyy");
        formats.add("hh:mm, d MMMM, yyyy");
        formats.add("hh:mm, dd MMM, yyyy");
        formats.add("hh:mm, dd MMMM, yyyy");
        formats.add("hh:mm, MMM d, yyyy");
        formats.add("hh:mm, MMMM d, yyyy");
        formats.add("hh:mm, MMM dd, yyyy");
        formats.add("hh:mm, MMMM dd, yyyy");
        formats.add("hh.mm, d MMM, yyyy");
        formats.add("hh.mm, d MMMM, yyyy");
        formats.add("hh.mm, dd MMM, yyyy");
        formats.add("hh.mm, dd MMMM, yyyy");
        formats.add("hh.mm, MMM d, yyyy");
        formats.add("hh.mm, MMMM d, yyyy");
        formats.add("hh.mm, MMM dd, yyyy");
        formats.add("hh.mm, MMMM dd, yyyy");

        // With delimeter "-"
        formats.add("hh:mm d-M-yy");
        formats.add("hh:mm dd-M-yy");
        formats.add("hh:mm d-MM-yy");
        formats.add("hh:mm dd-MM-yy");
        formats.add("hh:mm d-M-yyyy");
        formats.add("hh:mm dd-M-yyyy");
        formats.add("hh:mm d-MM-yyyy");
        formats.add("hh:mm dd-MM-yyyy");
        formats.add("hh:mm d-MMM-yy");
        formats.add("hh:mm d-MMM-yyyy");
        formats.add("hh:mm d-MMMM-yy");
        formats.add("hh:mm d-MMMM-yyyy");
        formats.add("hh:mm dd-MMM-yy");
        formats.add("hh:mm dd-MMM-yyyy");
        formats.add("hh:mm dd-MMMM-yy");
        formats.add("hh:mm dd-MMMM-yyyy");
        formats.add("hh.mm d-M-yy");
        formats.add("hh.mm dd-M-yy");
        formats.add("hh.mm d-MM-yy");
        formats.add("hh.mm dd-MM-yy");
        formats.add("hh.mm d-M-yyyy");
        formats.add("hh.mm dd-M-yyyy");
        formats.add("hh.mm d-MM-yyyy");
        formats.add("hh.mm dd-MM-yyyy");
        formats.add("hh.mm d-MMM-yy");
        formats.add("hh.mm d-MMM-yyyy");
        formats.add("hh.mm d-MMMM-yy");
        formats.add("hh.mm d-MMMM-yyyy");
        formats.add("hh.mm dd-MMM-yy");
        formats.add("hh.mm dd-MMM-yyyy");
        formats.add("hh.mm dd-MMMM-yy");
        formats.add("hh.mm dd-MMMM-yyyy");


        // Formats without time

        // With delimeter "/"
        formats.add("d/M/yy");
        formats.add("dd/M/yy");
        formats.add("d/MM/yy");
        formats.add("dd/MM/yy");
        formats.add("d/M/yyyy");
        formats.add("dd/M/yyyy");
        formats.add("d/MM/yyyy");
        formats.add("dd/MM/yyyy");

        // With delimeter " "
        formats.add("d MMM yy");
        formats.add("d MMM yyyy");
        formats.add("d MMMM yy");
        formats.add("d MMMM yyyy");
        formats.add("dd MMM yy");
        formats.add("dd MMM yyyy");
        formats.add("dd MMMM yy");
        formats.add("dd MMMM yyyy");

        // With delimeter ","
        formats.add("d MMM, yyyy");
        formats.add("d MMMM, yyyy");
        formats.add("dd MMM, yyyy");
        formats.add("dd MMMM, yyyy");
        formats.add("MMM d, yyyy");
        formats.add("MMMM d, yyyy");
        formats.add("MMM dd, yyyy");
        formats.add("MMMM dd, yyyy");

        // With delimeter "-"
        formats.add("d-M-yy");
        formats.add("dd-M-yy");
        formats.add("d-MM-yy");
        formats.add("dd-MM-yy");
        formats.add("d-M-yyyy");
        formats.add("dd-M-yyyy");
        formats.add("d-MM-yyyy");
        formats.add("dd-MM-yyyy");
        formats.add("d-MMM-yy");
        formats.add("d-MMM-yyyy");
        formats.add("d-MMMM-yy");
        formats.add("d-MMMM-yyyy");
        formats.add("dd-MMM-yy");
        formats.add("dd-MMM-yyyy");
        formats.add("dd-MMMM-yy");
        formats.add("dd-MMMM-yyyy");

        for (String format : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false); // Disable lenient parsing
            try {
                sdf.parse(dateString);
                return format;
            } catch (ParseException e) {
                // Do nothing
            }
        }

        throw new InvalidDateFormatException("Invalid date format. Example format: dd/MM/yyyy.");
    }

    /**
     * Converts a date string to a specified format.
     * 
     * @param dateString string to convert
     * @param targetFormat format to convert string into
     * @return converted date string
     * @throws InvalidDateFormatException If the string does not match any known format
     * @throws ParseException If the string cannot be parsed
     */
    public static String convertDateToFormat(String dateString, String targetFormat)
            throws InvalidDateFormatException {
        try {
            String currFormat = detectDateFormat(dateString);

            SimpleDateFormat currSdf = new SimpleDateFormat(currFormat);
            Date date = currSdf.parse(dateString);

            SimpleDateFormat targetSdf = new SimpleDateFormat(targetFormat);
            return targetSdf.format(date);
        } catch (InvalidDateFormatException e) {
            throw e;
        } catch (ParseException e) {
            return "    Error parsing date.";
        }
    }
}