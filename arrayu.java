

import java.util.ArrayList;


public class arrayu {
    
    public static void main(String[] args) {
        // Create a dynamic array (ArrayList)
        ArrayList<Integer> numbers = new ArrayList<>();

        // Adding elements
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        
        System.out.println("ArrayList Elements: " + numbers); 

        // Inserting an element at a specific index
        numbers.add(2, 25);  // Adds 25 at index 2
        System.out.println("After insertion: " + numbers);

        // Accessing elements
        System.out.println("Element at index 1: " + numbers.get(1));

        // Removing an element
        numbers.remove(3);  // Removes the element at index 3
        System.out.println("After deletion: " + numbers);

        // Checking if an element exists
        if (numbers.contains(20)) {
            System.out.println("20 is present in the list.");
        }

        // Iterating over the list
        System.out.print("Elements using loop: ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }

        // Getting size of ArrayList
        System.out.println("\nSize of ArrayList: " + numbers.size());

        // Clearing the ArrayList
        numbers.clear();
        System.out.println("After clearing, size: " + numbers.size());
    }
}
