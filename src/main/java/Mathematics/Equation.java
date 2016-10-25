package Mathematics;

import Mathematics.MathObjects.Expression;
import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.MathObject;
import Structures.Tree.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ulysses Howard Smith on 10/7/2016.
 */
public class Equation {
    public Tree<MathObject> equationTerms;
    public Equation(Tree equationTerms){
        this.equationTerms = equationTerms;
    }
    private boolean checkEquationTreesEqual(Tree<MathObject> tree1, Tree<MathObject> tree2){
        //Tree1 is a regular equation, tree2 might contain empty expressions and numbers
        if(tree2.data.equals(new Expression(0, false))){ //If we find an empty expression, we assume any generic expression can go into there.
            return true; //This node is good, we don't need to check children.
        }
        if(tree2.data.equals(new MathNumber())){ //If we have a constant, check that tree1 also is just a generic constant
            return (tree1.data.isConstant())? true : null;
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
            List<Tree> tree1Children = tree1.getChildren();
            List<Tree> tree2Children = tree2.getChildren();
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
    public Tree<MathObject> buildNewEq(Tree<MathObject> current, Tree<MathObject> after, Tree<MathObject> building, Tree<MathObject> before){
        //This function assumes that we already know that current and before are equal.
        if(before.data.equals(new Expression(0, false))){ //If we have an expression, add the current children to the equation.
            return current;
        }
        if(before.data.equals(new MathNumber())){
            building.data = current.data;
            return building;
        }
        if(!after.hasChildren()){ //If the after has no more children, it doesn't matter, just replace because we konw they are equal.
            building.data = current.data;
            return building;
        }
        if(after.hasChildren()){ //Just for extra clarity
            //This means that we recursively replace, and the final equation is what we use as a child.
            for(int i = 0; i<after.getChildren().size();i++){
                building.addChild(this.buildNewEq(current.getChild(i), after.getChild(i), building.getChild(i), before.getChild(i)));
            }
        }
        return building;
    }
    public Equation substitute(Equation before, Equation after){
        this.equationTerms.forEachNode((node) -> {
            if(node.data.equals(before.equationTerms.data)){
                if(this.checkEquationTreesEqual(node, before.equationTerms)){
                    this.buildNewEq(this.equationTerms, after.equationTerms, new Tree<>(), before.equationTerms).print();
                    System.out.println(true);
                }
            }
        });
        return null; //CHANGE THIS
    }
}
