package poet;

import graph.Graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphPoet {
    private final Graph<String> graph;
    private final List<String> corpusWords;
    
    public GraphPoet(File corpus) throws IOException {
        graph = Graph.empty();
        corpusWords = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))) {
            String line;
            String previousWord = null;
            
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        // Remove punctuation for graph storage
                        String cleanWord = stripPunctuation(word).toLowerCase();
                        if (!cleanWord.isEmpty()) {
                            corpusWords.add(cleanWord);
                            graph.add(cleanWord);
                            
                            if (previousWord != null) {
                                int currentWeight = graph.set(previousWord, cleanWord, 0);
                                graph.set(previousWord, cleanWord, currentWeight + 1);
                            }
                            previousWord = cleanWord;
                        }
                    }
                }
            }
        }
        checkRep();
    }
    
    private void checkRep() {
        assert graph != null : "graph should not be null";
        assert corpusWords != null : "corpusWords should not be null";
        for (String word : corpusWords) {
            assert word != null : "no word should be null";
            assert word.equals(word.toLowerCase()) : "all words should be lowercase";
        }
    }
    
    public List<String> getCorpusWords() {
        return Collections.unmodifiableList(corpusWords);
    }
    
    public String poem(String input) {
        if (input.trim().isEmpty()) {
            return input;
        }
        
        String[] words = input.trim().split("\\s+");
        StringBuilder poem = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            // Add space before word if not first word
            if (i > 0) {
                poem.append(" ");
            }
            
            String currentWord = words[i];
            String punctuation = extractPunctuation(currentWord);
            String cleanWord = stripPunctuation(currentWord);
            
            // Add the current word with its original case
            poem.append(cleanWord).append(punctuation);
            
            // If there's a next word, try to add a bridge
            if (i < words.length - 1) {
                String nextWord = stripPunctuation(words[i + 1]);
                String bridge = findBridge(cleanWord.toLowerCase(), nextWord.toLowerCase());
                
                if (bridge != null) {
                    poem.append(" ").append(bridge);
                }
            }
        }
        
        return poem.toString();
    }
    
    private String findBridge(String source, String target) {
        Map<String, Integer> sourceTargets = graph.targets(source);
        Map<String, Integer> targetSources = graph.sources(target);
        
        String bestBridge = null;
        int maxWeight = 0;
        
        for (String bridge : sourceTargets.keySet()) {
            if (targetSources.containsKey(bridge)) {
                int totalWeight = sourceTargets.get(bridge) + targetSources.get(bridge);
                if (totalWeight > maxWeight) {
                    maxWeight = totalWeight;
                    bestBridge = bridge;
                }
            }
        }
        
        return bestBridge;
    }
    
    private String stripPunctuation(String word) {
        StringBuilder cleaned = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(c);
            }
        }
        return cleaned.toString();
    }
    
    private String extractPunctuation(String word) {
        StringBuilder punct = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                punct.append(c);
            }
        }
        return punct.toString();
    }
    
    @Override
    public String toString() {
        return String.format("GraphPoet with %d words in corpus", corpusWords.size());
    }
}