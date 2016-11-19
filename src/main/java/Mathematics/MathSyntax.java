package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathNumberInteger;
import Mathematics.MathObjects.MathObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */

public class MathSyntax {
    public MathObject mathObject;
    public MathSyntaxExpression syntax;
    public MathSyntax(int arg){
        this.mathObject = new MathNumberInteger(arg);
    }
    public MathSyntax(double arg){
        this.mathObject = new MathNumber(arg);
    }
    public MathSyntax(BigDecimal arg){
        this.mathObject = new MathNumber(arg);
    }
    public MathSyntax(MathSyntaxExpression ex, List<String> args){
        this.syntax = ex;
        if(args == null){
            this.mathObject = ex.getMathObject(new ArrayList<>());
        }
        else{
            this.mathObject = ex.getMathObject(args);
        }
    }
    public MathSyntax(MathObject obj){
        this.syntax = null;
        this.mathObject = obj;
    }
    public MathSyntax(MathSyntaxExpression ex){
        this.syntax = ex;
    }
}
