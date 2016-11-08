package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jack on 11/3/2016.
 */
public class Simplifier {
    public Simplifier(){

    }
    private static Equation constantsOperation(String operation, Equation equation){
        Cloner cloner = new Cloner();
        Equation eq = cloner.deepClone(equation);
        EquationBuilder builder = new EquationBuilder();
        String patternIn = "CONSTANT " + operation + " CONSTANT";

        PatternEquation pattern = builder.makePatternEquation(patternIn);
        List<LinkedList<Integer>> paths = eq.patternMatch(pattern);
        for(LinkedList<Integer> path : paths){
            Tree<MathObject> temp = eq.equationTerms.getChildThroughPath(path);

            //We know that the first and second children are the only ones, and they are both MathNumbers.
            Tree eq1 = temp.getChild(0);
            Tree eq2 = temp.getChild(1);
            if(temp.getChild(0).data instanceof MathNumber && temp.getChild(1).data instanceof MathNumber){
                switch(operation){
                    case "+":
                        temp.replaceThis(new Tree(new MathNumber(((MathNumber) temp.getChild(0).data).number.add(((MathNumber) temp.getChild(1).data).number))));
                        break;
                    case "-":
                        temp.replaceThis(new Tree(new MathNumber(((MathNumber) temp.getChild(0).data).number.subtract(((MathNumber) temp.getChild(1).data).number))));
                        break;
                    case "*":
                        temp.replaceThis(new Tree(new MathNumber(((MathNumber) temp.getChild(0).data).number.multiply(((MathNumber) temp.getChild(1).data).number))));
                        break;
                    case "/":
                        BigDecimal newNum;
                        boolean isGood;
                        try{
                            newNum = ((MathNumber) eq1.data).number.divide(((MathNumber) eq2.data).number);
                            if(newNum.abs().compareTo(new BigDecimal("1")) == -1){ //Our number is between -1 and 1. We have a fraction - keep it that way.
                                //Try simplifying fraction here
                            }
                        } catch (ArithmeticException excep){ //Something that doesn't end in decimal, like 2/3 = .66666666666666666666
                            //Keep it as fraction and simplify it.
                            Equation gcd = GCD(eq1, eq2);
                        }
                        break;
                    default:
                }
            }
        }
        return eq;
    }
    public static Equation GCD(Equation eq1, Equation eq2){
        if(eq1.isType(EquationType.INTEGERCONSTANT) && eq2.isType(EquationType.INTEGERCONSTANT)){
            if(eq2.equationTerms.data.equals(new MathNumber(0))){
                return eq1;
            }
            else{
                return GCD(eq2, new Equation(new Tree(((MathNumber) eq1.equationTerms.data).number.remainder(((MathNumber) eq2.equationTerms.data).number))));
            }
        }
        return null; //CHANGE THIS
    }
    public static Equation LCM(Equation eq1, Equation eq2){
        if(eq1.isType(EquationType.INTEGERCONSTANT) && eq2.isType(EquationType.INTEGERCONSTANT)) {
            BigDecimal abs = ((MathNumber) eq1.equationTerms.data).number.multiply(((MathNumber) eq1.equationTerms.data).number).abs();
            BigDecimal gcd = ((MathNumber) GCD(eq1, eq2).equationTerms.data).number;
            return new Equation(new Tree(abs.divide(gcd)));
        }
        else{
            return null; //CHANGE THIS
        }
    }
    public static Equation constantsAddition(Equation equation){
        return constantsOperation("+", equation);
    }
    public static Equation constantsSubtraction(Equation equation){
        return constantsOperation("-", equation);
    }
    public static Equation constantsMultiplication(Equation equation){
        return constantsOperation("*", equation);
    }
    public static Equation constantsDivision(Equation equation){
        return constantsOperation("/", equation);
    }
}
