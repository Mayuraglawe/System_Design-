
import java.util.*;

public class Count_oddeven {
    public static void main(String[] args){


        System.out.println("Enter the number of elements in the list:");
        Scanner sc= new Scanner(System.in);

        int n=  sc.nextInt();
        int arr[]= new int[n];

        for(int i=0;i<n;i++){
            arr[i]=sc.nextInt();
        }
        int evens[]=new int[n];
        int odds[]=new int[n];
        int evenCount=0;
        int oddCount=0;

        for(int i=0;i<arr.length;i++){
                if(arr[i] %2==0){  
                      evens[evenCount++] = arr[i];
                }
                else{
                  odds[oddCount++] = arr[i];
                }
        }

        System.out.println("Array of the odd one:");
                     for(int j=0;j<oddCount;j++){
                       System.out.println(odds[j]);
                    }
                    System.out.println("Array of the even one:");
                    for(int j=0;j<evenCount;j++){
                       System.out.println(evens[j]);
                    }
    }
}
