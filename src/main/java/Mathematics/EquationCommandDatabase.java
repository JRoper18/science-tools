package Mathematics;

import Mathematics.MathObjects.*;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;

import java.math.BigDecimal;
import java.util.List;

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
    public static final EquationCommandGreatestCommonDenominatorInts gcdInts = new EquationCommandGreatestCommonDenominatorInts();
    public static final EquationCommandLeastCommonMultipleInts lcmInts = new EquationCommandLeastCommonMultipleInts();
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
    public static class EquationCommandGreatestCommonDenominatorInts extends EquationCommand {
        public EquationCommandGreatestCommonDenominatorInts(){

        }
        public Equation run(Equation equation){
            Equation recur = doRecursiveFunction(2, this, equation);
            if(recur != null){
                return recur;
            }
            PatternEquation pattern = builder.makePatternEquation("GCD{EXPRESSION{INTEGERCONSTANT}}");
            if(!equation.isPattern(pattern)) {
                throw makeBadTypeException(pattern, equation);
            }
            Equation eq1 = new Equation(equation.equationTerms.getChild(0));
            Equation eq2 = new Equation(equation.equationTerms.getChild(1));
            if(((MathNumber)eq2.equationTerms.data).number.compareTo(new BigDecimal(0)) == 0){
                return eq1;
            }
            else{
                MathNumber remainder = new MathNumber(((MathNumber) eq1.equationTerms.data).number.remainder(((MathNumber) eq2.equationTerms.data).number));
                Tree<MathObject> subEqTree = new Tree<>();
                subEqTree.data = new GreatestCommonDenominator();
                subEqTree.addChild(eq2.equationTerms.data);
                subEqTree.addChild(remainder);
                return this.simplify(new Equation(subEqTree));
            }
        }
    }
    public static class EquationCommandLeastCommonMultipleInts extends EquationCommand {
        public EquationCommandLeastCommonMultipleInts() {

        }
        public Equation run(Equation equation){
            Equation recur = doRecursiveFunction(2, this, equation);
            if(recur != null){
                return recur;
            }
            Equation eq1 = new Equation(equation.equationTerms.getChild(0));
            Equation eq2 = new Equation(equation.equationTerms.getChild(1));
            BigDecimal abs = ((MathNumber) eq2.equationTerms.data).number.multiply(((MathNumber) eq1.equationTerms.data).number).abs();
            Tree<MathObject> newTree = new Tree<>();
            newTree.data = new GreatestCommonDenominator();
            newTree.addChild(eq1.equationTerms);
            newTree.addChild(eq2.equationTerms);
            BigDecimal gcd = ((MathNumber) gcdInts.simplify(new Equation(newTree)).equationTerms.data).number;
            return new Equation(new Tree(new MathNumber(abs.divide(gcd))));
        }
    }
    private static Equation doRecursiveFunction(int depth, EquationCommand command, Equation equation){
        int argNum = equation.equationTerms.getChildren().size();
        if(argNum > depth){
            Tree<MathObject> subEquationTree = new Tree(equation.equationTerms.data);
            List<Tree> otherChildren = equation.equationTerms.getChildren().subList(1, equation.equationTerms.getChildren().size());
            subEquationTree.addTreeChildren(otherChildren);
            Equation subEquationCalc = command.simplify(new Equation(subEquationTree));
            Tree newEquationTree = new Tree();
            newEquationTree.data = equation.equationTerms.data;
            newEquationTree.addChild(subEquationCalc.equationTerms);
            newEquationTree.addChild(equation.equationTerms.getChild(0));
            return command.simplify(new Equation(newEquationTree));
        }
        else{
            return null;
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
