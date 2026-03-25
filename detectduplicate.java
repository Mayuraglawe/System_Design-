
import java.util.*;


class detectduplicate{
public static void main(String[] args){
    Scanner sc= new Scanner(System.in);
int n=sc.nextInt();
int arr[]= new int[n];

for (int idx = 0; idx < arr.length; idx++) {
    arr[idx]=sc.nextInt();
        }

        Set<Integer>seen=new HashSet<>();
       Set<Integer>duplicate=new LinkedHashSet<>();
        
       for(int num:arr){
        if(!seen.add(num)){
            duplicate.add(num);
        }
       }
       List <Integer> result= new ArrayList<>(duplicate);
       System.out.println(result);

}
}

