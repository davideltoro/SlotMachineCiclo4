import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Pruebas del ciclo 4: tipos de rueda (lefty, rebel y sleepy) y símbolo
 * ephemeral. Todas en modo
 * invisible.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SlotMachineC4Test
{
    private SlotMachine machine;

    /**
     * Crea una máquina con tres símbolos (red, blue, green) y sin ruedas.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    /**
     * Libera la referencia después de cada prueba.
     */
    @After
    public void tearDown()
    {
        machine = null;
    }

    // ---------------- Lo que sí debe hacer ----------------

    @Test
    public void shouldAddWheelsOfEveryType()
    {
        machine.addWheel("normal", 1);
        assertTrue(machine.ok());
        machine.addWheel("lefty", 2);
        assertTrue(machine.ok());
        machine.addWheel("rebel", 3);
        assertTrue(machine.ok());
        machine.addWheel("sleepy", 4);
        assertTrue(machine.ok());
        assertEquals(4, machine.configuration().length);
    }

    @Test
    public void shouldKeepOldAddWheelAsNormal()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldMakeLeftyCopyLeftWheelWhenSpinning()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "blue");
        for(int i = 0; i < 5; i++) {
            machine.spin(2);
            assertEquals("blue", machine.configuration()[1]);
        }
    }

    @Test
    public void shouldMakeLeftyCopyLeftWheelWhenRotatingSteps()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        machine.placeSymbol(1, "green");
        machine.spin(2, 1);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[1]);
    }

    @Test
    public void shouldMakeLeftyCopyNewSymbolWhenSpinningAll()
    {
        machine.addWheel(1);
        machine.addWheel("lefty", 2);
        for(int i = 0; i < 10; i++) {
            machine.spin();
            String[] config = machine.configuration();
            assertEquals(config[0], config[1]);
        }
    }

    @Test
    public void shouldMakeFirstLeftySpinAsNormal()
    {
        machine.addWheel("lefty", 1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldLetRebelSpin()
    {
        machine.addWheel("rebel", 1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldMakeSleepyRestEveryOtherSpin()
    {
        machine.addWheel("sleepy", 1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
        machine.spin(1, 1);
        assertEquals("green", machine.configuration()[0]);
    }

    @Test
    public void shouldLockSleepy()
    {
        machine.addWheel("sleepy", 1);
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldAddEphemeralSymbol()
    {
        machine.addSymbol("ephemeral", 4, "yellow");
        assertTrue(machine.ok());
        assertEquals("yellow", machine.symbols()[3]);
    }

    @Test
    public void shouldShrinkEphemeralUntilPoint()
    {
        EphemeralSymbol s = new EphemeralSymbol("red");
        assertEquals(30, s.getSize());
        s.onSpin();
        assertEquals(28, s.getSize());
        for(int i = 0; i < 50; i++) {
            s.onSpin();
        }
        assertEquals(4, s.getSize());
    }

    @Test
    public void shouldKeepJackpotWithEphemeralAsPoint()
    {
        SlotMachine m = new SlotMachine();
        m.addSymbol("ephemeral", 1, "red");
        m.addSymbol(2, "blue");
        m.addSymbol(3, "green");
        m.addWheel(1);
        m.addWheel(2);
        m.placeSymbol(1, "red");
        m.placeSymbol(2, "red");
        for(int i = 0; i < 20; i++) {
            m.spin(1, 3);   // una vuelta completa: vuelve a "red"
        }
        assertTrue(m.isJackpot());
        assertEquals(1, m.distinctSymbols());
    }

    // ---------------- Lo que no debe hacer ----------------

    @Test
    public void shouldNotAddUnknownWheelType()
    {
        machine.addWheel("loca", 1);
        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    @Test
    public void shouldNotAddUnknownSymbolType()
    {
        machine.addSymbol("raro", 1, "yellow");
        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    @Test
    public void shouldNotLockRebel()
    {
        machine.addWheel("rebel", 1);
        machine.lock(1);
        assertFalse(machine.ok());
        machine.spin(1, 1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotSwapRebel()
    {
        machine.addWheel(1);
        machine.addWheel("rebel", 2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    @Test
    public void shouldNotDeleteRebel()
    {
        machine.addWheel("rebel", 1);
        machine.delWheel(1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }
}
