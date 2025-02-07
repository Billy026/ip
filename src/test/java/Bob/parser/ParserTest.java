package bob.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import bob.exceptions.InvalidCommandException;
import bob.parser.Parser.Actions;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.parser = Mockito.spy(new Parser("test_data/test_tasks.txt"));
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("test_data/test_tasks.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void parseCommand_todoCommand_reachedSuccessfully() throws InvalidCommandException {
        doReturn(Actions.TODO).when(parser).convertToActions("todo");
        parser.parseCommand(new String[]{"todo", "buy milk"});
        verify(parser).convertToActions("todo");
    }

    @Test
    public void parseCommand_deadlineCommand_reachedSuccessfully() throws InvalidCommandException {
        doReturn(Actions.DEADLINE).when(parser).convertToActions("deadline");
        parser.parseCommand(new String[]{"deadline", "assignment", "/by", "30/01/2025"});
        verify(parser).convertToActions("deadline");
    }

    @Test
    public void parseCommand_eventCommand_reachedSuccessfully() throws InvalidCommandException {
        doReturn(Actions.EVENT).when(parser).convertToActions("event");
        parser.parseCommand(new String[]{"event", "meeting", "/from", "30/01/2025", "/to", "31/01/2025"});
        verify(parser).convertToActions("event");
    }

    @Test
    public void parseCommand_deleteCommand_reachedSuccessfully() throws InvalidCommandException {
        parser.parseCommand(new String[]{"todo", "buy milk"});
        doReturn(Actions.DELETE).when(parser).convertToActions("delete");
        parser.parseCommand(new String[]{"delete", "1"});
        verify(parser).convertToActions("delete");
    }

    @Test
    public void parseCommand_listCommand_reachedSuccessfully() throws InvalidCommandException {
        doReturn(Actions.LIST).when(parser).convertToActions("list");
        parser.parseCommand(new String[]{"list"});
        verify(parser).convertToActions("list");
    }

    @Test
    public void parseCommand_findCommand_reachedSuccessfully() throws InvalidCommandException {
        doReturn(Actions.FIND).when(parser).convertToActions("find");
        parser.parseCommand(new String[]{"find", "read"});
        verify(parser).convertToActions("find");
    }

    @Test
    public void parseCommand_markCommand_reachedSuccessfully() throws InvalidCommandException {
        parser.parseCommand(new String[]{"todo", "buy milk"});
        doReturn(Actions.MARK).when(parser).convertToActions("mark");
        parser.parseCommand(new String[]{"mark", "1"});
        verify(parser).convertToActions("mark");
    }

    @Test
    public void parseCommand_unmarkCommand_reachedSuccessfully() throws InvalidCommandException {
        parser.parseCommand(new String[]{"todo", "buy milk"});
        parser.parseCommand(new String[]{"mark", "1"});
        doReturn(Actions.UNMARK).when(parser).convertToActions("unmark");
        parser.parseCommand(new String[]{"unmark", "1"});
        verify(parser).convertToActions("unmark");
    }

    // displayIncomingDeadlines tested by taskManager

    @Test
    public void convertToActions_validCommand_correctOutput() {
        try {
            assertEquals(parser.convertToActions("todo"), Actions.TODO);
            assertEquals(parser.convertToActions("deadline"), Actions.DEADLINE);
            assertEquals(parser.convertToActions("event"), Actions.EVENT);
            assertEquals(parser.convertToActions("delete"), Actions.DELETE);
            assertEquals(parser.convertToActions("list"), Actions.LIST);
            assertEquals(parser.convertToActions("find"), Actions.FIND);
            assertEquals(parser.convertToActions("mark"), Actions.MARK);
            assertEquals(parser.convertToActions("unmark"), Actions.UNMARK);
        } catch (InvalidCommandException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void convertToActions_invalidCommand_exceptionThrown() {
        try {
            parser.convertToActions("null");
            fail("Exception should have been thrown.");
        } catch (InvalidCommandException e1) {
            try {
                parser.convertToActions("invalid");
                fail("Exception should have been thrown.");
            } catch (InvalidCommandException e2) {}
        }
    }
}
