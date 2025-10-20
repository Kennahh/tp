package astra.parser;

import astra.command.AddExamCommand;
import astra.command.AddLectureCommand;
import astra.command.AddTaskCommand;
import astra.command.AddTutorialCommand;
import astra.command.ChangeDeadlineCommand;
import astra.command.CheckExamCommand;
import astra.command.CheckLecturesCommand;
import astra.command.CheckTutorialsCommand;
import astra.command.CompleteCommand;
import astra.command.DeleteCommand;
import astra.command.ExitCommand;
import astra.command.HelpCommand;
import astra.command.ListCommand;
import astra.command.UnmarkCommand;
import astra.exception.InputException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Performs Parser-specific unit tests (dayOfWeekParser, parseDateTime, command types)
 */
public class ParserBasicTest {

    @Test
    public void parse_nullOrEmpty_throws() {
        assertThrows(InputException.class, () -> Parser.parse(null));
        assertThrows(InputException.class, () -> Parser.parse("   "));
    }

    @Test
    public void parse_commandWords_returnTypes() throws Exception {
        assertTrue(Parser.parse("close") instanceof ExitCommand);
        assertTrue(Parser.parse("task x /by 2025-10-10 23:59") instanceof AddTaskCommand);
        assertTrue(Parser.parse("lecture x /place LT /day Mon /from 10:00 /to 12:00") instanceof AddLectureCommand);
        assertTrue(Parser.parse("tutorial x /place COM1 /day Fri /from 14:00 /to 15:00") instanceof AddTutorialCommand);
        assertTrue(Parser.parse("exam x /date 2025-11-25 /from 09:00 /to 11:00") instanceof AddExamCommand);
        assertTrue(Parser.parse("delete 1") instanceof DeleteCommand);
        assertTrue(Parser.parse("list") instanceof ListCommand);
        assertTrue(Parser.parse("help") instanceof HelpCommand);
        assertTrue(Parser.parse("changedeadline 1 /to 2025-10-11 22:00") instanceof ChangeDeadlineCommand);
        assertTrue(Parser.parse("complete 1") instanceof CompleteCommand);
        assertTrue(Parser.parse("unmark 1") instanceof UnmarkCommand);
        assertTrue(Parser.parse("checkexam") instanceof CheckExamCommand);
        assertTrue(Parser.parse("checklecture mon") instanceof CheckLecturesCommand);
        assertTrue(Parser.parse("checktutorial fri") instanceof CheckTutorialsCommand);
    }

    @Test
    public void dayOfWeekParser_variousForms_success() throws Exception {
        assertEquals(DayOfWeek.MONDAY, Parser.dayOfWeekParser("mon"));
        assertEquals(DayOfWeek.FRIDAY, Parser.dayOfWeekParser("Friday"));
        assertEquals(DayOfWeek.TUESDAY, Parser.dayOfWeekParser("2"));
        assertThrows(InputException.class, () -> Parser.dayOfWeekParser("x"));
        assertThrows(InputException.class, () -> Parser.dayOfWeekParser("mo"));
    }
}

