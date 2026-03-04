public class U3 {
    public static int binarySearch(int[] arr, int key){
        int l = 0, r = arr.length - 1;
        while(l <= r){
            int m = (l + r)/2;
            if(arr[m] == key) return m;
            if(arr[m] < key) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }
}