
import java.util.Scanner;
public class Countelement {
    
    public static void main (String[]args){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of elements: ");
            int n = scanner.nextInt();
            int[] arr = new int[n];
            System.out.println("Enter the elements:");
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }
            for(int i=0;i<n;i++){
                int count=1;
                // if(arr[i]!=-1){ // Check if the element is not already counted
                    for(int j=i+1;j<n;j++){
                        if(arr[i]==arr[j]){
                            count++;
                            // arr[j]=-1; // Mark as counted
                        }
                    }
                    System.out.println(arr[i]+" occurs "+count+" times");
                }
            }
    }

