package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    public String evaluate(String statement) {

        if(statement!=null){
            statement = statement.replaceAll(" ","");
        }

        if(!checkString(statement)){
            return null;
        }

        // calculating the innards of the brackets blocks
        if(statement.contains("(")){
            statement = calcBrackets(statement);
        }

        if(!checkString(statement)){
            return null;
        }

        // in order to split the string by pluses to sum it later we're replacing minuses
        String toSplit = statement.replaceAll("-","+-");

        // here we return minuses adjacent to / or * as they were
        toSplit = fixMinuses(toSplit,statement);

        String[] splitByPlus = toSplit.split("\\+");

        double sum = 0.0;

        for (int i = 0; i < splitByPlus.length ; i++) {
            // if substring has no / or * we add the double parsed from it to the sum
            if(noDivNoMult(splitByPlus[i])){
                // replacing "-" with "+-" might have created some blanks which we skip
                if(splitByPlus[i].equals("")){
                    continue;
                }
                sum = sum + Double.parseDouble(splitByPlus[i]);
            }
            // if substring has * we handle it with an appropriate method ( / r handled in it as well)
            if(splitByPlus[i].contains("*")){
                sum += multiplyIt(splitByPlus[i]);
            }
            // if substring has only /
            if(splitByPlus[i].contains("/") && !splitByPlus[i].contains("*")){
                sum += divideIt(splitByPlus[i]);
            }

        }
        return roundUp(sum);
    }
    // "- 12)1//(" "(12*(5-1)" "5+41..1-6" "5++41-6" "5--41-6" "5**41-6" "5//41-6"
    public boolean checkString(String s){
        if(s == null || s.length() == 0){
            return false;
        }
        if(s.indexOf('(')>s.indexOf(')')){
            return false;
        }
        if(s.contains("//")||s.contains("..")||s.contains("++")||s.contains("--")||s.contains("**")){
            return false;
        }
        if(zeroDiv(s)){
            return false;
        }

        int open = 0;
        int close = 0;
        for (int i = 0; i <s.length() ; i++) {
            char ch = s.charAt(i);
            if(Character.isAlphabetic(ch)){
                return false;
            }
            if(!Character.isDigit(ch) && ch != '/' && ch != '*' && ch != '+' && ch != '-' && ch != '.' && ch != '(' && ch != ')'){
                return false;
            }
            if(ch == '('){
                open++;
            }
            if (ch == ')'){
                close++;
            }
        }

        if (open!=close){
            return false;
        }
        return true;
    }

    /**
     * a method to detect division by zero
     */
    public boolean zeroDiv(String s){

        int index = s.indexOf('/');

        if(index < s.length() && s.charAt(index + 1) == '0') {
            return true;
        }

        String left = "";
        String right = "";
        index = index + 1;
        char character = s.charAt(index);

        while (Character.isDigit(character) && index<s.length()-1){
            left+=(character);
            index++;
            if(index<s.length()-1){
                character = s.charAt(index);
            }
        }

        int minusIndex = index;

        if(s.charAt(minusIndex)=='-' && index<s.length()){
            minusIndex++;
            character=s.charAt(minusIndex);

            while (Character.isDigit(character) && minusIndex<s.length()){
                right+=(character);
                minusIndex = minusIndex + 1;

                if(minusIndex < s.length() - 1){
                    character =s.charAt(minusIndex);
                }
            }

            return left.equals(right);
        }

        return false;
    }

    private String calcBrackets(String s){
        int brackets = getBracketBlocks(s);
        while (brackets>0){
            // getting a substring of the block
            int innerestOpen =s.lastIndexOf('(');
            int innerestClose = s.indexOf(')',innerestOpen);
            String innerestBlock = s.substring(innerestOpen,innerestClose+1);
            // putting it through evaluation
            String result = calcBlock(innerestBlock);
            // replacing a block with the evaluation result
            if(result==null){
                s = s.replace(innerestBlock,"0");
            }
            else {
                s = s.replace(innerestBlock,result);
            }
            brackets=getBracketBlocks(s);
        }
        return s;
    }

    /**
     * a method to calculate the expression inside the single brackets block
     */
    private String calcBlock(String s){
        String result ="";
        if(s.contains("(")){
            int start = s.indexOf("(");
            int end = s.indexOf(")",start+1);
            String subString = s.substring(start+1,end);
            result = evaluate(subString);
        }
        return result;
    }

    private int getBracketBlocks(String s){
        int counter = 0;
        for (int i = 0; i <s.length() ; i++) {
            if(s.charAt(i)=='('){
                counter++;
            }
        }
        return counter;
    }

    /**
     * a method to revert minuses adjacent to / or * back to their previous state
     */
    private String fixMinuses(String noMinus, String statement){
        StringBuilder stringBuilder = new StringBuilder(noMinus);
        for (int i = 0; i <statement.length() ; i++) {
            if(statement.indexOf('-',i)!=-1){
                int index = statement.indexOf('-',i);
                if(index > 0 && ((statement.charAt(index-1) == '/' || statement.charAt(index-1) == '*'
                        || statement.charAt(index+1) == '/' || statement.charAt(index+1) == '*'))){
                            stringBuilder.delete(index,index+1);
                            i=i+index;
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * a method to check if there are any division or multiplying signs in the current substring
     */
    private static boolean noDivNoMult(String s){
        if(s.contains("/") || s.contains("*")){
            return false;
        }
        return true;
    }

    private double multiplyIt(String s){
        double result = 1.0;

        String[] splitByMult = s.split("\\*");

        for (String mult : splitByMult) {
            // if substring also contains / we handle it separately
            if(mult.contains("/")){
                result *= divideIt(mult);
            }
            else {
                result *= Double.parseDouble(mult);
            }
        }

        return result;
    }

    private double divideIt(String s){
        String[] splitByDiv = s.split("/");

        double dividend = Double.parseDouble(splitByDiv[0]);

        for (int i = 0; i < splitByDiv.length-1 ; i++) {
            dividend /= Double.parseDouble(splitByDiv[i+1]);
        }
        return dividend;
    }

    private static String roundUp(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        return decimalFormat.format(d);
    }
}
