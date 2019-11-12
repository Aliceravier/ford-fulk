package matching;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import matching.networkFlow.Network;
import matching.networkFlow.Vertex;
import matching.networkFlow.Edge;

public class StudentTestNetwork {
	
	Network net;
    Vertex start;
    Vertex middle;
    Vertex end;
    
    
    public StudentTestNetwork() {
    }
    
	/**
     * This setup is run before each test.
     */
    @BeforeEach
    public void setUp() {
        // Create a network with 3 vertices
        net = new Network(3);
        // Label the three vertices as start, middle and end
        start = new Vertex(0);
        middle = new Vertex(1);
        end = new Vertex(2);
        // Add one edge between start and middle, with capacity 3
        net.addEdge(start, middle, 3);
     // Add one edge between middle and end, with capacity 4
        net.addEdge(middle, end, 4);
    }
    
    /**
     * This tearDown is run after each test, to reset the testing environment.
     */
    @AfterEach
    public void tearDown() {
        // Delete the network
        net = null;
        start = null;
        middle = null;
        end = null;
    }
    
    @Test
    public void testGetValue() {
    	net.setFlow(start, middle, 2);
    	net.setFlow(middle, end, 2);
    	assertEquals(2, net.getValue());
    }
    
    @Test
    public void testAugmentPath() {
    	List<Edge> path = new LinkedList<Edge>();
    	path.add(new Edge(start, middle, 2));
    	path.add(new Edge(middle, end, 3));
    	net.augmentPath(path);
    	assertEquals(2, net.getAdjMatrixEntry(middle, end).getFlow());
    }
}
