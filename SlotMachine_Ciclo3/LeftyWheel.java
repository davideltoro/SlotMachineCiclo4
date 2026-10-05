import java.util.ArrayList;

/**
 * Rueda lefty: si hay una rueda a su izquierda, al girar copia su estado.
 * Hereda de Wheel la ventana, el indicador, el bloqueo y el símbolo actual.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class LeftyWheel extends Wheel
{
    /**
     * Crea una rueda lefty en el lugar indicado.
     * 
     * @param slotIndex lugar (1-based) que ocupa en la máquina
     */
    private ArrayList<Wheel>wheels;
    
    public LeftyWheel(int slotIndex, ArrayList<Wheel> wheels)
    {
        super(slotIndex);
        this.wheels = wheels;
        int x = 40 + (slotIndex - 1) * 50;
        mark = new Rectangle(6, 40, x, 153, "cyan");
    }
    
    @Override
    public Symbol chooseSymbol(Symbol proposed)
    {
        int index = wheels.indexOf(this);
        if(index > 0 && wheels.get(index - 1).getCurrentPosition() != null) {
        return wheels.get(index - 1).getCurrentPosition();
        }
        return proposed;   // sin vecina a la izquierda: gira normal
    }
    @Override
    public void makeVisible()
    {
        super.makeVisible();
        mark.makeVisible();
    }
 
    /**
     * Hace invisible la rueda y su franja.
     */
    @Override
    public void makeInvisible()
    {
        super.makeInvisible();
        mark.makeInvisible();
    }
}