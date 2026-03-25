
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class ReverseArray {
    
    public static void main(String[] args){
Scanner sc= new Scanner(System.in);
int n=sc.nextInt();
System.out.println("Enter list");

List <Integer>list =new ArrayList<>();
for(int num:new int[n]){
    num=sc.nextInt();
    list.add(num);
}

        Collections.reverse(list);
        System.out.println(list);
    }
}
