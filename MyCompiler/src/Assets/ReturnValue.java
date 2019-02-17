/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assets;

/**
 *
 * @author moheb
 */
public class ReturnValue {
    private boolean bool;
    private String[] value;

    public ReturnValue(boolean bool, String[] value) {
        this.bool = bool;
        this.value = value;
    }

    public String getValue(int i) {
        return value[i];
    }
    
    public String[] getValues() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
    
    
}
