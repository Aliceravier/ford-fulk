package matching.networkFlow;

import java.util.*;

/**
 * The Class Network. Represents a network - inherits from DirectedGraph class.
 */
public class Network extends DirectedGraph {

    /**
     * The source vertex of the network.
     */
    protected Vertex source;

    /**
     * The label of the source vertex.
     */
    protected int sourceLabel;

    /**
     * The sink vertex of the network.
     */
    protected Vertex sink;

    /**
     * The label of the sink vertex.
     */
    protected int sinkLabel;

    /**
     * Instantiates a new network.
     *
     * @param n the number of vertices
     */
    public Network(int n) {
        super(n);

        // add the source vertex - assumed to have label 0
        sourceLabel = 0;
        source = addVertex(sourceLabel);
        // add the sink vertex - assumed to have label numVertices - 1
        sinkLabel = numVertices - 1;
        sink = addVertex(sinkLabel);

        // add the remaining vertices
        for (int i = 1; i <= numVertices - 2; i++) {
            addVertex(i);
        }
    }

    /**
     * Gets the source vertex.
     *
     * @return the source vertex
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Gets the sink vertex.
     *
     * @return the sink vertex
     */
    public Vertex getSink() {
        return sink;
    }

    /**
     * Adds the edge with specified source and target vertices and capacity.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param capacity the capacity of the edge
     */
    public void addEdge(Vertex sourceEndpoint, Vertex targetEndpoint, int capacity) {
        Edge e = new Edge(sourceEndpoint, targetEndpoint, capacity);
        adjLists.get(sourceEndpoint.getLabel()).addLast(targetEndpoint);
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()] = e;
    }

    /**
     * Set the flow on a given edge. This does not, and should not, do any
     * checking for validity of the input flow.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param flow the flow of the edge
     */
    public void setFlow(Vertex sourceEndpoint, Vertex targetEndpoint, int flow) {
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].setFlow(flow);
    }

    /**
     * Get the capacity along a given edge.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @return the capacity of the given edge
     */
    public int getEdgeCapacity(Vertex sourceEndpoint, Vertex targetEndpoint) {
        return adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].getCap();
    }

    /**
     * Calculates by how much the flow along the given path can be increased,
     * and then augments the network along this path by this amount.
     *
     * @param path a list of edges along which the flow should be augmented
     */
    public void augmentPath(List<Edge> path) {
    	
    	// figure out minimum flow
    	int minFlow = path.get(0).getCap();
    	
    	for(int i = 1; i < path.size(); i++) {
    		int possibleMinFlow = path.get(i).getCap();
			if(possibleMinFlow < minFlow) {
    			minFlow = possibleMinFlow;
    		}
    	}
    	
    	// augment by that along path
    	for (int i=0; i < path.size(); i++) {
    		Edge edgeToAugment = adjMatrix[path.get(i).getSourceVertex().getLabel()][path.get(i).getTargetVertex().getLabel()];
   		
    		// if edge is in the network (adjMatrix) then add minFlow to flow
    		if (edgeToAugment != null) {
    			int flowOfEdgeToAugment = edgeToAugment.getFlow();
    			setFlow(edgeToAugment.getSourceVertex(), edgeToAugment.getTargetVertex(), flowOfEdgeToAugment + minFlow);
    		}
    		// otherwise the edge is in the other direction and we should remove minFlow from flow
    		else {
    			Edge backwardsEdgeToAugment = adjMatrix[path.get(i).getTargetVertex().getLabel()][path.get(i).getSourceVertex().getLabel()];
    			int flowOfEdgeToAugment = backwardsEdgeToAugment.getFlow();
    			setFlow(backwardsEdgeToAugment.getSourceVertex(), backwardsEdgeToAugment.getTargetVertex(), flowOfEdgeToAugment - minFlow);
    		}
    	}  	    	
    }

    /**
     * Returns true if and only if the assignment of integers to the flow fields
     * of each edge in the network is a valid flow.
     *
     * @return true, if the assignment is a valid flow
     */
    public boolean isFlow() {
    	Boolean isValid = true;
    	
        // for each edge check the flow is below the capacity
    	for(int i = 0; i < adjMatrix.length; i++) {
	    	for(int j = 0; j < adjMatrix[i].length; j++) {
	    		if((adjMatrix[i][j] != null) && (adjMatrix[i][j].getFlow() > adjMatrix[i][j].getCap())) {
	    			return false;
	    		}
	    	}
    	}
    	
    	// for source check there is no incoming flow
    	int vertexLabel = 0;
    	int incomingFlow = 0;
		for (int j = 0; j < adjMatrix.length; j++) {
			if(adjMatrix[j][vertexLabel] != null) {
				incomingFlow += adjMatrix[j][vertexLabel].getFlow();
			}
		}
		if(incomingFlow != 0) {
			return false;
		}
		
		//for target vertex check outgoing flow is zero
		vertexLabel = vertices.length - 1;
		int outgoingFlow = 0;
		for (int j = 0; j < adjMatrix[vertexLabel].length; j++) {
			if(adjMatrix[vertexLabel][j] != null) {
				outgoingFlow += adjMatrix[vertexLabel][j].getFlow();
			}
		}
		if(outgoingFlow != 0) {
			return false;
		}
    	
    	// for each vertex except the source and target check incoming flow is equal to outgoing flow
    	for (int i = 1; i < vertices.length - 1; i++) {
    		vertexLabel = vertices[i].getLabel();
    		//outgoing flow
    		outgoingFlow = 0;
    		for (int j = 0; j < adjMatrix[vertexLabel].length; j++) {
    			if(adjMatrix[vertexLabel][j] != null) {
    				outgoingFlow += adjMatrix[vertexLabel][j].getFlow();
    			}
    		}
    		//incoming flow
    		incomingFlow = 0;
    		for (int j = 0; j < adjMatrix.length; j++) {
    			if(adjMatrix[j][vertexLabel] != null) {
    				incomingFlow += adjMatrix[j][vertexLabel].getFlow();
    			}
    		}
    		if(incomingFlow != outgoingFlow) {
    			return false;
    		}
    	}
        return isValid;
    }

    /**
     * Gets the value of the flow.
     *
     * @return the value of the flow
     */
    public int getValue() { 

    	int totalFlow = 0;
    	//total flow is just the flow going out of the source vertex (vertex 0) (assuming the flow is valid)
    	for(int i = 0; i < adjMatrix[0].length; i++) {
    		if(adjMatrix[0][i] != null) {
    			totalFlow += adjMatrix[0][i].getFlow();
    		}
    	}

        return totalFlow;
    }

    /**
     * Prints the flow. Display the flow through the network in the following
     * format: (u,v) c(u,v)/f(u,v) where (u,v) is an edge, c(u,v) is the
     * capacity of that edge and f(u,v) is the flow through that edge - one line
     * for each edge in the network
     */
    public void printFlow() {
    	for(int i = 0; i < adjMatrix.length; i++) {
	    	for(int j = 0; j < adjMatrix[i].length; j++) {
	    		Edge currentEdge = adjMatrix[i][j];
	    		if(currentEdge != null) {
	    			System.out.printf("(%d,%d) %d/%d%n", 
	    					currentEdge.getSourceVertex().getLabel(),
	    					currentEdge.getTargetVertex().getLabel(),
	    					currentEdge.getCap(),
	    					currentEdge.getFlow()
	    					);
	    		}
	    	}
    	}
    }
}
