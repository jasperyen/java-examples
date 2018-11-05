/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinatorics.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author User
 * @param <T>
 */
public class DecrementCarryLex<T extends Comparable> extends PermutationGenerator<T> {
    private final List<T> list, sortList;
    private final char[] midNum;
    
    public DecrementCarryLex(List<T> list) {
        this.list = list;
        Collections.sort(list);
        
        sortList = new ArrayList<>();
        sortList.addAll(list);
        
        midNum = new char[list.size()-1];
    }
    
    
    private boolean incMidNum() {
        int i;
        for (i = midNum.length-1; i >= 0; i--) {
            char carry = (char)(i + 1);
            if (++midNum[i] > carry)
                midNum[i] = 0;
            else
                break;
        }
        
        if (i != -1)
            return true;
        
        for (i = midNum.length-1; i >= 0; i--) {
            char carry = (char)(i + 1);
            midNum[i] = carry;
        }
        
        return false;
    }
    
    @Override
    public boolean goNext() {
        if (!incMidNum())
            return false;
        
        T min = sortList.get(0);
        for (int i = 0; i < list.size(); i++)
            list.set(i, min);
        
        for (int i = 0; i < midNum.length; i++) {
            int n = midNum[midNum.length-1 - i], index, count;
            T t = sortList.get(list.size()-1 - i);
            
            for (index = list.size()-1, count = 0;;) {
               T tmp = list.get(index);
               
               if (tmp.equals(min) && count == n)
                   break;
               else if (tmp.compareTo(t) < 0)
                   count++;
               index--;
            }
            list.set(index, t);
        }
        return true;
    }

    @Override
    public void reset() {
        for (int i = 0; i < midNum.length; i++)
            midNum[i] = 0;
    }
}
