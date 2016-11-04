package Mathematics.MathObjects.PatternMatching;

import Mathematics.MathObjects.MathObject;

/**
 * Created by jack on 10/31/2016.
 */
public class GenericConstant extends MathObject {
    public String tag;
    public GenericConstant(){
        super(0, false);
        this.tag = "none";
    }
    public GenericConstant(String tag){
        super(0, false);
        this.tag = tag;
    }
}
