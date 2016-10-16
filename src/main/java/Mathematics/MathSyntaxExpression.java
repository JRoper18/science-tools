package Mathematics;

import Mathematics.MathObjects.Addition;
import Mathematics.MathObjects.MathObject;
import Mathematics.MathObjects.Multiplication;
import Mathematics.MathObjects.Parenthesis;

/**
 * Created by jack on 10/12/2016.
 */
public enum MathSyntaxExpression {
    CLOSE_PAREN,
    OPEN_PAREN,
    MULTIPLY,
    PLUS;

    public MathObject getMathObject() {
        switch (this) {
            case CLOSE_PAREN:
                return new Parenthesis(false);
            case OPEN_PAREN:
                return new Parenthesis(true);
            case PLUS:
                return new Addition();
            case MULTIPLY:
                return new Multiplication();
            default:
                return new Addition();
        }
    }
}
