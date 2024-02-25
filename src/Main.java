import java.util.*;

public class Main {
    // Define the starting symbol, non-terminal symbols, terminal symbols, and production rules
    public static final String SS = "S"; // Starting symbol
    public static final Set<String> VN = Set.of("S", "B", "C"); // Non-terminal symbols
    public static final Set<String> VT = Set.of("a", "b", "c"); // Terminal symbols
    public static final Map<String, List<String>> P = Map.of( // Production rules
            "S", List.of("aB"), // Production rule for S
            "B", List.of("aC", "bB"), // Production rule for B
            "C", List.of("bB", "c", "aS")); // Production rule for C

    public static void main(String[] args) {
        String toCheck = "aac"; // Word to check

        // Create a Grammar object with the defined grammar
        Grammar grammar = new Grammar(SS, VN, VT, P);

        // Generate strings needed for the validation process
        grammar.generateNeededStrings(5);

        // Check if the given word is valid according to the grammar
        System.out.println("Is " + toCheck + " valid? " + grammar.isValid(toCheck));
    }
}
