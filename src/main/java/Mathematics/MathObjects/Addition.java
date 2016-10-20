package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */
public class Addition extends MathObject {
    public MathObject[] terms;
    public Addition(MathObject[] terms){
        super(2, false);
        this.terms = terms;
    }
    public Addition(){
        super(2, false);
    }

}
