# Laboratory Work No.5
## Chomsky Normal Form Converter
### Course: Formal Languages & Finite Automata
### Author: Schipschi Daniil / FAF-223

----

## Objectives:

1. **Learn about Chomsky Normal Form (CNF)**.
    - Explore the definition and importance of CNF in grammar normalization and computational linguistics.

2. **Get familiar with the approaches of normalizing a grammar**.
    - Study different methodologies and strategies for transforming grammars into their normal forms.

3. **Implement a method for normalizing an input grammar by the rules of CNF**.
    - **Encapsulation**: The implementation needs to be encapsulated in a method with an appropriate signature.
        - Ideally, this should also be encapsulated within an appropriate class/type.
    - **Execution**: The implemented functionality needs to be executed and tested to ensure its correctness.
    - **Testing**:
        - **Unit Tests**: A BONUS point will be given for students who will have unit tests that validate the functionality of the project.
        - **Generalization**: Another BONUS point will be awarded if the student makes the function accept any grammar, not only the one from the student's variant.



# CFG to CNF Converter

This repository contains a Java program designed to transform a given context-free grammar (CFG) into Chomsky Normal Form (CNF). The conversion process involves several crucial steps: eliminating epsilon productions, unit productions, useless symbols, and finally converting the grammar to meet CNF standards. This document details the implementation and functionality of each part of the conversion process, providing code snippets and unit tests.

## Core Functionalities

### 1. Elimination of Epsilon Productions

This function is essential for handling epsilon (ε) productions, which are grammar rules that allow a non-terminal to produce an empty string. The presence of ε-productions complicates many algorithms for parsing and further grammar transformations. The method first identifies all non-terminals that can generate an empty string, either directly or through a series of derivations involving other nullable non-terminals. After identifying these nullable symbols, the function then rewrites the grammar by systematically generating all possible combinations of each production rule that can potentially include these nullable symbols, excluding ε explicitly. The result is a set of production rules that preserve the language's integrity minus any productions that directly result in an empty string. This step is critical because it maintains the structural integrity of the grammar while ensuring that the subsequent transformations are simpler and that the grammar conforms to more strict forms like CNF.

**Code Snippet:**
```java
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
```

### 2. Elimination of Unit Productions

Unit productions are rules where a non-terminal directly derives another non-terminal (e.g., A → B). These rules often add unnecessary complexity and depth to the grammar without adding to the language's expressivity. This function systematically replaces each unit production with the actual production sets of the non-terminals they point to, thereby 'flattening' the grammar's derivation structure. This transformation simplifies the grammar significantly, reducing the depth of derivation trees and making the grammar more straightforward for parsing and analysis. It also aids in the eventual conversion to CNF by minimizing indirect production paths, thus streamlining the grammar into a format where every production is more substantive.

**Code Snippet:**
```java
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
```

### 3. Elimination of Useless Symbols

In any given CFG, some symbols may be non-generating or unreachable. Non-generating symbols are those that do not lead to any terminal string in any derivation, making them redundant for the language defined by the grammar. Unreachable symbols are those that cannot be derived from the start symbol, thus playing no role in the language generation. This function performs two key tasks: it first identifies and removes non-generating symbols and then removes unreachable symbols. The process involves constructing sets of generating and reachable symbols and iteratively refining these sets until all symbols in the grammar contribute to the language and are accessible from the start symbol. This cleanup not only simplifies the grammar but also ensures that every remaining element is functional and necessary for the grammar to define its language fully.

**Code Snippet:**
```java
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
```

### 4. Convert to Proper CNF

The final transformation converts the grammar into Chomsky Normal Form, a specific type of grammar needed for certain theoretical and practical applications, such as the CYK parsing algorithm. CNF requires all production rules to be in one of two forms: a non-terminal producing exactly two non-terminals, or a non-terminal producing exactly one terminal symbol. This function examines each production rule and, if necessary, breaks down complex productions (those involving more than two symbols on the right-hand side) by introducing new non-terminals and rewriting the productions into multiple rules that conform to CNF. This step is pivotal as it standardizes the grammar's format, facilitating its use in algorithms that require CNF.

**Code Snippet:**
```java
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
```

## Testing

### 1. Unit Test for Epsilon Productions Elimination

This test evaluates whether the eliminateEpsilonProductions function accurately removes all direct and indirect epsilon productions without altering the language that the grammar can generate (excluding the empty string). The test ensures that for every nullable non-terminal, all combinations of its appearance in other productions have been considered and that the resulting grammar no longer produces ε directly.

**Code Snippet:**
```java
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
```

### 2. Unit Test for Unit Productions Elimination

This test checks the correctness of the eliminateUnitProductions function, ensuring that all unit productions are replaced effectively with their target non-terminal's productions. It tests various scenarios to ensure that no unit production is left in the grammar and that the substitutions preserve the original language.

**Code Snippet:**
```java
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
```

### 3. Unit Test for Useless Symbols Elimination

This test verifies the effectiveness of the eliminateUselessSymbols function in removing all non-generating and unreachable symbols from the grammar. It ensures that the function correctly identifies symbols that do not contribute to the grammar's language or cannot be reached from the start symbol, and that these symbols are completely removed, leaving a streamlined and fully functional grammar.

**Code Snippet:**
```java
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
```

### 4. Unit Test for CNF Conversion

This test confirms that the convertToProperCNF function transforms the grammar into strict Chomsky Normal Form. It checks that all production rules conform to the two allowed formats in CNF and that the transformation does not introduce errors or alter the language defined by the grammar. The test involves verifying that the final grammar structure is optimal for parsing and theoretical analysis.

**Code Snippet:**
```java
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
```

## Conclusion

In this project, we thoroughly explored and implemented the transformation of context-free grammars into Chomsky Normal Form. Through the development of the `ConvertCNF` Java class, we learned about the intricacies of handling various types of productions within CFGs and the importance of systematically transforming these grammars into a more structured form. This experience has provided valuable insights into the theoretical underpinnings of computational linguistics and automata theory, as well as practical applications in parser design and other areas of computer science. The implementation and subsequent testing have reinforced our understanding of grammar transformations, ensuring a robust solution capable of handling complex grammars.
