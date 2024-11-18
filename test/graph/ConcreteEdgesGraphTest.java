package graph;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   empty graph
    //   graph with vertices only
    //   graph with vertices and edges
    
    @Test
    public void testToStringEmpty() {
        Graph<String> graph = emptyInstance();
        assertTrue("empty graph toString incorrect",
                  graph.toString().contains("Vertices: []"));
    }
    
    @Test
    public void testToStringVerticesOnly() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        String result = graph.toString();
        assertTrue("vertices not in toString",
                  result.contains("A") && result.contains("B"));
    }
    
    @Test
    public void testToStringWithEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        String result = graph.toString();
        assertTrue("edge not in toString",
                  result.contains("A -> B (1)"));
    }
    
    // Testing strategy for Edge
    //   constructor:
    //     - valid inputs
    //     - null source/target
    //     - negative weight
    //   methods:
    //     - getSource, getTarget, getWeight
    //     - toString
    
    @Test
    public void testEdgeConstructorValid() {
        Edge edge = new Edge("A", "B", 1);
        assertEquals("source incorrect", "A", edge.getSource());
        assertEquals("target incorrect", "B", edge.getTarget());
        assertEquals("weight incorrect", 1, edge.getWeight());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEdgeConstructorNullSource() {
        new Edge(null, "B", 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEdgeConstructorNullTarget() {
        new Edge("A", null, 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEdgeConstructorNegativeWeight() {
        new Edge("A", "B", -1);
    }
    
    @Test
    public void testEdgeToString() {
        Edge edge = new Edge("A", "B", 1);
        assertEquals("toString format incorrect",
                    "A -> B (1)", edge.toString());
    }
}