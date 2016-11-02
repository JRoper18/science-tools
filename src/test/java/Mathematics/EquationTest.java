package Mathematics;

import Mathematics.MathObjects.PatternMatching.PatternEquation;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ulysses Howard Smith on 10/10/2016.
 */
public class EquationTest {

    EquationBuilder builder = new EquationBuilder();
    List<MathSyntax> input1 = Arrays.asList(new MathSyntax(MathSyntaxExpression.NUMBER), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(MathSyntaxExpression.NUMBER));
    List<MathSyntax> input2 = Arrays.asList(new MathSyntax(1), new MathSyntax(MathSyntaxExpression.MULTIPLY), new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(12), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(13), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN), new MathSyntax(MathSyntaxExpression.MULTIPLY), new MathSyntax(2), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN));
    List<MathSyntax> input3 = Arrays.asList(new MathSyntax(14), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(13));
    PatternEquation test1 = builder.makePatternEquation(input3);
    Equation test2 = builder.makeEquation(input2);
    Equation test3 = builder.makeEquation(input1);

    @Test
    public void testPatternMatch() throws Exception {
        assertEquals(new Integer(0), test2.patternMatch(test1).get(0).get(0));
        assertEquals(true, test3.patternMatch(test1).get(0).isEmpty());
    }

    @Test
    public void testSubstitute() throws Exception {
        List<MathSyntax> replaceInput = Arrays.asList(new MathSyntax(27));
        PatternEquation replace = builder.makePatternEquation(replaceInput);
        test3.substitute(test1, replace);
        test3.equationTerms.print();
    }
}