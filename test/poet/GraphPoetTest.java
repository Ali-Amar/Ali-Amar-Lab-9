package poet;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class GraphPoetTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testBasicPoemGeneration() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        String input = "Seek to explore new and exciting synergies!";
        String output = poet.poem(input);
        assertEquals("Basic poem generation failed",
                    "Seek to explore strange new life and exciting synergies!",
                    output);
    }
    
    @Test
    public void testNoBridgeWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        String input = "hello world";
        String output = poet.poem(input);
        assertEquals("Should not add bridge words when none available",
                    input, output);
    }
    
    @Test
    public void testEmptyInput() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        assertEquals("Empty input should return empty string",
                    "", poet.poem(""));
        assertEquals("Whitespace should be preserved",
                    "   ", poet.poem("   "));
    }
    
    @Test
    public void testCaseInsensitivity() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        String input = "TO EXPLORE NEW worlds";
        String output = poet.poem(input);
        assertTrue("Should find bridges regardless of case",
                  output.contains("strange") && output.contains("TO EXPLORE"));
    }
    
    @Test
    public void testSpecialCharacters() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        String input = "to, explore. new! worlds?";
        String output = poet.poem(input);
        assertTrue("Should handle punctuation correctly",
                  output.contains(",") && output.contains(".") && 
                  output.contains("!") && output.contains("?"));
    }
    
    @Test
    public void testBridgeWordsInMiddle() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/TestOneLine.txt"));
        String input = "explore new";
        String output = poet.poem(input);
        assertTrue("Should add bridge word between input words",
                  output.split(" ").length > 2 && output.contains("strange"));
    }
}