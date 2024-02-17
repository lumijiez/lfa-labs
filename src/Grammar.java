import java.util.*;

public class Grammar {
    private Set<Character> VN;
    private Set<Character> VT;
    private Map<Character, List<String>> P;
    private char S;
    private Random random;

    public Grammar(Set<Character> VN, Set<Character> VT, Map<Character, List<String>> P, char S) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.S = S;
        this.random = new Random(System.currentTimeMillis());
    }

    public String generateString() {
        StringBuilder sb = new StringBuilder();
        generateStringHelper(S, sb);
        return sb.toString();
    }

    private void generateStringHelper(char symbol, StringBuilder sb) {
        if (VT.contains(symbol)) {
            sb.append(symbol);
        } else {
            List<String> productions = P.get(symbol);
            String chosenProduction = productions.get(random.nextInt(productions.size()));
            for (char c : chosenProduction.toCharArray()) {
                generateStringHelper(c, sb);
            }
        }
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<Character> Q = new HashSet<>(VN);
        Q.addAll(VT);
        Set<Character> Sigma = new HashSet<>(VT);
        char q0 = S;
        Set<Character> F = new HashSet<>();
        for (char vn : VN) {
            if (P.get(vn).contains("ε")) {
                F.add(vn);
            }
        }
        Map<Character, Map<Character, Character>> delta = new HashMap<>();
        for (char q : Q) {
            delta.put(q, new HashMap<>());
            for (char c : Sigma) {
                delta.get(q).put(c, ' ');
            }
        }
        for (char vn : VN) {
            if (!P.containsKey(vn)) {
                P.put(vn, Collections.emptyList());
            }
            for (String production : P.get(vn)) {
                char nextState = production.charAt(0);
                char inputSymbol = production.length() > 1 ? production.charAt(1) : ' ';
                delta.get(vn).put(inputSymbol, nextState);
            }
        }
        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }
}
