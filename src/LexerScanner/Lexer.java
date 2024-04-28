package LexerScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lexer {
    public enum TokenType {
        NUMBER,
        OPERATOR,
        LPAREN,
        RPAREN,
        FUNCTION,
        COMMA,
        EOF
    }

    public static class Token {
        public TokenType type;
        public String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

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

        if (!areParenthesesBalanced(tokens)) {
            throw new RuntimeException("Parentheses are not balanced.");
        }

        return tokens;
    }

    private static boolean areParenthesesBalanced(List<Token> tokens) {
        Stack<TokenType> stack = new Stack<>();
        for (Token token : tokens) {
            if (token.type == TokenType.LPAREN) {
                stack.push(TokenType.LPAREN);
            } else if (token.type == TokenType.RPAREN) {
                if (stack.isEmpty() || stack.pop() != TokenType.LPAREN) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String input = "(3 + 5) * loge(16)";
        try {
            List<Token> tokens = tokenize(input);
            for (Token token : tokens) {
                System.out.println(token.type + " : " + token.value);
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
