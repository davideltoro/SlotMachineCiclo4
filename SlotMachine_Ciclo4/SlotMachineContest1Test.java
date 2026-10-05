import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Pruebas unitarias para SlotMachineContest1.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SlotMachineContest1Test
{
    @Test
    public void testSolveReturnsWellFormedActions()
    {
        int n = 4;
        int[][] actions = SlotMachineContest1.solve(n);

        assertNotNull(actions);
        for(int[] action : actions) {
            assertEquals(2, action.length);
            assertTrue(action[0] >= 1 && action[0] <= n);
        }
    }

    @Test
    public void testSolveWorksForDifferentSizes()
    {
        // corre solve() para varios tamaños; si el algoritmo se cae o
        // entra en un loop infinito, esta prueba nunca termina/falla
        for(int n = 2; n <= 6; n++) {
            assertNotNull(SlotMachineContest1.solve(n));
        }
    }

    @Test
    public void testSolveWithOneWheelDoesNotCrash()
    {
        int[][] actions = SlotMachineContest1.solve(1);
        assertNotNull(actions);
    }
}