package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

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
                        temp.replaceThis(new Tree(new MathNumber(((MathNumber) temp.getChild(0).data).number.divide(((MathNumber) temp.getChild(1).data).number))));
                        break;
                    default:
                }
            }
        }
        return eq;
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
