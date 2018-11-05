/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinatorics.project;

/**
 *
 * @author User
 * @param <T>
 */
public abstract class PermutationGenerator<T extends Comparable> {
    
    //public abstract boolean hasNext();
    public abstract boolean goNext();
    public abstract void reset();
    
}
