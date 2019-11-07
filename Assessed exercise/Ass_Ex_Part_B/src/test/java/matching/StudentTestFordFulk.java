package matching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import matching.networkFlow.Edge;
import matching.networkFlow.Network;

public class StudentTestFordFulk {

	FordFulk instance;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    public StudentTestFordFulk() {
    }
    
    /**
     * Test the fordFulkerson method.
     */
    @Test
    public void testSmallFordfulkerson() {
    	System.setOut(new PrintStream(outContent));
        instance = new FordFulk("src/test/resources/task_3_student_test.txt");
        instance.readNetworkFromFile();
        instance.fordFulkerson();
        instance.printResults();
        String goal = 
        		"Student 1 is unassigned\r\n" + 
        		"Student 2 is assigned to project 1\r\n" + 
        		"Student 3 is assigned to project 2\r\n" +  
        		"\r\n" + 
        		"Project 1 with capacity 2 is assigned 1 student\r\n" + 
        		"Project 2 with capacity 1 is assigned 1 student\r\n" +  
        		"\r\n" + 
        		"Lecturer 1 with capacity 3 is assigned 2 students" ;
        String[] goals = goal.split("\\r?\\n");
        String[] out = outContent.toString().split("\\r?\\n");
        assertEquals(8, out.length, "Wrong number of lines when printing results");

        for (int i = 0; i < 8; ++i) {
            assertEquals(goals[i], out[i], "Error in line " + i + " when printing results");
        }
        instance = null;
        System.setOut(originalOut);
    }
    
    /**
     * Test edge between SE student and non-SE project has capacity 0 if student picked that project.
     */
    @Test
    public void testNoIllegalSEProject() {
    	System.setOut(new PrintStream(outContent));
        instance = new FordFulk("src/test/resources/task_3_student_test.txt");
        instance.readNetworkFromFile();
        instance.fordFulkerson();
        Network network = instance.getNetwork();
        Edge illegalEdge = network.getAdjMatrixEntry(network.getVertexByIndex(1), network.getVertexByIndex(4));
        assertEquals(0, illegalEdge.getCap());
    }
    
    /**
     * Test the fordFulkerson method.
     */
    @Test
    public void testFordfulkerson() {
        System.setOut(new PrintStream(outContent));
        instance = new FordFulk("src/test/resources/task_3_example.txt");
        instance.readNetworkFromFile();
        instance.fordFulkerson();
        instance.printResults();
        String goal = "Student 1 is assigned to project 1\r\n" + 
        		"Student 2 is assigned to project 1\r\n" + 
        		"Student 3 is assigned to project 2\r\n" + 
        		"Student 4 is unassigned\r\n" + 
        		"Student 5 is assigned to project 4\r\n" + 
        		"Student 6 is assigned to project 5\r\n" + 
        		"Student 7 is assigned to project 8\r\n" + 
        		"\r\n" + 
        		"Project 1 with capacity 2 is assigned 2 students\r\n" + 
        		"Project 2 with capacity 1 is assigned 1 student\r\n" + 
        		"Project 3 with capacity 1 is assigned 0 students\r\n" + 
        		"Project 4 with capacity 1 is assigned 1 student\r\n" + 
        		"Project 5 with capacity 1 is assigned 1 student\r\n" + 
        		"Project 6 with capacity 1 is assigned 0 students\r\n" + 
        		"Project 7 with capacity 1 is assigned 0 students\r\n" + 
        		"Project 8 with capacity 1 is assigned 1 student\r\n" + 
        		"\r\n" + 
        		"Lecturer 1 with capacity 3 is assigned 3 students\r\n" + 
        		"Lecturer 2 with capacity 2 is assigned 2 students\r\n" + 
        		"Lecturer 3 with capacity 2 is assigned 1 student";
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
