import java.util.*;

public class SumofAllelement {

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);

        System.out.println("Enter the no.of element want to add in the array:");
        int n=sc.nextInt();
        int sum=0;
         int arr[]= new int[n];

        List<Integer>list=new ArrayList<>();
        for(int num:new int[n]){
            num=sc.nextInt();
            list.add(num);
            sum+=num;
        }
  
        System.out.println("Sum of all element in array is :"+sum);

    }
}
