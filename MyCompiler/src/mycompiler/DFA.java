/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Zain
 */
public class DFA {
    
    String[] inputChar;
    int IS;
    int[] FS;
    int[][] transitionTable;
    
    public DFA(String[] inChar, int initState, int[] finalState, int[][]TT){

        this.inputChar = inChar;
        this.IS = initState;
        this.FS = finalState;
        this.transitionTable = TT;
    }

    DFA() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
     public boolean validate(String input){
    
        int CS=IS;

        for(int i=0; i<input.length(); i++){
        
            if(CS==-1)
                return false;
            CS=transition(CS, input.charAt(i));
       
        }
        
        for (int i = 0; i < FS.length; i++) 
            if(FS[i]==CS)
                return true;
        return false;
        
    }

    private int transition(int CS,char input){
    
        boolean found=false;
        int position=-1;
        Pattern p;
        Matcher m;
        for (int i = 0; i < this.inputChar.length; i++) {
            p=Pattern.compile(this.inputChar[i]);
            m=p.matcher(String.valueOf(input));
            if(m.matches()){
                position=i;
                found=true;
                break;
            }  
        }
        
        if(!found)
            return position;
        else
            return transitionTable[CS][position];
    }
    
    
    
    public void display()
    {
        System.out.println("\tYOUR INPUT");
        System.out.println("initial state "+this.IS);
        System.out.print("input char : ");
        for (int i = 0; i < inputChar.length; i++) {
            System.out.print(inputChar[i]);
        }
        System.out.println("");
        System.out.print("final state: ");
        for (int i = 0; i < FS.length; i++) {
            System.out.print(FS[i]);
        }
        System.out.println("");
        
    }
    
}
