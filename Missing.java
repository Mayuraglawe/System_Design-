import java.util.*;

// public class Missing {
//     public static void main(String[] args) {
//       System.out.println("Hello, World!");

//              Scanner scanner=new Scanner(System.in);
//              int n=scanner.nextInt();
//              int arr[]=new int[n-1];
             
//              int total=0;
//              int sum=0;
//              for(int j=0;j<n-1;j++){
//                arr[j]=scanner.nextInt();
//                  sum +=arr[j];
//              }
//              total=n*(n+1)/2;
//              System.out.println("Missing "+ "total-sum"+(total-sum));
//          }

// }

      
      
      
      
      
      
      
      
      
      
      
      class Missing {
    public int getSecondLargest(int[] arr) {
        // code here
        
        int large=Integer.MIN_VALUE;
        int secondlarge=Integer.MIN_VALUE;
        for(int j=0;j<arr.length;j++){
        if(arr[j]>large){
             secondlarge=large;
             large=arr[j];
        }
        
        else if(arr[j]>secondlarge && arr[j]<large){
            secondlarge =arr[j];
        }
        else if( secondlarge==Integer.MIN_VALUE){
            return -1;
        }
        
        }
        System.out.println("the second Large "+secondlarge);
        return secondlarge;
    }
    

    
    public static void main (String[] args){
       Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]= new int[n];

        for (int i = 0; i < n; i++) {
            arr[i]=sc.nextInt();
        }


        Missing obj= new Missing();
     int ans =obj.getSecondLargest(arr);
        if(ans==-1){
            System.out.println("No second largest element");
        } else {
            System.out.println("The second largest element is: " + ans);
        }
    }
}
      
      
      
      
      
      
  