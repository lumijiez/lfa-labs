import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<Character> VN = new HashSet<>(Arrays.asList('S', 'A', 'B'));
        Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd'));
        Map<Character, List<String>> P = new HashMap<>();
        P.put('S', Arrays.asList("bS", "dA"));
        P.put('A', Arrays.asList("aA", "dB", "b"));
        P.put('B', Arrays.asList("cB", "a"));
        char S = 'S';

        Grammar grammar = new Grammar(VN, VT, P, S);

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();

        System.out.println("Generated Strings: ");
        for (int i = 1; i <= 5; i++) {
            String generated = grammar.generateString();
            System.out.println(i + " " + generated);
//            System.out.println("Accepted by automaton? " + finiteAutomaton.stringBelongToLanguage(generated));
        }



    }
}

