package Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jack on 11/5/2016.
 */

public class IdentifierTest {
    EquationBuilder builder = new EquationBuilder();
    @Test
    public void testConstantPlusConstant() throws Exception {
        Equation test1 = builder.makeEquation("1 + 1");
        Equation test2 = builder.makeEquation("1 + 2");
        Equation test3 = builder.makeEquation("1 * 2");

        assertEquals(true, test1.isType(EquationType.CONSTANTPLUSCONSTANT));
        assertEquals(true, test2.isType(EquationType.CONSTANTPLUSCONSTANT));
        assertEquals(false, test3.isType(EquationType.CONSTANTPLUSCONSTANT));
    }
}