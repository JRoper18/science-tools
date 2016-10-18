package Mathematics;

import Mathematics.MathObjects.MathObject;
import Structures.Tree.*;

/**
 * Created by Ulysses Howard Smith on 10/7/2016.
 */
public class Equation {
    public Tree<MathObject> equationTerms;
    public Equation(Tree equationTerms){
        this.equationTerms = equationTerms;
    }
    public Equation substitute(Equation before, Equation after){
        return new Equation(); //CHANGE THIS
    }
}
