package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jack on 11/3/2016.
 */
public class Simplifier {
    private static EquationBuilder builder = new EquationBuilder();
    private static Cloner cloner = new Cloner();
    public Simplifier(){

    }
    private static Equation constantsOperation(String operation, Equation equation){
        Equation eq = cloner.deepClone(equation);
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
                                eq = simplifyFraction(equation);
                            }
                        } catch (ArithmeticException excep){ //Something that doesn't end in decimal, like 2/3 = .66666666666666666666
                            //Keep it as fraction and simplify it.
                            eq = simplifyFraction(equation);
                        }
                        break;
                    default:
                }
            }
        }
        return eq;
    }
    public static Equation simplifyFraction(Equation equation){
        if(!equation.isType(EquationType.FRACTION)){
            throw new UncheckedIOException(new IOException("Input is not a fraction!"));
        }
        Equation numerator = new Equation(equation.equationTerms.getChild(0));
        Equation demoninator = new Equation(equation.equationTerms.getChild(1));
        Equation gcd;
        try{
            gcd = GCD(numerator, demoninator);
        } catch (UncheckedIOException e){ //So we have a fraction of not integer constants.

        }
        if(((MathNumber) gcd.equationTerms.data).number.doubleValue() == 1){
            return equation; //Already simplified.
        }
        if(gcd.isType(EquationType.INTEGERCONSTANT)){ //We have a constant, so just divide the top and bottom by it.
            BigDecimal newNumeratorDec = ((MathNumber) numerator.equationTerms.data).number.divide(((MathNumber) gcd.equationTerms.data).number);
            BigDecimal newDenominatorDec = ((MathNumber) demoninator.equationTerms.data).number.divide(((MathNumber) gcd.equationTerms.data).number);
            Equation newEq = builder.makeEquation(Arrays.asList(new MathSyntax(newNumeratorDec), new MathSyntax(MathSyntaxExpression.DIVIDE), new MathSyntax(newDenominatorDec)));
            return newEq;
        }
        return null;
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
        throw new UncheckedIOException(new IOException("Equation inputs must be integer constants!"));
    }
    public static Equation decimalToFraction(Equation eq){
        if(eq.isType(EquationType.RATIONALCONSTANT)){
            
        }
    }
    public static Equation LCM(Equation eq1, Equation eq2){
        if(eq1.isType(EquationType.INTEGERCONSTANT) && eq2.isType(EquationType.INTEGERCONSTANT)) {
            BigDecimal abs = ((MathNumber) eq1.equationTerms.data).number.multiply(((MathNumber) eq1.equationTerms.data).number).abs();
            BigDecimal gcd = ((MathNumber) GCD(eq1, eq2).equationTerms.data).number;
            return new Equation(new Tree(abs.divide(gcd)));
        }
        throw new UncheckedIOException(new IOException("Equation inputs must be integer constants!"));
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
