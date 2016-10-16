package Mathematics;

import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.Parenthesis;
import Structures.Tree.Tree;
import Structures.Tuples.Triplet;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Stack;


/**
 * Created by Ulysses Howard Smith on 10/10/2016.
 */
public class EquationBuilder {
    public EquationBuilder(){

    }

    /**
     *
     * @param eq A List of MathSyntax objects that is your equation.
     * @return A list of triplets, each which is <Open paren index, closing paren index, level of parenthesis>
     */
    private static List<Triplet<Integer, Integer, Integer>> findParen(List<MathSyntax> eq){
        List<Triplet<Integer, Integer, Integer>> result = new ArrayList<Triplet<Integer, Integer, Integer>>();
        int level = 0;
        Stack<Integer> notClosedParens = new Stack<Integer>();
        for(int i = 0; i<eq.size(); i++){
            MathObject current = eq.get(i).mathObject;
            if(current instanceof Parenthesis){
                if(((Parenthesis) current).open){
                    level++;
                    notClosedParens.push(new Integer(i));
                    Triplet<Integer, Integer, Integer> newTrip = new Triplet<Integer, Integer, Integer>(new Integer(i), null, null);
                    result.add(newTrip);
                }
                else{
                    level--;
                    int nextParentIndex = notClosedParens.pop();
                    for(int j = 0; j<result.length; j++){
                        if(result.get(j).val1 == new Integer(j)){
                            result.get(j).val2 = new Integer(i);
                            result.get(j).val3 = new Integer(level);
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    private static Tree<MathObject> makeEquationTree(List<MathSyntax> eq) throws Exception{
        List<Triplet<Integer, Integer, Integer>> parenthesis = findParen(eq);
        Tree<MathObject> toReturn = new Tree<>();
        if(parenthesis.isEmpty()){ //We are as far down as we can go into this part of the equation.
            if(eq.size() == 1){ //We only have one argument. Could just be a variable, or a constant.
                if(!eq.get(0).mathObject.isConstant()) { //The fuck? Someone put in an expression as a root term. It's like trying to find sin(sin), or cos() * 3.
                    throw new Exception("Your equation input is bad! You have an expression placed where a constant should be. ");
                }
                else{
                    toReturn.data = eq.get(0).mathObject;
                }
            }
            else if(eq.get(0).mathObject.isConstant()){ //Since in a math equation we can't have 2 numbers right next to each other, like 3 1. 31 has meaning, 3 1 doesn't.
                //If the first part of it is a number, the second part must be a binary expression, taking 2 arguments. But just in case, let's check.
                if(eq.get(1).mathObject.arguments() != 2){ //Input error
                    throw new Exception("Your equation input is bad! You are putting an non-binary operator in-between 2 arguments. ");
                }
                toReturn.data = eq.get(1).mathObject;
                toReturn.addChild(eq.get(0));
                toReturn.addChild(eq.get(2));
            }
            else{ //Else, we have an operator that isn't binary. It could take 1 argument, like sin or cos, of more than 2, like summation or a limit.
                //Either way, we are going to assume that the input looks like:
                //[SUM, from, to, equation] or [COS, something]
                //So, we assume that the first part is the expression itself.
                toReturn.data = eq.get(0).mathObject;
                //Then, add the next parts as arguments to that expression.
                for(int i = 1; i<eq.size(); i++){
                    toReturn.addChild(eq.get(i).mathObject);
                }
            }
        }
        else { //Recursively go down to the next parenthesis
            //Find all the parenthesis on the next level. Run this function for each of those.
            for(int i = 0; i<parenthesis.size(); i++){
                Triplet<Integer,Integer, Integer> currentParenPair = parenthesis.get(i);
                if(currentParenPair.val3 == 1){
                    //The underlined parenthesis are in level 1:
                    // ()((()))
                    // |||    |
                    toReturn = makeEquationTree(eq.subList(currentParenPair.val1 + 1, currentParenPair.val2)); //Solves the equation in the parenthesis.
                }
            }
        }
        return toReturn;
    }
    public static Equation makeEquation(List<MathSyntax> eq){
        int firstParenIndex = parenthesis.values().toArray()[0];
        makeEquationTree(eq.subList(firstParenIndex, parenthesis.get(firstParenIndex))); //Make an equation from first opening paren to the last closing paren
        return new Equation(equation); //Change this
    }
}
