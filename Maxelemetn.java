import java.util.*;


public class Maxelemetn{


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of elements in the list:");
         // Read the number of elements
        int n=scanner.nextInt();
        
        List<Integer> set = new ArrayList<>();
        for(int num:new int[n]){
            num=scanner.nextInt();
            set.add(num);
        }
        // Convert set to list
        int m= Collections.max(set);

        System.out.println("Maximum element in the list: " + m);

           int k= Collections.min(set);

        System.out.println("Maximum element in the list: " + k);

    }
}
