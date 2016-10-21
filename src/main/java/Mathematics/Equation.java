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
                if(!this.checkEquationTreesEqual(tree1.getChild(i), tree2.getChild(i))){ //If a single expression is wrong, return false.
                    return false;
                }
            }
        }
        else{ //The operators for our current expression are unordered, like + or -, where you can switch the order of the arguements without affecting outcome.
            //However, their children might be ordered. We need to check them.
            //To do this, we need to match
            //Match each child with it's corresponding child in the other tree
            List<Tree> tree1Children = tree1.getChildren();
            List<Tree> tree2Children = tree2.getChildren();
            for(int i = 0; i<tree2Children.size(); i++){
                boolean foundMatch = false;
                for(int j = 0; j<tree1Children.size(); j++){
                    if(this.checkEquationTreesEqual(tree1Children.get(j), tree2Children.get((i)))){
                        foundMatch = true;
                        tree1Children.remove(j);
                        tree2Children.remove(i);
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
    public Equation substitute(Equation before, Equation after){
        this.equationTerms.forEachNode((node) -> {
            if(node.data.equals(before.equationTerms.data)){

            }
        });
        return new Equation(); //CHANGE THIS
    }
}
