/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinatorics.project;

import java.util.List;


/**
 *
 * @author User
 * @param <T>
 * @param <L>
 */
public class Lexicographic<T extends Comparable, L extends List<T>> extends PermutationGenerator<T, L> {
    private final L list;
    
    public Lexicographic(L list) {
        this.list = list;
    }
    
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public L goNext() {
        return list;
    }
}