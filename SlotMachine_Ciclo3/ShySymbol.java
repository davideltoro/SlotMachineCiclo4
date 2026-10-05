/**
 * Clase Shy: hija de symbol, este simbolo
 * sera timido osea va alternar entre visible y invisible
 * cada vez que la rueda es seleccionada
 * 
 * 
 * @author Juan Castellanos - Nicole Paez
 * @version (a version number or a date)
 */
public class ShySymbol extends Symbol
{
   private boolean shown;
   
   public ShySymbol(String color){
       super(color);
       shown = true;
   }
   /**
    * metodo para alternar 
    * entre visible e invisible
    */
   public void altern(){
       if(shown){
           shown = !shown;
       }else{
           shown = shown;   
       }
   }
   public boolean isShown(){
       return shown;
   }
   public String getShape(){
       return "square";
   }
}