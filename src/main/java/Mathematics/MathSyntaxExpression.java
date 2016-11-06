package Mathematics;

import Mathematics.MathObjects.Addition;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.Multiplication;
import Mathematics.MathObjects.Parenthesis;
import Mathematics.MathObjects.PatternMatching.GenericConstant;
import Mathematics.MathObjects.PatternMatching.GenericExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 10/12/2016.
 */
public enum MathSyntaxExpression {
    CLOSE_PAREN,
    OPEN_PAREN,
    NUMBER,
    EXPRESSION,
    MULTIPLY,
    PLUS;
    public MathObject getMathObject(){
        return this.getMathObject(new ArrayList<Object>());
    }
    public MathObject getMathObject(List<Object> args) {
        switch (this) {
            case CLOSE_PAREN:
                return new Parenthesis(false);
            case OPEN_PAREN:
                return new Parenthesis(true);
            case NUMBER:
                if(args.size() == 0){
                    return new GenericConstant();
                }
                return new GenericConstant((String) args.get(0));
            case EXPRESSION:
                if(args.size() == 0){
                    return new GenericExpression();
                }
                return new GenericExpression((String) args.get(0));
            case PLUS:
                return new Addition();
            case MULTIPLY:
                return new Multiplication();
            default:
                return new Addition();
        }
    }
}
