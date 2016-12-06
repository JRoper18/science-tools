package Mathematics;

import Mathematics.MathObjects.MathObject;
import Structures.Tree.Tree;
import com.rits.cloning.Cloner;

import java.io.IOException;
import java.io.UncheckedIOException;

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
                /*
                if(temp.equals(new Equation(newTree))){
                    System.out.println(builder.makeEquation("1 + 2").equals(temp));
                    this.simplify(builder.makeEquation("1 + 2")).printTree();
                }
                */
                node.replaceThis(newTree);
            } catch (BadEquationTypeException e){
                //IGNORE IT
            }
        });
        return newEquation;
    }
    public final Equation simplifyAllUntilCondition(Equation equation, EquationCondition condition){
        Equation newEquation = cloner.deepClone(equation);
        Equation previousEquation;
        while(!condition.meetsCondition(equation)){
            previousEquation = cloner.deepClone(newEquation);
            newEquation = this.simplifyAll(newEquation);
            if(previousEquation.equals(newEquation)){
                newEquation.printTree();
                throw new UncheckedIOException(new IOException("INFINITE LOOP DETECTED! Your condition doesn't change for the equation above."));
            }
        }
        return newEquation;
    }
    protected abstract Equation run(Equation equation);
}
