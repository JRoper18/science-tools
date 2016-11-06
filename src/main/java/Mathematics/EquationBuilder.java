package Mathematics;

import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.Parenthesis;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import Structures.Tuples.Triplet;

import java.util.ArrayList;
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
        Stack<Integer> notClosedParens = new Stack<Integer>();
        for(int i = 0; i<eq.size(); i++){
            MathObject current = eq.get(i).mathObject;
            if(current instanceof Parenthesis){
                if(((Parenthesis) current).open){
                    notClosedParens.push(new Integer(i));
                }
                else{
                    int nextParenIndex = notClosedParens.pop();
                    Triplet<Integer, Integer, Integer> newTrip = new Triplet<Integer, Integer, Integer>(nextParenIndex, new Integer(i), notClosedParens.size() + 1);
                    result.add(newTrip);
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
                toReturn.addChild(eq.get(0).mathObject);
                toReturn.addChild(eq.get(2).mathObject);
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
        else {
            //Find all the parenthesis on the next level. Add their results as arguments of out current function.
            for (int i = 0; i < parenthesis.size(); i++) {
                Triplet<Integer, Integer, Integer> currentParenPair = parenthesis.get(i);
                if (currentParenPair.val3 == 1) {
                    //The underlined parenthesis are in level 1:
                    // ()((()))
                    // |||    |
                    toReturn.addChild(makeEquationTree(eq.subList(currentParenPair.val1 + 1, currentParenPair.val2))); //Solves the equation in the parenthesis.
                }
            }
            //Find out current function.
            int level = 0;
            for (int i = 0; i < eq.size(); i++) {
                MathObject currentTerm = eq.get(i).mathObject;
                if (currentTerm instanceof Parenthesis) {
                    if (((Parenthesis) currentTerm).open) {
                        level++;
                    } else {
                        level--;
                    }
                }
                else if (level == 0) { //If we are on level 0, we are on the top level, and our expression is here somewhere.
                    //Check for expression
                    if (!currentTerm.isConstant()) { //Here's our expression!
                        if (toReturn.data != null) { //We have more than one expression on this level. In the future we should allow this, like 1+2+3, but for now,
                            //Just throw an error telling them to put parenthesis around it.
                            //1+2+3 -> (1+2) + 3
                            throw new Exception("You have more than one expression per level of parenthesis! Add more parenthesis!" + toReturn.data);
                        }
                        toReturn.data = currentTerm;
                    }
                    else{ //This is a constant not caught by the expression and equation finder above. Like 2*(sin(x)) instead of (sin(x)) + (cos(x))
                        toReturn.addChild(currentTerm);
                    }
                }
            }
        }
        return toReturn;

    }
    private static MathSyntax stringToSyntax(String str){
        switch(str){
            case "+":
                return new MathSyntax(MathSyntaxExpression.PLUS);
            case "*":
                return new MathSyntax(MathSyntaxExpression.MULTIPLY);
            case "CONSTANT":
                return new MathSyntax(MathSyntaxExpression.NUMBER);
            default:

        }
        //It's not an expression if we've made it here. Check for numbers
        int intNum;
        double doubNum;
        try{
            intNum = Integer.parseInt(str);
            return new MathSyntax(intNum);
        } catch (NumberFormatException e){ //Can't be an int. Try a double?
            try{
                doubNum = Double.parseDouble(str);
                return new MathSyntax(doubNum);
            } catch (NumberFormatException fin){
                fin.printStackTrace();
            }
        }
        return null; //IDK
    }
    public static Equation makeEquation(String eq){
        List<MathSyntax> input = new ArrayList<>();
        StringBuilder tokenBuild = new StringBuilder();
        for(int i = 0; i<eq.length(); i++){
            char currentChar = eq.charAt(i);
            if(currentChar == ' '){ //If there's a space
                input.add(stringToSyntax(tokenBuild.toString()));
                tokenBuild.delete(0, tokenBuild.length());
            }else{
                tokenBuild.append(currentChar);
            }
            if(i == (eq.length() - 1)){ //Last character, process whatever we have
                input.add(stringToSyntax(tokenBuild.toString()));
            }
        }
        return makeEquation(input);
    }
    public static Equation makeEquation(List<MathSyntax> eq){
        Tree equation;
        try{
            equation = makeEquationTree(eq);
            return new Equation(equation);

        } catch(Exception e){
            e.printStackTrace();
            return null;

        }
    }
    public static PatternEquation makePatternEquation(List<MathSyntax> eq){
        return makeEquation(eq).toPatternEquation();
    }
    public static PatternEquation makePatternEquation(String eq){
        return makeEquation(eq).toPatternEquation();
    }
}
