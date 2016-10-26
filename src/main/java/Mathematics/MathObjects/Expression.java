package Mathematics.MathObjects;

import Mathematics.Equation;
import Structures.Tree.Tree;

/**
 * Created by Ulysses Howard Smith on 10/20/2016.
 */
public abstract class Expression extends MathObject {
    public Expression(int arguements, boolean ordered){
        super(arguements, ordered);
    }
    abstract public Equation eval();
}
