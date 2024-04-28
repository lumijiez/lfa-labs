package Lab6;

import java.util.List;

public class MainParser {
    public static void main(String[] args) {
        String input = "(3 + 5) * sqrt(16) + powtwo(5)";
        List<Token> tokens = Lexer.tokenize(input);
        System.out.println("Tokens:");
        tokens.forEach(System.out::println);

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();
        double result = expression.evaluate();
        System.out.println("Result: " + result);
    }
}
