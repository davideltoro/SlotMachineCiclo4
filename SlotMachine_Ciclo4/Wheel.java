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
    private int centerX;
    private int centerY;
    
    
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
        triInd = new Triangle(24, 24, targetX + 20, circleY, "white");   // punta arriba, centrada en la ventana
        drawnSize = 24;
        visible = false;
        centerX = targetX + 20;   // centro de la ventana (40 de ancho)
        centerY = targetY + 30;   // centro de la ventana (60 de alto)
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
        paint();
    }

    /**
     * Hace visible la ventana y el indicador de la rueda.
     */
    public void makeVisible()
    {
        window.makeVisible();
        visible = true;
        paint();
    }

    /**
     * Hace invisible la ventana y el indicador de la rueda.
     */
    public void makeInvisible()
    {
        window.makeInvisible();
        visible = false;
        paint();
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

    /**
     * Ciclo 4
     * Dibuja el símbolo actual dentro de la ventana con su forma y su
     * tamaño: un círculo para el símbolo normal y un triángulo para el
     * efímero. Si la rueda está invisible, solo oculta las figuras.
     */
    private void paint()
    {
        indicator.makeInvisible();
        triInd.makeInvisible();
        squareInd.makeInvisible();
        if(!visible) {
            return;
        }
        if(current == null) {
            drawCircle("white", 24);
        } else if (!current.isShown()){
             return;
        } else if(current.getShape().equals("triangle")) {
            drawTriangle(current.getColor(), current.getSize());
        }else if (current.getShape().equals("square")){
            drawSquare(current.getColor(), current.getSize());
        }else {
            drawCircle(current.getColor(), current.getSize());
        }
    }

    /**
     * Dibuja un círculo centrado en la ventana.
     * @param color color del círculo
     * @param size diámetro del círculo
     */
    private void drawCircle(String color, int size)
    {
        indicator = new Circle(size, centerX - size / 2, centerY - size / 2, color);
        indicator.makeVisible();
    }

    /**
     * Dibuja un triángulo centrado en la ventana.
     * @param color color del triángulo
     * @param size alto y ancho del triángulo
     */
    private void drawTriangle(String color, int size)
    {
        triInd = new Triangle(size, size, centerX, centerY - size / 2, color);
        triInd.makeVisible();
    }
    /**
     * Dibuja un cuadrado centrado en la ventana.
     * @param color color del cuadrado
     * @param size lado del cuadrado
     */
    private void drawSquare(String color, int size)
    {
        squareInd = new Rectangle(size, size, centerX - size / 2, centerY - size / 2, color);
        squareInd.makeVisible();
    }
    
    /**
     * Ciclo 4
     * Indica si la rueda gira cuando se le pide. Una rueda normal
     * siempre gira; las ruedas de otros tipos pueden redefinirlo.
     * @return true si gira en esta petición
     */
    public boolean wantsToSpin()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja fijar.
     * @return true: una rueda normal se deja fijar
     */
    public boolean canLock()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja intercambiar con otra.
     * @return true: una rueda normal se deja intercambiar
     */
    public boolean canSwap()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja eliminar de la máquina.
     * @return true: una rueda normal se deja eliminar
     */
    public boolean canDelete()
    {
        return true;
    }

}