import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Grammar {
    // Instance variables to hold the grammar components
    private final String SS; // Starting symbol
    private final Set<String> VN; // Set of non-terminal symbols
    private final Map<String, List<String>> P; // Production rules
    private final FiniteAutomaton FA; // Finite Automaton representation of the grammar

    // Constructor to initialize the Grammar object with the provided grammar components.
    public Grammar(String SS, Set<String> VN, Set<String> VT, Map<String, List<String>> P) {
        this.SS = SS; // Initialize the starting symbol
        this.VN = VN; // Initialize the set of non-terminal symbols
        this.P = P; // Initialize the production rules
        // Convert the grammar to a Finite Automaton for validation.
        // The Finite Automaton will use the terminal symbols and transitions built from the production rules.
        this.FA = new FiniteAutomaton(VT, buildTransitions(P), SS);
    }

    // Method to generate and print a specified number of strings and their validity.
    public void generateNeededStrings(int num) {
        System.out.println("Strings + Validity:");
        // Loop to generate and check validity for each string
        for (int i = 0; i < num; i++) {
            // Generate a random string according to the grammar.
            String generatedString = generateString();
            // Check if the generated string is valid and print its validity.
            System.out.println((i + 1) + ": " + generatedString + " " + (isValid(generatedString) ? "is valid" : "is not valid"));
        }
    }

    // Method to check if a given word is valid according to the grammar.
    public boolean isValid(String word) {
        // Delegate the validation to the Finite Automaton representation of the grammar.
        return FA.isValid(word);
    }

    // Method to generate a random string according to the grammar.
    public String generateString() {
        // Start with the starting symbol.
        String result = getRandomProduction(SS);
        boolean containsNonTerminal = true;

        // Continue replacing non-terminals with their productions until no more non-terminals remain.
        while (containsNonTerminal) {
            containsNonTerminal = false;
            // Iterate over each character in the current string.
            for (char entry : result.toCharArray()) {
                String entryString = String.valueOf(entry);
                // If the character is a non-terminal, replace it with a random production.
                if (VN.contains(entryString)) {
                    result = result.replaceFirst(entryString, getRandomProduction(entryString));
                    containsNonTerminal = true;
                }
            }
        }
        return result;
    }

    // Method to get a random production for a given non-terminal symbol.
    private String getRandomProduction(String nonTerminal) {
        // Get the list of productions for the given non-terminal and select one randomly.
        return P.get(nonTerminal).get(ThreadLocalRandom.current().nextInt(P.get(nonTerminal).size()));
    }

    // Method to build transitions for the Finite Automaton representation of the grammar.
    private Map<String, Map<String, String>> buildTransitions(Map<String, List<String>> productions) {
        // Initialize a map to hold the transitions for each state (non-terminal symbol).
        Map<String, Map<String, String>> transitions = new HashMap<>();
        // Iterate over each production rule.
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String state = entry.getKey(); // Get the non-terminal symbol as the state.
            List<String> productionList = entry.getValue(); // Get the list of productions for the state.
            Map<String, String> stateTransitions = new HashMap<>();
            // Iterate over each production.
            for (String production : productionList) {
                String symbol = production.substring(0, 1); // Get the first character as the symbol.
                // Get the next state as the rest of the production, or "OK" if it's empty.
                String nextState = production.length() > 1 ? production.substring(1) : "OK";
                stateTransitions.put(symbol, nextState); // Add the transition to the state's transitions.
            }
            transitions.put(state, stateTransitions); // Add the state and its transitions to the overall transitions map.
        }
        return transitions;
    }

}
