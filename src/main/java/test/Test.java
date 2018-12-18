package test;

import java.util.Stack;

/**
 * Created by 18435 on 2018/7/31.
 */
public class Test {

    public static void main(String[] args) {
//        System.out.println(reverseWordsTwo("   a   b "));
//        char[] chars = new char[]{'a','a','c'};
//        System.out.println(compress(chars));
//        System.out.println(chars);
        System.out.println(firstUniqChar("aadadaad"));
    }

    public static String reverseWords(String s) {
        Stack<String> reverseWord = new Stack<>();
        StringBuffer sb = new StringBuffer();
        char[] datas = s.toCharArray();
        for(int i=0;i<datas.length;i++){
            if(datas[i]!=' '){
                sb.append(datas[i]);
                if(i==datas.length-1){
                    reverseWord.push(sb.toString());
                }
            }else {
                if(!sb.toString().equals("")){
                    reverseWord.push(sb.toString());
                }
                sb.delete(0,sb.length());
            }
        }

        StringBuilder sbb = new StringBuilder();
        while (!reverseWord.isEmpty()){
            sbb.append(reverseWord.pop()+" ");
        }
        return sbb.toString().trim();
    }

    public static String reverseWordsTwo(String s) {
        StringBuilder sbb = new StringBuilder();
        String[] words = s.split(" ");
        for(int i=words.length - 1 ;i > 0;i--){
            if(!words[i].equals(" ") && words[i].length()>0){
                System.out.println("words[i] : " + words[i]);
                sbb.append(words[i] + " ");
            }
        }
        return sbb.toString().trim();
    }

    public static int compress(char[] chars) {
        char data = chars[0];
        int index = 0;
        int length = -1;
        if(chars.length == 1){
            return 1;
        }else {
            for (int i = 1;i< chars.length;i++){
                if(chars[i]!=data || i == chars.length-1){
                    chars[++length]=data;
                    if(i == chars.length -1 && chars[i]==data){
                        i++;
                    }
                    if(i-index>1){
                        char[] num = String.valueOf((i-index)).toCharArray();
                        for (int j=0;j<num.length;j++){
                            chars[++length] = num[j];
                        }
                    }
                    if(i < chars.length - 1){
                        data = chars[i];
                        index = i;
                    }
                    if(i == chars.length -1 && chars[i]!=data){
                        chars[++length]=chars[i];
                    }
                }
            }
        }
        return length + 1;
    }

    public static int firstUniqChar(String s) {
        char[] datas = s.toCharArray();
        if(s==null || s.equals(" ")){
            return -1;
        }
        if(datas.length == 1){
            return 0;
        }

        for(int i=0;i<datas.length;i++){
            if((s.substring(i+1).indexOf(datas[i])) == -1 && s.substring(0,i).indexOf(datas[i]) == -1){
                return i;
            }
        }
        return -1;
    }

    /**
     * 关键点 注意事项：您可以假定该字符串只包含小写字母。
     * @param s
     * @return
     */
    public static int firstUniqCharTwo(String s) {
        int res = 0;
        for(int i = 'a';i<='c';i++){
            int index = s.indexOf(i);
            if(index!=-1 && index == s.lastIndexOf(i)){
                res = res > i ? i :res;
            }
        }
        return res;
    }

    /**
     * 给定一个仅包含大小写字母和空格 ' ' 的字符串，返回其最后一个单词的长度。
     * 如果不存在最后一个单词，请返回 0 。
     * 说明：一个单词是指由字母组成，但不包含任何空格的字符串。
     * @param s
     * @return
     */
    public static int lengthOfLastWord(String s) {
        if(s == null || s.length()<=0){
            return 0;
        }
        String[] words = s.split(" ");
        for(int i = words.length-1;i>=0;i++){
            if(words[i].length()>0){
                return words[i].length();
            }
        }
        return 0;
    }

    /**
     * 如果一个树的左子树与右子树镜像对称，那么这个树是对称的。
     * 如果同时满足下面的条件，两个树互为镜像：
     * 1.它们的两个根结点具有相同的值。
     * 2.每个树的右子树都与另一个树的左子树镜像对称。
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        return  isMirrorSymmetry(root,root);
    }

    //递归
    public boolean isMirrorSymmetry(TreeNode left,TreeNode right){
        if(left==null && right==null){
            return true;
        }
        if(left==null || right == null){
            return false;
        }
        return left.getVal()==right.getVal() && isMirrorSymmetry(left.left,right.right) && isMirrorSymmetry(left.right,right.left);
    }

    //迭代
//    public boolean isMirrorSymmetryTwo(TreeNode left,TreeNode right){
//
//    }

//    public List<List<Integer>> levelOrder(TreeNode root) {
//        List<Integer> list = new ArrayList<>();
//    }
}
