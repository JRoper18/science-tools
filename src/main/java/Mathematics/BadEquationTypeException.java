package Mathematics;

import Mathematics.MathObjects.PatternMatching.PatternEquation;

/**
 * Created by Ulysses Howard Smith on 11/16/2016.
 */
public class BadEquationTypeException extends RuntimeException {
    private Equation badEquation;
    private EquationType wantedType;
    private PatternEquation wantedPattern;
    public BadEquationTypeException(EquationType wantedType, Equation equation){
        this.badEquation = equation;
        this.wantedType = wantedType;
    }
    public BadEquationTypeException(PatternEquation pattern, Equation equation){
        this.badEquation = equation;
        this.wantedPattern = pattern;
    }
    @Override
    public String getMessage(){
        if(this.wantedType == null){
            System.out.println("Your equation should match the pattern: ");
            wantedPattern.printTree();
            return "Your equation should match the pattern: ";
        }
        else {
            String typeStr = this.wantedType.toString();
            String message = typeStr + " EquationType needed! You equation did not match that type. ";
            return message;
        }
    }
    public EquationType getWantedType(){
        return this.wantedType;
    }
    public PatternEquation getWantedPattern(){
        return this.wantedPattern;
    }
    public void printMessage(){
        System.out.println(this.getMessage() + "/n");
        System.out.println("You bad equation looked like this: /n");
        this.badEquation.equationTerms.print();
    }
    public Equation equation(){
        return this.badEquation;
    }
}
