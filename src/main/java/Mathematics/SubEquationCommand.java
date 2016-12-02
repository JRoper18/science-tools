package Mathematics;

import com.rits.cloning.Cloner;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */
public class SubEquationCommand extends EquationCommand implements EquationCommandInterface {
    public final EquationCommand command;
    public SubEquationCommand(EquationCommand command){
        this.command = command;
    }
    public Equation run(Equation eq){
        Equation equation = cloner.deepClone(eq);
        equation.equationTerms.forEachNode((node) ->{
            this.command.run(equation);
        });
        return equation;
    }
}
