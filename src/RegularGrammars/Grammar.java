package RegularGrammars;

import java.util.*;

public class Grammar {
    private final String SS;
    private final FiniteAutomaton FA;

    public Grammar(String SS, Set<String> VT, Map<String, List<String>> P) {
        this.SS = SS;
        this.FA = new FiniteAutomaton(SS, VT, generateTransitions(P));
    }

    public Map<String, Map<String, String>> generateTransitions(Map<String, List<String>> P) {
        Map<String, Map<String, String>> transitions = new HashMap<>();
        for (Map.Entry<String, List<String>> entryP : P.entrySet()) {
            String state = entryP.getKey();
            List<String> productionList = entryP.getValue();
            Map<String, String> stateTransitions = new HashMap<>();
            for (String production : productionList) {
                String symbol = production.substring(0, 1);
                String nextState = production.length() > 1 ? production.substring(1) : "OK";
                stateTransitions.put(symbol, nextState);
            }
            transitions.put(state, stateTransitions);
        }
        return transitions;
    }

    public boolean isValid(String word) {
        return this.FA.isValid(word);
    }

    public void generateWords(int numWords) {
        List<String> generatedWords = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numWords; i++) {
            StringBuilder wordBuilder = new StringBuilder();
            String currentState = SS;

            while (!currentState.equals("OK")) {
                Map<String, String> transitions = FA.getTransitions().get(currentState);
                List<String> symbols = new ArrayList<>(transitions.keySet());
                String symbol = symbols.get(random.nextInt(symbols.size()));
                wordBuilder.append(symbol);
                currentState = transitions.get(symbol);
            }

            generatedWords.add(wordBuilder.toString());
        }

        System.out.println("===================================");
        for (String word: generatedWords) {
            System.out.println(word + " " + (isValid(word) ? "- Valid" : "- Not valid"));
        }
    }
}
