package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/6/2016.
 */
public class Variable extends MathObject {
    public final String symbol;
    public final String type;
    public Variable(String symbol, String type){
        super(0, false);
        this.symbol = symbol;
        this.type = type;
        this.arguments = 0; //A variable isn't an expression, and doesn't take arguments. You can't have the X(2), or 2X3. you have 2 * x * 3
    }
}
