package Mathematics;

import Mathematics.MathObjects.MathNumber;
import Mathematics.MathObjects.PatternMatching.PatternEquation;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jack on 11/5/2016.
 */

public class Identifier {
    public Identifier(){

    }

    /**
     * Checks if an equation fits every possible type of equation
     * @param eq The equation to check
     * @return A complete map to be put into the tags of the equation.
     */
    public static Map<EquationType, Boolean> identifyAll(Equation eq){
        HashMap<EquationType, Boolean> toReturn = new HashMap<>();
        for(EquationType type : EquationType.values()){
            toReturn.putAll(identify(eq, type));
        }
        return toReturn;
    }

    /**
     * Checks if an equation fits the specified EquationType
     * @param eq The equation to check
     * @param type The type of equation you want to check the equation for.
     * @return A HashMap of all the equationtypes that the equation was found to match.
     */
    public static Map<EquationType, Boolean> identify(Equation eq, EquationType type){
        HashMap<EquationType, Boolean> toReturn = new HashMap<>();
        PatternEquation pattern;
        EquationBuilder builder = new EquationBuilder();
        switch(type){
            case CONSTANT:
                toReturn.put(EquationType.CONSTANT, eq.equationTerms.data.isConstant());
                break;
            case DECIMALCONSTANT:
                boolean isMathNumber = eq.equationTerms.data instanceof MathNumber;
                toReturn.put(EquationType.CONSTANT, isMathNumber);
                toReturn.put(EquationType.DECIMALCONSTANT, isMathNumber);
                break;
            case INTEGERCONSTANT:
                if (eq.isType(EquationType.DECIMALCONSTANT)) {
                    toReturn.put(EquationType.INTEGERCONSTANT, ((MathNumber) eq.equationTerms.data).isInteger());
                }
                else{
                    toReturn.put(EquationType.INTEGERCONSTANT, false);
                }
                break;
            case FRACTION:
                pattern = builder.makePatternEquation("EXPRESSION / EXPRESSION");
                toReturn.put(EquationType.FRACTION, eq.isPattern(pattern));
            case INTEGERFRACTION:
                pattern = builder.makePatternEquation("EXPRESSION{INTEGERCONSTANT} / EXPRESSION{INTEGERCONSTANT}");
                boolean isPattern = eq.isPattern(pattern);
                if(isPattern){
                    toReturn.put(EquationType.FRACTION, true);
                }
                toReturn.put(EquationType.INTEGERFRACTION, isPattern);
            default:
         }
         return toReturn;
    }
}
