package Mathematics.MathObjects.PatternMatching;

import Mathematics.Equation;
import Mathematics.EquationType;
import Mathematics.MathObjects.Expression;

/**
 * Created by Ulysses Howard Smith on 10/26/2016.
 */
public class GenericExpression extends Expression {
    public EquationType type;
    public String tag;
    public GenericExpression(){
        super(0, false);
        this.tag = null;
    }
    public GenericExpression(String tag){
        super(0, false);
        this.tag = tag;
        this.type = null;
    }
    public GenericExpression(EquationType type, String tag){
        super(0, false);
        this.tag = tag;
        this.type = type;
    }
    public Equation eval(){
        return null;
    }
}
