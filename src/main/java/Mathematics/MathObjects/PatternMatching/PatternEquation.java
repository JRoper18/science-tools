package Mathematics.MathObjects.PatternMatching;

import Mathematics.MathObjects.MathObject;
import Structures.Tree.Tree;

/**
 * Created by jack on 10/31/2016.
 */
public class PatternEquation {
    public Tree<MathObject> equationTerms;
    public PatternEquation(Tree<MathObject> equationTerms){
        this.equationTerms = equationTerms;
    }
}
