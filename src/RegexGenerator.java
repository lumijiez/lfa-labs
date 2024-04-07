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
            System.out.println(sb);

            int length = random.nextInt(1,5);
            for (int j = 0; j < length; j++) {
                sb.append(randomChar("PQR"));
                System.out.println(sb);
            }

            sb.append("2");
            System.out.println(sb);

            sb.append(randomChar("34"));
            System.out.println(sb);

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
                sb.append(randomChar("A"));
                System.out.println(sb);
            }

            sb.append("B");
            System.out.println(sb);

            sb.append(randomChar("CDE"));
            System.out.println(sb);

            sb.append("F");
            System.out.println(sb);

            char chr = randomChar("GHI");
            sb.append(chr);
            System.out.println(sb);

            sb.append(chr);
            System.out.println(sb);

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
                System.out.println(sb);
            }

            sb.append("K");
            System.out.println(sb);

            length1 = random.nextInt(6);
            for (int j = 0; j < length1; j++) {
                sb.append(randomChar("LMN"));
                System.out.println(sb);
            }

            length1 = random.nextInt(2);
            for (int j = 0; j < length1; j++) {
                sb.append("O");
                System.out.println(sb);
            }

            length1 = 3;
            char chr = randomChar("PQ");
            for (int j = 0; j < length1; j++) {
                sb.append(chr);
                System.out.println(sb);
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
