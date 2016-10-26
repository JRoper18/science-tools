package Mathematics.MathObjects;

import Mathematics.Equation;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */
public class Addition extends Expression {
    public MathObject term1;
    public MathObject term2;
    public Addition(MathObject term1, MathObject term2){
        super(2, false);
        this.term1 = term1;
        this.term2 = term2;
    }
    public Equation eval(){
        if(term1.isConstant() && term2.isConstant()){

        }
        else if(term1.isConstant()){

        }
        else if(term2.isConstant()){

        }
        else{

        }
    }
}
