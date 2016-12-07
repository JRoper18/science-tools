package Mathematics.MathObjects.PatternMatching;

import Mathematics.EquationBuilder;
import Mathematics.MathObjects.MathObject;

/**
 * Created by jack on 12/6/2016.
 */
public class InfiniteArgExpression extends MathObject {
    public PatternEquation pattern = null;

    public InfiniteArgExpression(boolean ordered, String patternString) {
        super(-1, ordered);
        EquationBuilder builder = new EquationBuilder();
        this.pattern = builder.makePatternEquation(patternString);
    }

    public InfiniteArgExpression(boolean ordered) {
        super(-1, ordered);
    }
}
