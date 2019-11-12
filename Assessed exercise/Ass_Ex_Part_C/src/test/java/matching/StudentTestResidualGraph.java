package matching;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import matching.networkFlow.Network;
import matching.networkFlow.ResidualGraph;
import matching.networkFlow.Vertex;
import matching.networkFlow.Edge;

public class StudentTestResidualGraph {
	
	Network net;
    Vertex start;
    Vertex project1;
    Vertex project2;
    Vertex lecturer1;
    Vertex lecturer2;
    Vertex end;
    ResidualGraph rg;
    HashMap<Integer, Integer> lecturerLowerBounds = new HashMap<Integer, Integer>();
    
    
    public StudentTestResidualGraph() {
    }
    
	/**
     * This setup is run before each test.
     */
    @BeforeEach
    public void setUp() {
        // Create a network with 6 vertices
        net = new Network(6);
        // Label the six vertices
        start = new Vertex(0);
        project1 = new Vertex(1);
        project2 = new Vertex(2);
        lecturer1 = new Vertex(3);
        lecturer2 = new Vertex(4);
        end = new Vertex(5);
        // Add edges
        net.addEdge(start, project1, 1);
        net.addEdge(start, project2, 1);

        net.addEdge(project1, lecturer1, 1);
        net.addEdge(project2, lecturer2, 1);
        net.addEdge(project2, lecturer1, 1);
        
        net.addEdge(lecturer1, end, 2);
        net.addEdge(lecturer2, end, 2);
    }
    
    /**
     * This tearDown is run after each test, to reset the testing environment.
     */
    @AfterEach
    public void tearDown() {
        // Delete the network
        net = null;
        start = null;
        project1 = null;
        project2 = null;
        lecturer1 = null;
        lecturer2 = null;
        end = null;
    }
    
    /**
     * Test the search for an augmenting path.
     */
    @Test
    public void testAugmentPathCap() {
        rg = new ResidualGraph(net, lecturerLowerBounds);
        LinkedList<Edge> result = rg.findAugmentingPath();
        assertEquals(3, result.size());
        Edge e = result.getFirst();
        assertEquals(1, e.getCap());
    }
    
    /**
     * Test residual graph is constructed properly when a lecturer's lower bound isn't met
     */
    @Test
    public void buildCorrectResidualGraph() {
    	net.setFlow(start, project1, 1);
    	net.setFlow(project1, lecturer1, 1);
    	net.setFlow(lecturer1, end, 1);
    	lecturerLowerBounds.put(3, 1);
    	lecturerLowerBounds.put(4, 1);
    	rg = new ResidualGraph(net, lecturerLowerBounds);
    	LinkedList<Edge> result = rg.findAugmentingPath();
    	assertEquals(3, result.size());
        Edge e = result.getLast();
        assertEquals(4, e.getSourceVertex().getLabel());
    }
}
