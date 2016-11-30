package Mathematics;

import Mathematics.MathObjects.PatternMatching.GenericExpression;
import Mathematics.MathObjects.PatternMatching.PatternEquation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jack on 11/6/2016.
 */
public class SimplifierTest {
    Simplifier simplifier = new Simplifier();
    EquationBuilder builder = new EquationBuilder();

    @Test
    public void testConstantPlusConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 + 1");
        Equation expected1 = builder.makeEquation("2");
        Equation input2 = builder.makeEquation("2.343 + 5.4");
        Equation expected2 = builder.makeEquation("7.743");
        assertEquals(expected1, simplifier.constantsAddition(input1));
        assertEquals(expected2, simplifier.constantsAddition(input2));
    }
    @Test
    public void testConstantMinusConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 - 1");
        Equation expected1 = builder.makeEquation("0");
        Equation input2 = builder.makeEquation("2 - 1");
        Equation expected2 = builder.makeEquation("1");
        Equation input3 = builder.makeEquation("2 - 3");
        Equation expected3 = builder.makeEquation("-1");

        assertEquals(expected1, simplifier.constantsSubtraction(input1));
        assertEquals(expected2, simplifier.constantsSubtraction(input2));
        assertEquals(expected3, simplifier.constantsSubtraction(input3));
    }

    @Test
    public void testConstantTimesConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 * 1");
        Equation expected1 = builder.makeEquation("1");
        Equation input2 = builder.makeEquation("2 * 2");
        Equation expected2 = builder.makeEquation("4");
        Equation input3 = builder.makeEquation("-2 * 3");
        Equation expected3 = builder.makeEquation("-6");
        assertEquals(expected1, simplifier.constantsMultiplication(input1));
        assertEquals(expected2, simplifier.constantsMultiplication(input2));
        assertEquals(expected3, simplifier.constantsMultiplication(input3));
    }

    @Test
    public void testConstantDivideConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 / 1");
        Equation expected1 = builder.makeEquation("1");
        Equation input2 = builder.makeEquation("2 / 2");
        Equation expected2 = builder.makeEquation("1");
        Equation input3 = builder.makeEquation("-4 / 2");
        Equation expected3 = builder.makeEquation("-2");
        Equation input4 = builder.makeEquation("2 / 3");
        assertEquals(expected1, simplifier.constantsDivision(input1));
        assertEquals(expected2, simplifier.constantsDivision(input2));
        assertEquals(expected3, simplifier.constantsDivision(input3));
        assertEquals(input4, simplifier.constantsDivision(input4));
    }


    @Test
    public void testDecimalToFraction() throws Exception {
        Equation input1 = builder.makeEquation(".023");
        Equation expected1 = builder.makeEquation("23 / 1000");
        Equation input2 = builder.makeEquation("34.2");
        Equation expected2 = builder.makeEquation("342 / 10");
        assertEquals(expected1, simplifier.decimalsToFractions(input1));
        assertEquals(expected2, simplifier.decimalsToFractions(input2));
    }

    @Test
    public void testFractionSimplification() throws Exception {
        Equation input1 = builder.makeEquation("4 / 6");
        Equation expected1 = builder.makeEquation("2 / 3");
        Equation input2 = builder.makeEquation("1476 / 1476000000");
        Equation expected2 = builder.makeEquation("1 / 1000000");
        assertEquals(expected1, simplifier.simplifyIntegerFraction(input1));
        assertEquals(expected2, simplifier.simplifyIntegerFraction(input2));
    }

    @Test
    public void testGCDIntegers() throws Exception {
        Equation input1 = builder.makeEquation("60");
        Equation input2 = builder.makeEquation("12");
        Equation input3 = builder.makeEquation("4");
        Equation input4 = builder.makeEquation("6");
        Equation expected1 = builder.makeEquation("12");
        Equation expected2 = builder.makeEquation("2");
        assertEquals(expected1, simplifier.GCDIntegers(input1, input2));
        assertEquals(expected2, simplifier.GCDIntegers(input3, input4));
    }

    @Test
    public void testNestedFractionRemoval() throws Exception {
        Equation input1 = builder.makeEquation("2 / ( 3 / 4 )");
        PatternEquation pattern = builder.makePatternEquation("EXPRESSION / EXPRESSION{FRACTION}");
        System.out.println(input1.isPattern(pattern));
        // Equation expected1 = builder.makeEquation("( 2 * 4 ) / 3");
    }
}
