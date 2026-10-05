
/**
 * Symbol: que va a reducir su tamaño en cada giro
 * hasta quedar como un punto
 * 
 * @author Juan Castellanos - Nicole Paez
 * @version (a version number or a date)
 */
public class EphemeralSymbol extends Symbol
{
    
     private int size;

     public EphemeralSymbol(String color){
         super(color);
         size= 30;
     }
     /**
      * Metodo para reducir el tamaño del
      * del symbolo, cada vez que se haga spin
      * hasta que sea como un punto
      */
     public void reduce(){
         int valueReduce=2;
         if (size>=2){
             size = size-valueReduce;
         }return;
     }
     public int getSize(){
         return size;
     }
     @Override
     public String getShape(){
         return "triangle";
     }
}