package astra.parser;

import astra.exception.InputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class DateTimeParser {
    private static String[] DATE_FORMATS_FULL = {
        "yyyy-MM-dd","yyyy-MMM-dd","yyyy-MMMM-dd","yyyy-MM-d","yyyy-MMM-d","yyyy-MMMM-d",
        "yyyy/MM/dd","yyyy/MMM/dd","yyyy/MMMM/dd","yyyy/MM/d","yyyy/MMM/d","yyyy/MMMM/d",
        "yyyy MM dd","yyyy MMM dd","yyyy MMMM dd","yyyy MM d","yyyy MMM d","yyyy MMMM d",
        "dd-MM-yyyy","dd-MMM-yyyy","dd-MMMM-yyyy","d-MM-yyyy","d-MMM-yyyy","d-MMMM-yyyy",
        "dd/MM/yyyy","dd/MMM/yyyy","dd/MMMM/yyyy","d/MM/yyyy","d/MMM/yyyy","d/MMMM/yyyy",
        "dd MM yyyy","dd MMM yyyy","dd MMMM yyyy","d MM yyyy","d MMM yyyy","d MMMM yyyy",
        "MM-dd-yyyy","MMM-dd-yyyy","MMMM-dd-yyyy","MM-d-yyyy","MMM-d-yyyy","MMMM-d-yyyy",
        "MM/dd/yyyy","MMM/dd/yyyy","MMMM/dd/yyyy","MM/d/yyyy","MMM/d/yyyy","MMMM/d/yyyy",
        "MM dd yyyy","MMM dd yyyy","MMMM dd yyyy","MM d yyyy","MMM d yyyy","MMMM d yyyy"
    };

    private static String[] DATE_FORMATS_DAY_MONTH = {
        "d-MMM","dd-MMM","d-MMMM","dd-MMMM","d-MM","dd-MM",
        "d MMM","dd MMM","d MMMM","dd MMMM","d MM","dd MM",
        "d/MMM","dd/MMM","d/MMMM","dd/MMMM","d/MM","dd/MM",
        "MMM-d","MMM-dd","MMMM-d","MMMM-dd","MM-d","MM-dd",
        "MMM d","MMM dd","MMMM d","MMMM dd","MM d","MM dd",
        "MMM/d","MMM/dd","MMMM/d","MMMM/dd","MM/d","MM/dd"
    };

    private static String[] TIME_FORMATS = {
        "HHmm",
        "HH:mm",
        "HH mm"
    };

    /**
     * Parses date string provided by user into date format
     * Supports multiple formats
     * @param input user provided string
     * @return parsed user provided date into LocalDate format
     * @throws DateTimeParseException when the provided date does not match the expected formatting
     */
    public static LocalDate parseDate(String input) throws InputException {
        if (input.equalsIgnoreCase("today")) {
            return LocalDate.now();
        }

        for (String format : DATE_FORMATS_FULL) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern(format)
                        .toFormatter(Locale.ENGLISH);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }

        // none worked, try appending year
        int currentYear = LocalDate.now().getYear();

        for (String format : DATE_FORMATS_DAY_MONTH) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern(format)
                        .parseDefaulting(ChronoField.YEAR, currentYear) // default missing year
                        .toFormatter(Locale.ENGLISH);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }
        throw new InputException("Invalid time format");
    }


    /**
     * Parses time string provided by user into time format
     * @param input user provided string
     * @return parsed user provided time into LocalTime format
     * @throws DateTimeParseException when the provided time does not match the expected formatting
     */
    public static LocalTime parseTime(String input) throws InputException {
        for (String format : TIME_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalTime.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }
        throw new InputException("Invalid time format");
    }

    /**
     * Parses date and time string provided by user into date and time format
     * @param dateAndTime user provided string
     * @return parsed user provided date and time into LocalDateTime format
     * @throws DateTimeParseException when the provided date and time does not match the expected formatting
     */
    public static LocalDateTime parseDateTime(String dateAndTime) throws DateTimeParseException {
        // append 00:00 to the end of the string if no timing is provided
        if (!dateAndTime.contains(" ")) { // no space, likely no timing, defaults to 2359H
            dateAndTime += " 2359";
        }
        // the only accepted format is ISO and whatever is written below
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm");
        return LocalDateTime.parse(dateAndTime, formatter);
    }
}
