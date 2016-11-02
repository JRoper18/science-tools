package Mathematics;

import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.PatternMatching.GenericConstant;
import Mathematics.MathObjects.PatternMatching.GenericExpression;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import Structures.Tree.Tree;
import Structures.Tree.TreeSearchCallback;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ulysses Howard Smith on 10/7/2016.
 */
public class Equation {
    public Tree<MathObject> equationTerms;
    public Equation(Tree equationTerms){
        this.equationTerms = equationTerms;
    }
    /**
     *
     * @param pattern A PatternEquation to check this equation agains
     * @return A List of LinkedLists, each containing a set of numbers that points down the equation tree to the matching pattern.
     */
    public List<LinkedList<Integer>> patternMatch(PatternEquation pattern){
        List<LinkedList<Integer>> paths = new ArrayList<LinkedList<Integer>>();
        TreeSearchCallback callback = (node) -> {
            if(this.checkEquationTreesEqual(node, pattern.equationTerms)){
                paths.add(node.getPathFromRoot());
            }
        };
        this.equationTerms.forEachNode(callback);
        return paths;
    }
    private boolean checkEquationTreesEqual(Tree<MathObject> tree1, Tree<MathObject> tree2){
        //Tree1 is a regular equation, tree2 might contain empty expressions and numbers
        if(tree2.data.equals(new GenericExpression())){ //If we find an empty expression, we assume any generic expression can go into there.
            return true; //This node is good, we don't need to check children.
        }
        if(tree2.data.equals(new GenericConstant())){ //If we have a constant, check that tree1 also is just a generic constant
            return tree1.data.isConstant();
        }
        //We've checked for generic constants and expressions, now just compare the 2
        if(!tree1.data.equals(tree2.data)){ //The root expression or constant isn't the same
            return false;
        }
        //Now, check the children's sizes
        if(tree1.getChildren().size() != tree2.getChildren().size()){
            return false;
        }

        //Check the data inside the children. To do this, we need to know if our current expression is ordered.
        if(tree2.data.isOrdered()){ //We have an ordered expression, like SUM. We need to check every term IN ORDER.
            for(int i = 0; i<tree1.getChildren().size(); i++){
                if(!this.checkEquationTreesEqual(tree1.getChild(i), tree2.getChild(2))){ //If a single expression is wrong, return false.
                    return false;
                }
            }
        }
        else{ //The operators for our current expression are unordered, like + or -

            Cloner cloner = new Cloner();

            List<Tree> tree1Children = cloner.deepClone(tree1.getChildren());
            List<Tree> tree2Children = cloner.deepClone(tree2.getChildren());
            //Match each child with it's corresponding child in the other tree by comparing children. If we find a single match, we go on. If we don't find a single match,
            //That means one term is not in the other, so we return false.
            for(int i = 0; i<tree2Children.size(); i++){
                boolean foundMatch = false;
                for(int j = 0; j<tree1Children.size(); j++){
                    if(checkEquationTreesEqual(tree2Children.get(i), tree1Children.get(j))){
                        tree1Children.remove(j);
                        tree2Children.remove(i);
                        foundMatch = true;
                        break;
                    }
                }
                if(!foundMatch){
                    return false;
                }
            }
        }
        //So we know our children, our data, and our children's children are equal. We must be the same.
        return true;
    }
    public void substitute(PatternEquation before, PatternEquation after){
        List<LinkedList<Integer>> pathsToMatches = this.patternMatch(before);
        List<LinkedList<Integer>> expressionLocations = before.equationTerms.findPaths(new GenericExpression());
        for(int i = 0; i<pathsToMatches.size(); i++){
            LinkedList<Integer> currentPath = pathsToMatches.get(i);
            Tree<MathObject> currentEquation = this.equationTerms.getChildThroughPath(currentPath);
            Cloner cloner = new Cloner();
            Tree<MathObject> replaceTree = cloner.deepClone(after.equationTerms);
            for(int j = 0; j<expressionLocations.size(); j++){
                LinkedList<Integer> currentExpressionPath = expressionLocations.get(j);
                Tree<MathObject> previousExpression = before.equationTerms.getChildThroughPath(currentExpressionPath);
                Tree<MathObject> expressionLocation = replaceTree.getChildThroughPath(currentExpressionPath);
                expressionLocation = previousExpression;
            }
            currentEquation = replaceTree;
        }
    }
    /**
     *
     * @return A PatternEquation equivalent of this Equation
     */
    public PatternEquation toPatternEquation(){
        return new PatternEquation(this.equationTerms);
    }
}
