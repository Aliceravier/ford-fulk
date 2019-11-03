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
        instance = new FordFulk("src/test/resources/tasks_1-2_example.txt");
        instance.readNetworkFromFile();
        instance.printResults();
        String goal = "The assignment is a valid flow\nA maximum flow has value: 0\nThe flows along the edges are as follows:\n(0,1) 2/0\n(0,3) 4/0\n(1,2) 1/0\n(1,4) 3/0\n(2,5) 2/0\n(3,2) 3/0\n(3,4) 1/0\n(4,3) 1/0\n(4,5) 4/0\n";
        String[] goals = goal.split("\\r?\\n");
        String[] out = outContent.toString().split("\\r?\\n");
        // Check first 3 lines;
        for (int i = 0; i < 3; ++i) {
            assertEquals(goals[i], out[i], "Error in line " + i + " when printing results");
        }

        // Count number of lines
        assertEquals(12, out.length, "Wrong number of lines when printing results");

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
