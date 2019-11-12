package matching.networkFlow;

/**
 * The Class Vertex. Represents a vertex in a directed graph.
 */
public class Vertex {

    /**
     * Stores an integer label associated with the vertex.
     */
    protected int label;

    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Vertex(int i) {
        label = i;
    }

    /**
     * Gets the vertex label.
     *
     * @return the vertex label
     */
    public int getLabel() {
        return label;
    }

    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + label;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (label != other.label)
			return false;
		return true;
	}

	public String toString() {
    	return "" + this.getLabel();
    }
}
