package crossword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {
    private static final int SIZE = 20;
    private static final char EMPTY = '-';

    private char[][] grid = new char[SIZE][SIZE];
    private final List<PlacedWord> placed = new ArrayList<>();

    public Grid() {
        for (char[] row : grid) Arrays.fill(row, EMPTY);
    }

    public static Crossword generate(List<String> words) {
        Grid g = new Grid();
        g.build(words);
        return new Crossword(g.grid, List.copyOf(g.placed));
    }

    private void build(List<String> words) {
        List<String> sorted = new ArrayList<>(words);
        sorted.sort((a, b) -> b.length() - a.length());

        String first = sorted.remove(0);
        int cx = (SIZE - first.length()) / 2;
        int cy = SIZE / 2;
        place(first, cx, cy, true);

        List<String> remaining = new ArrayList<>(sorted);

        // Первый проход
        remaining = passThrough(remaining);

        // Второй проход — по остаткам
        remaining = passThrough(remaining);

        // Третий — если ещё остались
        remaining = passThrough(remaining);

        // remaining — то, что так и не влезло, можно вывести в лог
        if (!remaining.isEmpty()) {
            System.out.println("Не размещено: " + remaining);
        }
    }

    /** Проходит по списку и возвращает те слова, которые не удалось разместить. */
    private List<String> passThrough(List<String> words) {
        List<String> skipped = new ArrayList<>();
        for (String w : words) {
            if (!tryPlace(w)) skipped.add(w);
        }
        return skipped;
    }

    private boolean tryPlace(String word) {
        // перебираем уже размещённые слова
        for (PlacedWord pw : placed) {
            for (int i = 0; i < pw.word.length(); i++) {
                char ch = pw.word.charAt(i);

                // позиция этой буквы в сетке
                int wx = pw.horizontal ? pw.x + i : pw.x;
                int wy = pw.horizontal ? pw.y     : pw.y + i;

                // ищем совпадающие буквы в новом слове
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) != ch) continue;

                    // новое слово ставим перпендикулярно
                    boolean newHorizontal = !pw.horizontal;

                    int nx = newHorizontal ? wx - j : wx;
                    int ny = newHorizontal ? wy     : wy - j;

                    if (canPlace(word, nx, ny, newHorizontal)) {
                        place(word, nx, ny, newHorizontal);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canPlace(String word, int x, int y, boolean horizontal) {
        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;

        // выход за границы
        if (x < 0 || y < 0) return false;
        if (horizontal && x + word.length() > SIZE) return false;
        if (!horizontal && y + word.length() > SIZE) return false;

        // клетка до и после слова должна быть пустой
        int bx = x - dx, by = y - dy;
        if (inBounds(bx, by) && grid[by][bx] != EMPTY) return false;
        int ex = x + dx * word.length(), ey = y + dy * word.length();
        if (inBounds(ex, ey) && grid[ey][ex] != EMPTY) return false;

        int intersections = 0;
        for (int i = 0; i < word.length(); i++) {
            int cx = x + dx * i;
            int cy = y + dy * i;
            char cur = grid[cy][cx];

            if (cur == EMPTY) {
                // соседи перпендикулярно должны быть пустыми
                if (horizontal) {
                    if (inBounds(cx, cy - 1) && grid[cy - 1][cx] != EMPTY) return false;
                    if (inBounds(cx, cy + 1) && grid[cy + 1][cx] != EMPTY) return false;
                } else {
                    if (inBounds(cx - 1, cy) && grid[cy][cx - 1] != EMPTY) return false;
                    if (inBounds(cx + 1, cy) && grid[cy][cx + 1] != EMPTY) return false;
                }
            } else if (cur == word.charAt(i)) {
                intersections++;
            } else {
                return false; // конфликт букв
            }
        }
        return intersections >= 1; // минимум одно пересечение
    }

    private void place(String word, int x, int y, boolean horizontal) {
        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;
        for (int i = 0; i < word.length(); i++) {
            grid[y + dy * i][x + dx * i] = word.charAt(i);
        }
        placed.add(new PlacedWord(word, x, y, horizontal));
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE;
    }

    public record PlacedWord(String word, int x, int y, boolean horizontal) {
        public int length() { return word.length(); }

        /** Координаты i-й буквы. */
        public int[] letterPos(int i) {
            int dx = horizontal ? 1 : 0;
            int dy = horizontal ? 0 : 1;
            return new int[]{ x + dx * i, y + dy * i }; // {x, y}
        }

        /** Все позиции букв слова в порядке следования. */
        public List<int[]> allPositions() {
            List<int[]> list = new ArrayList<>(word.length());
            for (int i = 0; i < word.length(); i++) list.add(letterPos(i));
            return list;
        }

        /** Позиции конкретных букв (может быть несколько одинаковых). */
        public List<int[]> positionsOf(char c) {
            List<int[]> list = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == c) list.add(letterPos(i));
            }
            return list;
        }
    }

    public record Crossword(char[][] grid, List<PlacedWord> placed) {
        public PlacedWord find(String word) {
            return placed.stream()
                    .filter(p -> p.word().equals(word))
                    .findFirst().orElse(null);
        }
    }
}
