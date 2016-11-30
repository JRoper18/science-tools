package Mathematics.MathObjects.PatternMatching;

import Mathematics.Equation;
import Mathematics.MathObjects.MathObject;
import Structures.Tree.Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jack on 10/31/2016.
 */
public class PatternEquation {
    public Tree<MathObject> equationTerms;
    public PatternEquation(Tree<MathObject> equationTerms){
        this.equationTerms = equationTerms;
    }
    public Equation toEquation(){
        List<LinkedList<Integer>> possibleBadPaths = new ArrayList<LinkedList<Integer>>();
        possibleBadPaths.addAll(equationTerms.findPaths(new GenericExpression()));
        possibleBadPaths.addAll(equationTerms.findPaths(new GenericConstant()));
        possibleBadPaths.addAll(equationTerms.findPaths(new GenericVariable()));
        if(possibleBadPaths.isEmpty()){
            return new Equation(this.equationTerms);
        }
        try {
            throw new Exception("Pattern equation has generics in it!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void printTree(){
        this.equationTerms.print();
    }
}
