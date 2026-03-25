import java.util.*;

public class SortingthenOdd {
    public static void main(String[] args) {
        int[] numbers = {7, 3, 9, 3, 1, 7, 5};
        
        // Remove duplicates using HashSet
        LinkedHashSet<Integer> hashSet = new LinkedHashSet<>();
        for (int num : numbers) {
            hashSet.add(num);
        }
        
        // Convert to List and sort
        // List<Integer> sortedList = new ArrayList<>(hashSet);
        // Collections.sort(sortedList);
        
        TreeSet<Integer> tree= new TreeSet(hashSet);
        System.out.println("Sorted numbers: " + tree);
        // Output: [1, 3, 5, 7, 9]
    }
}

