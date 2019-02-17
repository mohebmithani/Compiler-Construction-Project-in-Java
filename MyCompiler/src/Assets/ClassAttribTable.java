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
public class ClassAttribTable {
    private String name;
    private String sizeMod;
    private String signMod;
    private String type;
    private String am;
    private boolean isAssigned;
    private boolean isOverride;
    private boolean isStatic;
    private boolean isFinal;
    private boolean isAbstract;
    private boolean isArray;
    private int arrayDim;
    private boolean isList;
    private boolean isMultiList;
    private int listDim;
    private ArrayList<FuncAttribTable> link;

    public ClassAttribTable(String name, String sizeMod, String signMod, String type, String am, boolean isAssigned, boolean isOverride, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isArray, int arrayDim, boolean isList, boolean isMultiList, int listDim, ArrayList<FuncAttribTable> link) {
        this.name = name;
        this.sizeMod = sizeMod;
        this.signMod = signMod;
        this.type = type;
        this.am = am;
        this.isAssigned = isAssigned;
        this.isOverride = isOverride;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isArray = isArray;
        this.arrayDim = arrayDim;
        this.isList = isList;
        this.isMultiList = isMultiList;
        this.listDim = listDim;
        this.link = link;
    }
    
    public ClassAttribTable(String name, String sizeMod, String signMod, String type, String am, boolean isAssigned, boolean isOverride, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isArray, int arrayDim, boolean isList, boolean isMultiList, int listDim) {
        this.name = name;
        this.sizeMod = sizeMod;
        this.signMod = signMod;
        this.type = type;
        this.am = am;
        this.isAssigned = isAssigned;
        this.isOverride = isOverride;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isAbstract = isAbstract;
        this.isArray = isArray;
        this.arrayDim = arrayDim;
        this.isList = isList;
        this.isMultiList = isMultiList;
        this.listDim = listDim;
        this.link = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizeMod() {
        return sizeMod;
    }

    public void setSizeMod(String sizeMod) {
        this.sizeMod = sizeMod;
    }

    public String getSignMod() {
        return signMod;
    }

    public void setSignMod(String signMod) {
        this.signMod = signMod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public boolean isIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public boolean isIsOverride() {
        return isOverride;
    }

    public void setIsOverride(boolean isOverride) {
        this.isOverride = isOverride;
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

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isIsArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public int getArrayDim() {
        return arrayDim;
    }

    public void setArrayDim(int arrayDim) {
        this.arrayDim = arrayDim;
    }

    public boolean isIsList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    public boolean isIsMultiList() {
        return isMultiList;
    }

    public void setIsMultiList(boolean isMultiList) {
        this.isMultiList = isMultiList;
    }

    public int getListDim() {
        return listDim;
    }

    public void setListDim(int listDim) {
        this.listDim = listDim;
    }

    public ArrayList<FuncAttribTable> getLink() {
        return this.link;
    }
    
    public FuncAttribTable getLinkAtIndex(int i) {
        return this.link.get(i);
    }

    public void setLink(ArrayList<FuncAttribTable> link) {
        this.link = link;
    }
    
    public void setLinkAtIndex(int i, FuncAttribTable link) {
        this.link.set(i, link);
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
