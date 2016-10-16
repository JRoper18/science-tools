import SigFigs.SigFigNumber;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jack on 9/23/2016.
 */
public class SigFigNumberTest {
    private SigFigNumber test1 = new SigFigNumber("20");
    private SigFigNumber test2 = new SigFigNumber("20.0");
    private SigFigNumber test3 = new SigFigNumber("21");
    private SigFigNumber test4 = new SigFigNumber("21.001");

    @Test
    public void countSigFigs() throws Exception {
        assertEquals("Sig figs should count correctly", 2, test1.numOfSigFigs);
        assertEquals("Sig figs should count correctly", 3, test2.numOfSigFigs);
        assertEquals("Sig figs should count correctly", 2, test3.numOfSigFigs);
        assertEquals("Sig figs should count correctly", 5, test4.numOfSigFigs);
    }

    @Test
    public void getMathNum() throws Exception {
        assertEquals("Get correct number for math", 20, test1.expandedNum, .001);
        assertEquals("Get correct number for math", 20, test2.expandedNum, .001);
        assertEquals("Get correct number for math", 21, test3.expandedNum, .001);
        assertEquals("Get correct number for math", 21.001, test4.expandedNum, .001);
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals("Add sig figs by rounding to least decimal point", true, test1.add(test2, false).equals(new SigFigNumber("40")));
        assertEquals("Add sig figs by rounding to least decimal point", true, test4.add(test2, false).equals(new SigFigNumber("41.0")));
        assertEquals("Add sig figs by rounding to least decimal point", true, new SigFigNumber("20.001").add(test4, false).equals(new SigFigNumber("41.002")));
        assertEquals("Add sig figs by rounding to least decimal point", true, new SigFigNumber("20.09").add(test4, false).equals(new SigFigNumber("41.09")));
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("Multiply by rounding to least sig fig", true, test1.multiply(test2).equals(new SigFigNumber("4.0", 2)));
    }
}