/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

/**
 *
 * @author zainm
 */
public class Token {
    private String CP;
    private String VP;
    private int LN;
    
    public Token(String cp, String vp, int ln){
        this.CP = cp;
        this.VP = vp;
        this.LN = ln;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getVP() {
        return VP;
    }

    public void setVP(String VP) {
        this.VP = VP;
    }

    public int getLN() {
        return LN;
    }

    public void setLN(int LN) {
        this.LN = LN;
    }
    
    public void display(){
        if(this.CP == "TERMINATOR")
            System.out.println("("+this.CP+", \\r"+", "+this.LN+")");
        else
            System.out.println("("+this.CP+", "+this.VP+", "+this.LN+")");
       //
    }
    
    
}
