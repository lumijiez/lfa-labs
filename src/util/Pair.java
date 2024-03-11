package util;

public record Pair(String first, String second) {

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
