package Structures.Tuples;

/**
 * Created by jack on 10/16/2016.
 */
public class Pair<A, B> implements TupleInterface{
    public A val1;
    public B val2;
    public Pair(A x, B y){
        this.val1 = x;
        this.val2 = y;
    }
    public int size(){
        return 2;
    }
}
