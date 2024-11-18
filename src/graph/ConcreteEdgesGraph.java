package graph;

import java.util.*;

/**
 * An implementation of Graph using a list of edges and a set of vertices.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed weighted graph where:
    //   - vertices represents the set of vertices in the graph
    //   - edges represents the list of directed weighted edges between vertices
    
    // Representation invariant:
    //   - vertices is not null
    //   - edges is not null
    //   - no edge weight is negative
    //   - all vertices mentioned in edges exist in vertices set
    //   - no duplicate edges (same source and target)
    
    // Safety from rep exposure:
    //   - all fields are private and final
    //   - vertices() returns an unmodifiable set
    //   - sources() and targets() return new maps
    
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    private void checkRep() {
        assert vertices != null;
        assert edges != null;
        for (Edge edge : edges) {
            assert edge.getWeight() >= 0;
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
        }
    }
    
    @Override 
    public boolean add(String vertex) {
        if (vertex == null) return false;
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }
    
    @Override 
    public int set(String source, String target, int weight) {
        if (weight < 0) throw new IllegalArgumentException("Weight must be non-negative");
        
        // Add vertices if they don't exist
        add(source);
        add(target);
        
        // Find existing edge
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int oldWeight = edge.getWeight();
                if (weight == 0) {
                    edges.remove(i);
                } else {
                    edges.set(i, new Edge(source, target, weight));
                }
                checkRep();
                return oldWeight;
            }
        }
        
        // No existing edge found
        if (weight != 0) {
            edges.add(new Edge(source, target, weight));
        }
        checkRep();
        return 0;
    }
    
    @Override 
    public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) return false;
        
        // Remove all edges containing this vertex
        edges.removeIf(edge -> 
            edge.getSource().equals(vertex) || edge.getTarget().equals(vertex)
        );
        
        vertices.remove(vertex);
        checkRep();
        return true;
    }
    
    @Override 
    public Set<String> vertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
    @Override 
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        return sources;
    }
    
    @Override 
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targets;
    }
    
    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder("Vertices: " + vertices.toString() + "\n");
        sb.append("Edges:\n");
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Immutable class representing a directed edge in the graph.
 */
class Edge {
    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed edge from source to target with a weight
    
    // Representation invariant:
    //   - source is not null
    //   - target is not null
    //   - weight is non-negative
    
    // Safety from rep exposure:
    //   - all fields are private and final
    //   - all fields are immutable types
    
    public Edge(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target cannot be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }
    
    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight >= 0;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public int getWeight() {
        return weight;
    }
    
    @Override 
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}