package com.ave.simplestationsminer.uihelpers;

public class NumToString {
    public static String parse(int number, String postfix) {
        String[] units = { "", "k", "m", "b" };
        int idx = 0;
        double v = number;
        while (Math.abs(v) >= 1000 && idx < units.length - 1) {
            v /= 1000.0;
            idx++;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
        return df.format(Math.abs(v)) + " " + units[idx] + postfix;
    }
}
