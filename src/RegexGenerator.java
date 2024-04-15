import java.util.Random;

public class RegexGenerator {

    public static String[] generateRandomNr1(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        System.out.println("=========================");

        for (int i = 0; i < numStrings; i++) {
            System.out.println("String Nr. " + (i+1));
            StringBuilder sb = new StringBuilder();

            sb.append("O");
            System.out.println("Putting a fixed 'O'");

            int length = random.nextInt(1,5);
            for (int j = 0; j < length; j++) {
                char randomChar = randomChar("PQR");
                sb.append(randomChar);
                System.out.println("Putting a random char from \"PQR\": " + randomChar);
            }

            sb.append("2");
            System.out.println("Putting a fixed '2'");

            char randomDigit = randomChar("34");
            sb.append(randomDigit);
            System.out.println("Putting a random digit from \"34\": " + randomDigit);

            randomStrings[i] = sb.toString();
            System.out.println("=========================");
        }

        return randomStrings;
    }

    public static String[] generateRandomNr2(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        System.out.println("=========================");

        for (int i = 0; i < numStrings; i++) {
            System.out.println("String Nr. " + (i+1));
            StringBuilder sb = new StringBuilder();

            int length1 = random.nextInt(6);
            for (int j = 0; j < length1; j++) {
                char randomChar = randomChar("A");
                sb.append(randomChar);
                System.out.println("Putting a random char from \"A\": " + randomChar);
            }

            sb.append("B");
            System.out.println("Putting a fixed 'B'");

            char randomChar = randomChar("CDE");
            sb.append(randomChar);
            System.out.println("Putting a random char from \"CDE\": " + randomChar);

            sb.append("F");
            System.out.println("Putting a fixed 'F'");

            char chr = randomChar("GHI");
            sb.append(chr);
            System.out.println("Putting a random char from \"GHI\": " + chr);

            sb.append(chr);
            System.out.println("Repeating the previous random char: " + chr);

            randomStrings[i] = sb.toString();
            System.out.println("=========================");
        }

        return randomStrings;
    }

    public static String[] generateRandomNr3(int numStrings) {
        String[] randomStrings = new String[numStrings];
        Random random = new Random();

        System.out.println("=========================");

        for (int i = 0; i < numStrings; i++) {
            System.out.println("String Nr. " + (i+1));
            StringBuilder sb = new StringBuilder();

            int length1 = random.nextInt(1,6);
            for (int j = 0; j < length1; j++) {
                sb.append("J");
                System.out.println("Putting a fixed 'J'");
            }

            sb.append("K");
            System.out.println("Putting a fixed 'K'");

            length1 = random.nextInt(6);
            for (int j = 0; j < length1; j++) {
                char randomChar = randomChar("LMN");
                sb.append(randomChar);
                System.out.println("Putting a random char from \"LMN\": " + randomChar);
            }

            length1 = random.nextInt(2);
            for (int j = 0; j < length1; j++) {
                sb.append("O");
                System.out.println("Putting a fixed 'O'");
            }

            length1 = 3;
            char chr = randomChar("PQ");
            for (int j = 0; j < length1; j++) {
                sb.append(chr);
                System.out.println("Putting a random char from \"PQ\": " + chr);
            }

            randomStrings[i] = sb.toString();
            System.out.println("=========================");
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
            System.out.println(str);
        }
        System.out.println("=========================");

        String[] randomStrings2 = generateRandomNr2(5);
        System.out.println("All Strings Nr2: ");
        for (String str : randomStrings2) {
            System.out.println(str);
        }
        System.out.println("=========================");

        String[] randomStrings3 = generateRandomNr3(5);
        System.out.println("All Strings Nr3: ");
        for (String str : randomStrings3) {
            System.out.println(str);
        }
        System.out.println("=========================");
    }
}
