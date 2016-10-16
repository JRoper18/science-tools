package SigFigs;

/**
 * Created by jack on 9/21/2016.
 */
public final class SigFigNumber {
    public final String numSigFigs;
    public final double baseNum;
    public final double expandedNum;
    public final int numOfSigFigs;
    public final int decimalLength;
    public final int power;
    public SigFigNumber(String inputNum, int power) {
        double tenPower = Math.floor(Math.log10(Double.parseDouble(inputNum)));
        this.power = (int) (power + tenPower);
        this.baseNum = this.checkIfValidInput(inputNum) * 1/(Math.pow(10, tenPower));
        this.expandedNum = this.baseNum * Math.pow(10, this.power); //Sets to between 0 and 9.9999
        int decIndex = inputNum.indexOf('.');
        if(decIndex == -1){
            if(inputNum.length() == 1){
                this.numSigFigs = inputNum;
                this.numOfSigFigs = numSigFigs.length();
            }
            else{
                this.numSigFigs = inputNum.substring(0, 1) + "." + inputNum.substring(1, inputNum.length());
                this.numOfSigFigs = numSigFigs.length() - 1;
            }
        }
        else{
            this.numSigFigs = inputNum.substring(0, 1) + "." + inputNum.substring(1, decIndex) + inputNum.substring(decIndex + 1, inputNum.length());
            this.numOfSigFigs = numSigFigs.length() - 1;

        }
        this.decimalLength = this.numSigFigs.length() - 2;
    }
    public SigFigNumber(String inputNum){
        this(inputNum, 0);
    }
    private double checkIfValidInput(String inputNum){
        try {
            return Double.parseDouble(inputNum);
        } catch (NumberFormatException error){
            return 0;
        }
    }
    private int calcSigFigs(String inputNum) {
        boolean checkingDecimalDigit = false;
        int firstSigFigPosition = 0;
        int numOfSigFigs = 0;
        for (int i = 0; i < inputNum.length(); i++) {
            char currentDigit = inputNum.charAt(i);
            if (currentDigit == '.') {
                checkingDecimalDigit = true;
                int missedSigFigs = (i - firstSigFigPosition - numOfSigFigs);
                numOfSigFigs += missedSigFigs;
            } else if (currentDigit != '0') {
                if (numOfSigFigs == 0) {
                    //This is the first sigfig we've found. Save it for in-between checking.
                    firstSigFigPosition = i;
                }
                numOfSigFigs++;
            } else { //We're looking at 0
                if (checkingDecimalDigit) {
                    numOfSigFigs++;
                }
            }
        }
        return numOfSigFigs;
    }
    public boolean equals(SigFigNumber num){
        System.out.println(this.numSigFigs + " " + this.power);
        return (this.numSigFigs.equals(num.numSigFigs) && this.power == num.power);
    }
    public SigFigNumber add(SigFigNumber addNum, boolean definition){
        double addedNum = this.expandedNum + addNum.expandedNum;
        int minNumberOfDecimalPlaces = (definition) ? this.decimalLength : Math.min(this.decimalLength, addNum.decimalLength - (this.power - addNum.power));
        SigFigNumber currentSigFig = new SigFigNumber(Double.toString(addedNum));
        return currentSigFig.roundToDecimalPlace(minNumberOfDecimalPlaces);

    }
    private SigFigNumber roundToDecimalPlace(int place) {
        int strIndex = place + 2;
        if(place == 0){
            strIndex = place + 1;
        }
        return new SigFigNumber(this.numSigFigs.substring(0, strIndex), this.power);
    }
    public SigFigNumber multiply(SigFigNumber mulNum){
        double productNum = this.baseNum * mulNum.baseNum;
        int leastSigFigs = Math.min(this.numOfSigFigs, mulNum.numOfSigFigs);
        return new SigFigNumber(Double.toString(productNum), this.power + mulNum.power).roundToDecimalPlace(leastSigFigs - 1);
    }
}
