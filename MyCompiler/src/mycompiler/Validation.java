/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import Assets.Operators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author zainm
 */
public class Validation {
    
    
        private final Operators opr = new Operators();
        
        private final String identifierRE = "((_[a-zA-Z0-9]+)|([A-Za-z][a-zA-Z0-9_]*))";
        private final String intRE = "([+-]?(\\d)+)";
        
        //you can skip '.' when using e and you can also skip numbers after decimal in float constant
        private final String floatRE = "((([+-]?(\\d)+))([.]?)((\\d)+|((e|E)([+]|-)?(\\d)+))?)" 
                       + "|(([.])((\\d)+))";    //you can also skip numbers before '.'
                        
        private final String a = "\"";
        private final String b = "bntrf";
        private final String charRE = "'((\\\\['\\\\"+a+b+"])|([ "+b+"[^'\\\\"+a+b+"]"+"]))'";
        private final String strRE = "\"((\\\\['\\\\"+a+b+"])|([ "+b+"[^'\\\\"+a+b+"]"+"]))*\"";
        private final String[] arrOfRE = {identifierRE, intRE, floatRE, charRE, strRE};
        private final String[] tokenName = {"ID", "INT_CONST", "FLOAT_CONST", "CHAR_CONST", "STRING_CONST"};
        private Pattern p;

        public String isConstant(String input){
            for (int i = 0; i < arrOfRE.length; i++) 
            {
                p = Pattern.compile(arrOfRE[i]);
                Matcher m = p.matcher(input);
                if(m.matches())
                {
                    return tokenName[i];
                }
            }
            
            return "";
        }
        
        public String isKeyword(String temp){
            for (int i = 0; i < opr.getKeywords().size(); i++) {
                if(opr.getKeywords().get(i).validate(temp))
                    return opr.getKeywords().get(i).getCp();
            }
            return "";
        }
        
        public String isOperator(String temp){
            for (int i = 0; i < opr.getOperators().size(); i++) {
                if(opr.getOperators().get(i).validate(temp))
                    return opr.getOperators().get(i).getCp();
            }
            return "";
        }
        
        public String isPunctuator(String temp){
            for (int i = 0; i < opr.getPunctuators().size(); i++) {
                if(opr.getPunctuators().get(i).validate(temp))
                    return opr.getPunctuators().get(i).getCp();
            }
            return "";
        }
}