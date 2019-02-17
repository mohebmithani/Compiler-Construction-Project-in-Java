/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assets;

import java.util.ArrayList;

/**
 *
 * @author hiqba
 */

public class Classification {

    private String cp;
    private ArrayList<String> values;

    public Classification(String cp, ArrayList<String> values) {
        this.cp = cp;
        this.values = values;
    }

    public String getCp() {
        return cp;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
    
    public boolean validate(String c)
    {
        for (int i = 0; i < this.values.size(); i++) 
        {
            if(c.equals(this.values.get(i)))
                return true;   
        }
        return false;
    }
}
