/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import Assets.Operators;
import java.util.ArrayList;

/**
 *
 * @author zainm
 */
public class Tokenization {
    
    ArrayList<Token> tokens= new ArrayList<>();
    ArrayList<ArrayList<String>> word;
    Operators opr = new Operators();
    Validation validate = new Validation();
    
    public Tokenization(){
        this.tokens = new ArrayList<>();
    
    }
    
    public Tokenization(ArrayList<ArrayList<String>> word){
        this.word = word;
    }
    
    public ArrayList<Token> generateTokens()
    {
        int line;
        String temp, removeComas;
        for (int i = 0; i < word.size(); i++) {
            temp = null;
            line = i + 1;
            
            for (int j = 0; j < word.get(i).size(); j++) {
                
                
                if(!word.get(i).get(j).equals("")){
                    temp = word.get(i).get(j);
                    
                    if(validate.isConstant(temp).equals("ID"))
                    {
                        if(!validate.isKeyword(temp).equals(""))
                        {
                            tokens.add(new Token(validate.isKeyword(temp), temp,line));
                        }
                        
                        else if(!validate.isOperator(temp).equals(""))
                        {
                            tokens.add(new Token(validate.isOperator(temp), temp,line));
                        }
                        
                        else{
                            tokens.add(new Token("ID", temp,line));
                        }
                        
                    }
                    
                    else if(!validate.isOperator(temp).equals(""))
                    {
                        tokens.add(new Token(validate.isOperator(temp), temp, line));
                    }
                    
                    else if(!validate.isPunctuator(temp).equals(""))
                    {
                        tokens.add(new Token(validate.isPunctuator(temp), temp, line));
                    }
                    
                    else if(!validate.isConstant(temp).equals(""))
                    {
                        if(validate.isConstant(temp).equals("CHAR_CONST") || validate.isConstant(temp).equals("STRING_CONST")){
                            removeComas = temp.substring(1, temp.length()-1);
                        }
                        else{
                            removeComas = temp;
                        }
                        tokens.add(new Token(validate.isConstant(temp), removeComas,line));
                    }
                    
                    else
                    {
                        tokens.add(new Token("INVALID", temp, line));
                    }
                    
                }else
                    continue;
               
            }
        }
        
        return tokens;
    }
    
    public void display(){
        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).display();
            
        }
    }
}
