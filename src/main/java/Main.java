import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    public static void insertionsSort(ArrayList<Integer> a) {
        for (int i = 1; i < a.size(); i++) {
            for (int j = i; j > 0 && a.get(j - 1) > a.get(j); j--) {
                Collections.swap(a, j - 1, j);
            }
        }
    }

    public static int minRunLength(int n) {
        int flag = 0;
        while (n >= 64) {
            flag |= n & 1; // flag = flag | (n & 1)
            n >>= 1;
        }
        return n + flag;
    }

    public static void arrayTurn (ArrayList<Integer> thisArray) {
        for (int i = 0; i < thisArray.size() / 2; i++) {
            Collections.swap(thisArray, i, thisArray.size() - 1 - i);
        }
    }

    public static ArrayList<Integer> arrayMerge (ArrayList<Integer> firstArray, ArrayList<Integer> secondArray) {
        ArrayList<Integer> sharedArray = new ArrayList<>();
        int indexFirstArray = 0, indexSecondArray = 0;
        while (sharedArray.size() != firstArray.size() + secondArray.size()) {
            if (indexFirstArray < firstArray.size() && indexSecondArray < secondArray.size()) {
                if (firstArray.get(indexFirstArray) <= secondArray.get(indexSecondArray)) {
                    sharedArray.add(firstArray.get(indexFirstArray));
                    indexFirstArray++;
                } else {
                    sharedArray.add(secondArray.get(indexSecondArray));
                    indexSecondArray++;
                }
            } else {
                while (indexFirstArray < firstArray.size()) {
                    sharedArray.add(firstArray.get(indexFirstArray));
                    indexFirstArray++;
                }
                while (indexSecondArray < secondArray.size()) {
                    sharedArray.add(secondArray.get(indexSecondArray));
                    indexSecondArray++;
                }
            }
        }
        return sharedArray;
    }

    public static void main(String[] args) throws Exception {

        // заполнение input

        boolean completion  = false;
        if (completion) {
            new Generator().completion();
        }
        for (int i = 100; i <= 10000; i += 100)
            System.out.println(i);
        System.out.println();
        System.exit(0);
        // считывание

        for (int l = 100; l <= 10000; l+= 100) {
            String path = "data/Test" + l + ".txt";
            InputStream inputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            int n = Integer.parseInt(reader.readLine());
            Integer[] a = new Integer[n];
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(reader.readLine());
            }

            // начало алгоритма

            long time = System.nanoTime();

            if (n < 64) {
                ArrayList<Integer> tempList = new ArrayList<>(Arrays.asList(a));
                insertionsSort(tempList);
                System.out.println(tempList.toString());
                return;
            }

            ArrayList<ArrayList<Integer>> run = new ArrayList<>();
            int minRunLength = minRunLength(n);
            int runIndex = 0;
            int minRunIndex;

            //начало разделения входного массива

            while (runIndex < n) {
                minRunIndex = 0;
                ArrayList<Integer> tempArray = new ArrayList<>();
                tempArray.add(a[runIndex]);
                runIndex++;
                minRunIndex++;
                if (runIndex < n) {
                    tempArray.add(a[runIndex]);
                    runIndex++;
                    minRunIndex++;
                } else {
                    run.add(tempArray);
                    break;
                }
                if (runIndex < n) {
                    if (a[runIndex - 2] <= a[runIndex - 1]) {
                        while (runIndex < n && a[runIndex] >= a[runIndex - 1]) {
                            tempArray.add(a[runIndex]);
                            runIndex++;
                            minRunIndex++;
                        }
                    } else {
                        while (runIndex < n && a[runIndex] <= a[runIndex - 1]) {
                            tempArray.add(a[runIndex]);
                            runIndex++;
                            minRunIndex++;
                        }
                        arrayTurn(tempArray);
                    }
                }
                while (runIndex < n && minRunIndex < minRunLength) {
                    tempArray.add(a[runIndex]);
                    runIndex++;
                    minRunIndex++;
                }

                insertionsSort(tempArray);
                run.add(tempArray);
            }

            //конец разделения

            runIndex = 0;
            while (run.size() != 1) {
                if (runIndex == 0) {
                    runIndex++;
                    continue;
                }
                if (run.get(runIndex).size() >= run.get(runIndex - 1).size() || run.size() == 2) {
                    run.set(runIndex - 1, arrayMerge(run.get(runIndex), run.get(runIndex - 1)));
                    run.remove(runIndex);
                    runIndex--;
                    continue;
                }
                if (runIndex == 1) {
                    runIndex++;
                    continue;
                }
                if (run.get(runIndex - 2).size() <= run.get(runIndex).size() + run.get(runIndex - 1).size()) {
                    if (run.get(runIndex).size() <= run.get(runIndex - 2).size()) {
                        run.set(runIndex - 1, arrayMerge(run.get(runIndex), run.get(runIndex - 1)));
                        run.remove(runIndex);
                        runIndex--;
                    } else {
                        run.set(runIndex - 2, arrayMerge(run.get(runIndex - 2), run.get(runIndex - 1)));
                        run.remove(runIndex - 1);
                        runIndex--;
                    }
                } else {
                    if (run.size() - 1 == runIndex) {
                        run.set(runIndex - 1, arrayMerge(run.get(runIndex), run.get(runIndex - 1)));
                        run.remove(runIndex);
                        runIndex--;
                    } else {
                        runIndex++;
                    }
                }
            }
            long algoTime = (System.nanoTime() - time) / 1000;
            ArrayList<Integer> tempList = new ArrayList<>(Arrays.asList(a));
            insertionsSort(tempList);

            // запись в файл
            boolean check = true;
            if (l == 100)
                check = false;
            OutputStream outputStream = new FileOutputStream("data/OutputData.txt", check);
            PrintStream printStream = new PrintStream(outputStream, false, StandardCharsets.UTF_8);
            printStream.println(algoTime);
            if (!(tempList.equals(run.get(0))))
                printStream.print("!!!!!!!!!!!!");
        }

    }
}