package Structures.Tuples;

/**
 * Created by jack on 10/16/2016.
 */
public class Triplet<A, B, C> implements TupleInterface {
    public A val1;
    public B val2;
    public C val3;
    public Triplet(A v1, B v2, C v3){
        this.val1 = v1;
        this.val2 = v2;
        this.val3 = v3;
    }
    public int size(){
        return 3;
    }
}
