package matching.networkFlow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author William Pettersson
 */
public class NetworkTest {

    Network net;
    Vertex start;
    Vertex end;

    public NetworkTest() {
    }

    /**
     * This setup is run before each test.
     */
    @BeforeEach
    public void setUp() {
        // Create a network with 2 vertices
        net = new Network(2);
        // Label the two vertices as start and end
        start = new Vertex(0);
        end = new Vertex(1);
        // Add one edge between the two vertices, with capacity 3
        net.addEdge(start, end, 3);
    }

    /**
     * This tearDown is run after each test, to reset the testing environment.
     */
    @AfterEach
    public void tearDown() {
        // Delete the network
        net = null;
        start = null;
        end = null;
    }

    /**
     * Test that zero flow is considered valid.
     */
    @Test
    public void testZeroFlow() {
        // Set the flow to 0, which should be okay.
        net.setFlow(start, end, 0);
        assertTrue(net.isFlow(), "Zero flow counted as invalid");
    }

    /**
     * Test that if the flow is too high, it is considered invalid.
     */
    @Test
    public void testFlowTooHigh() {
        // Set the flow to 5, which should be invalid.
        net.setFlow(start, end, 5);
        assertFalse(net.isFlow(), "Flow too high not detected");
    }

}
