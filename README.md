# Formal Languages & Automata Laboratories

This repository contains a series of laboratory exercises and implementations related to formal languages and automata theory, a key area of theoretical computer science. The code is primarily written in Java and covers topics such as regular grammars, finite automata, lexical analysis, parsing, and the conversion of grammars to Chomsky Normal Form (CNF).

## Repository Structure

### Main.java
- **Purpose**: Serves as the main entry point for the project, showcasing examples and exercises related to regular grammars and finite automata.
- **Key Features**:
  - Defines grammar rules, states, alphabets, and transitions.
  - Implements Lab exercises on regular grammars and finite automata.

### ChomskyNormalForm (Package)
- **ConvertCNF.java**: Contains logic to convert context-free grammars into Chomsky Normal Form (CNF). This is a standard form for context-free grammars used in various parsing algorithms.

### FiniteAutomata (Package)
- **ChomskyChecker.java**: Checks whether a given context-free grammar is in Chomsky Normal Form.
- **ManualFiniteAutomaton.java**: Facilitates the manual creation and testing of finite automata.

### LexerScanner (Package)
- **Lexer.java**: Implements a lexical analyzer that processes input strings and converts them into tokens based on defined rules.

### ParserAST (Package)
- **BinaryOperatorExpr.java, FunctionExpr.java, NumberExpr.java, Expr.java**: Represent different types of expressions within an abstract syntax tree (AST).
- **Lexer.java**: Specific to parsing, it tokenizes input for the parser.
- **MainParser.java, Parser.java**: Implements the parsing logic, converting tokens into an AST.
- **Token.java, TokenType.java**: Define the types of tokens and their structure.

### RegularExpressions (Package)
- **RegexGenerator.java**: Generates regular expressions based on provided input, useful in the context of regular languages.

### RegularGrammars (Package)
- **FiniteAutomaton.java**: Models finite automata, interacting with grammars to check language acceptance.
- **Grammar.java**: Represents formal grammars, including methods for language generation and rule checking.

### util (Package)
- **Pair.java**: A utility class that represents a pair of objects, useful in mappings like state transitions in automata.

## License
This repository is licensed under the GPL-3.0 License. See the [LICENSE](LICENSE) file for more details.

## Getting Started
To explore and run the exercises:
1. Clone the repository to your local machine.
2. Open the project in an IDE like IntelliJ IDEA.
3. Run the `Main.java` file to see examples and outputs.

## Contributing
Feel free to fork this repository and contribute by submitting pull requests. For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgments
This project is intended for educational purposes and is based on coursework related to formal languages and automata theory.
