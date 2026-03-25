import java.util.Scanner;

public class MaxSubarray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input array size
        System.out.print("Enter the number of elements: ");
        int n = sc.nextInt();

        int[] nums = new int[n];
        System.out.println("Enter " + n + " elements:");
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }

        // Solve using Kadane's Algorithm
        int maxSum = maxSubArray(nums);

        System.out.println("\nThe Maximum Subarray Sum is: " + maxSum);
        sc.close();
    }

    /**
     * Kadane's Algorithm: O(n) Time Complexity
     */
    public static int maxSubArray(int[] nums) {
        // Initialize with the first element
        int currentSum = nums[0];
        int maxSum = nums[0];

        // Iterate from the second element
        for (int i = 1; i < nums.length; i++) {
            // Either start a new subarray at the current index, 
            // or extend the existing subarray
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            
            // Keep track of the best sum we've seen so far
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }
}
