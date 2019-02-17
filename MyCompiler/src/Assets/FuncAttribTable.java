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
public class FuncAttribTable {
    private String name;
    private String sizeMod;
    private String signMod;
    private String type;
    private String am;
    private boolean isAssigned;
    private boolean isFinal;
    private boolean isArray;
    private int arrayDim;
    private boolean isList;
    private boolean isMultiList;
    private int listDim;

    public FuncAttribTable(String name, String sizeMod, String signMod, String type, String am, boolean isAssigned, boolean isFinal, boolean isArray, int arrayDim, boolean isList, boolean isMultiList, int listDim) {
        this.name = name;
        this.sizeMod = sizeMod;
        this.signMod = signMod;
        this.type = type;
        this.am = am;
        this.isAssigned = isAssigned;
        this.isFinal = isFinal;
        this.isArray = isArray;
        this.arrayDim = arrayDim;
        this.isList = isList;
        this.isMultiList = isMultiList;
        this.listDim = listDim;
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
    
    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
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
    
}
