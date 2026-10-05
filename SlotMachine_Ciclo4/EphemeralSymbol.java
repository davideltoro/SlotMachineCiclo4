/**
 * Símbolo efímero: en cada giro de la máquina reduce su tamaño hasta
 * quedar como un punto. Se dibuja como un triángulo para distinguirlo.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class EphemeralSymbol extends Symbol
{
    private int size;

    /**
     * Crea un símbolo efímero con el color dado y su tamaño inicial.
     * @param color el color del símbolo (nombre CSS, ej. "red")
     */
    public EphemeralSymbol(String color)
    {
        super(color);
        size = 30;
    }

    /**
     * En cada giro reduce su tamaño en 2, hasta quedar como un punto
     * de tamaño 4 (no desaparece).
     */
    @Override
    public void onSpin()
    {
        if(size > 4) {
            size = size - 2;
        }
    }

    /**
     * @return el tamaño actual del símbolo
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * @return "triangle": el efímero se dibuja como triángulo
     */
    @Override
    public String getShape()
    {
        return "triangle";
    }
}
