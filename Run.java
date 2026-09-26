package crossword;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Run {
    static void main() {
        List<String> words = Arrays.asList("Run", "String", "Word", "World", "List", "Hello", "Util", "Java", "System", "Print", "Space");
        Collections.shuffle(words);
        Grid.Crossword cw = Grid.generate(words);

        for (char[] chars : cw.grid()) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }

        // все размещённые слова с координатами
        for (Grid.PlacedWord pw : cw.placed()) {
            System.out.println(pw.word() + " -> " + pw.x() + "," + pw.y()
                    + (pw.horizontal() ? " (H)" : " (V)"));
            for (int i = 0; i < pw.word().length(); i++) {
                int[] p = pw.letterPos(i);
                System.out.printf("  '%c' at (%d,%d)%n", pw.word().charAt(i), p[0], p[1]);
            }
        }

        // координаты конкретного слова
        Grid.PlacedWord hello = cw.find("Hello");
        if (hello != null) System.out.println("Hello start: " + hello.x() + "," + hello.y());
    }
}
