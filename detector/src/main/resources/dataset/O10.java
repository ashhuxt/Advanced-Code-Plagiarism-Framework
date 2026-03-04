public class O10 {
    public static int countVowels(String s){
        int count = 0;
        for(char c : s.toCharArray()){
            if("aeiouAEIOU".indexOf(c) != -1)
                count++;
        }
        return count;
    }
}