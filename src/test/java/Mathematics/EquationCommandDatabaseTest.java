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
    public void test1() throws Exception {
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
    public void testMultipleNestedFractionRemoval() throws Exception {
        Equation input1 = builder.makeEquation("( 1 / 2 ) / ( 3 / 4 )");
        /*
                1
                -
                2
              -----
                3
                -
                4
               ---
                5
                -
                6
         */
        database.removeNestedFractions.simplifyAll(input1).printTree();
        Equation expected1 = builder.makeEquation("( 4 / 3 ) / ( 2 / 1 )");
        assertEquals(expected1, database.removeNestedFractions.simplifyAll(input1));


    }
}