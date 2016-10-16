package Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */

public class EquationBuilderTest {

    @Test
    public void testCreation() throws Exception {
        EquationBuilder builder = new EquationBuilder();
        MathSyntax[] input1 = {new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(13), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(12), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN)};
        /*
        Tree should look like this:
                    (Null), the root object of an equation is always null
                    |
                    |
                    (+)
                    / \
                   /   \
                 (13)  (12)
         */

        MathSyntax[] input2 = {new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(12), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(13), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN), new MathSyntax(MathSyntaxExpression.MULTIPLY), new MathSyntax(2), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN)};
        Equation test1 = builder.makeEquation(input2);
        test1.equationTerms.print();
        assertEquals("TEST", test1.equationTerms.getChild(0).getChild(1).data.getClass().getName());
    }
}