package Mathematics;

import Mathematics.MathObjects.*;

/**
 * Created by jack on 10/12/2016.
 */
public enum MathSyntaxExpression {
    CLOSE_PAREN,
    OPEN_PAREN,
    NUMBER,
    EXPRESSION,
    MULTIPLY,
    PLUS;

    public MathObject getMathObject() {
        switch (this) {
            case CLOSE_PAREN:
                return new Parenthesis(false);
            case OPEN_PAREN:
                return new Parenthesis(true);
            case NUMBER:
                return new MathNumber();
            case EXPRESSION:
                return new Expression();
            case PLUS:
                return new Addition();
            case MULTIPLY:
                return new Multiplication();
            default:
                return new Addition();
        }
    }
}
