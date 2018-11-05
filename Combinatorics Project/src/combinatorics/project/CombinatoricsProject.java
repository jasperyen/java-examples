
package combinatorics.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author User
 */
public class CombinatoricsProject {

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        
        
        
        List<Integer> arrayList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        PermutationGenerator<Integer> pgArray = new IncrementCarryLex<>(arrayList);
        
        /*
        List<Integer> arrayList = new ArrayList<>();
        //List<Integer> linkedList = new LinkedList<>();
        
        for (int i = 0; i < 10; ++i) {
            arrayList.add(i);
            //linkedList.add(i);
        }
        
        PermutationGenerator<Integer> pgArray = new Lexicographic<>(arrayList);
        //PermutationGenerator<Integer> pgLinked = new Lexicographic<>(linkedList);
        
        while(pgArray.goNext())
            System.out.println(arrayList);
        */
        //benchMark(pgArray);
        //benchMark(pgLinked);
        
    }
    
    @SuppressWarnings("empty-statement")
    private static void benchMark(PermutationGenerator pg) {
        
        int count = 0;
        long sec = 1000 * 1000 * 1000;
        long total = 0, n;
        
        while (pg.goNext());
        
        while (total < sec) {
            count++;
            pg.reset();
        
            n = System.nanoTime();
            while(pg.goNext());
            total += System.nanoTime() - n;
        }
        
        double avg = ((double)total / sec) / count;
        System.out.println(avg);
    }
    
}
