package Lab2;

import java.util.List;
import java.util.Map;

public class ChomskyChecker {
    public static String classifyGrammar(Map<String, List<String>> grammar) {
        for (Map.Entry<String, List<String>> entry : grammar.entrySet()) {
            for (String listEntry : entry.getValue()) {
                String prod = entry.getKey() + " -> " + listEntry;
                if (isType3(prod)) {
                    return "Regular";
                } else if (isType2(prod)) {
                    return "Context-free";
                } else if (isType1(prod)) {
                    return "Context-sensitive";
                } else if (isType0(prod)) {
                    return "Unrestricted";
                }
            }
        }
        return "Unknown";
    }

    private static boolean isType0(String production) {
        return production.matches("^[^ ]+ -> .+$");
    }

    private static boolean isType1(String production) {
        return production.matches("^.+ .+ .+$");
    }

    private static boolean isType2(String production) {
        return production.matches("^[A-Z]+ -> [^A-Z]+$");
    }

    private static boolean isType3(String production) {
        return production.matches("^[A-Z]+ -> [a-zA-Z]$") || production.matches("^[A-Z]+ -> [a-zA-Z][A-Z]$");
    }

}
