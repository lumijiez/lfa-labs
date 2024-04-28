package Lab6;

class BinaryOperatorExpr extends Expr {
    Expr left;
    Expr right;
    char operator;

    BinaryOperatorExpr(Expr left, Expr right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    double evaluate() {
        return switch (operator) {
            case '+' -> left.evaluate() + right.evaluate();
            case '-' -> left.evaluate() - right.evaluate();
            case '*' -> left.evaluate() * right.evaluate();
            case '/' -> left.evaluate() / right.evaluate();
            default -> throw new RuntimeException("Unsupported operator: " + operator);
        };
    }
}
