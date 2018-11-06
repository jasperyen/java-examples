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
public class SteinhausJohnsonTrotter<T extends Comparable> extends PermutationGenerator<T> {
    private final List<T> list;
    private final boolean[] sign;
    
    public SteinhausJohnsonTrotter(List<T> list) {
        this.list = list;
        sign = new boolean[list.size()];
    }
    
    @Override
    public boolean goNext() {
        return switchIt(0, list.size());
    }
    
    private boolean switchIt(int start, int end) {
        if (end - start == 1)
            return false;
        
        int max = start;
        for (int i = start+1; i < end; i++){
            if (list.get(i).compareTo(list.get(max)) > 0) 
                max = i;
        }
        
        if (max == start && sign[max] == false) {
            if (!switchIt(start+1, end))
                return false;
            sign[max] = true;
        }
        else if (max == end-1 && sign[max] == true) {
            if (!switchIt(start, end-1))
                return false;
            sign[max] = false;
        }
        else{
            if (sign[max]){
                Collections.swap(list, max, max+1);
                boolean b = sign[max]; sign[max] = sign[max+1]; sign[max+1] = b;
            }
            else{
                Collections.swap(list, max, max-1);
                boolean b = sign[max]; sign[max] = sign[max-1]; sign[max-1] = b;
            }
        }
        return true;
    }

    @Override
    public void reset() {
        Collections.sort(list);
        for (int i = 0; i < sign.length; i++) 
            sign[i] = false;
    }
}
