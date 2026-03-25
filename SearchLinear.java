//Search for a given element linearly in an array.

import java.util.*;
public class SearchLinear {
    public static void main(String[] args){
        System.out.println("enter no.of element in array ");
        Scanner scanner= new Scanner(System.in);
        int n=scanner.nextInt();
        int arr[]=new int[n];

        for (int i = 0; i < n; i++) {
            arr[i]= scanner.nextInt();
        }


        System.out.println("Enter the  key :");
        int key=scanner.nextInt();

        boolean found=false;
        for(int j=0;j<arr.length;j++){
            if(arr[j]==key){
                System.out.println("key found at index:"+j++);
                found = true;
                break;
            }
            
        }
        if (!found) {
                System.out.println("Key is not present in the array");
            }

    }
}
