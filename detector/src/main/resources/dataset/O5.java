public class O5 {
    public static boolean isPalindrome(String str){
        String rev = new StringBuilder(str).reverse().toString();
        return str.equals(rev);
    }
}