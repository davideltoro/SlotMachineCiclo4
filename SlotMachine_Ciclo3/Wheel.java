/**
 * Una rueda de la máquina tragamonedas. Guarda en qué posición
 * (índice dentro de la lista de símbolos de SlotMachine) está parada,
 * y se representa visualmente como una ventana con un círculo indicador
 * que cambia de color según el símbolo que muestra.
 * 
 * @author Juan David Castellanos - Nicole Paez
 * @version 1.2
 */
public class Wheel
{
    private Symbol current;
    private Rectangle window;
    private Circle indicator;
    private boolean held;
    protected Rectangle mark;
    private Rectangle squareInd;
    private Triangle triInd;
    private boolean visible;
    private int drawnSize;  
    
    
    /**
     * Crea una rueda en la posición 1 por defecto, con su ventana
     * ubicada según el lugar que ocupa entre las demás ruedas.
     * @param slotIndex lugar (1-based) que ocupa esta rueda en la máquina
     */
    public Wheel(int slotIndex)
    {
        current = null;
        int targetX = 40 + (slotIndex - 1) * 50;
        int targetY = 90;

        window = new Rectangle(60,40,targetX,targetY,"white");
        
        int circleX = targetX + (40 - 24) / 2;   // 40 = ancho real de la ventana
        int circleY = targetY + (60 - 24) / 2;   
        indicator = new Circle(24,circleX,circleY,"white");
        
        held=false;
        squareInd = new Rectangle(24, 24, circleX, circleY, "white");
        triInd = new Triangle("white", 24, 24);
        triInd.changePositionXandY(targetX + 20, circleY);   // vértice superior, centrado en la ventana
        drawnSize = 24;
        visible = false;
    }

    /**
     * @return la posición actual de la rueda
     */
    public Symbol getCurrentPosition()
    {
        return current;
    }

    /**
     * Cambia la posición actual de la rueda.
     * @param pos la nueva posición
     */
    public void setCurrentPosition(Symbol s)
    {
        current = s;
        if(s== null){
            indicator.changeColor("white");
            
        }else{
            indicator.changeColor(s.getColor());
        
        }
    }

    /**
     * Hace visible la ventana y el indicador de la rueda.
     */
    public void makeVisible()
    {
        window.makeVisible();
        indicator.makeVisible();
    }

    /**
     * Hace invisible la ventana y el indicador de la rueda.
     */
    public void makeInvisible()
    {
        window.makeInvisible();
        indicator.makeInvisible();
    }
    
    /**
     * @return true si la rueda está fijada (no debe girar)
     */
    public boolean isHeld()
    {
        return held;
    }

    /**
     * Fija o suelta la rueda.
     * @param held true para fijarla, false para soltarla
     */
    public void setHeld(boolean held)
    {
        this.held = held;
    }
    /**
     * Decide en qué símbolo queda la rueda al girar.
     * @param proposed el símbolo en el que quedaría una rueda normal
     * @return el símbolo en el que realmente queda
     */
    public Symbol chooseSymbol(Symbol proposed)
    {
        return proposed;
    }

}