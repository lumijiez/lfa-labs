package Lab6;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        input = input.replaceAll("\\s", "");
        int i = 0;
        while (i < input.length()) {
            char ch = input.charAt(i);
            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    sb.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
            } else if (Character.isLetter(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    sb.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.FUNCTION, sb.toString()));
            } else {
                switch (ch) {
                    case '(':
                        tokens.add(new Token(TokenType.LPAREN, "("));
                        i++;
                        break;
                    case ')':
                        tokens.add(new Token(TokenType.RPAREN, ")"));
                        i++;
                        break;
                    case ',':
                        tokens.add(new Token(TokenType.COMMA, ","));
                        i++;
                        break;
                    default:
                        tokens.add(new Token(TokenType.OPERATOR, String.valueOf(ch)));
                        i++;
                        break;
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
