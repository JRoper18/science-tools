package Mathematics;

import Mathematics.MathObjects.Addition;
import Mathematics.MathObjects.MathNumber;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ulysses Howard Smith on 10/11/2016.
 */

public class EquationBuilderTest {

    @Test
    public void testCreation() throws Exception {
        EquationBuilder builder = new EquationBuilder();
        List<MathSyntax> input1 = Arrays.asList(new MathSyntax(13), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(12));
        /*
        Tree should look like this:
                    (+)
                    / \
                   /   \
                 (13)  (12)
         */

        List<MathSyntax> input2 = Arrays.asList(new MathSyntax(MathSyntaxExpression.OPEN_PAREN), new MathSyntax(12), new MathSyntax(MathSyntaxExpression.PLUS), new MathSyntax(13), new MathSyntax(MathSyntaxExpression.CLOSE_PAREN), new MathSyntax(MathSyntaxExpression.MULTIPLY), new MathSyntax(2));
        /*
        Also read as (12+13)*2
        Tree is:
              (*)
              /  \
             /    \
           (+)    (2)
           / \
          /   \
       (13)  (12)
         */
        Equation test1 = builder.makeEquation(input1);
        Equation test2 = builder.makeEquation(input2);
        Equation test3 = builder.makeEquation("1 + 2");
        assertEquals(true, test1.equationTerms.data instanceof Addition);
        assertEquals(true, test1.equationTerms.getChild(0).data instanceof MathNumber);
        assertEquals(true, test1.equationTerms.getChild(1).data instanceof MathNumber);
        assertEquals(true, test2.equationTerms.getChild(0).data instanceof Addition);
        assertEquals(true, test2.equationTerms.getChild(1).data instanceof MathNumber);
        assertEquals(true, test2.equationTerms.getChild(0).getChild(0).data instanceof MathNumber);
        assertEquals(true, test2.equationTerms.getChild(0).getChild(1).data instanceof MathNumber);
    }
}