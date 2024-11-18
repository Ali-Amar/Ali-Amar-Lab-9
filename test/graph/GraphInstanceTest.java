package graph;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public abstract class GraphInstanceTest {
    
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // Tests for add()
    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected true when adding new vertex", graph.add("A"));
        assertTrue("vertex not found after adding", graph.vertices().contains("A"));
    }
    
    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("expected false when adding duplicate vertex", graph.add("A"));
    }
    
    // Tests for set()
    @Test
    public void testSetNewEdge() {
        Graph<String> graph = emptyInstance();
        assertEquals("expected 0 when adding new edge", 
                    0, graph.set("A", "B", 1));
        assertEquals("edge weight incorrect", 
                    1, (int)graph.targets("A").get("B"));
    }
    
    @Test
    public void testSetExistingEdge() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        assertEquals("expected 1 when updating edge", 
                    1, graph.set("A", "B", 2));
    }
    
    @Test
    public void testSetZeroWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        assertEquals("expected 1 when removing edge", 
                    1, graph.set("A", "B", 0));
        assertTrue("edge not removed", 
                  graph.targets("A").isEmpty());
    }
    
    // Tests for remove()
    @Test
    public void testRemoveExistingVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertTrue("expected true when removing existing vertex", 
                  graph.remove("A"));
        assertFalse("vertex found after removal", 
                   graph.vertices().contains("A"));
    }
    
    @Test
    public void testRemoveVertexWithEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("B", "C", 1);
        graph.set("C", "A", 1);
        
        assertTrue("expected true when removing vertex", 
                  graph.remove("B"));
        assertTrue("edge from removed vertex still exists", 
                  graph.targets("B").isEmpty());
        assertTrue("edge to removed vertex still exists", 
                  graph.sources("B").isEmpty());
    }
    
    // Tests for vertices()
    @Test
    public void testVerticesMultiple() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        
        Set<String> expected = new HashSet<>(Arrays.asList("A", "B", "C"));
        assertEquals("vertices set incorrect", 
                    expected, graph.vertices());
    }
    
    // Tests for sources() and targets()
    @Test
    public void testSourcesAndTargets() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("C", "B", 2);
        graph.set("A", "C", 3);
        
        Map<String, Integer> expectedSources = new HashMap<>();
        expectedSources.put("A", 1);
        expectedSources.put("C", 2);
        assertEquals("sources map incorrect", 
                    expectedSources, graph.sources("B"));
        
        Map<String, Integer> expectedTargets = new HashMap<>();
        expectedTargets.put("B", 1);
        expectedTargets.put("C", 3);
        assertEquals("targets map incorrect", 
                    expectedTargets, graph.targets("A"));
    }
}