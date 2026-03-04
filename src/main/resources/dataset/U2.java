public class U2 {
    public static int power(int base, int exp){
        int result = 1;
        for(int i = 0; i < exp; i++)
            result *= base;
        return result;
    }
}