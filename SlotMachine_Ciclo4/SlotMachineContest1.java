import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Maratón Slot Machine (ciclo 3) y pruebas de aceptación del proyecto.
 * Las pruebas de aceptación son métodos estáticos: corren visibles, con
 * pausas, y escriben en la terminal lo obtenido y lo esperado.
 *
 * @author  Juan Castellanos - Nicole Paez
 * @version 2.2
 */
public class SlotMachineContest1 
{

    /**
     * Resuelve el problema Slot Machine (ciclo 3) Corre de forma invisible.
     *
     * @param n ruedas/símbolos de la máquina.
     * @return acciones {wheel, steps} que llevan la máquina al jackpot.
     */
    public static int[][] solve(int n) {
        try {
            SlotMachine m = new SlotMachine(n);
            return resolver(m, n);
        } catch (SlotMachineException e) {
            return new int[0][];   // n inválido: no hay acciones que devolver
        }
    }

    /**
     *metodo para simular el problema de la maraton en modo visible
     * @param n ruedas/símbolos de la máquina a simular.
     */
    public static void simulate(int n) {
        try {
            SlotMachine m = new SlotMachine(n);
            m.makeVisible();
            resolver(m, n);
        } catch (SlotMachineException e) {
            System.out.println(e.getMessage());   // n inválido: nada que simular
        }
    }

    /**
     * Corre el algoritmo completo sobre una máquina ya creada, y
     * devuelve las acciones que de verdad se aplicaron.
     *
     * @param m máquina ya creada (invisible o visible, no importa).
     * @param n ruedas/símbolos de esa máquina.
     * @return la secuencia de acciones {wheel, steps} aplicadas.
     * @throws SlotMachineException si alguna operación de la máquina falla
     */
    private static int[][] resolver(SlotMachine m, int n) throws SlotMachineException {
        List<int[]> log = new ArrayList<>();

        // Fase 1: dejar todas las ruedas mostrando símbolos distintos.
        for (int w = 1; w <= n; w++) {
            int mejor = m.distinctSymbols();
            int pasoMejor = 0;
            for (int p = 1; p <= n; p++) {
                m.spin(w, 1); log.add(new int[]{w, 1});
                int actual = m.distinctSymbols();
                if (actual > mejor) {
                    mejor = actual; pasoMejor = p;
                }
            }
            if (pasoMejor > 0) {
                m.spin(w, pasoMejor); log.add(new int[]{w, pasoMejor});
            }
        }

        // Fase 2: medir cuánto le falta a cada rueda para alcanzar a la rueda 1
        // (se prueba y se deshace; nadie se mueve de verdad todavía, y por
        // eso nada de esto se anota en el log: no es necesario para ganar).
        int[] gap = new int[n + 1];

        for (int k = 2; k <= n; k++) {
            for (int d = 1; d < n; d++) {
                m.spin(1, d);
                m.spin(k, -d);
                boolean coinciden = m.distinctSymbols() == n; // se intercambiaron , gap encontrado
                m.spin(1, -d);
                m.spin(k, d);
                if (coinciden) {
                    gap[k] = d;
                    break;
                }
            }
        }

        // Fase 3: alinear cada rueda con la rueda 1, de una sola vez.
        for (int k = 2; k <= n; k++) {
            m.spin(k, -gap[k]); log.add(new int[]{k, -gap[k]});
        }

        return log.toArray(new int[0][]);
    }

    /**
     * Prueba de aceptación 1: el usuario arma una máquina y la deja en jackpot.
     * Ciclo 1: crear, colocar, consultar y eliminar.
     * Ciclo 2: swap, lock, spin(set) y spin(pasos).
     */
    public static void acceptanceTest1() {
        System.out.println("=== ACEPTACION 1: ciclos 1 y 2 ===");
        try {
            SlotMachine m = new SlotMachine();
            m.addSymbol(1, "red");
            m.addSymbol(2, "blue");
            m.addSymbol(3, "green");
            m.addWheel(1);
            m.addWheel(2);
            m.addWheel(3);
            m.makeVisible();
            m.placeSymbol(1, "red");
            m.placeSymbol(2, "blue");
            m.placeSymbol(3, "green");
            Canvas.getCanvas().wait(1500);

            System.out.println("Ciclo 1 - symbols: " + Arrays.toString(m.symbols())
                + " (esperado: [red, blue, green])");
            System.out.println("Ciclo 1 - configuration: " + Arrays.toString(m.configuration())
                + " (esperado: [red, blue, green])");
            System.out.println("Ciclo 1 - distinctSymbols: " + m.distinctSymbols()
                + ", jackpot: " + m.isJackpot() + " (esperado: 3, false)");

            m.delWheel(3);
            m.addWheel(3);
            m.placeSymbol(3, "green");
            System.out.println("Ciclo 1 - ruedas tras delWheel + addWheel: "
                + m.configuration().length + " (esperado: 3)");
            try {
                m.placeSymbol(1, "purple");
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 1 - color inexistente: " + e.getMessage());
            }

            m.swap(1, 2);
            System.out.println("Ciclo 2 - swap(1,2): " + Arrays.toString(m.configuration())
                + " (esperado: [blue, red, green])");
            Canvas.getCanvas().wait(1500);

            m.lock(3);
            try {
                m.spin(3, 1);
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 2 - rueda fijada: " + e.getMessage());
            }
            m.spin(new String[]{"red", "red", "red"});
            System.out.println("Ciclo 2 - spin(set) con la rueda 3 fijada: "
                + Arrays.toString(m.configuration()) + " (esperado: [red, red, green])");
            Canvas.getCanvas().wait(1500);

            m.unlock(3);
            m.spin(3, 1);
            System.out.println("Ciclo 2 - tras unlock y spin(3,1), jackpot: " + m.isJackpot()
                + " (esperado: true)");
            Canvas.getCanvas().wait(2500);
            m.exit();
        } catch (SlotMachineException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Prueba de aceptación 2: tipos de ruedas y símbolos (ciclo 4) y solver (ciclo 3).
     * El efímero y el tímido se comprueban mirando la pantalla.
     */
    public static void acceptanceTest2() {
        System.out.println("=== ACEPTACION 2: ciclos 4 y 3 ===");
        try {
            SlotMachine m = new SlotMachine();
            m.addSymbol("normal", 1, "red");
            m.addSymbol("ephemeral", 2, "blue");
            m.addSymbol("normal", 3, "yellow");
            m.addSymbol("shy", 4, "green");
            m.addWheel("normal", 1);
            m.addWheel("lefty", 2);
            m.addWheel("rebel", 3);
            m.addWheel("sleepy", 4);
            m.makeVisible();
            m.placeSymbol(1, "red");
            m.placeSymbol(2, "yellow");
            m.placeSymbol(3, "yellow");
            m.placeSymbol(4, "red");
            Canvas.getCanvas().wait(2000);

            // Rebel (rueda 3): no se deja fijar, intercambiar ni eliminar
            try {
                m.lock(3);
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 4 - rebel lock: " + e.getMessage());
            }
            try {
                m.swap(3, 1);
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 4 - rebel swap: " + e.getMessage());
            }
            try {
                m.delWheel(3);
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 4 - rebel delWheel: " + e.getMessage());
            }
            Canvas.getCanvas().wait(1500);

            // Sleepy (rueda 4): gira, descansa, gira. Orden de simbolos: red, blue, yellow, green
            System.out.println("Ciclo 4 - sleepy parte en red; esperado: blue, blue, yellow");
            for (int i = 1; i <= 3; i++) {
                m.spin(4, 1);
                System.out.println("   giro " + i + ": " + m.configuration()[3]);
                Canvas.getCanvas().wait(800);
            }

            // Lefty (rueda 2): al girar copia a la rueda de su izquierda
            System.out.println("Ciclo 4 - lefty copia a la rueda 1 en cada giro:");
            for (int i = 1; i <= 3; i++) {
                m.spin();
                System.out.println("   giro " + i + ": " + Arrays.toString(m.configuration()));
                Canvas.getCanvas().wait(1000);
            }

            // Ephemeral y shy: se comprueban mirando la pantalla
            System.out.println("Ciclo 4 - OBSERVAR: el triangulo AZUL (ephemeral) se encoge en cada giro");
            m.placeSymbol(1, "blue");
            for (int i = 1; i <= 6; i++) {
                m.spin(1, 4);   // vuelta completa: termina otra vez en blue
                Canvas.getCanvas().wait(400);
            }
            System.out.println("Ciclo 4 - OBSERVAR: el cuadrado VERDE (shy) aparece y desaparece");
            for (int i = 1; i <= 4; i++) {
                m.placeSymbol(1, "green");
                Canvas.getCanvas().wait(1000);
            }
            m.exit();

            // Ciclo 3: constructor de n y solver
            try {
                new SlotMachine(14);
            } catch (SlotMachineException e) {
                System.out.println("Ciclo 3 - SlotMachine(14): " + e.getMessage());
            }
            System.out.println("Ciclo 3 - solve(4): " + Arrays.deepToString(solve(4)));
            System.out.println("Ciclo 3 - simulacion visible con n = 4 (termina en jackpot)");
            simulate(4);
        } catch (SlotMachineException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}