package Mathematics.MathObjects.PatternMatching;

import Mathematics.MathObjects.MathObject;

/**
 * Created by jack on 10/31/2016.
 */
public class GenericVariable extends MathObject{
    public final String name;
    public GenericVariable(String name){
        super(0, false);
        this.name = name;
    }
    public GenericVariable(){
        super(0, false);
        this.name = "x";
    }
}
