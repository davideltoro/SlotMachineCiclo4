import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Simula una máquina tragamonedas con n ruedas y n símbolos posibles,
 * inspirada en el Problema I de la maratón ICPC 2025.
 *
 * Los símbolos se guardan en dos estructuras que trabajan juntas:
 * - symbols: un HashMap indexado por color, porque el color es la identidad
 *   del símbolo y casi todas las operaciones lo buscan por ahí. La búsqueda
 *   es en tiempo constante y evita recorrer el catálogo entero.
 * - order: la lista de colores en el orden en que están en la rueda. El
 *   HashMap no guarda ningún orden, y los requisitos piden que addSymbol
 *   reciba una posición y que symbols() devuelva los colores en orden.
 * Toda operación que agregue o elimine un símbolo debevisi tocar las dos.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.2
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private HashMap<String,Symbol> symbols;
    private ArrayList<String> order;
    private boolean ok;
    private boolean visible;

    private Rectangle body;
    private Rectangle base;
    private Rectangle arm;
    private Circle knob;
    
    
    /**
     * Ciclo 3 
     * Contrustor sobrecargado: para SlotMachineContest
     * @param: n : int : numero de ruedas y simbolos a crear , para la simulacion
     * 
     */
    public SlotMachine(int n){
        buildCasing();
        wheels = new ArrayList<Wheel>();
        symbols = new HashMap<String,Symbol>();
        order = new ArrayList<String>();
 
        String[] PALETTE = {"red", "blue", "yellow", "green", "orange", "magenta",
        "cyan", "black", "darkGray", "lightGray", "pink", "white","gray"};
        if(n<=13){
            for (int i=1; i<=n; i++){
                addWheel(i);
                addSymbol(i,PALETTE[i-1]);
            }
            spin();
        }else{
           showError("El numero debe ser menor o igual a cantidad de simbolos permitidos(13)");
           ok=false;
        }
        visible = false;
    }
    /**
     * Crea una máquina tragamonedas vacía (sin ruedas ni símbolos)
     * junto con su representación visual.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new HashMap<String,Symbol>();
        order = new ArrayList<String>();
        ok = true;
        visible = false;
        buildCasing();
    }

    /**
     * Construye y posiciona las figuras que forman la carcasa
     * de la máquina (cuerpo, base y palanca).
     */
    private void buildCasing()
    {
        body = new Rectangle(150,220,30,50,"blue");
        base = new Rectangle(20,160,50,200,"black");
        arm = new Rectangle(50,10,260,60,"black");
        knob = new Circle(30,250,30,"red");
    }
    

    /**
     * Agrega una rueda nueva en la posición indicada (1-based).
     * @param pos posición donde insertar la rueda
     */
    public void addWheel(int pos)
    {
        int difference;
        ok = false;
        int index = fixPosition(pos, wheels.size() + 1);
        
        Wheel w = new Wheel(index);
         if(index <= wheels.size()) {
            Wheel oldWheel = wheels.set(index - 1, w);
            oldWheel.makeInvisible();
        }
        else {
            wheels.add(w);
        }
        if(wheels.size()>4){
            difference=(wheels.size()-4)*50;
            body.changeSize(150,220+difference);
            base.changeSize(20,160+difference);
            if(wheels.size()>=8){
                arm.moveHorizontal((wheels.size()-4)*15);
                knob.moveHorizontal((wheels.size()-4)*15);
            }else{
            arm.moveHorizontal((wheels.size()-4)*25);
            knob.moveHorizontal((wheels.size()-4)*25);
            }
        }
        if(visible) {
            w.makeVisible();
        }
        ok = true;
    }

    /**
     * Elimina la rueda en la posición indicada (1-based).
     * @param pos posición de la rueda a eliminar
     */
    public void delWheel(int pos){
        ok = false;
        if(wheels.isEmpty()) {
            ok = false;
            showError("No hay ruedas para eliminar");
            return;
        }
        int index = fixPosition(pos, wheels.size());
        Wheel w = wheels.remove(index - 1);
        w.makeInvisible();
        ok = true;
    }

    /**
     * Agrega un símbolo nuevo del color dado en la posición indicada.
     * @param pos posición donde insertar el símbolo
     * @param color color del nuevo símbolo
     */
    public void addSymbol(int pos, String color){
        ok = false;
        if (symbols.containsKey(color)){
            ok = false;
            showError("Ya existe un simbolo con este color");
            return;
        }
        int index = fixPosition(pos, order.size() + 1);
        Symbol s = new Symbol(color);
        symbols.put(color,s);
        order.add(index - 1, color);
        ok = true;
    }

    /**
     * Elimina el primer símbolo que tenga el color indicado.
     * @param symbol color del símbolo a eliminar
     */
        public void delSymbol(String symbol)
    {
        ok = false;
        Symbol deleted = symbols.remove(symbol);
        if(deleted == null) {
            ok = false;
            showError("El simbolo a eliminar no existe");
            return;
        }
        order.remove(symbol);
        for(Wheel w : wheels) {
            if(w.getCurrentPosition() == deleted) {
                w.setCurrentPosition(null);
            }
        }
        ok = true;
    }

    /**
     * Hace visible la máquina completa: la carcasa y cada una de sus ruedas.
     */
    public void makeVisible(){
        body.makeVisible();
        base.makeVisible();
        arm.makeVisible();
        knob.makeVisible();
    for(Wheel w : wheels) {
        w.makeVisible();
        if(w.getCurrentPosition() == null) {
            w.setCurrentPosition(randomSymbol());
            }
        }
        visible = true;
        ok = true;
    }

    /**
     * Hace invisible la máquina completa: la carcasa y cada una de sus ruedas.
     */
    public void makeInvisible()
    {
        body.makeInvisible();
        base.makeInvisible();
        arm.makeInvisible();
        knob.makeInvisible();
        for(Wheel w : wheels) {
            w.makeInvisible();
        }
        visible = false;
        ok = true;
    }

    /**
     * Termina el simulador, ocultando toda su representación visual.
     */
    public void exit()
    {
        makeInvisible();
    }

    /**
     * @return true si la última operación se realizó con éxito
     */
    public boolean ok()
    {
        return ok;
    }

        /**
     * Ubica el símbolo del color indicado en la rueda indicada.
     * @param wheel posición de la rueda (1-based)
     * @param symbol color del símbolo a colocar
     */
    public void placeSymbol(int wheel, String symbol)
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            showError("No hay ruedas o símbolos disponibles.");
            return;
        }
        Symbol s = symbols.get(symbol);
        if(s == null) {
            ok = false;
            showError("No existe un símbolo de color \"" + symbol + "\".");
            return;
        }
        int wIndex = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(wIndex - 1);
        w.setCurrentPosition(s);
        changesJackpot();
        ok = true;
    }
    
    /**
     * Gira la rueda indicada, dejándola en un símbolo aleatorio del catálogo.
     * @param wheel posición de la rueda a girar (1-based)
     */
    public void spin(int wheel)
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            showError("No hay ruedas o símbolos para girar.");
            return;
        }
        int wIndex = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(wIndex - 1);
        w.setCurrentPosition(randomSymbol());
        changesJackpot();
        ok = true;
    }
    /**
     * Escoge un symbolo random del catalogo de symbols
     */
    private Symbol randomSymbol(){
    
        if(order.isEmpty()) {
            return null;
        }
        int target = (int)(Math.random() * order.size());
        return symbols.get(order.get(target));
    }
     
    /**
     * Gira todas las ruedas de la máquina./Preguntar a la profe
     */
    public void spin()
    {

        for(int i = 1; i <= wheels.size(); i++) {
            spin(i);
        }
    }
    
    /**
     * Busca saber los colores de los simbolos creados, en el orden en que
     * estan en la rueda empezando por el 1.
     * @return los colores de los simbolos
     */
    public String[] symbols(){
        ok=true;
        return order.toArray(new String[0]);
    }
    /**
     * Busca saber que simbolo esta en cada rueda 
     * @return positions los colores de los simbolos de cada rueda
     */
    public String [] configuration(){
        ok = false;
        if(symbols.isEmpty()) {
            showError("No hay símbolos disponibles.");
            return new String[0];
            }
        String[] positions = new String[wheels.size()];
        for (int i=0;i< wheels.size();i++){
            Symbol s = wheels.get(i).getCurrentPosition();
            if (s!= null){
                positions[i]= s.getColor();
                }
            else{positions[i]= "";}
        }
        ok = true;
        return positions;
    }
    
    /**
     * Cuenta cuántos colores distintos hay actualmente entre las ruedas.
     * @return el número de símbolos distintos visibles en la máquina
     */
    public int distinctSymbols(){
        ok = false;
        String [] positions = configuration();
        ArrayList<String> diferent = new ArrayList<String>();
        for (int i=0;i< positions.length;i++){
            if (!positions[i].isEmpty() && !diferent.contains(positions[i])){
                diferent.add(positions[i]);
            }
        }
        ok = true;
        return diferent.size();
        
        }

    /**
     * Indica si la máquina está en configuración ganadora.
     * @return true si todas las ruedas muestran el mismo símbolo
     */
    public boolean isJackpot(){
        ok = false;
        String[] positions = configuration();
        if(positions.length == 0) {
            return false;
        }
        for(String color : positions){
            if(color.isEmpty() || !color.equals(positions[0])){
            return false;
        }
        }
        return true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 5: Intercambiar ruedas
     * Intercambia los símbolos que muestran dos ruedas.
     * @param wheel1 posición de la primera rueda (1-based)
     * @param wheel2 posición de la segunda rueda (1-based)
     */
    public void swap(int wheel1, int wheel2)
    {
        if(wheels.size() < 2) {
            ok = false;
            showError("Se necesitan al menos dos ruedas para intercambiar.");
            return;
        }
        int i1 = fixPosition(wheel1, wheels.size());
        int i2 = fixPosition(wheel2, wheels.size());
        Wheel w1 = wheels.get(i1 - 1);
        Wheel w2 = wheels.get(i2 - 1);

        Symbol temp = w1.getCurrentPosition();
        w1.setCurrentPosition(w2.getCurrentPosition());
        w2.setCurrentPosition(temp);

        changesJackpot();
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 6: Fijar y soltar una rueda
     * Fija una rueda para que no gire hasta que se suelte.
     * @param wheel posición de la rueda a fijar (1-based)
     */
    public void lock(int wheel)
    {
        if(wheels.isEmpty()) {
            ok = false;
            showError("No hay ruedas para fijar.");
            return;
        }
        int index = fixPosition(wheel, wheels.size());
        wheels.get(index - 1).setHeld(true);
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 6: Fijar y soltar una rueda
     * Suelta una rueda previamente fijada.
     * @param wheel posición de la rueda a soltar (1-based)
     */
    public void unlock(int wheel)
    {
        if(wheels.isEmpty()) {
            ok = false;
            showError("No hay ruedas para soltar.");
            return;
        }
        int index = fixPosition(wheel, wheels.size());
        wheels.get(index - 1).setHeld(false);
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 7: Rotar una rueda n pasos
     * Sobrecarga de spin: rota una rueda un número de pasos dentro de la
     * lista de símbolos, dando la vuelta si se pasa del final (o del
     * inicio, si steps es negativo). No hace nada si la rueda está fijada.
     * @param wheel posición de la rueda a rotar (1-based)
     * @param steps número de pasos a avanzar (puede ser negativo)
     */
    public void spin(int wheel, int steps)
    {
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            showError("No hay ruedas o símbolos para rotar.");
            return;
        }
        int index = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(index - 1);
        if(w.isHeld()) {
            ok = false;
            showError("La rueda está fijada, suéltela antes de rotar.");
            return;
        }
        
        Symbol currentSymbol= w.getCurrentPosition();
        int size = order.size();
        int start = 0;
        if(currentSymbol != null) {
            start = order.indexOf(currentSymbol.getColor());
            if(start < 0) {
            start = 0;
            }
        }
        
        int direction = (steps < 0) ? -1 : 1;
        for(int i = 0; i < Math.abs(steps); i++) {
            start = (start + direction + size) % size;   // +size basta: direction es -1 o 1
            w.setCurrentPosition(symbols.get(order.get(start)));
            if(visible) {
            Canvas.getCanvas().wait(120);
            }
        }
        changesJackpot();
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 8: Dejar la máquina en una configuración dada
     * Sobrecarga de spin: deja la máquina en la configuración dada, un
     * color por cada rueda, en el mismo orden de las ruedas. Las ruedas
     * fijadas no se modifican.
     * @param setSymbols arreglo con el color deseado para cada rueda
     */
    public void spin(String[] setSymbols)
    {
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            showError("No hay ruedas o símbolos disponibles.");
            return;
        }
        if(setSymbols.length != wheels.size()) {
            ok = false;
            showError("La cantidad de colores no coincide con la cantidad de ruedas.");
            return;
        }
        for(int i = 0; i < setSymbols.length; i++) {
            if(!symbols.containsKey(setSymbols[i])) {
                ok = false;
                showError("No existe un símbolo de color \"" + setSymbols[i] + "\".");
                return;
            }
        }

        for(int i = 0; i < setSymbols.length; i++) {
            Wheel w = wheels.get(i);
            if(!w.isHeld()) {
                Symbol sIndex = symbols.get(setSymbols[i]);
                w.setCurrentPosition(sIndex);
            }
        }
        changesJackpot();
        ok = true;
    }
        /**
     * Muestra un mensaje de error al usuario, únicamente si el
     * simulador está visible.
     * @param message el mensaje a mostrar
     */
    private void showError(String message)
    {
        if(visible) {
            JOptionPane.showMessageDialog(null, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }   
    /**
     * Ajusta una posición 1-based al rango válido [1, max].
     * @param pos posición solicitada
     * @param max valor máximo permitido
     * @return la posición corregida
     */
    private int fixPosition(int pos, int max)
    {
        if(pos < 1) {
            return 1;
        }
        if(pos > max) {
            return max;
        }
        return pos;
    }
    /**
     * Actualiza el color de la máquina según si hay jackpot, y
     * redibuja las ruedas para que queden por encima del cuerpo.
     */
    private void changesJackpot(){
        if(isJackpot()) {
            body.changeColor("green");
        } else {
            body.changeColor("blue");
        }
        if(visible) {
            for(Wheel w : wheels) {
                w.makeVisible();  
                }
            }
            
        }
    
    }