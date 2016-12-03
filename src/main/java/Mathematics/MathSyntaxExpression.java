package Mathematics;

import Mathematics.MathObjects.*;
import Mathematics.MathObjects.PatternMatching.GenericConstant;
import Mathematics.MathObjects.PatternMatching.GenericExpression;

import java.io.IOException;
import java.io.UncheckedIOException;
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
    MINUS,
    DIVIDE,
    PLUS,
    VARIABLE;
    public MathObject getMathObject(){
        return this.getMathObject(new ArrayList<String>());
    }
    public MathObject getMathObject(List<String> args) {
        switch (this) {
            case CLOSE_PAREN:
                return new Parenthesis(false);
            case OPEN_PAREN:
                return new Parenthesis(true);
            case NUMBER:
                if(args.size() == 0){
                    return new GenericConstant();
                }
                return new GenericConstant(args.get(0));
            case EXPRESSION:
                if(args.size() == 0){
                    return new GenericExpression();
                }
                if(args.size() == 2){ //Two args, find out which one is EquationType
                    String tag;
                    EquationType type;
                    try{
                        type = EquationType.valueOf(args.get(0));
                        tag = args.get(1);
                    } catch(Exception e){
                        try{
                            type = EquationType.valueOf(args.get(1));
                            tag = args.get(0);
                        } catch(Exception ee){
                            ee.printStackTrace(); //IDK
                            return null;
                        }
                    }
                    return new GenericExpression(type, tag);
                }
                //One arg, is it a tag or a type?
                boolean isType = true;
                try{
                     EquationType type = EquationType.valueOf(args.get(0));
                } catch(Exception e) {
                    //Tag, not type
                    isType = false;
                }
                if(isType) {
                    return new GenericExpression(EquationType.valueOf(args.get(0)));
                }
                return new GenericExpression(EquationType.valueOf(args.get(0)));
            case PLUS:
                return new Addition();
            case MULTIPLY:
                return new Multiplication();
            case MINUS:
                return new Subtraction();
            case DIVIDE:
                return new Division();
            case VARIABLE:
                if(args.isEmpty()){
                    throw new UncheckedIOException(new IOException("You need to give your variable a name!"));
                }
                return new Variable(args.get(0));
            default:
                return new Addition();
        }
    }
}
