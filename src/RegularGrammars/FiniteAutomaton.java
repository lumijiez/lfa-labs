package RegularGrammars;

import java.util.Map;
import java.util.Set;

public class FiniteAutomaton {
    private final String SS;
    private final Set<String> alphabet;
    private final Map<String, Map<String, String>> T;
    public FiniteAutomaton(String SS, Set<String> VT, Map<String, Map<String, String>> T) {
        this.SS = SS;
        this.alphabet = VT;
        this.T = T;
    }

    public boolean isValid(String word) {
        String currentState = SS;
        for (char symbol : word.toCharArray()) {
            String symbolStr = String.valueOf(symbol);

            if (!alphabet.contains(symbolStr)) return false;

            if (T.containsKey(currentState) && T.get(currentState).containsKey(symbolStr))
                currentState = T.get(currentState).get(symbolStr);
            else
                return false;
        }
        return currentState.equals("OK");
    }


    public Map<String, Map<String, String>> getTransitions() {
        return T;
    }

}
