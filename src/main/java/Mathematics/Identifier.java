package Mathematics;

import Mathematics.MathObjects.PatternMatching.PatternEquation;

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
    public static Map<EquationType, Boolean> identify(Equation eq, EquationType type){
        HashMap<EquationType, Boolean> toReturn = new HashMap<>();
        EquationBuilder builder = new EquationBuilder();
        switch(type){
            case CONSTANTPLUSCONSTANT:
                PatternEquation pattern = builder.makePatternEquation("CONSTANT + CONSTANT");
                toReturn.put(EquationType.CONSTANTPLUSCONSTANT, eq.isPattern(pattern));
            default:
         }
         return toReturn;
    }
}
