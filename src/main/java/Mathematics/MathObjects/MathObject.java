package Mathematics.MathObjects;

import Mathematics.MathObjects.PatternMatching.GenericConstant;

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
    @Override
    public boolean equals(Object n){ //This equals method only checks if the mathobjects are of the same type. It does not check arguements.
        return this.getClass().getName().equals(n.getClass().getName()); //Check class names. For expressions, have a seperate equals method that checks arguements.
    }
}

