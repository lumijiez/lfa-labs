package ParserAST;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        return expression();
    }

    private Expr expression() {
        Expr expr = term();
        while (match(TokenType.OPERATOR)) {
            char operator = tokens.get(current - 1).value.charAt(0);
            Expr right = term();
            expr = new BinaryOperatorExpr(expr, right, operator);
        }
        return expr;
    }

    private Expr term() {
        if (match(TokenType.NUMBER)) {
            return new NumberExpr(Double.parseDouble(previous().value));
        } else if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return expr;
        } else if (match(TokenType.FUNCTION)) {
            String functionName = previous().value;
            consume(TokenType.LPAREN, "Expect '(' after function name.");
            List<Expr> args = new ArrayList<>();
            if (!check(TokenType.RPAREN)) {
                do {
                    args.add(expression());
                } while (match(TokenType.COMMA));
            }
            consume(TokenType.RPAREN, "Expect ')' after arguments.");
            return new FunctionExpr(functionName, args);
        }
        throw new RuntimeException("Unexpected token.");
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            current++;
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return tokens.get(current).type == type;
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void consume(TokenType type, String message) {
        if (check(type)) {
            current++;
            return;
        }
        throw new RuntimeException(message);
    }

    private boolean isAtEnd() {
        return current >= tokens.size() || tokens.get(current).type == TokenType.EOF;
    }
}
