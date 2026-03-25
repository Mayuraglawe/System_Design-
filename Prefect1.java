package Array;

import java.util.*;

public class Prefect1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the no. of elements in the array: ");
        int n = scanner.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        System.out.println("Enter the key: ");
        int key = scanner.nextInt();

        List<Integer> list = new ArrayList<>(); // Declare list outside loop

        for (int i = 0; i < arr.length; i++) {
            if (key > arr[i]) {
                int k = arr[i] % 10;
                int m = (arr[i]/10) % 10; // maybe compare last digit with key's last digit?
                if ((k - m) == 1) {
                    list.add(arr[i]);
                }
            }
        }

        System.out.println("Matching numbers: " + list);
    }
}
