/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinatorics.project;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author User
 * @param <T>
 */
public class IncrementCarryLex<T extends Comparable> extends PermutationGenerator<T> {
    private final List<T> list;
    private final int[] midNum;
    
    public IncrementCarryLex(List<T> list) {
        this.list = list;
        reset();
        
        midNum = new int[list.size()-1];
        for (int m : midNum)
            m = 0;
        
        for (int i = 0; i < 200; i++) {
        incMidNum();
        for (int m : midNum)
            System.out.print(m + " ");
        System.out.println();
        }
    }
    
    
    private void incMidNum() {
        int i;
        for (i = midNum.length-1; i >= 0; i--) {
            int carry = midNum.length - i;
            if (++midNum[i] > carry)
                midNum[i] = 0;
            else
                break;
        }
        //if (i == -1)
            
    }
    
    /*
    private int[] getMidNum(List<T> list) {
        int[] tmp = new int[list.size()-1];
        
        for (int i = 0; i < list.size()-1; i++) {
            int count = 0;
            for (int j = i+1; j < list.size(); j++) {
                if (list.get(i).compareTo(list.get(j)) > 0) {
                    count++;
                }
            }
            tmp[i] = count;
            System.out.println(count);
        }
        
        return null;
    }
    */
    
    @Override
    public boolean goNext() {
        return false;
    
    }

    @Override
    public void reset() {
        Collections.sort(list);
    }
    
}
