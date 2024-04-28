package Lab5;

import java.util.*;

public class ConvertCNF {
    static class Grammar {
        Set<Character> nonTerminals;
        Set<Character> terminals;
        Map<Character, List<String>> productions;
        char startSymbol;

        public Grammar(Set<Character> nonTerminals, Set<Character> terminals, Map<Character, List<String>> productions, char startSymbol) {
            this.nonTerminals = nonTerminals;
            this.terminals = terminals;
            this.productions = productions;
            this.startSymbol = startSymbol;
        }
    }

    public static void main(String[] args) {
        Set<Character> nonTerminals = new HashSet<>(Arrays.asList('S', 'A', 'B', 'C', 'E'));
        Set<Character> terminals = new HashSet<>(Arrays.asList('a', 'b'));
        Map<Character, List<String>> productions = new HashMap<>();
        productions.put('S', Arrays.asList("baC", "B"));
        productions.put('A', Arrays.asList("a", "aS", "bCaCb"));
        productions.put('B', Arrays.asList("AC", "bS", "aAa"));
        productions.put('C', Arrays.asList("e", "AB"));
        productions.put('E', List.of("BA"));

        Grammar grammar = new Grammar(nonTerminals, terminals, productions, 'S');
        ConvertCNF converter = new ConvertCNF();
        Grammar cnfGrammar = converter.convertToCNF(grammar);

        System.out.println("Converted Grammar to CNF:");
        for (Map.Entry<Character, List<String>> entry : cnfGrammar.productions.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        testEliminateEpsilonProductions();
        testEliminateUnitProductions();
        testEliminateUselessSymbols();
        testConvertToProperCNF();
    }

    public Grammar convertToCNF(Grammar grammar) {
        eliminateEpsilonProductions(grammar);
        eliminateUnitProductions(grammar);
        eliminateUselessSymbols(grammar);
        convertToProperCNF(grammar);
        return grammar;
    }

    private void eliminateEpsilonProductions(Grammar grammar) {
        Set<Character> nullable = new HashSet<>();
        boolean changes;
        do {
            changes = false;
            for (Map.Entry<Character, List<String>> entry : grammar.productions.entrySet()) {
                for (String production : entry.getValue()) {
                    if (production.equals("e") || production.chars().allMatch(c -> nullable.contains((char) c))) {
                        if (nullable.add(entry.getKey())) {
                            changes = true;
                        }
                    }
                }
            }
        } while (changes);

        Map<Character, List<String>> newProductions = new HashMap<>();
        grammar.productions.forEach((nt, rules) -> {
            List<String> modifiedRules = new ArrayList<>();
            for (String rule : rules) {
                if (!rule.equals("e")) {
                    Set<String> currentRules = new HashSet<>();
                    currentRules.add(rule);
                    generateEpsilonFreeVersions(rule, 0, "", nullable, currentRules);
                    modifiedRules.addAll(currentRules);
                }
            }
            newProductions.put(nt, new ArrayList<>(new HashSet<>(modifiedRules)));
        });
        grammar.productions = newProductions;
    }

    private void generateEpsilonFreeVersions(String rule, int index, String current, Set<Character> nullable, Set<String> results) {
        if (index == rule.length()) {
            if (!current.isEmpty()) {
                results.add(current);
            }
            return;
        }
        char ch = rule.charAt(index);
        generateEpsilonFreeVersions(rule, index + 1, current + ch, nullable, results);
        if (nullable.contains(ch)) {
            generateEpsilonFreeVersions(rule, index + 1, current, nullable, results);
        }
    }


    private void eliminateUnitProductions(Grammar grammar) {
        Map<Character, Set<Character>> unitChains = new HashMap<>();
        grammar.productions.forEach((key, value) -> unitChains.put(key, new HashSet<>()));
        boolean changes;
        do {
            changes = false;
            for (Map.Entry<Character, List<String>> entry : grammar.productions.entrySet()) {
                Set<Character> currentSet = unitChains.get(entry.getKey());
                List<String> productions = new ArrayList<>(entry.getValue());
                for (String production : productions) {
                    if (production.length() == 1 && grammar.nonTerminals.contains(production.charAt(0))) {
                        Character target = production.charAt(0);
                        if (currentSet.add(target) && unitChains.get(entry.getKey()).addAll(unitChains.get(target))) {
                            changes = true;
                        }
                    }
                }
            }
        } while (changes);

        Map<Character, List<String>> newProductions = new HashMap<>();
        unitChains.forEach((key, value) -> {
            List<String> combinedProductions = new ArrayList<>();
            value.forEach(v -> combinedProductions.addAll(grammar.productions.getOrDefault(v, Collections.emptyList())
                    .stream()
                    .filter(p -> p.length() != 1 || !grammar.nonTerminals.contains(p.charAt(0)))
                    .toList()));
            newProductions.put(key, new ArrayList<>(new HashSet<>(combinedProductions)));
        });
        grammar.productions = newProductions;
    }

    private void eliminateUselessSymbols(Grammar grammar) {
        Set<Character> reachable = new HashSet<>();
        reachable.add(grammar.startSymbol);
        int size;
        do {
            size = reachable.size();
            Set<Character> newReachables = new HashSet<>();
            for (Character symbol : reachable) {
                List<String> productions = grammar.productions.get(symbol);
                if (productions != null) {
                    for (String production : productions) {
                        for (char c : production.toCharArray()) {
                            if (grammar.nonTerminals.contains(c)) {
                                newReachables.add(c);
                            }
                        }
                    }
                }
            }
            reachable.addAll(newReachables);
        } while (reachable.size() > size);

        Set<Character> generating = new HashSet<>();
        do {
            size = generating.size();
            for (Map.Entry<Character, List<String>> entry : new HashMap<>(grammar.productions).entrySet()) {
                outerloop:
                for (String production : entry.getValue()) {
                    for (char c : production.toCharArray()) {
                        if (grammar.nonTerminals.contains(c) && !generating.contains(c)) {
                            continue outerloop;
                        }
                    }
                    generating.add(entry.getKey());
                }
            }
        } while (generating.size() > size);

        Set<Character> useful = new HashSet<>(reachable);
        useful.retainAll(generating);

        grammar.nonTerminals.retainAll(useful);
        Map<Character, List<String>> newProductions = new HashMap<>();
        for (Map.Entry<Character, List<String>> entry : grammar.productions.entrySet()) {
            if (useful.contains(entry.getKey())) {
                List<String> newProdList = new ArrayList<>();
                for (String prod : entry.getValue()) {
                    if (prod.chars().allMatch(c -> grammar.terminals.contains((char) c) || useful.contains((char) c))) {
                        newProdList.add(prod);
                    }
                }
                if (!newProdList.isEmpty()) {
                    newProductions.put(entry.getKey(), newProdList);
                }
            }
        }
        grammar.productions = newProductions;
    }


    private void convertToProperCNF(Grammar grammar) {
        Map<Character, List<String>> newProductions = new HashMap<>();
        grammar.productions.forEach((key, productions) -> {
            List<String> newProductionList = new ArrayList<>();
            for (String prod : productions) {
                if (prod.length() == 1 && grammar.terminals.contains(prod.charAt(0))) {
                    newProductionList.add(prod);
                } else if (prod.length() == 2 && prod.chars().allMatch(c -> grammar.nonTerminals.contains((char) c))) {
                    newProductionList.add(prod);
                } else {
                    char lastNonTerminal = prod.charAt(prod.length() - 1);
                    String rest = prod.substring(0, prod.length() - 1);
                    if (rest.length() > 1) {
                        char newSymbol = getNextSymbol(grammar);
                        grammar.nonTerminals.add(newSymbol);
                        newProductions.putIfAbsent(newSymbol, new ArrayList<>());
                        newProductions.get(newSymbol).add(rest);
                        newProductionList.add(newSymbol + "" + lastNonTerminal);
                    } else {
                        newProductionList.add(rest + lastNonTerminal);
                    }
                }
            }
            newProductions.put(key, newProductionList);
        });
        grammar.productions = newProductions;
    }

    private char getNextSymbol(Grammar grammar) {
        for (char c = 'Z'; c >= 'A'; c--) {
            if (!grammar.nonTerminals.contains(c) && !grammar.terminals.contains(c)) {
                return c;
            }
        }
        throw new RuntimeException("Ran out of symbols to use for new non-terminals");
    }

    private static void testEliminateEpsilonProductions() {
        Set<Character> nonTerminals = new HashSet<>(Arrays.asList('S', 'A', 'B'));
        Set<Character> terminals = new HashSet<>(Arrays.asList('a', 'b'));
        Map<Character, List<String>> productions = new HashMap<>();
        productions.put('S', Arrays.asList("A", "a"));
        productions.put('A', Arrays.asList("B", "e"));
        productions.put('B', List.of("e"));

        Grammar grammar = new Grammar(nonTerminals, terminals, productions, 'S');
        ConvertCNF converter = new ConvertCNF();
        converter.eliminateEpsilonProductions(grammar);

        assert new HashSet<>(grammar.productions.get('A')).containsAll(Arrays.asList("B", ""));
        assert grammar.productions.get('B').contains("");
        assert new HashSet<>(grammar.productions.get('S')).containsAll(Arrays.asList("A", "a", ""));
        System.out.println("Epsilon Productions Elimination Test Passed!");
    }

    private static void testEliminateUnitProductions() {
        Set<Character> nonTerminals = new HashSet<>(Arrays.asList('S', 'A', 'B'));
        Set<Character> terminals = new HashSet<>(Arrays.asList('a', 'b'));
        Map<Character, List<String>> productions = new HashMap<>();
        productions.put('S', Arrays.asList("A", "a"));
        productions.put('A', List.of("B"));
        productions.put('B', List.of("a"));

        Grammar grammar = new Grammar(nonTerminals, terminals, productions, 'S');
        ConvertCNF converter = new ConvertCNF();
        converter.eliminateUnitProductions(grammar);

        assert grammar.productions.get('S').contains("a");
        assert grammar.productions.get('A').contains("a");
        System.out.println("Unit Productions Elimination Test Passed!");
    }

    private static void testEliminateUselessSymbols() {
        Set<Character> nonTerminals = new HashSet<>(Arrays.asList('S', 'A', 'B', 'C'));
        Set<Character> terminals = new HashSet<>(Arrays.asList('a', 'b'));
        Map<Character, List<String>> productions = new HashMap<>();
        productions.put('S', Arrays.asList("A", "B"));
        productions.put('A', List.of("a"));
        productions.put('B', List.of("C"));
        productions.put('C', List.of("C"));

        Grammar grammar = new Grammar(nonTerminals, terminals, productions, 'S');
        ConvertCNF converter = new ConvertCNF();
        converter.eliminateUselessSymbols(grammar);

        assert !grammar.nonTerminals.contains('C') : "C should have been removed";
        assert grammar.productions.get('B') == null || !grammar.productions.get('B').contains("C");
        System.out.println("Useless Symbols Elimination Test Passed!");
    }

    private static void testConvertToProperCNF() {
        Set<Character> nonTerminals = new HashSet<>(Arrays.asList('S', 'A'));
        Set<Character> terminals = new HashSet<>(Arrays.asList('a', 'b'));
        Map<Character, List<String>> productions = new HashMap<>();
        productions.put('S', Arrays.asList("Aa", "b"));
        productions.put('A', Arrays.asList("SS", "a"));

        boolean properCNF = isProperCNF(nonTerminals, terminals, productions);
        assert properCNF : "All productions should be in CNF";
        System.out.println("Proper CNF Conversion Test Passed!");
    }

    private static boolean isProperCNF(Set<Character> nonTerminals, Set<Character> terminals, Map<Character, List<String>> productions) {
        Grammar grammar = new Grammar(nonTerminals, terminals, productions, 'S');
        ConvertCNF converter = new ConvertCNF();
        converter.convertToProperCNF(grammar);

        boolean properCNF = true;
        for (List<String> prodList : grammar.productions.values()) {
            for (String prod : prodList) {
                if (!(prod.length() == 1 && terminals.contains(prod.charAt(0))) && !(prod.length() == 2 && nonTerminals.contains(prod.charAt(0)) && nonTerminals.contains(prod.charAt(1)))) {
                    properCNF = false;
                    break;
                }
            }
        }
        return properCNF;
    }
}

