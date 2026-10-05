import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Pruebas unitarias básicas para la clase SlotMachine.
 * 
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SlotMachineTest
{
    private SlotMachine machine;

    /**
     * Crea una máquina nueva antes de cada prueba (sin hacerla visible,
     * para no abrir ventanas durante las pruebas).
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
    }

    /**
     * Libera la referencia después de cada prueba.
     */
    @After
    public void tearDown()
    {
        machine = null;
    }

    @Test
    public void testAddWheel()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
    }

    @Test
    public void testDelWheelOnEmptyFails()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void testAddSymbol()
    {
        machine.addSymbol(1,"red");
        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
    }

    @Test
    public void testAddDuplicateSymbolFails()
    {
        machine.addSymbol(1,"red");
        machine.addSymbol( 1,"red");
        assertFalse(machine.ok());
    }

    @Test
    public void testDelSymbolNotFoundFails()
    {
        machine.delSymbol("red");
        assertFalse(machine.ok());
    }

    @Test
    public void testIsJackpotWhenAllSame()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1,"red");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.isJackpot());
    }

    @Test
    public void testIsNotJackpotWhenDifferent()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1,"red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());
        assertEquals(2, machine.distinctSymbols());
    }

    /**
     * Ciclo 4
     * Agrega a la máquina tres símbolos normales: red, blue y green.
     */
    private void addThreeSymbols()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    // ================= Ciclo 4: tipos de rueda y de símbolo =================

    // ---------------- Lo que sí debe hacer ----------------

    @Test
    public void shouldAddWheelsOfEveryType()
    {
        addThreeSymbols();
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
        addThreeSymbols();
        machine.addWheel(1);
        assertTrue(machine.ok());
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldMakeLeftyCopyLeftWheelWhenSpinning()
    {
        addThreeSymbols();
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
        addThreeSymbols();
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
        addThreeSymbols();
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
        addThreeSymbols();
        machine.addWheel("lefty", 1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldLetRebelSpin()
    {
        addThreeSymbols();
        machine.addWheel("rebel", 1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    @Test
    public void shouldMakeSleepyRestEveryOtherSpin()
    {
        addThreeSymbols();
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
        addThreeSymbols();
        machine.addWheel("sleepy", 1);
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldAddEphemeralSymbol()
    {
        addThreeSymbols();
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

    @Test
    public void shouldAddShySymbol()
    {
        addThreeSymbols();
        machine.addSymbol("shy", 1, "yellow");
        assertTrue(machine.ok());
        assertEquals("yellow", machine.symbols()[0]);
    }

    @Test
    public void shouldToggleShyEverySelection()
    {
        ShySymbol s = new ShySymbol("yellow");
        assertTrue(s.isShown());
        s.onSelected();
        assertFalse(s.isShown());
        s.onSelected();
        assertTrue(s.isShown());
        s.onSelected();
        assertFalse(s.isShown());
    }

    @Test
    public void shouldKeepConfigurationWithShyHidden()
    {
        addThreeSymbols();
        machine.addSymbol("shy", 4, "yellow");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "yellow");
        machine.placeSymbol(2, "yellow");
        assertEquals("yellow", machine.configuration()[0]);
        assertEquals("yellow", machine.configuration()[1]);
        assertTrue(machine.isJackpot());
    }

    // ---------------- Lo que no debe hacer ----------------

    @Test
    public void shouldNotAddUnknownWheelType()
    {
        addThreeSymbols();
        machine.addWheel("loca", 1);
        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    @Test
    public void shouldNotAddUnknownSymbolType()
    {
        addThreeSymbols();
        machine.addSymbol("raro", 1, "yellow");
        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    @Test
    public void shouldNotLockRebel()
    {
        addThreeSymbols();
        machine.addWheel("rebel", 1);
        machine.lock(1);
        assertFalse(machine.ok());
        machine.spin(1, 1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotSwapRebel()
    {
        addThreeSymbols();
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
        addThreeSymbols();
        machine.addWheel("rebel", 1);
        machine.delWheel(1);
        assertFalse(machine.ok());
        assertEquals(1, machine.configuration().length);
    }
}
