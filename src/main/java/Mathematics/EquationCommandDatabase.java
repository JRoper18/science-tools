package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */
public class EquationCommandDatabase {
    public EquationCommandDatabase(){
    }
    private class EquationCommandSimplifyConstants extends EquationCommand{
        public final String operation;
        public EquationCommandSimplifyConstants(String operation){
            this.operation = operation;
        }
        public Equation run(Equation equation){
            String patternIn = "CONSTANT " + operation + " CONSTANT";
            PatternEquation pattern = builder.makePatternEquation(patternIn);
            List<LinkedList<Integer>> paths = equation.patternMatch(pattern);
            for(LinkedList<Integer> path : paths){
                Tree<MathObject> temp = equation.equationTerms.getChildThroughPath(path);

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
            }
            return equation;
        }

    }
}
