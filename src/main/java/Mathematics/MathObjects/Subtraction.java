package Mathematics.MathObjects;

import Mathematics.Equation;

/**
 * Created by jack on 11/6/2016.
 */
public class Subtraction extends Expression{
        public MathObject term1;
        public MathObject term2;
        public Subtraction(MathObject term1, MathObject term2){
            super(2, true);
            this.term1 = term1;
            this.term2 = term2;
        }
        public Subtraction(){
            super(2, true);
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
            return null; //CHANGE THIS
        }
    }

