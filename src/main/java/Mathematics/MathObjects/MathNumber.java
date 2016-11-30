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
        this.number = BigDecimal.valueOf(num); //NOTE: ValueOf for double values is VERY IMPORTANT instead of using new BigDecimal. Check here:
        //http://stackoverflow.com/questions/7186204/bigdecimal-to-use-new-or-valueof
    }
    public boolean isInteger(){
        boolean isInt;
        try {
            this.number.toBigIntegerExact();
            isInt = true;
        } catch (ArithmeticException ex) {
            isInt = false;
        }
        return isInt;
    }
    public boolean numericEquals(MathNumber number){
        return (this.number.compareTo(number.number) == 0);
    }

}
