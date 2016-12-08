package Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jack on 12/2/2016.
 */
public class EquationCommandDatabaseTest {
    EquationCommandDatabase database = new EquationCommandDatabase();
    EquationBuilder builder = new EquationBuilder();

    @Test
    public void testConstantPlusConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 + 1");
        Equation expected1 = builder.makeEquation("2");
        Equation input2 = builder.makeEquation("2.343 + 5.4");
        Equation expected2 = builder.makeEquation("7.743");
        assertEquals(expected1, database.constantsAddition.simplify(input1));
        assertEquals(expected2, database.constantsAddition.simplify(input2));
    }
    @Test
    public void testConstantMinusConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 - 1");
        Equation expected1 = builder.makeEquation("0");
        Equation input2 = builder.makeEquation("2 - 1");
        Equation expected2 = builder.makeEquation("1");
        Equation input3 = builder.makeEquation("2 - 3");
        Equation expected3 = builder.makeEquation("-1");

        assertEquals(expected1, database.constantsSubtraction.simplify(input1));
        assertEquals(expected2, database.constantsSubtraction.simplify(input2));
        assertEquals(expected3, database.constantsSubtraction.simplify(input3));
    }

    @Test
    public void testConstantTimesConstant() throws Exception {
        Equation input1 = builder.makeEquation("1 * 1");
        Equation expected1 = builder.makeEquation("1");
        Equation input2 = builder.makeEquation("2 * 2");
        Equation expected2 = builder.makeEquation("4");
        Equation input3 = builder.makeEquation("-2 * 3");
        Equation expected3 = builder.makeEquation("-6");
        assertEquals(expected1, database.constantsMultiplication.simplify(input1));
        assertEquals(expected2, database.constantsMultiplication.simplify(input2));
        assertEquals(expected3, database.constantsMultiplication.simplify(input3));
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
        assertEquals(expected1, database.constantsDivision.simplify(input1));
        assertEquals(expected2, database.constantsDivision.simplify(input2));
        assertEquals(expected3, database.constantsDivision.simplify(input3));
        assertEquals(input4, database.constantsDivision.simplify(input4));
    }
    @Test
    public void testSingleNestedFractionRemoval() throws Exception {
        Equation input1 = builder.makeEquation("2 / ( 3 / 4 )");
        Equation input2 = builder.makeEquation("( 1 / 3 ) / 4");
        Equation expected1 = builder.makeEquation("2 * ( 4 / 3 )");
        Equation expected2 = builder.makeEquation("1 / ( 3 * 4 )");
        assertEquals(expected1, database.removeNestedFractions.simplify(input1));
        assertEquals(expected2, database.removeNestedFractions.simplify(input2));
    }

    @Test
    public void testConstantCondensing() throws Exception {
        Equation input1 = builder.makeEquation("( 1 + 2 ) - 3");
        Equation input2 = builder.makeEquation("( 1 * 2 ) - ( 4 + ( 3 * 6 ) )");
        Equation input3 = builder.makeEquation("( 2 + 3 ) - ( 2 / 4 )");
        database.condenceConstants.simplifyAll(input3).printTree();
        Equation expected1 = builder.makeEquation("0");
        Equation expected2 = builder.makeEquation("-20");
        assertEquals(expected1, database.condenceConstants.simplifyAll(input1));
        assertEquals(expected2, database.condenceConstants.simplifyAll(input2));
    }

    @Test
    public void testGCDInts() throws Exception {
        Equation input1 = builder.makeEquation("GCD 45 15 25 10 10000");
        Equation expected1 = builder.makeEquation("5");
        Equation input2 = builder.makeEquation("GCD 9000000 10000");
        Equation expected2 = builder.makeEquation("10000");
        assertEquals(expected1, database.gcdInts.simplify(input1));
        assertEquals(expected2, database.gcdInts.simplify(input2));
    }

    @Test
    public void testLCMInts() throws Exception {
        Equation input1 = builder.makeEquation("LCM 2 3 5");
        Equation expected1 = builder.makeEquation("30");
        Equation input2 = builder.makeEquation("LCM 1 3 6 2");
        Equation expected2 = builder.makeEquation("6");
        assertEquals(expected1, database.lcmInts.simplify(input1));
        assertEquals(expected2, database.lcmInts.simplify(input2));
    }

    @Test
    public void testIntegerFractionSimplification() throws Exception {
        Equation input1 = builder.makeEquation("4 / 6");
        Equation expected1 = builder.makeEquation("2 / 3");
        Equation input2 = builder.makeEquation("90000 / 10000");
        Equation expected2 = builder.makeEquation("9 / 1");
        assertEquals(expected1, database.integerFractionSimplify.simplify(input1));
        assertEquals(expected2, database.integerFractionSimplify.simplify(input2));
    }

    @Test
    public void testDecimalToFraction() throws Exception {
        Equation input1 = builder.makeEquation(".023");
        Equation expected1 = builder.makeEquation("23 / 1000");
        Equation input2 = builder.makeEquation("34.2");
        Equation expected2 = builder.makeEquation("342 / 10");
        assertEquals(expected1, database.decimalToFraction.simplify(input1));
        assertEquals(expected2, database.decimalToFraction.simplify(input2));
    }
}