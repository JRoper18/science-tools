package Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jack on 11/5/2016.
 */

public class IdentifierTest {
    EquationBuilder builder = new EquationBuilder();
    @Test
    public void testFraction() throws Exception {
        Equation test1 = builder.makeEquation("1 + 1");
        Equation test2 = builder.makeEquation("1 / 2");
        Equation test3 = builder.makeEquation("4 / 6");
        Equation test4 = builder.makeEquation("( 2 + 3 ) / 2");
        assertEquals(true, test4.isType(EquationType.FRACTION));
        assertEquals(true, test3.isType(EquationType.FRACTION));
    }

    @Test
    public void testIntegerConstant() throws Exception {
        Equation test1 = builder.makeEquation("1");
        Equation test2 = builder.makeEquation("1.5");
        Equation test3 = builder.makeEquation("1.0");
        assertEquals(true, test1.isType(EquationType.INTEGERCONSTANT));
        assertEquals(false, test2.isType(EquationType.INTEGERCONSTANT));
        assertEquals(true, test3.isType(EquationType.INTEGERCONSTANT));

    }

    @Test
    public void testIntegerFraction() throws Exception {
        Equation test1 = builder.makeEquation("2 / 4");
        test1.isType(EquationType.INTEGERFRACTION);

    }
}