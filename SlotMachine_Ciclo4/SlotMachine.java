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
     * @param n numero de ruedas y simbolos a crear, para la simulacion
     * @throws SlotMachineException si n supera la cantidad de simbolos permitidos (13)
     */
    public SlotMachine(int n) throws SlotMachineException{
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
           ok = false;
           throw new SlotMachineException(SlotMachineException.MAX_SYMBOLS);
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
     * @throws SlotMachineException nunca en la práctica: el tipo "normal" siempre existe
     */
    public void addWheel(int pos) throws SlotMachineException
    {
        addWheel("normal", pos);
    }
    /**
     * Ciclo 4
     * Agrega una rueda del tipo indicado en la posición indicada (1-based).
     * @param type tipo de rueda: "normal", "lefty", "rebel" o "sleepy"
     * @param pos posición donde insertar la rueda
     * @throws SlotMachineException si el tipo de rueda no existe
     */
    public void addWheel(String type, int pos) throws SlotMachineException
    {
        int difference;
        ok = false;
        int index = fixPosition(pos, wheels.size() + 1);
 
        Wheel w = newWheel(type, index);
        if(w == null) {
            throw new SlotMachineException(SlotMachineException.WHEEL_TYPE_UNKNOWN + type);
        }
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
     * Ciclo 4
     * Crea una rueda del tipo indicado.
     * @param type tipo de rueda: "normal", "lefty", "rebel" o "sleepy"
     * @param index lugar (1-based) que ocupará la rueda
     * @return la rueda creada, o null si el tipo no existe
     */
    private Wheel newWheel(String type, int index)
    {
        String lower = type.toLowerCase();
        if(lower.equals("normal")) {
            return new Wheel(index);
        } else if(lower.equals("lefty")) {
            return new LeftyWheel(index, wheels);
        } else if(lower.equals("rebel")) {
            return new RebelWheel(index);
        } else if(lower.equals("sleepy")) {
            return new SleepyWheel(index);
        }
        return null;
    }
    /**
     * Elimina la rueda en la posición indicada (1-based).
     * @param pos posición de la rueda a eliminar
     * @throws SlotMachineException si no hay ruedas o la rueda no se deja eliminar
     */
    public void delWheel(int pos) throws SlotMachineException{
        ok = false;
        if(wheels.isEmpty()) {
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_TO_DELETE);
        }
        int index = fixPosition(pos, wheels.size());
        if(!wheels.get(index - 1).canDelete()) {
            throw new SlotMachineException(SlotMachineException.WHEEL_NOT_DELETABLE);
        }
        Wheel w = wheels.remove(index - 1);
        w.makeInvisible();
        ok = true;
    }

    /**
     * Agrega un símbolo nuevo del color dado en la posición indicada.
     * @param pos posición donde insertar el símbolo
     * @param color color del nuevo símbolo
     * @throws SlotMachineException si ya existe un símbolo con ese color
     */
    public void addSymbol(int pos, String color) throws SlotMachineException{
        addSymbol("normal",pos,color);
    }
    /**
     * Ciclo 4
     * Agrega un símbolo del tipo y color dados en la posición indicada.
     * @param type tipo de símbolo: "normal", "ephemeral" o "shy"
     * @param pos posición donde insertar el símbolo
     * @param color color del nuevo símbolo
     * @throws SlotMachineException si el color ya existe o el tipo de símbolo no existe
     */
    public void addSymbol(String type,int pos, String color) throws SlotMachineException{
        ok = false;
        String lower = color.toLowerCase();
        if (symbols.containsKey(lower)){
            throw new SlotMachineException(SlotMachineException.SYMBOL_EXISTS);
        }
        int index = fixPosition(pos, order.size() + 1);
        Symbol s = newSymbol(type,lower);
        if(s == null) {
            throw new SlotMachineException(SlotMachineException.SYMBOL_TYPE_UNKNOWN + type);
        }
        symbols.put(lower,s);
        order.add(index - 1, lower);
        ok = true;
    }
    /**
     * Ciclo 4
     * Crea un simbolo del tipo indicado.
     * @param type tipo de símbolo: "normal", "ephemeral" o "shy"
     * @param color color del símbolo
     * @return el símbolo creado, o null si el tipo no existe
     */
    private Symbol newSymbol(String type, String color)
    {
        String lower = type.toLowerCase();
        if(lower.equals("normal")) {
            return new Symbol(color);
        } else if(lower.equals("ephemeral")) {
            return new EphemeralSymbol(color);
        } else if(lower.equals("shy")) {
            return new ShySymbol(color);
        } 
        return null;
    }
    /**
     * Elimina el primer símbolo que tenga el color indicado.
     * @param symbol color del símbolo a eliminar
     * @throws SlotMachineException si no existe un símbolo con ese color
     */
    public void delSymbol(String symbol) throws SlotMachineException
    {
        ok = false;
        String lower = symbol.toLowerCase();
        Symbol deleted = symbols.remove(lower);
        if(deleted == null) {
            throw new SlotMachineException(SlotMachineException.SYMBOL_NOT_FOUND);
        }
        order.remove(lower);
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
     * @throws SlotMachineException si no hay ruedas o símbolos, o el color no existe
     */
    public void placeSymbol(int wheel, String symbol) throws SlotMachineException
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_OR_SYMBOLS);
        }
        String lower = symbol.toLowerCase();
        Symbol s = symbols.get(lower);
        if(s == null) {
            throw new SlotMachineException(SlotMachineException.COLOR_NOT_FOUND + symbol);
        }
        int wIndex = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(wIndex - 1);
        select(w,s);
        changesJackpot();
        ok = true;
    }
    
    /**
     * Gira la rueda indicada, dejándola en un símbolo aleatorio del catálogo.
     * @param wheel posición de la rueda a girar (1-based)
     * @throws SlotMachineException si no hay ruedas o símbolos para girar
     */
    public void spin(int wheel) throws SlotMachineException
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_OR_SYMBOLS_TO_SPIN);
        }

        int wIndex = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(wIndex - 1);
        if(w.wantsToSpin()) {
            select(w,w.chooseSymbol(randomSymbol()));
        }
        afterSpin();
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
     * @throws SlotMachineException si no hay ruedas o símbolos para girar
     */
    public void spin() throws SlotMachineException
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
     * @throws SlotMachineException si no hay símbolos disponibles
     */
    public String [] configuration() throws SlotMachineException{
        ok = false;
        if(symbols.isEmpty()) {
            throw new SlotMachineException(SlotMachineException.NO_SYMBOLS);
        }
        String[] positions = currentColors();
        ok = true;
        return positions;
    }
    /**
     * Arma el arreglo con el color que muestra cada rueda ("" si está vacía),
     * sin validar nada. Lo usan las consultas internas (isJackpot,
     * distinctSymbols) para que no lancen excepción por un catálogo vacío.
     * @return los colores que muestran las ruedas
     */
    private String[] currentColors(){
        String[] positions = new String[wheels.size()];
        for (int i=0;i< wheels.size();i++){
            Symbol s = wheels.get(i).getCurrentPosition();
            if (s!= null){
                positions[i]= s.getColor();
                }
            else{positions[i]= "";}
        }
        return positions;
    }
    
    /**
     * Cuenta cuántos colores distintos hay actualmente entre las ruedas.
     * @return el número de símbolos distintos visibles en la máquina
     */
    public int distinctSymbols(){
        ok = false;
        String [] positions = currentColors();
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
        String[] positions = currentColors();
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
     * @throws SlotMachineException si hay menos de dos ruedas o alguna no se deja intercambiar
     */
    public void swap(int wheel1, int wheel2) throws SlotMachineException
    {
        if(wheels.size() < 2) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.NEED_TWO_WHEELS);
        }
        int i1 = fixPosition(wheel1, wheels.size());
        int i2 = fixPosition(wheel2, wheels.size());
        Wheel w1 = wheels.get(i1 - 1);
        Wheel w2 = wheels.get(i2 - 1);
        if(!w1.canSwap() || !w2.canSwap()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.WHEEL_NOT_SWAPPABLE);
        }

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
     * @throws SlotMachineException si no hay ruedas o la rueda no se deja fijar
     */
    public void lock(int wheel) throws SlotMachineException
    {
        if(wheels.isEmpty()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_TO_LOCK);
        }
        int index = fixPosition(wheel, wheels.size());
        if(!wheels.get(index - 1).canLock()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.WHEEL_NOT_LOCKABLE);
        }
        wheels.get(index - 1).setHeld(true);
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 6: Fijar y soltar una rueda
     * Suelta una rueda previamente fijada.
     * @param wheel posición de la rueda a soltar (1-based)
     * @throws SlotMachineException si no hay ruedas
     */
    public void unlock(int wheel) throws SlotMachineException
    {
        if(wheels.isEmpty()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_TO_UNLOCK);
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
     * @throws SlotMachineException si no hay ruedas o símbolos, o la rueda está fijada
     */
    public void spin(int wheel, int steps) throws SlotMachineException
    {
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_OR_SYMBOLS_TO_ROTATE);
        }
        int index = fixPosition(wheel, wheels.size());
        Wheel w = wheels.get(index - 1);
        if(w.isHeld()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.WHEEL_HELD);
        }
        if(!w.wantsToSpin()) {
            ok = true;
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
        select(w,w.chooseSymbol(w.getCurrentPosition()));
        afterSpin();
        changesJackpot();
        ok = true;
    }
    /**
     * Ciclo 2 - Mini-ciclo 8: Dejar la máquina en una configuración dada
     * Sobrecarga de spin: deja la máquina en la configuración dada, un
     * color por cada rueda, en el mismo orden de las ruedas. Las ruedas
     * fijadas no se modifican.
     * @param setSymbols arreglo con el color deseado para cada rueda
     * @throws SlotMachineException si no hay ruedas o símbolos, si la cantidad de
     *         colores no coincide con la de ruedas, o si algún color no existe
     */
    public void spin(String[] setSymbols) throws SlotMachineException
    {
        if(wheels.isEmpty() || symbols.isEmpty()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.NO_WHEELS_OR_SYMBOLS);
        }
        if(setSymbols.length != wheels.size()) {
            ok = false;
            throw new SlotMachineException(SlotMachineException.COLOR_COUNT_MISMATCH);
        }
        for(int i = 0; i < setSymbols.length; i++) {
            String lower = setSymbols[i].toLowerCase();
            if(!symbols.containsKey(lower)) {
                ok = false;
                throw new SlotMachineException(SlotMachineException.COLOR_NOT_FOUND + setSymbols[i]);
            }
        }

        for(int i = 0; i < setSymbols.length; i++) {
            Wheel w = wheels.get(i);
            if(!w.isHeld()) {
                String lower = setSymbols[i].toLowerCase();
                Symbol sIndex = symbols.get(lower);
                select(w,sIndex);
            }
        }
        changesJackpot();
        ok = true;
    }
    /**
     * Ciclo 4
     * Avisa a todos los símbolos que hubo un giro (el efímero se encoge).
     */
    private void afterSpin()
    {
        for(Symbol s : symbols.values()) {
            s.onSpin();
        }
    }
    /**
     * Ciclo 4
     * Deja la rueda en el símbolo dado y le avisa al símbolo que fue
     * seleccionado (el tímido alterna entre visible e invisible).
     * @param w la rueda
     * @param s el símbolo en el que queda la rueda
     */
    private void select(Wheel w, Symbol s)
    {
        if(s != null) {
            s.onSelected();
        }
        w.setCurrentPosition(s);
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