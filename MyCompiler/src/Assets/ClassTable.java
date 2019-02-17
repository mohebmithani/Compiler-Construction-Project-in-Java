/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assets;

import java.util.ArrayList;

/**
 *
 * @author moheb
 */
public class ClassTable {
    private String name;
    private String am;
    private boolean isStatic;
    private boolean isFinal;
    private String extend;
    private String extendVirtual;
    private String innerClass;
    private ArrayList<ClassAttribTable> link;

    public ClassTable(String name, String am, boolean isStatic, boolean isFinal, String extend, String extendVirtual, String innerClass, ArrayList<ClassAttribTable> link) {
        this.name = name;
        this.am = am;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.extend = extend;
        this.extendVirtual = extendVirtual;
        this.innerClass = innerClass;
        this.link = link;
    }
    
    public ClassTable(String name, String am, boolean isStatic, boolean isFinal, String extend, String extendVirtual, String innerClass) {
        this.name = name;
        this.am = am;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.extend = extend;
        this.extendVirtual = extendVirtual;
        this.innerClass = innerClass;
        this.link = new ArrayList<>();
    }

    public ArrayList<ClassAttribTable> getLink() {
        return this.link;
    }
    
    public ClassAttribTable getLinkAtIndex(int i) {
        return this.link.get(i);
    }

    public void setLink(ArrayList<ClassAttribTable> link) {
        this.link = link;
    }
    
    public void setLinkAtIndex(int i, ClassAttribTable link) {
        this.link.set(i, link);
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExtendVirtual() {
        return extendVirtual;
    }

    public void setExtendVirtual(String extendVirtual) {
        this.extendVirtual = extendVirtual;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(String innerClass) {
        this.innerClass = innerClass;
    }
    
    public int indexOf(String name)
    {
        for (int i = 0; i < this.link.size(); i++) {
            if(this.link.get(i).getName().equals(name))
                return i;
        }
        
        return -1;
    }
    
}
