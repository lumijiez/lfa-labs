package Lab6;

import java.util.List;

class FunctionExpr extends Expr {
    String function;
    List<Expr> arguments;

    FunctionExpr(String function, List<Expr> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    double evaluate() {
        return switch (function) {
            case "sqrt" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("sqrt expects exactly one argument");
                }
                yield Math.sqrt(arguments.get(0).evaluate());
            }
            case "logtwo" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("log2 expects exactly one argument");
                }
                yield Math.log(arguments.get(0).evaluate()) / Math.log(2);
            }
            case "logten" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("log10 expects exactly one argument");
                }
                yield Math.log10(arguments.get(0).evaluate());
            }
            case "loge" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("loge expects exactly one argument");
                }
                yield Math.log(arguments.get(0).evaluate());
            }
            case "powtwo" -> {
                if (arguments.size() != 1) {
                    throw new RuntimeException("pow2 expects exactly one argument");
                }
                yield Math.pow(2, arguments.get(0).evaluate());
            }
            default -> throw new RuntimeException("Unsupported function or wrong number of arguments: " + function);
        };
    }
}
