import java.util.*;



public class SubArray {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Take dynamic input
        System.out.print("Enter size of array: ");
        int n = sc.nextInt();
        int arr[] = new int[n];

        System.out.println("Enter elements of array:");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        // Kadane’s Algorithm
        int maxSoFar = arr[0];
        int currMax = arr[0];

        for (int i = 1; i < n; i++) {
            currMax = Math.max(arr[i], currMax + arr[i]);
            maxSoFar = Math.max(maxSoFar, currMax);
        }

        System.out.println("Maximum Subarray Sum = " + maxSoFar);
    }
}
