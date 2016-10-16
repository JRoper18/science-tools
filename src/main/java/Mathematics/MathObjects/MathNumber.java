package Mathematics.MathObjects;

import java.math.BigDecimal;

/**
 * Created by jack on 10/12/2016.
 */

public class MathNumber extends MathObject{
    public BigDecimal number;
    public MathNumber(String arbitraryLengthNum){
        this.number = new BigDecimal(arbitraryLengthNum);
    }
    public MathNumber(double num){
        this.number = new BigDecimal(num);
    }
}
