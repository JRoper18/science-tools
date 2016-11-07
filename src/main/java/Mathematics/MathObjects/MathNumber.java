package Mathematics.MathObjects;

import java.math.BigDecimal;

/**
 * Created by jack on 10/12/2016.
 */

public class MathNumber extends MathObject{
    public BigDecimal number;
    public MathNumber(String arbitraryLengthNum){
        super(0, false);
        this.number = new BigDecimal(arbitraryLengthNum);
    }
    public MathNumber(BigDecimal num){
        super(0, false);
        this.number = num;
    }
    public MathNumber(double num){
        super(0, false);
        this.number = new BigDecimal(num);
    }
}
