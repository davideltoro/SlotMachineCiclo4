/**
 * Rueda rebelde: no se deja bloquear, ni intercambiar, ni eliminar.
 * Hereda de Wheel la ventana, el indicador y el símbolo actual.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class RebelWheel extends Wheel
{
    /**
     * Crea una rueda rebel en el lugar indicado.
     * @param slotIndex lugar (1-based) que ocupa en la máquina
     */
    
    public RebelWheel(int slotIndex)
    {
        super(slotIndex);
        int x = 40 + (slotIndex - 1) * 50;
        mark = new Rectangle(6, 40, x, 153, "yellow");
    }

    /**
     * Una rueda rebelde nunca queda fijada: ignora la orden.
     * Redefine el método heredado de Wheel.
     * @param held se ignora
     */
    @Override
    public void setHeld(boolean held)
    {
        super.setHeld(false);
    }

    /**
     * @return false: no se deja bloquear
     */
    public boolean canLock()
    {
        return false;
    }

    /**
     * @return false: no se deja intercambiar
     */
    public boolean canSwap()
    {
        return false;
    }

    /**
     * @return false: no se deja eliminar
     */
    public boolean canDelete()
    {
        return false;
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