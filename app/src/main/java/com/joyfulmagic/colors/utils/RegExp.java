package com.joyfulmagic.colors.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular Expression checker
 */
public class RegExp {

    public static boolean checkWithRegExp(String userNameString, String regExp){
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }
}
