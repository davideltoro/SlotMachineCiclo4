import java.util.ArrayList;
import java.util.List;
/**
 * The test class SlotMachineContest1.
 *
 * @author  Juan Castellanos - Nicole Paez
 * @version (a version number or a date)
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
        SlotMachine m = new SlotMachine(n);
        return resolver(m, n);
    }

    /**
     *metodo para simular el problema de la maraton en modo visible
     * @param n ruedas/símbolos de la máquina a simular.
     */
    public static void simulate(int n) {
        SlotMachine m = new SlotMachine(n);
        m.makeVisible();
        resolver(m, n);
    }

    /**
     * Corre el algoritmo completo sobre una máquina ya creada, y
     * devuelve las acciones que de verdad se aplicaron.
     *
     * @param m máquina ya creada (invisible o visible, no importa).
     * @param n ruedas/símbolos de esa máquina.
     * @return la secuencia de acciones {wheel, steps} aplicadas.
     */
    private static int[][] resolver(SlotMachine m, int n) {
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
}