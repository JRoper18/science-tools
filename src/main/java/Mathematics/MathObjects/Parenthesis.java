package Mathematics.MathObjects;

/**
 * Created by jack on 10/14/2016.
 */
public class Parenthesis extends MathObject {
    public final boolean open;
    public Parenthesis(boolean open){
        super(0, false);
        this.open = open;
        this.arguments = -1;
    }
}
