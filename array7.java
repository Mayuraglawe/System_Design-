import java.util.Scanner;

public class array7 {
    /**
     * @param args
     */
    public static void main(String[] args) {
        
     Scanner f=new Scanner(System.in);
        int size =f.nextInt(); 
        int arr[]=new int [size];

        for (int i =0;i<=arr.length-1;i++){
            System.out.println("Enter the arrray element ");
            arr[i]= f.nextInt();
        }

        for(int i=0;i<=arr.length-1;i++){
            System.out.println("array have  an element :"+i+"="+arr[i]);
        }
        
        
    }
    }
