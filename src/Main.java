import RegularGrammars.Grammar;
import FiniteAutomata.ChomskyChecker;
import FiniteAutomata.ManualFiniteAutomaton;
import util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    // LAB 1
    public static final String SS = "S";
    //public static final Set<String> VN = Set.of("S", "B", "C");
    public static final Set<String> VT = Set.of("a", "b", "c");
    public static final Map<String, List<String>> P = Map.of(
            "S", List.of("aB"),
            "B", List.of("aC", "bB"),
            "C", List.of("aS", "c"));

    // LAB 2
    public static final List<String> stateList = Arrays.asList("q0", "q1", "q2");
    public static final Set<String> alphabet = Set.of("a", "b");
    //public static final Set<String> acceptingStates = Set.of("q2");
    public static final Map<String, List<Pair>> transitions= Map.of(
                "q0", List.of(new Pair("a", "q0"), new Pair("a", "q1")),
                    "q1", List.of(new Pair("b", "q2"), new Pair("a", "q0")),
                        "q2", List.of(new Pair("b", "q2"))
            );


    public static void main(String[] args) {
        String toCheck = "aac";
        Grammar grammar = new Grammar(SS, VT, P);
        grammar.generateWords(5);
        System.out.println("===================================");
        System.out.println("Is " + toCheck + " valid? " + grammar.isValid(toCheck));
        System.out.println("===================================");
        System.out.println(ChomskyChecker.classifyGrammar(P));

        ManualFiniteAutomaton MFA = new ManualFiniteAutomaton(stateList, alphabet, transitions);
        System.out.println("===================================");
        MFA.toGrammar();
        System.out.println("===================================");
        MFA.isDeterministic();
        System.out.println("===================================");
        MFA.toDFA();
        System.out.println("===================================");
    }
}