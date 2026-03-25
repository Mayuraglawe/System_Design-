
import java.util.Scanner;

public class array6 {
        public static void main(String[] args) {
       
     Scanner d=new Scanner(System.in);
     int size=d.nextInt();
        int arr[]=new int [size];

     for(int i=0;i<size;i++) {
         System.out.println("ente the value");
     arr[i]=d.nextInt();
     }  
            
            System.out.println("Reversed array:");
            for (int i = arr.length - 1; i >= 0; i--) {
                System.out.print(arr[i] + " ");
            }
        }
    }
    