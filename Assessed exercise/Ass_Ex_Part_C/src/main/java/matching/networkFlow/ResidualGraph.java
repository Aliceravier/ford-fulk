package matching.networkFlow;

import java.util.*;

/**
 * The Class ResidualGraph. Represents the residual graph corresponding to a
 * given network.
 */
public class ResidualGraph extends Network {

    /**
     * Instantiates a new ResidualGraph object. Builds the residual graph
     * corresponding to the given network net. Residual graph has the same
     * number of vertices as net.
     *
     * @param net the network
     */
    public ResidualGraph(Network net, HashMap<Integer, Integer> lecturerLowerBounds) {
        super(net.numVertices);
        
        int lecturerToAugment = -1; // used to check if a lecturer is below their lower quota
        // for each edge check if something should be added to the residual graph
        for(int i = 0; i < adjMatrix.length; i++) {
	    	for(int j = 0; j < adjMatrix[i].length; j++) {

	    		Edge currentEdge = net.adjMatrix[i][j];
	    		
	    		if(currentEdge == null) {
	    			continue;
	    		}
	    		
	    		if (lecturerLowerBounds.containsKey(currentEdge.getSourceVertex().getLabel()))
	    		{
	    			if (currentEdge.getFlow() < lecturerLowerBounds.get(currentEdge.getSourceVertex().getLabel()))
	    			{
	    				lecturerToAugment = currentEdge.getSourceVertex().getLabel();
	    			}
	    		}	    		
	    		
	    		
	    		// make a backwards edge if flow is higher than 0 with a capacity of flow
	    		if(currentEdge.getFlow() > 0) {    			
	    			addEdge(currentEdge.getTargetVertex(), currentEdge.getSourceVertex(), currentEdge.getFlow());
	    		}
	    		// make a forwards edge if capacity - flow is higher than 0 with that capacity
	    		int possibleForwardsFlow = currentEdge.getCap() - currentEdge.getFlow();
	    		if( possibleForwardsFlow > 0) {
	    			addEdge(currentEdge.getSourceVertex(), currentEdge.getTargetVertex(), possibleForwardsFlow);	    				    	        
	    		}
	    	}
	    	//if there is a lecturer which doesn't have their lower bound respected, remove the edges for all the other lecturers to the sink
	    	if(lecturerToAugment != -1) {
	        	Iterator<Map.Entry<Integer, Integer>> itr = lecturerLowerBounds.entrySet().iterator();
	        	
	        	while(itr.hasNext()) {
	        		Map.Entry<Integer, Integer> lecturerBoundPair = (Map.Entry<Integer, Integer>)itr.next();
	        		int lecturerLabel = (Integer)lecturerBoundPair.getKey();
	        		
	        		if(lecturerLabel != lecturerToAugment) {
	        			//delete lecturer -> sink edge
	        			this.adjMatrix[lecturerLabel][net.vertices.length - 1] = null;
	        			this.adjLists.get(lecturerLabel).remove(net.getSink());	        			
	        		}
	        		
	        	}
	        	
	        }
	        	
    	}
        
        
        
        
    }

    /**
     * Find an augmenting path if one exists. Determines whether there is a
     * directed path from the source to the sink in the residual graph -- if so,
     * return a linked list containing the edges in the augmenting path in the
     * form (s,v_1), (v_1,v_2), ..., (v_{k-1},v_k), (v_k,t); if not, return an
     * empty linked list.
     *
     * @return the linked list
     */
    public LinkedList<Edge> findAugmentingPath() {
    	
    	Vertex currentVertex = vertices[0]; 
    	LinkedList<Edge> path = new LinkedList<Edge>();
    	Queue<Vertex> search = new LinkedList<Vertex>();
    	Vertex[] predecessors = new Vertex[vertices.length];
    	boolean foundTarget = false;
    	
    	//breadth first search through the graph
    	while ((currentVertex.getLabel() != adjLists.size() - 1) && !foundTarget) {
    		
    		for (int i = 0; i < adjLists.get(currentVertex.getLabel()).size(); i++) {
    			Vertex linkedVertex = adjLists.get(currentVertex.getLabel()).get(i);
    			
    			if(predecessors[linkedVertex.getLabel()] != null) {
    				continue;
    			}
    			search.add(linkedVertex);
    			predecessors[linkedVertex.getLabel()] = currentVertex;
    			
    			if(linkedVertex.getLabel() == vertices.length - 1) {
    				foundTarget = true;
    				break;
    			}
    		}
    		
    		if(search.isEmpty() == true) { //there is no augmenting path so we return null
    			return null;
    		}
    		else {
    			currentVertex = search.remove();
    		}
    	}
    	
    	//use predecessors to reconstruct the path backwards
    	Stack<Edge> invPath = new Stack<Edge>();
    	Vertex current = vertices[vertices.length - 1];
    	Vertex predecessor = predecessors[current.getLabel()];
    	
    	while(true) {
    		invPath.push(adjMatrix[predecessor.getLabel()][current.getLabel()]);
    		current = predecessor;
    		if(current.getLabel() == 0) {
    			break;
    		}
    		predecessor = predecessors[current.getLabel()];   
    	}
    	
    	//reconstruct the path in the right direction by switching the order
    	while(invPath.size() > 0) {
    		path.add(invPath.pop());
    	}
    	    	    	   	
        return path;
    }
}
