package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */
public class Addition extends MathObject{
    public MathObject[] terms;
    public Addition(MathObject[] terms){
        this.terms = terms;
    }
    public Addition(){
        this.arguments = 2;
    }

}
