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
public class Lexicographic<T extends Comparable> extends PermutationGenerator<T> {
    private final List<T> list;
    
    public Lexicographic(List<T> list) {
        this.list = list;
    }
    
    @Override
    public boolean goNext() {
        
        int i, index = -1;
        for (i = list.size()-2; i >= 0; --i) {
            //  [i] < [i+1]
            if (list.get(i).compareTo(list.get(i+1)) < 0){
                index = i;
                break;
            }
        }
        
        if (index == -1)
            return false;
        
        for (i = list.size()-1; i >= 0; i--)
            //  [i] > [index]
            if (list.get(i).compareTo(list.get(index)) > 0)
                break;
        
        Collections.swap(list, i, index);
        Collections.reverse(list.subList(index + 1, list.size()));
        
        return true;
    }

    @Override
    public void reset() {
        Collections.sort(list);
    }
}