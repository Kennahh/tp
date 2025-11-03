package astra.parser;

import astra.exception.InputException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeParser {
    private static final Map<String, DayOfWeek> dayMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger("DateTimeParser");

    private static final String[] DATE_FORMATS_FULL = {
        "yyyy-MM-dd","yyyy-MMM-dd","yyyy-MMMM-dd","yyyy-MM-d","yyyy-MMM-d","yyyy-MMMM-d",
        "yyyy/MM/dd","yyyy/MMM/dd","yyyy/MMMM/dd","yyyy/MM/d","yyyy/MMM/d","yyyy/MMMM/d",
        "dd-MM-yyyy","dd-MMM-yyyy","dd-MMMM-yyyy","d-MM-yyyy","d-MMM-yyyy","d-MMMM-yyyy",
        "dd/MM/yyyy","dd/MMM/yyyy","dd/MMMM/yyyy","d/MM/yyyy","d/MMM/yyyy","d/MMMM/yyyy",
        "MMM-dd-yyyy","MMMM-dd-yyyy","MMM-d-yyyy","MMMM-d-yyyy",
        "MMM/dd/yyyy","MMMM/dd/yyyy","MMM/d/yyyy","MMMM/d/yyyy",
    };

    private static final String[] DATE_FORMATS_DAY_MONTH = {
        "d-MMM","dd-MMM","d-MMMM","dd-MMMM","d-MM","dd-MM",
        "d MMM","dd MMM","d MMMM","dd MMMM","d MM","dd MM",
        "d/MMM","dd/MMM","d/MMMM","dd/MMMM","d/MM","dd/MM",
        "MMM-d","MMM-dd","MMMM-d","MMMM-dd",
        "MMM d","MMM dd","MMMM d","MMMM dd",
        "MMM/d","MMM/dd","MMMM/d","MMMM/dd",
    };

    private static final String[] TIME_FORMATS = {
        "HHmm",
        "HH:mm",
        "HH mm"
    };

    static {
        for (DayOfWeek day : DayOfWeek.values()) {
            String full = day.name().toLowerCase();  // e.g. "monday"
            dayMap.put(full, day);
            String shortForm = full.substring(0, 3); // e.g. "mon"
            dayMap.put(shortForm, day);
        }
        dayMap.put("tues", DayOfWeek.TUESDAY);
        dayMap.put("thur", DayOfWeek.THURSDAY);
        dayMap.put("thurs", DayOfWeek.THURSDAY);
        logger.setLevel(Level.OFF);
    }

    /**
     * Parses date string provided by user into date format
     * Supports multiple formats
     * @param input user provided string
     * @return parsed user provided date into LocalDate format
     * @throws DateTimeParseException when the provided date does not match the expected formatting
     */
    public static LocalDate parseDate(String input) throws InputException {
        assert (input != null) : "parseDate input string should not be null";
        logger.info("Executing parseDate with input: " + input);

        if (input.equalsIgnoreCase("today")) {
            logger.info("today string detected, return today's date");
            return LocalDate.now();
        }

        for (String format : DATE_FORMATS_FULL) {
            logger.info("attempting to parse with full date format");
            try {
                DateTimeFormatter formatter = getFullFormatter(format);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }

        logger.info("format did not match full date format, attempting with partial date format");

        // none worked, try appending year
        int currentYear = LocalDate.now().getYear();
        logger.info("Got current year as: " + currentYear);

        for (String format : DATE_FORMATS_DAY_MONTH) {
            try {
                DateTimeFormatter formatter = getPartialFormatter(format, currentYear);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }
        logger.info("Did not match with any formats");
        throw new InputException("Invalid date format");
    }

    private static DateTimeFormatter getPartialFormatter(String format, int currentYear) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(format)
                .parseDefaulting(ChronoField.YEAR, currentYear) // default missing year
                .toFormatter(Locale.ENGLISH);
    }

    private static DateTimeFormatter getFullFormatter(String format) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(format)
                .toFormatter(Locale.ENGLISH);
    }



    /**
     * Parses time string provided by user into time format
     * @param input user provided string
     * @return parsed user provided time into LocalTime format
     * @throws DateTimeParseException when the provided time does not match the expected formatting
     */
    public static LocalTime parseTime(String input) throws InputException {
        assert (input != null) : "parseTime input string should not be null";
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
        assert (dateAndTime != null) : "dateAndTime string should not be null";
        logger.info("Executing parseDateTime with input: " + dateAndTime);
        // append 00:00 to the end of the string if no timing is provided
        if (!dateAndTime.contains(" ")) { // no space, likely no timing, defaults to 2359H
            dateAndTime += " 2359";
            logger.info("No time provided, appending 2359");
        }
        // the only accepted format is ISO and whatever is written below
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm");
        return LocalDateTime.parse(dateAndTime, formatter);
    }

    /**
     * Parses day string provided by user into DayOfWeek enum
     * Multiple formats of input supported, number, fully spelt and shorthand spelling
     * @param input User input string
     * @return The DayOfWeek enum corresponding to user input
     * @throws InputException Thrown when user input does not correspond to the supported formats, or a nonexistent day
     */
    public static DayOfWeek dayOfWeekParser(String input) throws InputException {
        assert (input != null) : "Input to dayOfWeekParser should not be null";
        logger.info("Executing dayOfWeekParser with input: " + input);

        String sanitisedInput = input.trim().toLowerCase();
        logger.info("Sanitised input: " + sanitisedInput);

        if (sanitisedInput.isEmpty()) {
            logger.warning("input is empty");
            throw new InputException("Day string cannot be empty");
        }

        if (input.matches("[1-7]")) {
            logger.info("Input is a number correponding to day of week");
            int number = Integer.parseInt(sanitisedInput);
            return DayOfWeek.of(number);
        }

        logger.info("Attempting to map input to day of week");

        DayOfWeek day;
        day = dayMap.get(input.trim().toLowerCase());
        if (day == null) {
            logger.warning("Input does map to any day");
            throw new InputException("This is not a valid day!");
        }

        return day;
    }
}
