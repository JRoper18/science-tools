package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */
public abstract class MathObject {
    protected int arguments = 0;
    public int arguments(){
        return this.arguments;
    }
    public boolean isConstant(){
        return (this.arguments == 0);
    }
}

