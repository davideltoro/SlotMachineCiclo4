/**
 * Excepción del simulador. Centraliza todos los mensajes de error
 * de SlotMachine como constantes.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SlotMachineException extends Exception
{
    // Máquina
    public static final String MAX_SYMBOLS =
        "El numero debe ser menor o igual a la cantidad de simbolos permitidos (13).";
    public static final String NO_WHEELS_OR_SYMBOLS =
        "No hay ruedas o simbolos disponibles.";
    public static final String NO_SYMBOLS =
        "No hay simbolos disponibles.";

    // Ruedas
    public static final String WHEEL_TYPE_UNKNOWN = "No existe el tipo de rueda: ";
    public static final String NO_WHEELS_TO_DELETE = "No hay ruedas para eliminar.";
    public static final String WHEEL_NOT_DELETABLE = "Esta rueda no se deja eliminar.";
    public static final String NEED_TWO_WHEELS =
        "Se necesitan al menos dos ruedas para intercambiar.";
    public static final String WHEEL_NOT_SWAPPABLE =
        "Una de las ruedas no se deja intercambiar.";
    public static final String NO_WHEELS_TO_LOCK = "No hay ruedas para fijar.";
    public static final String WHEEL_NOT_LOCKABLE = "Esta rueda no se deja fijar.";
    public static final String NO_WHEELS_TO_UNLOCK = "No hay ruedas para soltar.";
    public static final String WHEEL_HELD =
        "La rueda esta fijada, sueltela antes de rotar.";

    // Símbolos
    public static final String SYMBOL_TYPE_UNKNOWN = "No existe el tipo de simbolo: ";
    public static final String SYMBOL_EXISTS = "Ya existe un simbolo con este color.";
    public static final String SYMBOL_NOT_FOUND = "El simbolo a eliminar no existe.";
    public static final String COLOR_NOT_FOUND = "No existe un simbolo de color: ";
    public static final String COLOR_COUNT_MISMATCH =
        "La cantidad de colores no coincide con la cantidad de ruedas.";

    // Giros
    public static final String NO_WHEELS_OR_SYMBOLS_TO_SPIN =
        "No hay ruedas o simbolos para girar.";
    public static final String NO_WHEELS_OR_SYMBOLS_TO_ROTATE =
        "No hay ruedas o simbolos para rotar.";

    /**
     * Crea una excepción con el mensaje dado.
     * @param message uno de los mensajes constantes de esta clase
     */
    public SlotMachineException(String message)
    {
        super(message);
    }
}