package fr.miage.barcodeproduct.utils;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {

    /**
     * Convert a list into a string
     * @param stringList
     * @return a string
     */
    public static String listToString(List<String> stringList){
        if(stringList.size() > 0){
            StringBuilder res = new StringBuilder();
            for(String s : stringList){
                res.append(s).append(",");
            }

            return res.substring(0,res.length()-1);
        }else{
            return "";
        }
    }

    /**
     * Parse a string into a list
     * @param stringList
     * @return a list
     */
    public static List<String> stringToList(String stringList){
        if(stringList.length() > 0){
            String[] arrayString = stringList.split(",");
            return Arrays.asList(arrayString);
        }else{
            return Collections.emptyList();
        }
    }
}
