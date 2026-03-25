
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class ShortestDistance {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            set.add(arr[i]);
        }
         // Convert set to list
        List<Integer> uniqueList = new ArrayList<>(set);
        // Sort the list
        Collections.sort(uniqueList);

  System.out.println(uniqueList);
   int count1 = Integer.MAX_VALUE;
        for (int i = 1; i < uniqueList.size(); i++) {
            int diff = uniqueList.get(i) - uniqueList.get(i - 1);
            if (diff < count1) {
                count1 = diff;
            }
        }
        System.out.println("Shortest distance between any two elements: " + count1);

        scanner.close();
    }
}
