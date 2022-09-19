import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger threeCharWords = new AtomicInteger(0);
    static AtomicInteger fourCharWords = new AtomicInteger(0);
    static AtomicInteger fiveCharWords = new AtomicInteger(0);
    static final String LETTER = "abc";

    public static void main(String[] args) {
        String[] texts = generateArrayText(100_000, LETTER);

        Runnable palindromeLogic = () -> {
            for (String text : texts) {
                if (isPalindrome(text)) increase(text);
            }
        };
        Runnable sameLetterLogic = () -> {
            for (String text : texts) {
                if (isSameLetter(text)) increase(text);
            }
        };
        Runnable ascendingLogic = () -> {
            for (String text : texts) {
                if (isAscendingOrder(text)) increase(text);
            }
        };

        ThreadGroup group = new ThreadGroup("Group1");

        new Thread(group, palindromeLogic).start();
        new Thread(group, sameLetterLogic).start();
        new Thread(group, ascendingLogic).start();

        waitAndFinish(group);

        System.out.println("Beautiful words with length 3: " + threeCharWords);
        System.out.println("Beautiful words with length 4: " + fourCharWords);
        System.out.println("Beautiful words with length 5: " + fiveCharWords);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String[] generateArrayText(int length, String letters) {
        Random random = new Random();
        String[] texts = new String[length];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(letters, 3 + random.nextInt(3));
        }
        return texts;
    }

    public static void increase(String word) {
        switch (word.length()) {
            case 3 -> threeCharWords.getAndIncrement();
            case 4 -> fourCharWords.getAndIncrement();
            case 5 -> fiveCharWords.getAndIncrement();
        }
    }

    public static boolean isPalindrome(String word) {
        String firstHalf = word.substring(0, word.length() / 2);
        StringBuilder secondHalf = new StringBuilder();
        for (int i = word.length() - 1; i >= word.length() / 2; i--) {
            secondHalf.append(word.charAt(i));
        }
        return firstHalf.equals(secondHalf.toString());
    }

    public static boolean isSameLetter(String word) {
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(0) != word.charAt(i)) return false;
        }
        return true;
    }

    //bca
    public static boolean isAscendingOrder(String word) {
        int letterCount = 0;
        for (int i = 0; i < word.length(); i++) {

            if (word.charAt(i) == LETTER.charAt(letterCount)) continue;

            for (int j = 1; j + letterCount < LETTER.length(); j++) {
                if (word.charAt(i) == LETTER.charAt(letterCount + j)) {
                    letterCount += j;
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public static void waitAndFinish(ThreadGroup group) {
        while (group.activeCount() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(group.getName() + " is end");
        group.interrupt();
    }
}
