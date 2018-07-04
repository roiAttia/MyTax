package roiattia.com.tax.utils;

public class FormatsUtil {

    /**
     * Update currentAmountString with newDigit
     * @param currentAmountString the current amount in string
     * @param newDigit the new digit to be added to the string
     * @return the new string
     */
    public static String addNewDigitToString(String currentAmountString, int newDigit){
        // check if string has no digits, if so then replace it with newDigit
        if(currentAmountString.equals("--")){
            currentAmountString = newDigit + "";
        }
        // add newDigit to the string
        else {
            currentAmountString = currentAmountString + newDigit;
        }
        return currentAmountString;
    }

    /**
     * Remove the last digit from the currentAmount string
     * @param currentAmount the currentAmount string
     * @return the new string
     */
    public static String removeLastDigit(String currentAmount) {
        currentAmount = currentAmount.substring(0, currentAmount.length()-1);
        // if current string has no digits then return empty string
        if(currentAmount.length() == 0){
            return "";
        }
        return currentAmount;
    }

    /**
     * Handle add dot to currentAmount string
     * @param currentAmount the currentAmount string
     * @return the new string
     */
    public static String addDot(String currentAmount) {
        // check if there is already a dot in the amount as there can
        // be only one dot
        if(!currentAmount.contains(".")
                && !currentAmount.equals("0")) {
            currentAmount = currentAmount + ".";
        }
        return currentAmount;
    }
}
