import java.util.Map;
import java.util.Set;

public class FiniteAutomaton {
    private final Set<String> ALPHABET; // Set of symbols in the alphabet
    private final Map<String, Map<String, String>> P; // Transition function represented as a map of maps
    private final String SS; // Starting state

    // Constructor to initialize the FiniteAutomaton with the alphabet, transition function, and starting state
    public FiniteAutomaton(Set<String> ALPHABET, Map<String, Map<String, String>> P, String SS) {
        this.ALPHABET = ALPHABET; // Initialize the alphabet
        this.P = P; // Initialize the transition function
        this.SS = SS; // Initialize the starting state
    }

    // Method to validate an input string against the finite automaton
    public boolean isValid(String input) {
        String currentState = SS; // Start from the initial state

        // Iterate over each symbol in the input string
        for (char symbol : input.toCharArray()) {
            String symbolStr = String.valueOf(symbol); // Convert the symbol to a string

            // Check if the symbol is in the alphabet
            if (!ALPHABET.contains(symbolStr)) return false; // If not, the input is invalid

            // Check if there is a transition defined for the current state and symbol
            if (P.containsKey(currentState) && P.get(currentState).containsKey(symbolStr))
                currentState = P.get(currentState).get(symbolStr); // Move to the next state
            else
                return false; // If no transition is defined, the input is invalid
        }
        // Check if the final state is the "OK" state, indicating the input is valid
        return currentState.equals("OK");
    }
}
