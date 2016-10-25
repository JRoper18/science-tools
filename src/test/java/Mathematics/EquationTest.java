package Mathematics;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ulysses Howard Smith on 10/10/2016.
 */
public class EquationTest {
    @Test
    public void testPrint() throws Exception {
        EquationBuilder builder = new EquationBuilder();
        List<MathSyntax> input1 = Arrays.asList(new MathSyntax(MathSyntaxExpression.NUMBER), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(MathSyntaxExpression.NUMBER));
        List<MathSyntax> input2 = Arrays.asList(new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(12), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(13), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN), new MathSyntax(MathSyntaxExpression.MULTIPLY), new MathSyntax(2));
        Equation test1 = builder.makeEquation(input1);
        Equation test2 = builder.makeEquation(input2);
        System.out.println(test1.substitute(test1, test2));
    }
}