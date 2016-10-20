package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */
public abstract class MathObject {
    protected int arguments = 0;
    protected boolean ordered;
    public int arguments(){
        return this.arguments;
    }
    public boolean isConstant(){
        return (this.arguments == 0);
    }
    public boolean isOrdered(){
        return this.ordered;
    }
    public MathObject(int args, boolean ordered){
        this.arguments = args;
        this.ordered = ordered;
    }
}

