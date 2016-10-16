import Chemistry.AtomicElement;
import SigFigs.SigFigNumber;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ulysses Howard Smith on 9/28/2016.
 */
public class AtomicElementTest {
    private AtomicElement nitrite = new AtomicElement("nitrite");
    private AtomicElement oxygen_gas = new AtomicElement("oxygen");

    @Test
    public void testApiData() throws Exception {
        assertEquals("NO2-", nitrite.molecularFormula);
        assertEquals("O2", oxygen_gas.molecularFormula);
    }
}
