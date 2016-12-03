package Mathematics;

import Mathematics.MathObjects.MathObject;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */
public abstract class EquationCommand{
    public final Cloner cloner = new Cloner();
    public final EquationBuilder builder = new EquationBuilder();
    public EquationCommand(){

    }
    public final Equation simplify(Equation equation){
        Equation newEquation = cloner.deepClone(equation);
        return this.run(newEquation);
    }
    public final Equation simplifyAll(Equation equation){
        Equation newEquation = cloner.deepClone(equation);
        newEquation.equationTerms.forEachNode((node) -> {
            try{
                Equation temp = new Equation(node);
                Tree<MathObject> newTree = this.simplify(temp).equationTerms;
                node.replaceThis(newTree);
            } catch (BadEquationTypeException e){
                //IGNORE IT
            }
        });
        return newEquation;
    }
    protected abstract Equation run(Equation equation);
}
