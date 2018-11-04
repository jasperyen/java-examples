
package combinatorics.project;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author User
 */
public class CombinatoricsProject {

    public static void main(String[] args) {
        
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        
        PermutationGenerator<Integer, ArrayList<Integer>> pg = new Lexicographic<>(list);
        
        if (pg.hasNext())
            System.out.println(pg.goNext());
        
    }
    
}
