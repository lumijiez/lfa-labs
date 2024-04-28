package Lab4;

import java.util.Random;

public class RegexGenerator {

    public static String[] generateRandomNr1(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        for (int i = 0; i < numStrings; i++) {
            StringBuilder sb = new StringBuilder();

            sb.append("O");

            int length = random.nextInt(1,5);
            for (int j = 0; j < length; j++) {
                char randomChar = randomChar("PQR");
                sb.append(randomChar);
            }

            sb.append("2");
            char randomDigit = randomChar("34");
            sb.append(randomDigit);

            randomStrings[i] = sb.toString();
        }

        return randomStrings;
    }

    public static String[] generateRandomNr2(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        for (int i = 0; i < numStrings; i++) {
            StringBuilder sb = new StringBuilder();

            int length1 = random.nextInt(6);
            for (int j = 0; j < length1; j++) {
                char randomChar = randomChar("A");
                sb.append(randomChar);
            }

            sb.append("B");

            char randomChar = randomChar("CDE");
            sb.append(randomChar);

            sb.append("F");

            char chr = randomChar("GHI");
            sb.append(chr);
            sb.append(chr);

            randomStrings[i] = sb.toString();
        }

        return randomStrings;
    }

    public static String[] generateRandomNr3(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        for (int i = 0; i < numStrings; i++) {
            StringBuilder sb = new StringBuilder();

            int length1 = random.nextInt(1,6);
            sb.append("J".repeat(Math.max(0, length1)));

            sb.append("K");

            length1 = random.nextInt(6);
            for (int j = 0; j < length1; j++) {
                char randomChar = randomChar("LMN");
                sb.append(randomChar);
            }

            length1 = random.nextInt(2);
            sb.append("O".repeat(length1));

            length1 = 3;
            char chr = randomChar("PQ");
            sb.append(String.valueOf(chr).repeat(length1));

            randomStrings[i] = sb.toString();
        }

        return randomStrings;
    }

    private static char randomChar(String characters) {
        Random random = new Random();
        int index = random.nextInt(characters.length());
        return characters.charAt(index);
    }

    public static void main(String[] args) {
        String[] randomStrings1 = generateRandomNr1(5);
        System.out.println("All Strings Nr1: ");
        for (String str : randomStrings1) {
            explainRandomNr1(str);
        }
        System.out.println("=========================");

        String[] randomStrings2 = generateRandomNr2(5);
        System.out.println("All Strings Nr2: ");
        for (String str : randomStrings2) {
            explainRandomNr2(str);
        }
        System.out.println("=========================");

        String[] randomStrings3 = generateRandomNr3(5);
        System.out.println("All Strings Nr3: ");
        for (String str : randomStrings3) {
            explainRandomNr3(str);
        }
        System.out.println("=========================");
    }

    public static void explainRandomNr1(String s) {
        System.out.println("Input: " + s);
        System.out.println("O -> O");
        int mid = s.indexOf('2');
        System.out.println("(P|Q|R)+ -> " + s.substring(1, mid));
        System.out.println("2 -> 2");
        System.out.println("(3|4) -> " + s.charAt(s.length() - 1));
    }

    public static void explainRandomNr2(String s) {
        System.out.println("Input: " + s);
        int idx = 0;
        while (s.charAt(idx) == 'A') {
            idx++;
        }
        System.out.println("A* -> " + (idx > 0 ? s.substring(0, idx) : "'' (no A's)"));
        System.out.println("B -> B");
        char cde = s.charAt(idx + 1);
        System.out.println("(C|D|E) -> " + cde);
        System.out.println("F -> F");
        System.out.println("(G|H|I)^2 -> " + s.substring(idx + 3));
    }

    public static void explainRandomNr3(String s) {
        System.out.println("Input: " + s);
        int idx = s.indexOf('K');
        System.out.println("J+ -> " + s.substring(0, idx));
        System.out.println("K -> K");
        int nextPart = idx + 1;
        while (nextPart < s.length() && "LMN".contains(s.charAt(nextPart) + "")) {
            nextPart++;
        }
        System.out.println("(L|M|N)* -> " + (nextPart > idx + 1 ? s.substring(idx + 1, nextPart) : "'' (no L, M, N)"));
        if (nextPart < s.length() && s.charAt(nextPart) == 'O') {
            System.out.println("O? -> O");
            nextPart++;
        }
        System.out.println("(P|Q)^3 -> " + s.substring(nextPart));
    }
}
