package Mathematics;

import Mathematics.MathObjects.*;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

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
                        try{
                            newNum = ((MathNumber) eq1.data).number.divide(((MathNumber) eq2.data).number);
                            if(newNum.abs().compareTo(new BigDecimal("1")) == -1){ //Our number is between -1 and 1. We have a fraction - keep it that way.
                                //Try simplifying fraction here
                                eq = simplifyIntegerFraction(equation);
                            }
                            else{ //It simplifies to a normal number.
                                return new Equation(new Tree(new MathNumber(newNum)));
                            }
                        } catch (ArithmeticException excep){ //Something that doesn't end in decimal, like 2/3 = .66666666666666666666
                            //Keep it as fraction and simplify it.
                            eq = simplifyIntegerFraction(equation);
                        }
                        break;
                    default:
                }
            }
        }
        return eq;
    }
    /**
     * Simplifies a fraction with integer denominator and numerator.
     * @param equation The fraction
     * @return A simplified fraction.
     */
    public static Equation simplifyIntegerFraction(Equation equation){
        if(!equation.isType(EquationType.INTEGERFRACTION)){
            throw new BadEquationTypeException(EquationType.INTEGERFRACTION, equation);
        }
        Equation numerator = new Equation(equation.equationTerms.getChild(0));
        Equation demoninator = new Equation(equation.equationTerms.getChild(1));
        Equation gcd = GCDIntegers(numerator, demoninator);
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
    /**
     * Finds the greatest common divisor between two integers.
     * @param eq1 The first integer.
     * @param eq2 The second integer.
     * @return The Greatest Common Denominator.
     */
    public static Equation GCDIntegers(Equation eq1, Equation eq2){
        if(eq1.isType(EquationType.INTEGERCONSTANT) && eq2.isType(EquationType.INTEGERCONSTANT)){
            if(((MathNumber)eq2.equationTerms.data).number.compareTo(new BigDecimal(0)) == 0){
                return eq1;
            }
            else{
                Equation remainder = new Equation(new Tree(new MathNumber(((MathNumber) eq1.equationTerms.data).number.remainder(((MathNumber) eq2.equationTerms.data).number))));
                return GCDIntegers(eq2, remainder);
            }
        }
        throw throwBadTypeException(EquationType.INTEGERCONSTANT, eq1, eq2);
    }
    /**
     * Finds the least common multiple between two integers.
     * @param eq1 The first integer.
     * @param eq2 The second integer.
     * @return
     */
    public static Equation LCMIntegers(Equation eq1, Equation eq2){
        if(eq1.isType(EquationType.INTEGERCONSTANT) && eq2.isType(EquationType.INTEGERCONSTANT)) {
            BigDecimal abs = ((MathNumber) eq1.equationTerms.data).number.multiply(((MathNumber) eq1.equationTerms.data).number).abs();
            BigDecimal gcd = ((MathNumber) GCDIntegers(eq1, eq2).equationTerms.data).number;
            return new Equation(new Tree(abs.divide(gcd)));
        }
        throw throwBadTypeException(EquationType.INTEGERCONSTANT, eq1, eq2);
    }
    /**
     * Turns decimal numbers into fractions in the provided equation.
     * @param equation The equation of mathnumbers to convert.
     * @return The equation with converted numbers.
     */
    public static Equation decimalsToFractions(Equation equation){
        Equation newEquation = cloner.deepClone(equation);
        List<LinkedList<Integer>> paths = newEquation.patternMatch(builder.makePatternEquation("EXPRESSION{DECIMALCONSTANT}"));
        for(LinkedList<Integer> path : paths){
            Tree currentTree =  newEquation.equationTerms.getChildThroughPath(path);
            MathNumber currentNum = ((MathNumber) currentTree.data);
            int scale = currentNum.number.stripTrailingZeros().scale();
            BigDecimal newNumDec = currentNum.number.movePointRight(scale);
            Tree fraction = new Tree();
            fraction.data = new Division();
            fraction.addChild(new MathNumber(newNumDec.toString()));
            fraction.addChild(new MathNumber(Math.pow(10, scale)));
            currentTree.replaceThis(fraction);
        }
        return newEquation;
    }

    /**
     * Removes nested fractions. Some examples are x / (y / z), or (x / y) / z.
     * @param equation The equation you wanted nested fraction removed from.
     * @return An equivilant equation with no nested fractions.
     */
    public static Equation removeNestedFractions(Equation equation){
        Equation newEquation = cloner.deepClone(equation);
        //First remove fractions in the denominator.
        // (x / (y / z) = x * (z / y)
        List<LinkedList<Integer>> paths = newEquation.patternMatch(builder.makePatternEquation("EXPRESSION / EXPRESSION{FRACTION}"));
        for(LinkedList<Integer> path : paths){
            Tree<MathObject> currentLocation = newEquation.equationTerms.getChildThroughPath(path);
            Tree<MathObject> numerator = currentLocation.getChild(0);
            Tree<MathObject> denomFraction = currentLocation.getChild(1);
            Tree<MathObject> newTree = new Tree();
            newTree.data = new Multiplication();
            newTree.addChild(numerator);
            newTree.addChild(denomFraction);
            currentLocation.replaceThis(newTree);
        }
        //Removed fractions in the denominator. Now check for fractions in numerator.
        // (x / y) / z = (x / (y * z))
        paths = newEquation.patternMatch(builder.makePatternEquation("EXPRESSION{FRACTION} / EXPRESSION"));
        for(LinkedList<Integer> path : paths) {
            Tree<MathObject> currentLocation = newEquation.equationTerms.getChildThroughPath(path);
            Tree<MathObject> denominator = currentLocation.getChild(1);
            Tree<MathObject> numerator = currentLocation.getChild(0);
            Tree<MathObject> newTree = new Tree();
            newTree.data = new Division();
            newTree.addChild(numerator.getChild(0));
            Tree<MathObject> bottom = newTree.addChild(new Multiplication());
            bottom.addChild(denominator);
            bottom.addChild(numerator.getChild(1));
            currentLocation.replaceThis(newTree);
        }
        return newEquation;
    }
    public static Equation simplifyFraction(Equation equation){
        if(equation.isType(EquationType.INTEGERFRACTION)){
            return simplifyIntegerFraction(equation);
        }
        else if(equation.isType(EquationType.RATIONALFRACTION)){

        }
        return null;
    }
    private static BadEquationTypeException throwBadTypeException(EquationType type, Equation eq1, Equation eq2){ //TO ME IN THE FUTURE: In case you forget, this just checks both inputs of an equation
        //And throws an error for whichever one is the bad type. It's really not too complicated.
        if(!eq1.isType(type)){
            return new BadEquationTypeException(type, eq1);
        }
        return new BadEquationTypeException(type, eq2);
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
