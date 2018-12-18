package JianZhiOffer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by 18435 on 2018/12/11.
 * 输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否可能为该栈的弹出顺序。
 * 假设压入栈的所有数字均不相等。例如序列1,2,3,4,5是某栈的压入顺序，序列4,5,3,2,1是该压栈序列对应的一个弹出序列，
 * 但4,3,5,1,2就不可能是该压栈序列的弹出序列。（注意：这两个序列的长度是相等的）
 */
public class Solution {

    public static boolean IsPopOrder(int [] pushA,int [] popA) {
        if(pushA.length == 1 && popA.length==1){
            if(pushA[0] == popA[0]){
                return true;
            }else {
                return false;
            }
        }
        Stack<Integer> stack = new Stack<>();//临时缓存栈
        ArrayList<Integer> list = new ArrayList<>();
        for (int push : pushA){
            list.add(push);
        }
        for(int pop : popA){
            if(!stack.isEmpty() && pop == stack.peek()){
                stack.pop();//出栈
                continue;
            }else if (!stack.isEmpty() && stack.contains(pop)){
                return false; //需要出栈的元素 已在栈中但是不在栈顶，所以这个序列不能是弹出顺序
            }else {
                int index = list.indexOf(pop);
                for(int i=0;i<index;i++){
                    if((Integer) list.get(i)!=null){
                        stack.push((Integer) list.get(i));
                        list.set(i, null);
                    }
                }
                list.set(index,null);
            }
        }
        if(stack.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 二叉排序树的性质：左子树上所有节点的值均小于它的根节点；右子树上所有节点的值均大于它的根节点。
     * 二叉排序树后序遍历的性质：序列最后一个数字是根节点，序列剩余部分分成两部分，前一部分是左子树，后一部分是右子树。
     * @param sequence
     * @return
     */
    public static boolean VerifySquenceOfBST(int [] sequence) {
        return bst(sequence,0,sequence.length-1);
    }

    public static boolean bst(int [] sequence,int start,int end){
        int root = sequence[end];
        for(int i=0;i<end;i++){
            if(sequence[i] < root){
                start = i;
            }else {
                break;
            }
        }

//        for(int i=start+1;i<end;i++){
//            if(sequence[i] ){}
//        }
        return false;

    }

    public static void main(String[] args) {
        int[] a = new int[]{1,2,3,4,5};
        int[] b = new int[]{4,3,5,1,2};
        System.out.println(IsPopOrder(a,b));
    }



}
