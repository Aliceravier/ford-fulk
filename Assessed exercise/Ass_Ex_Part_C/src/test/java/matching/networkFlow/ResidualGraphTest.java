package matching.networkFlow;

import java.io.Console;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author William Pettersson
 */
public class ResidualGraphTest {

    Network net;
    ResidualGraph rg;
    Vertex start;
    Vertex end;
    HashMap<Integer, Integer> lecturerLowerBounds = new HashMap<Integer, Integer>();

    public ResidualGraphTest() {
    }

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

    @AfterEach
    public void tearDown() {
        net = null;
        start = null;
        end = null;
    }

    /**
     * Test constructor when no flow present.
     */
    @Test
    public void testConstructorNoFlow() {   	
        rg = new ResidualGraph(net, lecturerLowerBounds);        
        assertEquals(rg.getEdgeCapacity(start, end), 3);
    }

    /**
     * Test constructor when a simple flow present.
     */
    @Test
    public void testConstructorSimpleFlow() {
        net.setFlow(start, end, 2);
        rg = new ResidualGraph(net, lecturerLowerBounds);
        assertEquals(rg.getEdgeCapacity(start, end), 1, "Forward flow incorrect");
        assertEquals(rg.getEdgeCapacity(end, start), 2, "Reverse flow incorrect");
    }

    /**
     * Test the search for an augmenting path.
     */
    @Test
    public void testAugmentPathNoFlow() {
        rg = new ResidualGraph(net, lecturerLowerBounds);
        LinkedList<Edge> result = rg.findAugmentingPath();
        assertEquals(result.size(), 1);
        Edge e = result.getFirst();
        assertEquals(e.getSourceVertex(), start);
        assertEquals(e.getTargetVertex(), end);
    }
}
