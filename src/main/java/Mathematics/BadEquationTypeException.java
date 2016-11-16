package Mathematics;

import java.io.UncheckedIOException;

/**
 * Created by Ulysses Howard Smith on 11/16/2016.
 */
public class BadEquationTypeException extends RuntimeException {
    private Equation badEquation;
    private EquationType wantedType;
    public BadEquationTypeException(EquationType wantedType, Equation equation){
        this.badEquation = equation;
        this.wantedType = wantedType;
    }
    @Override
    public String getMessage(){
        String typeStr = this.wantedType.toString();
        String message = typeStr + " EquationType needed! You equation did not match that type. ";
        return message;
    }
    public EquationType getWantedType(){
        return this.wantedType;
    }
    public void printMessage(){
        System.out.println(this.getMessage() + "/n");
        this.badEquation.equationTerms.print();
    }
    public Equation equation(){
        return this.badEquation;
    }
}
