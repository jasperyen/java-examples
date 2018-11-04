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
public abstract class PermutationGenerator<T extends Comparable, L extends List<T>> {
    
    public abstract boolean hasNext();
    public abstract L goNext();
    
}
