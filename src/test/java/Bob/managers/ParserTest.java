package Bob.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        this.parser = new Parser("test_data/test_tasks.txt");
    }

    @Test
    public void createTask_noTaskName_taskNotAddedSuccessfully() {
        this.parser.createTask("E", new String[]{});
    }

    @Test
    public void createTask_noDate_taskNotAddedSucessfully() {

    }

    @Test
    public void createTask_eventUsingBy_taskNotAddedSuccessfully() {

    }

    @Test
    public void deleteTask_invalidIndex_taskNotDeletedSuccessfully() {

    }

    @Test
    public void markTask_invalidIndex_taskNotMarkedSuccessfully() {

    }

    @Test
    public void unmarkTask_invalidIndex_taskNotUnmarkedSuccessfully() {

    }

    // displayIncomingDeadlines tested by taskManager
}
