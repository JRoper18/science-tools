package Mathematics;

import Mathematics.MathObjects.*;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;

import java.math.BigDecimal;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */

public class EquationCommandDatabase {
    public static final EquationCommandSimplifyConstants constantsAddition = new EquationCommandSimplifyConstants("+");
    public static final EquationCommandSimplifyConstants constantsSubtraction = new EquationCommandSimplifyConstants("-");
    public static final EquationCommandSimplifyConstants constantsMultiplication = new EquationCommandSimplifyConstants("*");
    public static final EquationCommandSimplifyConstants constantsDivision = new EquationCommandSimplifyConstants("/");
    public static final EquationCommandRemoveNestedFractionDenominator removeDenominatorFraction = new EquationCommandRemoveNestedFractionDenominator();
    public static final EquationCommandRemoveNestedFractionNumerator removeNumeratorFraction = new EquationCommandRemoveNestedFractionNumerator();
    public static final EquationCommandRemoveNestedFractions removeNestedFractions = new EquationCommandRemoveNestedFractions();
    public static final EquationCommandCondenceConstants condenceConstants = new EquationCommandCondenceConstants();
    public EquationCommandDatabase(){
    }
    public static class EquationCommandSimplifyConstants extends EquationCommand{
        public final String operation;
        public EquationCommandSimplifyConstants(String operation){
            this.operation = operation;
        }
        public Equation run(Equation equation){
            String patternIn = "CONSTANT " + operation + " CONSTANT";
            PatternEquation pattern = builder.makePatternEquation(patternIn);
            if(!equation.isPattern(pattern)){
                throw makeBadTypeException(pattern, equation);
            }
            Tree<MathObject> temp = equation.equationTerms;
            //We know that the first and second children are the only ones, and they are both MathNumbers.
            Tree eq1 = temp.getChild(0);
            Tree eq2 = temp.getChild(1);

            if(temp.getChild(0).data instanceof MathNumber && temp.getChild(1).data instanceof MathNumber){
                switch(operation){
                    case "+":
                        BigDecimal num = ((MathNumber) temp.getChild(0).data).number.add(((MathNumber) temp.getChild(1).data).number);
                        Tree<MathObject> newTree = new Tree<>();
                        newTree.data = new MathNumber(num);
                        temp.replaceThis(newTree);
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
                            }
                            else{ //It simplifies to a normal number.
                                return new Equation(new Tree(new MathNumber(newNum)));
                            }
                        } catch (ArithmeticException excep){ //Something that doesn't end in decimal, like 2/3 = .66666666666666666666
                            //Keep it as fraction and simplify it.
                        }
                        break;
                    default:

                }
            }
            return equation;
        }
    }
    public static class EquationCommandRemoveNestedFractionDenominator extends EquationCommand{
        public EquationCommandRemoveNestedFractionDenominator(){
        }
        public Equation run(Equation equation) {
            //First remove fractions in the denominator.
            // (x / (y / z) = x * (z / y)
            PatternEquation pattern = builder.makePatternEquation("EXPRESSION / EXPRESSION{FRACTION}");
            if(!equation.isPattern(pattern)){
                throw makeBadTypeException(pattern, equation);
            };
            Tree<MathObject> currentLocation = equation.equationTerms;
            Tree<MathObject> numerator = currentLocation.getChild(0);
            Tree<MathObject> denomFraction = currentLocation.getChild(1);
            Tree<MathObject> newTree = new Tree();
            newTree.data = new Multiplication();
            newTree.addChild(numerator);
            newTree.addChild(denomFraction);
            currentLocation.replaceThis(newTree);
            return equation;
        }
    }
    public static class EquationCommandRemoveNestedFractionNumerator extends EquationCommand{
        public EquationCommandRemoveNestedFractionNumerator(){

        }
        public Equation run(Equation equation){
            // (x / y) / z = (x / (y * z))
            PatternEquation pattern = builder.makePatternEquation("EXPRESSION{FRACTION} / EXPRESSION");
            if(!equation.isPattern(pattern)){
                throw makeBadTypeException(pattern, equation);
            };
            Tree<MathObject> currentLocation = equation.equationTerms;
            Tree<MathObject> denominator = currentLocation.getChild(1);
            Tree<MathObject> numerator = currentLocation.getChild(0);
            Tree<MathObject> newTree = new Tree();
            newTree.data = new Division();
            newTree.addChild(numerator.getChild(0));
            Tree<MathObject> bottom = newTree.addChild(new Multiplication());
            bottom.addChild(denominator);
            bottom.addChild(numerator.getChild(1));
            currentLocation.replaceThis(newTree);
            return equation;
        }
    }
    public static class EquationCommandRemoveNestedFractions extends EquationCommand {
        public EquationCommandRemoveNestedFractions(){

        }
        public Equation run(Equation equation){
            try{
                equation = removeNumeratorFraction.simplify(equation);
            } catch (BadEquationTypeException e){
                //Ignore it.
            }
            try{
                equation = removeDenominatorFraction.simplify(equation);
            } catch (BadEquationTypeException e){
                //Ignore it.
            }
            return equation;
        }
    }
    public static class EquationCommandCondenceConstants extends EquationCommand {
        public EquationCommandCondenceConstants(){

        }
        public Equation run(Equation equation){
            if(equation.isPattern(builder.makePatternEquation("CONSTANT + CONSTANT"))){
                equation = constantsAddition.simplify(equation);
            }
            else if(equation.isPattern(builder.makePatternEquation("CONSTANT - CONSTANT"))){
                equation = constantsSubtraction.simplify(equation);
            }
            else if(equation.isPattern(builder.makePatternEquation("CONSTANT * CONSTANT"))){
                equation = constantsMultiplication.simplify(equation);
            }
            return equation;
        }
    }
    private static BadEquationTypeException makeBadTypeException(EquationType type, Equation eq1, Equation eq2){ //TO ME IN THE FUTURE: In case you forget, this just checks both inputs of an equation
        //And throws an error for whichever one is the bad type. It's really not too complicated.
        if(!eq1.isType(type)){
            return new BadEquationTypeException(type, eq1);
        }
        return new BadEquationTypeException(type, eq2);
    }
    private static BadEquationTypeException makeBadTypeException(EquationType type, Equation eq1){
        return new BadEquationTypeException(type, eq1);
    }
    private static BadEquationTypeException makeBadTypeException(PatternEquation type, Equation eq1){
        return new BadEquationTypeException(type, eq1);
    }
}
