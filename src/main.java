import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

public class main {

    public static void main(String[] args) {

        if(args.length > 2 || args.length < 1) {
            System.err.println("Usage: java Anagrams <word> [count].");
            System.exit(1);
        }

        HashMap<String, Stack<String>> map  = new HashMap<>();
        BufferedReader wordsReader  = null;
        BufferedReader sortedReader = null;

        try {
            File words  = new File("words.txt");
            File sorted = new File("sorted.txt");

            wordsReader     = new BufferedReader(new FileReader(words));
            sortedReader    = new BufferedReader(new FileReader(sorted));

            String key;
            String word;

            while((key = sortedReader.readLine()) != null) {
                word = wordsReader.readLine();
                if (!map.containsKey(key)) {
                    map.put(key, new Stack<>());
                }
                map.get(key).push(word);
            }
        } catch (IOException fileError) {
            System.err.println("An error has occurred whilst reading the dictionary file, please try again.");
            System.exit(2);
        } finally {
            try {
                if(wordsReader != null) {
                    wordsReader.close();
                }
                if(sortedReader != null) {
                    sortedReader.close();
                }
            } catch (IOException fileError) {
                System.err.println("An error has occurred whilst reading the dictionary file, please try again.");
                System.exit(2);
            }
        }

        String value = args[0].toLowerCase(Locale.ROOT);
        int cnt = 1;
        if (args.length > 1) {
            try {
                cnt = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.err.println("Usage: [count] must be a number.");
                System.exit(1);
            }
        }

        String sortedVal = sort(value);

        Stack<String> anagrams = null;

        try {
            anagrams = map.get(sortedVal);
        } catch (NullPointerException npe) {
            System.err.println("This word is not listed in the dictionary file.");
            System.exit(1);
        }

        if(anagrams != null) {
            StringBuilder builder = new StringBuilder(value + "'s anagram(s) are: ");
            int curr = cnt;
            if(anagrams.size() == 1) {
                System.out.println(value + " does not have any anagrams.");
                System.exit(0);
            }
            while (curr != 0 && !anagrams.isEmpty()) {
                if(!anagrams.peek().equals(value)) {
                    builder.append(anagrams.pop());
                    curr--;
                    if(curr != 0) {
                        builder.append(", ");
                    }
                } else {
                    anagrams.pop();
                }
                if (anagrams.isEmpty() && curr != 0) {
                    System.err.println(value + " does not have " + cnt + " anagram(s).");
                    System.exit(1);
                }
            }
            System.out.println(builder.toString());
            System.exit(0);
        } else {
            System.err.println("This word is not listed in the dictionary file.");
            System.exit(1);
        }
    }

    private static String sort(String value) {
        value = value.toLowerCase(Locale.ROOT);
        char[] arr = value.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }
}
