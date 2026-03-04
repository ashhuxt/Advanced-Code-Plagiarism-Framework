public class O7 {
    public static int max(int[] arr){
        int m = arr[0];
        for(int val : arr){
            if(val > m) m = val;
        }
        return m;
    }
}