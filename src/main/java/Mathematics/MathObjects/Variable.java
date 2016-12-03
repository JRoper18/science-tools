package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/6/2016.
 */
public class Variable extends MathObject {
    public final String name;
    public Variable(String name){
        super(0, false);
        this.name = name;
    }
}
