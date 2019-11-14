package matching;

import matching.networkFlow.Vertex;
import matching.networkFlow.Network;
import matching.networkFlow.ResidualGraph;
import matching.networkFlow.Edge;
import java.util.*;
import java.io.*;

/**
 * The Class FordFulk. Contains main part of the Ford-Fulkerson implementation
 * and code for file input
 */
public class FordFulk {

    /**
     * The name of the file that encodes the given network.
     */
    private final String filename;

    /**
     * The network on which the Ford-Fulkerson algorithm is to be run.
     */
    private Network net;

	private int numStudents;
	private int numProjects;
	private int numLecturers;
	//map of lecturer labels to lower quotas, serves in creating the residual graph
	private HashMap<Integer, Integer> lecturerLowerBounds = new HashMap<Integer, Integer>();

    /**
     * Instantiates a new FordFulk object.
     *
     * @param s the name of the input file
     */
    public FordFulk(String s) {
        filename = s; // store name of input file
    }

    public Network getNetwork() {
    	return this.net;
    }
    
    /**
     * Read in network from file. See assessed exercise specification for the
     * file format.
     */
    public void readNetworkFromFile() {
        FileReader fr = null;
        Scanner in = null;
        //lookup for all the students and projects to see if they are Software engineering
        boolean[] isSE;
        // open file with name given by filename
        try {
            try {
                fr = new FileReader(filename);
                in = new Scanner(fr);

                // get number of students
                String line = in.nextLine();
                this.numStudents = Integer.parseInt(line);
                // get number of projects
                line = in.nextLine();
                this.numProjects = Integer.parseInt(line);
                // get number of lecturers
                line = in.nextLine();
                this.numLecturers = Integer.parseInt(line);
                
                int numTotalVertices = numStudents + numProjects + numLecturers + 2;
                
                //initialise SE array
                isSE = new boolean[numStudents + numProjects + 1];
                isSE[0] = false; //we want to keep the index of the array the same as the label name for students and projects so set 0 (the source) to be false (not SE)

                // create new network with desired number of vertices
                net = new Network(numTotalVertices);

                // now add the edges between the (source and students) and (students and projects) without distinguishing between SE and non SE
                for(int i = 0; i < numStudents; i++) {
                	line = in.nextLine();
                	String[] tokens = line.split(" ");
                	
                	//get student label
                	int label = Integer.parseInt(tokens[0]);
                	
                	//get if student is SE
                	isSE[label] = (tokens[1].equals("Y"))? true : false;
                	
                	//get student vertex
                	Vertex student = net.getVertexByIndex(label);

                	//link the student with their chosen projects and the source
                	int j = 2;
                    while (j < tokens.length) {
                        // get label project
                        int projectLabel = Integer.parseInt(tokens[j++]) + numStudents;
                        // get corresponding Vertex object
                        Vertex project = net.getVertexByIndex(projectLabel);
                        //get source vertex
                        Vertex source = net.getSource();
                        
                        //add edge from source to student
                        net.addEdge(source, student, 1);
                        // add edge (student, project) with capacity 1 to network 
                        net.addEdge(student, project, 1);
                        
                    }
                	
                }
                //add edges between projects and lecturers
                for(int i = numStudents; i < numStudents + numProjects; i++) {
                	line = in.nextLine();
                	String[] tokens = line.split(" ");
                	
                	int label = Integer.parseInt(tokens[0]) + numStudents;
                	
                	isSE[label] = (tokens[1].equals("Y"))? true : false;
                	
                	Vertex project = net.getVertexByIndex(label);

            		// get label of vertex v adjacent to u
                    int lecturerLabel = Integer.parseInt(tokens[2]) + numStudents + numProjects;
                    
                    //get capacity of project
                    int capacity = Integer.parseInt(tokens[3]);
                    
                    // get corresponding Vertex object
                    Vertex lecturer = net.getVertexByIndex(lecturerLabel);
                    
                    // add edge (project, lecturer) with capacity of project to network 
                    net.addEdge(project, lecturer, capacity);
                }
                //add edges from lecturers to target
                while (in.hasNextLine()) {
                    line = in.nextLine();
                    String[] tokens = line.split(" ");
                    // get lecturer number and calculate label of lecturer
                	int label = Integer.parseInt(tokens[0]) + numStudents + numProjects;
                    // get corresponding Vertex object
                    Vertex lecturer = net.getVertexByIndex(label);
                    
                    // get lower bound of lecturer
                    int lowerBound = Integer.parseInt(tokens[1]);
                    lecturerLowerBounds.put(label, lowerBound);

                    // get capacity of lecturer
                    int capacity = Integer.parseInt(tokens[2]);
                    // get sink Vertex object
                    Vertex sink = net.getSink();
                    // add edge (lecturer, target) with capacity c to network 
                    net.addEdge(lecturer, sink, capacity);
                    
                    

                }
                //remove unwanted edges for illegal projects between SE students and non-SE projects
                for(int i = 1; i < numStudents + 1; i++) {
                	if(isSE[i]) {
                		Vertex SEstudent = net.getVertexByIndex(i);
                		for(int j = 0; j < net.getAdjList(SEstudent).size(); j++) {
                			Vertex project = net.getAdjList(SEstudent).get(j);
                			int projectLabel = project.getLabel();
                			if(!isSE[projectLabel]) {
                				Edge edgeToDelete = net.getAdjMatrixEntry(SEstudent, project);
                				edgeToDelete.setCap(0); //effectively delete this edge by setting capacity to 0
                			}
                		}
                	}
                	
                }
                
            } finally {
                if (fr != null) {
                    fr.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            System.err.println("IO error:");
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Executes Ford-Fulkerson algorithm on the constructed network net.
     */
    public void fordFulkerson() {
    	while(true) {
    		ResidualGraph residualGraph = new ResidualGraph(net, this.lecturerLowerBounds);
    		LinkedList<Edge> pathToAugment = residualGraph.findAugmentingPath();
    		if(pathToAugment != null) {
    			net.augmentPath(pathToAugment);
    		}
    		else {
    			break;
    		}

    	}
    	return;
    }

    /**
     * Get the maximum flow in the network. If fordFulkerson has not been
     * called, the return value of this function is zero.
     *
     * @return the flow in the network.
     */
    public int getFlow() {
        return net.getValue();
    }
    
    private String getCharForPlurality(Edge edgeToPrint) {
    	if(edgeToPrint.getFlow() == 1) 
    		return "";
    	else 
    		return "s";
    }
    
    /**
     * Checks if all the lecturers' lower quotas are fulfilled in the network
     * 
     * @return whether or not the network respects the lower quotas
     */
    private boolean respectsLowerBounds(Network network) {
    	
    	boolean respectsLB = true;
    	Iterator<Map.Entry<Integer, Integer>> itr = this.lecturerLowerBounds.entrySet().iterator();
    	
    	while(itr.hasNext()) {
    		Map.Entry<Integer, Integer> lecturerBoundPair = (Map.Entry<Integer, Integer>)itr.next();
    		int lecturerLabel = (Integer)lecturerBoundPair.getKey();
    		Vertex lecturerVertex = network.getVertexByIndex(lecturerLabel);
    		Edge lecturerSinkEdge = network.getAdjMatrixEntry(lecturerVertex, network.getSink());
    		
    		if(lecturerSinkEdge.getFlow() < this.lecturerLowerBounds.get(lecturerLabel)) {
    			respectsLB = false;
    		}
    	}
    	
    	return respectsLB;
    }

    /**
     * Print the results of the execution of the Ford-Fulkerson algorithm.
     */
    public void printResults() {
        if (net.isFlow() && respectsLowerBounds(net)) {
        	//looping through all vertices in the graph except source and sink
        	for(int i = 1; i < net.getNumVertices() - 1; i++) {
        		
        		Vertex sourceVertex = net.getVertexByIndex(i);
        		LinkedList<Vertex> adjacentVertices = net.getAdjList(sourceVertex);
        		
        		if(i <= this.numStudents) {
        			boolean studentHasProject = false;
        			for(int j = 0; j < adjacentVertices.size(); j++) {
        				Vertex adjacentVertex = adjacentVertices.get(j);
        				if(net.getAdjMatrixEntry(sourceVertex, adjacentVertex).getFlow() == 1) {
        					System.out.printf("Student %d is assigned to project %d%n", 
        							sourceVertex.getLabel(),
        							adjacentVertex.getLabel() - this.numStudents
        							);
        					studentHasProject = true;
        					break;
        				}        				
        			}   
        			if(!studentHasProject) { //could replace with if j is at the end of range in for loop
        				System.out.printf("Student %d is unassigned%n",
        						sourceVertex.getLabel()
        						);
        			}
        		}
        		
        		else  {
        			Vertex adjacentVertex = adjacentVertices.get(0);
        			Edge edgeToPrint = net.getAdjMatrixEntry(sourceVertex, adjacentVertex);
        			if(i <= this.numProjects + this.numStudents) {
        				
        				System.out.printf("Project %d with capacity %d is assigned %d student%s%n",
        					sourceVertex.getLabel() - numStudents,
        					edgeToPrint.getCap(),
        					edgeToPrint.getFlow(),
        					getCharForPlurality(edgeToPrint)
        					);
        			}        		
        			else {
        				System.out.printf("Lecturer %d with lower quota %d and upper quota %d is assigned %d student%s%n",
            					sourceVertex.getLabel() - numStudents - numProjects,
            					this.lecturerLowerBounds.get(sourceVertex.getLabel()),
            					edgeToPrint.getCap(),
            					edgeToPrint.getFlow(),
            					getCharForPlurality(edgeToPrint)
            					);
        			}
        		}
	            if(i == this.numStudents || i == this.numProjects + this.numStudents || i == this.numProjects + this.numStudents + this.numLecturers)          
	            	System.out.println();
	              
        	}
        } else {
        	System.out.println("No assignment exists that meets all the lecturer lower quotas");

        }
    }
}
