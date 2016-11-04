package Mathematics.MathObjects.PatternMatching;

import Mathematics.Equation;
import Mathematics.MathObjects.Expression;

/**
 * Created by Ulysses Howard Smith on 10/26/2016.
 */
public class GenericExpression extends Expression {
    public String tag;
    public GenericExpression(){
        super(0, false);
        this.tag = "none";
    }
    public GenericExpression(String tag){
        super(0, false);
        this.tag = tag;
    }
    public Equation eval(){
        return null;
    }
}
