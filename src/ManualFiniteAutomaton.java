import util.Pair;
import java.util.*;

public class ManualFiniteAutomaton {

    private final List<String> stateList;
    private final Set<String> alphabet;
    private final Set<String> acceptingStates;
    private final Map<String, List<Pair>> T;

    public ManualFiniteAutomaton(List<String> stateList, Set<String> alphabet, Set<String> acceptingStates, Map<String, List<Pair>> T) {
        this.stateList = stateList;
        this.alphabet = alphabet;
        this.acceptingStates = acceptingStates;
        this.T = T;
    }

    // Method to map original states to equivalent single-character states
    public Map<String, String> mapStates() {
        Map<String, String> mappedStates = new HashMap<>(); // Creating a HashMap to store mappings
        char mappedState = 'A'; // Starting mapping with character 'A'
        for (String state : stateList) { // Iterating through each state in stateList
            mappedStates.put(state, String.valueOf(mappedState)); // Mapping original state to a single character
            mappedState++; // Moving to the next character for mapping
        }
        return mappedStates; // Returning the mapped states
    }

    // Method to check if the automaton is deterministic
    public void isDeterministic() {
        for (Map.Entry<String, List<Pair>> entry : T.entrySet()) { // Iterating through each transition in T
            if (entry.getValue().size() > 1) { // If there are multiple transitions for a state and symbol
                System.out.println("It is not deterministic!"); // Print that the automaton is not deterministic
                return; // Exit the method
            }
        }
        System.out.println("It is deterministic!"); // Print that the automaton is deterministic
    }

    // Method to convert the NFA to DFA
    public void toDFA() {
        Map<Set<String>, Map<String, Set<String>>> dfaTransitions = new HashMap<>(); // Creating a map for DFA transitions
        Queue<Set<String>> queue = new LinkedList<>(); // Queue to store sets of states

        Set<String> initialState = new HashSet<>(stateList); // Creating initial set of states containing all states
        queue.add(initialState); // Adding the initial state to the queue

        while (!queue.isEmpty()) { // While there are unprocessed states in the queue
            Set<String> currentState = queue.poll(); // Dequeue a state from the queue

            Map<String, Set<String>> transitions = new HashMap<>(); // Map to store transitions from current state
            for (String symbol : alphabet) { // For each symbol in the alphabet
                Set<String> nextStates = new HashSet<>(); // Set to store next states for the symbol
                for (String state : currentState) { // For each state in the current set of states
                    List<Pair> transitionsForState = T.get(state); // Get transitions for the current state
                    if (transitionsForState != null) { // If there are transitions for the state
                        for (Pair p : transitionsForState) { // For each transition from the state
                            if (p.first().equals(symbol)) { // If the transition symbol matches the current symbol
                                nextStates.add(p.second()); // Add the destination state to nextStates
                            }
                        }
                    }
                }
                transitions.put(symbol, nextStates); // Add transitions for the current symbol
                if (!nextStates.isEmpty() && !dfaTransitions.containsKey(nextStates)) { // If nextStates is not empty and not already processed
                    queue.add(nextStates); // Enqueue nextStates for further processing
                }
            }

            dfaTransitions.put(currentState, transitions); // Add transitions from current state to DFA transitions
        }

        // Printing DFA transitions
        System.out.println("DFA Transitions:");
        for (Map.Entry<Set<String>, Map<String, Set<String>>> entry : dfaTransitions.entrySet()) { // For each DFA state and its transitions
            Set<String> currentState = entry.getKey(); // Get the current DFA state
            Map<String, Set<String>> transitions = entry.getValue(); // Get its transitions
            System.out.println("State: " + currentState); // Print the current DFA state
            for (Map.Entry<String, Set<String>> transition : transitions.entrySet()) { // For each transition from the state
                String inputSymbol = transition.getKey(); // Get the input symbol
                Set<String> nextState = transition.getValue(); // Get the next state(s)
                if (!nextState.isEmpty()) { // If there is a transition
                    System.out.println("    Input: " + inputSymbol + " --> " + nextState); // Print the transition
                }
            }
        }
    }

    // Method to convert the NFA to Grammar
    public void toGrammar() {
        Map<String, String> mappedStates = mapStates(); // Map original states to single-character states
        Map<String, List<String>> grammar = new HashMap<>(); // Create a map to store the grammar rules

        for(Map.Entry<String, List<Pair>> te : T.entrySet()) { // For each transition in the transition function
            List<String> maps = new ArrayList<>(); // Create a list to store grammar rules for the current state
            for (Pair p : te.getValue()) { // For each transition from the current state
                maps.add(p.first() + mappedStates.get(p.second())); // Add a grammar rule to the list
            }
            grammar.put(mappedStates.get(te.getKey()), maps); // Add the grammar rules to the grammar map
        }

        // Printing the grammar rules
        for (Map.Entry<String, List<String>> grammarEntry : grammar.entrySet()) { // For each state and its grammar rules
            System.out.print(grammarEntry.getKey() + " -> "); // Print the state
            if (grammarEntry.getValue().size() > 1) { // If there are multiple rules
                System.out.print(grammarEntry.getValue().get(0)); // Print the first rule
                for (int i = 1; i < grammarEntry.getValue().size(); i++) { // For the remaining rules
                    System.out.print(" | " + grammarEntry.getValue().get(i)); // Print each rule
                }
            } else { // If there is only one rule
                System.out.print(grammarEntry.getValue().get(0)); // Print the rule
            }
            System.out.println(); // Move to the next line
        }
    }
}
