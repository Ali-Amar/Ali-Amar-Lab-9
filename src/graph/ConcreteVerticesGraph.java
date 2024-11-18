package graph;

import java.util.*;

/**
 * An implementation of Graph using vertices that store their edges.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed weighted graph where each vertex maintains its own edges
    
    // Representation invariant:
    //   - vertices is not null
    //   - no vertex has null label
    //   - no duplicate vertex labels
    //   - no negative edge weights
    
    // Safety from rep exposure:
    //   - all fields are private and final
    //   - vertices() returns an unmodifiable set
    //   - sources() and targets() return new maps
    
    public ConcreteVerticesGraph() {
        checkRep();
    }
    
    private void checkRep() {
        assert vertices != null;
        Set<String> labels = new HashSet<>();
        for (Vertex v : vertices) {
            assert v.getLabel() != null;
            assert labels.add(v.getLabel()); // ensures no duplicates
        }
    }
    
    @Override 
    public boolean add(String vertex) {
        if (vertex == null) return false;
        
        // Check if vertex already exists
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                return false;
            }
        }
        
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override 
    public int set(String source, String target, int weight) {
        if (weight < 0) throw new IllegalArgumentException("Weight must be non-negative");
        
        // Add vertices if they don't exist
        add(source);
        add(target);
        
        Vertex sourceVertex = null;
        for (Vertex v : vertices) {
            if (v.getLabel().equals(source)) {
                sourceVertex = v;
                break;
            }
        }
        
        int oldWeight = sourceVertex.setEdge(target, weight);
        checkRep();
        return oldWeight;
    }
    
    @Override 
    public boolean remove(String vertex) {
        // Remove the vertex and all edges to/from it
        boolean found = false;
        Iterator<Vertex> it = vertices.iterator();
        while (it.hasNext()) {
            Vertex v = it.next();
            if (v.getLabel().equals(vertex)) {
                it.remove();
                found = true;
            } else {
                v.removeEdge(vertex);
            }
        }
        checkRep();
        return found;
    }
    
    @Override 
    public Set<String> vertices() {
        Set<String> result = new HashSet<>();
        for (Vertex v : vertices) {
            result.add(v.getLabel());
        }
        return Collections.unmodifiableSet(result);
    }
    
    @Override 
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex v : vertices) {
            Integer weight = v.getEdgeWeight(target);
            if (weight != null) {
                sources.put(v.getLabel(), weight);
            }
        }
        return sources;
    }
    
    @Override 
    public Map<String, Integer> targets(String source) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(source)) {
                return v.getTargets();
            }
        }
        return new HashMap<>();
    }
    
    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Vertex v : vertices) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Mutable class representing a vertex in the graph.
 */
class Vertex {
    private final String label;
    private final Map<String, Integer> edges = new HashMap<>();
    
    // Abstraction function:
    //   Represents a vertex with label and its outgoing edges
    //   where edges maps target vertex labels to edge weights
    
    // Representation invariant:
    //   - label is not null
    //   - edges is not null
    //   - all edge weights are positive
    
    // Safety from rep exposure:
    //   - label is private and final
    //   - edges is private and final
    //   - getTargets returns a copy of the edges map
    
    public Vertex(String label) {
        if (label == null) throw new IllegalArgumentException("Label cannot be null");
        this.label = label;
        checkRep();
    }
    
    private void checkRep() {
        assert label != null;
        assert edges != null;
        for (Integer weight : edges.values()) {
            assert weight > 0;
        }
    }
    
    public String getLabel() {
        return label;
    }
    
    public int setEdge(String target, int weight) {
        Integer oldWeight = edges.get(target);
        if (weight == 0) {
            edges.remove(target);
        } else {
            edges.put(target, weight);
        }
        checkRep();
        return oldWeight != null ? oldWeight : 0;
    }
    
    public void removeEdge(String target) {
        edges.remove(target);
        checkRep();
    }
    
    public Integer getEdgeWeight(String target) {
        return edges.get(target);
    }
    
    public Map<String, Integer> getTargets() {
        return new HashMap<>(edges);
    }
    
    @Override 
    public String toString() {
        return label + " -> " + edges.toString();
    }
}