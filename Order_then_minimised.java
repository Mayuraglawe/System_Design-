import java.util.*;


public class Order_then_minimised {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];

        List<Integer>list=new ArrayList<>();
        for(int i=0;i<n;i++){
             list.add(sc.nextInt());
        }
        // Collections.sort(list);
         list.sort(Collections.reverseOrder());
        // List <Integer>result=new ArrayList<>(list);
        System.out.println(list);

        sc.close();

        }
    }
