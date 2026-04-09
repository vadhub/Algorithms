import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] arr = {2, 5, -1, 12, 72, 3, 38, 45, 56, 72};

//        quickSort(arr, 0, arr.length - 1);              // быстрая сортировка
//        bubbleSort(arr);                                // сортировка пузырьком
        insertionSort(arr);                               // сортировка вставками
        System.out.println(Arrays.toString(arr));         // вывод массива в консоль
        System.out.println(max(arr));                     // поиск максимального элемента в массиве
        System.out.println(binarySearch(arr, 56));  // бинарный поиск
        System.out.println(Arrays.toString(revers(arr))); // вывод массива в обратном порядке (реверс)
        System.out.println(sumNumber(-103));              // сумма цифр числа
        System.out.println(isPalindrome("aabbaa"));   // палиндром
        System.out.println(factorial(5));              // факториал 1*2*3*4...
        System.out.println(fibonacci(7));              // поиск 7-го числа Фибоначчи (0, 1, 2, 3, 5, 8, 13...)
    }

    // сортировка вставками
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    // сортировка пузырьком
    public static void bubbleSort(int[] arr) {
        boolean isSorted = false;

        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    isSorted = false;
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                }
            }
        }
    }

    // =======================Quick Sort==============================
    // быстрая сортировка
    // разделение массива на несколько частей по индексу опорного элемента
    public static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    // взять массив и переставить элементы так, чтобы:
    // - слева от опорного (pivot) были элементы меньше или равные ему
    // - справа - больше него
    // - вернуть индекс, где стоит опорный элемент
    public static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, right);
        return i + 1;
    }

    // замена элементов местами
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    // ==============================================================

    // бинарный поиск
    public static int binarySearch(int[] arr, int target) {

        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    // поиск максимального элемента
    public static int max(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return max;
    }

    // вывод массива в обратном порядке (реверс)
    public static int[] revers(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) return arr;
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }

        return arr;
    }

    // сумма цифр числа
    public static int sumNumber(int number) {
        number = Math.abs(number);
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    // изменится ли строка, если ее прочитать в обратном порядке (палиндром)
    public static boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    // проверка на палиндром (читерский способ)
    public static boolean isPalindrome2(String str) {
        return new StringBuffer(str).reverse().toString().equals(str);
    }

    // факториал числа (рекурсивный)
    public static int factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Факториал определён только для n >= 0");
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // факториал числа
    public static int factorial2(int n) {
        if (n < 0) throw new IllegalArgumentException("Факториал определён только для n >= 0");
        if (n <= 1) return 1;
        int f = 1;

        for (int i = 1; i <= n; i++) {
            f*=i;
        }

        return f;
    }

    // поиск n-го числа в последовательности Фибоначчи (рекурсивный)
    // этот вариант работает медленнее чем нерекурсивный
    // 50 - элемент будет считаться от нескольких минут до часов (зависит от мощности компьютера)
    public static int fibonacci(int n) {
        if (n == 0 || n == 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // поиск n-го числа в последовательности Фибоначчи
    public static int fibonacci2(int n) {
        if (n <= 1) return n;
        int n1 = 0;
        int n2 = 1;
        int n3 = n1 + n2;
        for (int i = 2; i < n; i++) {
            n1 = n2;
            n2 = n3;
            n3 = n1 + n2;
        }
        return n3;
    }
}
