public class P3 {
    public static int find(int[] data, int target){
        int i = 0;
        while(i < data.length){
            if(data[i] == target) return i;
            i++;
        }
        return -1;
    }
}