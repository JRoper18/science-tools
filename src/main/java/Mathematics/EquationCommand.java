package Mathematics;

import com.rits.cloning.Cloner;

/**
 * Created by Ulysses Howard Smith on 12/2/2016.
 */
public abstract class EquationCommand implements EquationCommandInterface{
    public final Cloner cloner = new Cloner();
    public final EquationBuilder builder = new EquationBuilder();
    public EquationCommand(){

    }
}
