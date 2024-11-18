package graph;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   empty graph
    //   graph with single vertex
    //   graph with multiple vertices and edges
    
    @Test
    public void testToStringEmpty() {
        Graph<String> graph = emptyInstance();
        assertTrue("empty graph toString incorrect",
                  graph.toString().contains("Graph:"));
    }
    
    @Test
    public void testToStringWithVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertTrue("vertex not in toString",
                  graph.toString().contains("A"));
    }
    
    @Test
    public void testToStringWithEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("B", "C", 2);
        String result = graph.toString();
        assertTrue("edges not in toString",
                  result.contains("A") && result.contains("B") && 
                  result.contains("C") && result.contains("1") && 
                  result.contains("2"));
    }
    
    // Testing strategy for Vertex
    //   constructor:
    //     - valid label
    //     - null label
    //   operations:
    //     - setEdge: new edge, update edge, remove edge
    //     - removeEdge: existing edge, non-existing edge
    //     - getEdgeWeight: existing edge, non-existing edge
    //     - getTargets: no edges, one edge, multiple edges
    
    @Test
    public void testVertexValid() {
        Vertex vertex = new Vertex("A");
        assertEquals("label incorrect", "A", vertex.getLabel());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testVertexNullLabel() {
        new Vertex(null);
    }
    
    @Test
    public void testVertexSetEdge() {
        Vertex vertex = new Vertex("A");
        assertEquals("expected 0 for new edge", 
                    0, vertex.setEdge("B", 1));
        assertEquals("edge weight incorrect", 
                    1, (int)vertex.getEdgeWeight("B"));
    }
    
    @Test
    public void testVertexRemoveEdge() {
        Vertex vertex = new Vertex("A");
        vertex.setEdge("B", 1);
        vertex.removeEdge("B");
        assertNull("edge not removed", 
                  vertex.getEdgeWeight("B"));
    }
    
    @Test
    public void testVertexGetTargets() {
        Vertex vertex = new Vertex("A");
        vertex.setEdge("B", 1);
        vertex.setEdge("C", 2);
        Map<String, Integer> targets = vertex.getTargets();
        assertEquals("targets size incorrect", 
                    2, targets.size());
        assertEquals("target weight incorrect", 
                    1, (int)targets.get("B"));
        assertEquals("target weight incorrect", 
                    2, (int)targets.get("C"));
    }
}