package Mathematics;

import Mathematics.MathObjects.*;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */

public class EquationCommandDatabase {
    public static final SimplifyTwoConstants constantsAddition = new SimplifyTwoConstants("+");
    public static final SimplifyTwoConstants constantsSubtraction = new SimplifyTwoConstants("-");
    public static final SimplifyTwoConstants constantsMultiplication = new SimplifyTwoConstants("*");
    public static final SimplifyTwoConstants constantsDivision = new SimplifyTwoConstants("/");
    public static final RemoveNestedFractionDenominator removeDenominatorFraction = new RemoveNestedFractionDenominator();
    public static final RemoveNestedFractionNumerator removeNumeratorFraction = new RemoveNestedFractionNumerator();
    public static final RemoveNestedFractions removeNestedFractions = new RemoveNestedFractions();
    public static final CondenceConstants condenceConstants = new CondenceConstants();
    public static final GreatestCommonDenominatorInts gcdInts = new GreatestCommonDenominatorInts();
    public static final LeastCommonMultipleInts lcmInts = new LeastCommonMultipleInts();
    public static final IntegerFractionSimplify integerFractionSimplify = new IntegerFractionSimplify();
    public static final DecimalToFraction decimalToFraction = new DecimalToFraction();
    public static final SimplifyDivideByOne divideByOne = new SimplifyDivideByOne();
    public static final CondenceTwoConstants condenceTwoConstants = new CondenceTwoConstants();
    public EquationCommandDatabase(){
    }
    public static class SimplifyTwoConstants extends EquationCommand{
        public final String operation;
        public SimplifyTwoConstants(String operation){
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
                                equation = decimalToFraction.simplifyAll(equation);
                                equation = integerFractionSimplify.simplify(equation);
                            }
                            else{ //It simplifies to a normal number.
                                return new Equation(new Tree(new MathNumber(newNum)));
                            }
                        } catch (ArithmeticException excep){ //Something that doesn't end in decimal, like 2/3 = .66666666666666666666
                            //Keep it as fraction and simplify it.
                            equation = decimalToFraction.simplifyAll(equation);
                            equation = integerFractionSimplify.simplify(equation);
                        }
                        break;
                    default:

                }
            }
            return equation;
        }
    }
    public static class RemoveNestedFractionDenominator extends EquationCommand{
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
    public static class RemoveNestedFractionNumerator extends EquationCommand{
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
    public static class RemoveNestedFractions extends EquationCommand {
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
    public static class CondenceTwoConstants extends EquationCommand{
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
            else if(equation.isPattern(builder.makePatternEquation("CONSTANT / CONSTANT"))){
                equation = constantsDivision.simplify(equation);
            }
            return equation;
        }
    }
    public static class CondenceConstants extends EquationCommand {
        public Equation run(Equation equation){
            equation = removeNestedFractions.simplifyAll(equation);
            equation = decimalToFraction.simplifyAll(equation);
            equation = condenceTwoConstants.simplifyAll(equation);
            equation = divideByOne.simplifyAll(equation);
            return equation;
        }
    }
    public static class GreatestCommonDenominatorInts extends EquationCommand {
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
                Equation subGCD = combineEquationsWithOperation(new GreatestCommonDenominator(), eq2, new Equation(new Tree(remainder)));
                return this.simplify(subGCD);
            }
        }
    }
    public static class LeastCommonMultipleInts extends EquationCommand {
        public Equation run(Equation equation){
            Equation recur = doRecursiveFunction(2, this, equation);
            if(recur != null){
                return recur;
            }
            Equation eq1 = new Equation(equation.equationTerms.getChild(0));
            Equation eq2 = new Equation(equation.equationTerms.getChild(1));
            BigDecimal abs = ((MathNumber) eq2.equationTerms.data).number.multiply(((MathNumber) eq1.equationTerms.data).number).abs();
            Equation newEquation = combineEquationsWithOperation(new GreatestCommonDenominator(), eq1, eq2);
            BigDecimal gcd = ((MathNumber) gcdInts.simplify(newEquation).equationTerms.data).number;
            return new Equation(new Tree(new MathNumber(abs.divide(gcd))));
        }
    }
    public static class IntegerFractionSimplify extends EquationCommand {
        public Equation run(Equation equation){
            if(!equation.isType(EquationType.INTEGERFRACTION)) {
                throw makeBadTypeException(EquationType.INTEGERFRACTION, equation);
            }
            Equation numerator = iden.getFractionNumerator(equation);
            Equation denominator = iden.getFractionDenominator(equation);
            Equation gcdUnsimplified = combineEquationsWithOperation(new GreatestCommonDenominator(), numerator, denominator);
            Equation gcd = gcdInts.simplify(gcdUnsimplified);
            if(((MathNumber) gcd.equationTerms.data).number.doubleValue() == 1){
                return equation; //Already simplified.
            }
            if(gcd.isType(EquationType.INTEGERCONSTANT)){ //We have a constant, so just divide the top and bottom by it.
                BigDecimal newNumeratorDec = ((MathNumber) numerator.equationTerms.data).number.divide(((MathNumber) gcd.equationTerms.data).number);
                BigDecimal newDenominatorDec = ((MathNumber) denominator.equationTerms.data).number.divide(((MathNumber) gcd.equationTerms.data).number);
                Equation newEq = builder.makeEquation(Arrays.asList(new MathSyntax(newNumeratorDec), new MathSyntax(MathSyntaxExpression.DIVIDE), new MathSyntax(newDenominatorDec)));
                return newEq;
            }
            return null;
        }
    }
    public static class SimplifyDivideByOne extends EquationCommand{
        public Equation run(Equation equation){
            PatternEquation pattern = builder.makePatternEquation("EXPRESSION / 1");
            if(!equation.isPattern(pattern)){
                throw makeBadTypeException(pattern, equation);
            }
            return iden.getFractionNumerator(equation);
        }
    }
    public static class DecimalToFraction extends EquationCommand{
        public Equation run(Equation equation){
            if(!equation.isType(EquationType.CONSTANT)){
                throw makeBadTypeException(EquationType.CONSTANT, equation);
            }
            if(equation.isType(EquationType.INTEGERCONSTANT)){
                return equation; //All done
            }
            Tree currentTree =  equation.equationTerms;
            MathNumber currentNum = ((MathNumber) currentTree.data);
            int scale = currentNum.number.stripTrailingZeros().scale();
            BigDecimal newNumDec = currentNum.number.movePointRight(scale);
            Tree fraction = new Tree();
            fraction.data = new Division();
            fraction.addChild(new MathNumber(newNumDec.toString()));
            fraction.addChild(new MathNumber(Math.pow(10, scale)));
            return new Equation(fraction);
        }
    }
    public static class SimplifyTwoFractions extends EquationCommand {
        public Equation run(Equation equation){
            char[] operations = {'+', '-', '/', '*'};
            for(char operation : operations){
                if(equation.isPattern(builder.makePatternEquation("CONSTANT " + operation + " EXPRESSION{FRACTION}"))){
                    equation.equationTerms.replaceChild(0, decimalToFraction.simplify(new Equation(equation.equationTerms.getChild(0))).equationTerms);
                }
                else if(equation.isPattern(builder.makePatternEquation("EXPRESSION{FRACTION} " + operation + " CONSTANT"))){
                    equation.equationTerms.replaceChild(1, decimalToFraction.simplify(new Equation(equation.equationTerms.getChild(0))).equationTerms);
                }
                PatternEquation pattern = builder.makePatternEquation("EXPRESSION{FRACTION} " + operation + " EXPRESSION{FRACTION}");
                if(!equation.isPattern(pattern)){
                    continue;
                }
                //First, get the denominators and numerators.
                Tree<MathObject> denomLeft = equation.equationTerms.getChild(0).getChild(1);
                Tree<MathObject> numeratorLeft = equation.equationTerms.getChild(0).getChild(0);
                Tree<MathObject> denomRight = equation.equationTerms.getChild(1).getChild(1);
                Tree<MathObject> numeratorRight = equation.equationTerms.getChild(1).getChild(0);
            }

        }
    }
    private static Equation doRecursiveFunction(int depth, EquationCommand command, Equation equation){
        int argNum = equation.equationTerms.getChildren().size();
        if(argNum > depth){
            Tree<MathObject> subEquationTree = new Tree(equation.equationTerms.data);
            List<Tree> otherChildren = equation.equationTerms.getChildren().subList(1, equation.equationTerms.getChildren().size());
            subEquationTree.addTreeChildren(otherChildren);
            Equation subEquationCalc = command.simplify(new Equation(subEquationTree));
            Equation newEquation = combineEquationsWithOperation(((Expression) equation.equationTerms.data), subEquationCalc, new Equation(equation.equationTerms.getChild(0)));
            newEquationTree.addChild(subEquationCalc.equationTerms);
            newEquationTree.addChild(equation.equationTerms.getChild(0));
            return command.simplify(new Equation(newEquationTree));
        }
        else{
            return null;
        }
    }
    public static class CondenceFractions extends EquationCommand{
        public Equation run(Equation equation){
            return null; //WORK ON THIS LATER
        }
    }
    public static Equation combineEquationsWithOperation(Expression expression, Equation eq1, Equation eq2){
        Tree<MathObject> newTree = new Tree<>();
        newTree.data = expression;
        newTree.addChild(eq1.equationTerms);
        newTree.addChild(eq2.equationTerms);
        return new Equation(newTree);
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
