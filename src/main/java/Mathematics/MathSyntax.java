package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */

public class MathSyntax {
    public MathObject mathObject;
    public MathSyntaxExpression syntax;
    public MathSyntax(int arg){
        this.mathObject = new MathNumber(arg);
    }
    public MathSyntax(double arg){
        this.mathObject = new MathNumber(arg);
    }
    public MathSyntax(MathSyntaxExpression ex){
        this.syntax = ex;
        this.mathObject = ex.getMathObject();
    }
}
