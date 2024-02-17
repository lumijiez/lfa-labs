import java.util.Map;
import java.util.Set;

class FiniteAutomaton {
    private Set<Character> Q;
    private Set<Character> Sigma;
    private Map<Character, Map<Character, Character>> delta;
    private char q0;
    private Set<Character> F;

    public FiniteAutomaton(Set<Character> Q, Set<Character> Sigma, Map<Character, Map<Character, Character>> delta,
                           char q0, Set<Character> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(final String inputString) {
        char currentState = q0;
        for (char c : inputString.toCharArray()) {
            if (!Sigma.contains(c)) {
                return false;
            }
            if (!delta.get(currentState).containsKey(c)) {
                return false;
            }
            currentState = delta.get(currentState).get(c);
        }
        return F.contains(currentState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("States (Q): ").append(Q).append("\n");
        sb.append("Alphabet (Sigma): ").append(Sigma).append("\n");
        sb.append("Transition Function (delta): ").append(delta).append("\n");
        sb.append("Initial State (q0): ").append(q0).append("\n");
        sb.append("Accepting States (F): ").append(F);
        return sb.toString();
    }
}