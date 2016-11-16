package Mathematics.MathObjects;

/**
 * Created by Ulysses Howard Smith on 11/16/2016.
 */
public class MathNumberInteger extends MathNumber {
    public MathNumberInteger(int num){
        super(((double) num));
    }
    public MathNumberInteger(String num){
        super(num);
    }
}
