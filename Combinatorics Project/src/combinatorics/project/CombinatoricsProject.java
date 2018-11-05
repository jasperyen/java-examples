
package combinatorics.project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class CombinatoricsProject {

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        List<Integer> lexList = new ArrayList<>();
        List<Integer> incList = new ArrayList<>();
        List<Integer> decList = new ArrayList<>();
        
        for (int i = 0; i < 10; ++i) {
            lexList.add(i);
            incList.add(i);
            decList.add(i);
        }
        
        PermutationGenerator<Integer> lex = new Lexicographic<>(lexList);
        PermutationGenerator<Integer> inc = new IncrementCarryLex<>(incList);
        PermutationGenerator<Integer> dec = new DecrementCarryLex<>(decList);
        
        /*
        int count;
        for (count = 0; lex.goNext(); count++);
        System.out.println(count);
        for (count = 0; inc.goNext(); count++);
        System.out.println(count);
        for (count = 0; dec.goNext(); count++);
        System.out.println(count);
        */
        
        System.out.println("Lexicographic benchmark : " + benchMark(lex));
        System.out.println("IncrementCarryLex benchmark : " + benchMark(inc));
        System.out.println("DecrementCarryLex benchmark : " + benchMark(dec));
    }
    
    
    @SuppressWarnings("empty-statement")
    private static double benchMark(PermutationGenerator pg) {
        
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
        
        return ((double)total / sec) / count;
    }
    
}
