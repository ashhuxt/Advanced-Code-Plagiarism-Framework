public class P10 {
    public static int vowels(String input){
        int c = 0;
        for(int i = 0; i < input.length(); i++){
            char ch = input.charAt(i);
            if("aeiouAEIOU".contains(String.valueOf(ch)))
                c++;
        }
        return c;
    }
}