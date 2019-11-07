package matching;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author William Pettersson
 */
public class FordFulkTest {

    FordFulk instance;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public FordFulkTest() {
    }

    /**
     * Test the printResults method when we have no flow assigned.
     */
    @Test
    public void testSimplePrintResults() {
        System.setOut(new PrintStream(outContent));
        instance = new FordFulk("src/test/resources/task_3_example.txt");
        instance.readNetworkFromFile();
        instance.printResults();
        String goal = "Student 1 is unassigned\nStudent 2 is unassigned\nStudent 3 is unassigned\nStudent 4 is unassigned\nStudent 5 is unassigned\nStudent 6 is unassigned\nStudent 7 is unassigned\n\n"
        		+ "Project 1 with capacity 2 is assigned 0 students\nProject 2 with capacity 1 is assigned 0 students\nProject 3 with capacity 1 is assigned 0 students\nProject 4 with capacity 1 is assigned 0 students\nProject 5 with capacity 1 is assigned 0 students\nProject 6 with capacity 1 is assigned 0 students\nProject 7 with capacity 1 is assigned 0 students\nProject 8 with capacity 1 is assigned 0 students\n\n"
        		+ "Lecturer 1 with capacity 3 is assigned 0 students\nLecturer 2 with capacity 2 is assigned 0 students\nLecturer 3 with capacity 2 is assigned 0 students";
        String[] goals = goal.split("\\r?\\n");
        String[] out = outContent.toString().split("\\r?\\n");
        // Check first 3 lines;
        for (int i = 0; i < 3; ++i) {
            assertEquals(goals[i], out[i], "Error in line " + i + " when printing results");
        }

        // Count number of lines
        assertEquals(20, out.length, "Wrong number of lines when printing results");

        // Now check for the right lines. The assignment does not specify an
        // ordering on the output, so the test does not require one.
        List<String> outList = Arrays.asList(out);
        for (int i = 3; i < goals.length; ++i) {
            assertTrue(outList.contains(goals[i]), "Failed to find \"" + goals[i] + "\" in output when printing results, output is " + outList);
        }
        instance = null;
        System.setOut(originalOut);
    }
}
