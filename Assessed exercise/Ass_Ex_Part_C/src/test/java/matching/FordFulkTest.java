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
        instance = new FordFulk("src/test/resources/task_4_example.txt");
        instance.readNetworkFromFile();
        instance.printResults();
        String goal = "No assignment exists that meets all the lecturer lower quotas";
        String[] out = outContent.toString().split("\\r?\\n");
        assertEquals(goal, out[0], "Error when printing results");

        // Count number of lines
        assertEquals(1, out.length, "Wrong number of lines when printing results");

        instance = null;
        System.setOut(originalOut);
    }
}
