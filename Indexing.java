import java.util.*;
public class Indexing {

    public static void main(String [] args){

 Scanner scanner =new Scanner(System.in);
 System.out.print("Hello");
 int n=scanner.nextInt();

 int arr[]= new int[n];
 Set <Integer>seen=new HashSet<>();
 Set<Integer>list =new LinkedHashSet<>();
 for (int num : new int [n]) {
     num=scanner.nextInt();
      if(!seen.add(num))
     list.add(num);
 }
       List <Integer> result= new ArrayList<>(list);
       System.out.println(result);

    }

}
