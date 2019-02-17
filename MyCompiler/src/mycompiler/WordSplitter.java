/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import Assets.Operators;
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;

/**
 *
 * @author moheb
 */
public class WordSplitter {
    private ArrayList<String> codeArray;

    public WordSplitter(ArrayList<String> codeArray) {
        this.codeArray = codeArray;
    }

    public ArrayList<String> getCodeArray() {
        return codeArray;
    }

    public void setCodeArray(ArrayList<String> codeArray) {
        this.codeArray = codeArray;
    }
    
    public ArrayList<ArrayList<String>> split()
    {
        ArrayList<String> splitWord = new ArrayList<>();
        ArrayList<String> splitWord1 = new ArrayList<>();
        ArrayList<ArrayList<String>> word = new ArrayList<ArrayList<String>>();

        String temp;
        String tempOpr;
        boolean isMultiLine = false;
        boolean isExponent = false;
        boolean isBlank;
        for (int i = 0; i < codeArray.size(); i++) 
        {
            isBlank = true;
            temp = null;
            tempOpr = null;
            char c;
            for (int j = 0; j < codeArray.get(i).length(); j++) 
            {
                c = codeArray.get(i).charAt(j);
                if(!(c == ' ' || c == '\n' || c == '\r'))
                    isBlank = false;
                
                if(c == '/')
                {
                    if((j + 1) < codeArray.get(i).length())
                    {
                        if(codeArray.get(i).charAt(j + 1) == '/')
                            break;
                        else if(codeArray.get(i).charAt(j + 1) == '*')
                        {
                            j++;
                            isMultiLine = true;
                            continue;
                        }
                    }
                    else
                        tempOpr = "" + c;
                }
                
                if(isMultiLine == true)
                {
                    if(c == '*')
                    {
                        if(codeArray.get(i).charAt(j + 1) == '/')
                        {
                            isMultiLine = false;
                            continue;
                        }
                        else
                            continue;
                    }
                    else
                        continue;
                }
                
                
                if(!(breaker(c)))
                {
                    if(temp == null)
                    {
                        temp = "" + c;
                        if(!((j + 1) < codeArray.get(i).length()))
                            if(temp.equals("$"))
                                splitWord.add(temp);
                    }
                    else
                        temp += c;
                }
                else
                {
                        if (c == '/') 
                        {
                            if ((j + 1) < codeArray.get(i).length()) 
                            {
                                if (codeArray.get(i).charAt(j + 1) == '/') 
                                {
                                    break;
                                } else if (codeArray.get(i).charAt(j + 1) == '*') 
                                {
                                    isMultiLine = true;
                                    continue;
                                }
                                else
                                    tempOpr  = "" + c;
                        }

                    } else if (c == '+') 
                    {
                        if(isExponent)
                            if(codeArray.get(i). charAt(j - 1) == 'e' || codeArray.get(i). charAt(j - 1) == 'E')
                            {
                                temp += c;
                                isExponent = false;
                                continue;
                            }
                                
                            
                        if ((j + 1) < codeArray.get(i).length()) 
                        {
                            if (codeArray.get(i).charAt(j + 1) == '/') {
                                if ((j + 2) < codeArray.get(i).length()) {
                                    if (codeArray.get(i).charAt(j + 2) == '/') {
                                        tempOpr = "+";
                                        break;
                                    }
                                }
                            }

                            if (codeArray.get(i).charAt(j + 1) == '+') {
                                tempOpr = "++";
                                j++;
                            } else {
                                tempOpr = "+";
                            }
                        } else {
                            tempOpr = "+";
                        }
                    } else if (c == '-') {
                        if ((j + 1) < codeArray.get(i).length()) {
                            if (codeArray.get(i).charAt(j + 1) == '/') {
                                if ((j + 2) < codeArray.get(i).length()) {
                                    if (codeArray.get(i).charAt(j + 2) == '/') {
                                        tempOpr = "-";
                                        break;
                                    }
                                }
                            }

                            if (codeArray.get(i).charAt(j + 1) == '-') {
                                tempOpr = "--";
                                j++;
                            } else {
                                tempOpr = "-";
                            }
                        } else {
                            tempOpr = "-";
                        }
                    } else if (c == '*') {
                        if ((j + 1) < codeArray.get(i).length()) {
                            if (codeArray.get(i).charAt(j + 1) == '/') {
                                if ((j + 2) < codeArray.get(i).length()) {
                                    if (codeArray.get(i).charAt(j + 2) == '/') {
                                        tempOpr = "*";
                                        break;
                                    }
                                }
                            }

                            if (codeArray.get(i).charAt(j + 1) == '*') {
                                tempOpr = "**";
                                j++;
                            } else {
                                tempOpr = "*";
                            }
                        } else {
                            tempOpr = "*";
                        }
                    } else if (c == '\'') {
                        if (temp != null) {
                            splitWord.add(temp);
                        }
                        temp = null;
                        if ((j + 1) < codeArray.get(i).length()) 
                        {
                            if (codeArray.get(i).charAt(j + 1) == '/') 
                            {
                                if ((j + 2) < codeArray.get(i).length()) 
                                {
                                    if (codeArray.get(i).charAt(j + 2) == '/') 
                                    {
                                        splitWord.add("\'");
                                        break;
                                    } else if (codeArray.get(i).charAt(j + 2) == '*') 
                                    {
                                        isMultiLine = true;
                                        tempOpr = "\'";
                                        continue;
                                    } else
                                    {
                                        tempOpr = "\'" + codeArray.get(i).charAt(j + 1) + codeArray.get(i).charAt(j + 2);
                                        j += 2;
                                        continue;
                                    }
                                }
                            }

                            if ((j + 2) < codeArray.get(i).length()) 
                            {
                                if (codeArray.get(i).charAt(j + 1) == '\\')
                                {
                                    if ((j + 3) < codeArray.get(i).length()) 
                                    {
                                        if(codeArray.get(i).charAt(j + 2) != '\r' && codeArray.get(i).charAt(j + 3) != '\r')
                                        {
                                            tempOpr = "\'\\" + codeArray.get(i).charAt(j + 2) + codeArray.get(i).charAt(j + 3);
                                            j += 3;
                                        }
                                        else if(codeArray.get(i).charAt(j + 2) != '\r' )
                                        {
                                            tempOpr = "\'\\" + codeArray.get(i).charAt(j + 2);
                                            j += 2;
                                        }
                                        else
                                        {
                                            tempOpr = "\'\\";
                                            j++;
                                        }
                                        
                                    }
                                    else 
                                    {
                                        tempOpr = "\'\\";
                                        j++;
                                    }
                                }
                                else 
                                {
                                    if(codeArray.get(i).charAt(j + 1) != '\r')
                                    {
                                        tempOpr = "\'" + (c = codeArray.get(i).charAt(j + 1)) + (c = codeArray.get(i).charAt(j + 2));
                                        j += 2;
                                    }
                                    else 
                                    {
                                        tempOpr = "\'";
                                    }
                                    
                                }
                            }
                            else
                            {
                                if(codeArray.get(i).charAt(j + 1) != '\r')
                                {
                                    tempOpr = "\'" + (c = codeArray.get(i).charAt(j + 1));
                                    j++;
                                }
                                else 
                                {
                                    tempOpr = "\'";
                                }
                            }
                        } else {
                            tempOpr = "\'";
                        }
                    } else if (c == '"') 
                    {
                        if (temp != null)
                        {
                            splitWord.add(temp);
                            temp = null;
                        }
                        
                        do {
                            if (temp == null) {
                                temp = "" + c;
                            } else {
                                temp += c;
                            }

                            if (c  == '\\')
                            {
                                if ((j + 1) < codeArray.get(i).length()) 
                                {
                                    if(codeArray.get(i).charAt(j + 1) != '\r')
                                    {
                                        temp += codeArray.get(i).charAt(j + 1);
                                        j++;
                                    }
                                }    
                            }
                            j++;
                            c = codeArray.get(i).charAt(j);
                        } while (((j + 1) < codeArray.get(i).length()) && (c != '\"') && (c != (char)13));
                        
                        if (c == '"') 
                        {
                            temp += '"';
                        }
                        
                        if(c == (char)13)
                        {
                            tempOpr = "" + (char)13;
                        }
                        
                    } else if (c == '.') {
                        if (temp != null && isInt(temp)) {
                            if (Character.isDigit(codeArray.get(i).charAt(j + 1))) {
                                temp += '.';
                                isExponent = true;
                                continue;
                            } else {
                                splitWord.add(temp);
                                splitWord.add(".");
                                temp = null;
                                continue;
                            }
                        } else if (Character.isDigit(codeArray.get(i).charAt(j + 1))) {
                            if (temp != null) {
                                splitWord.add(temp);
                            }
                            temp = ".";
                            isExponent = true;
                            continue;
                        } else {
                            tempOpr = ".";
                        }

                    } 
                    else if (!(c == ' ' || c == '\n')) {
                        if((c == '\r' && (j == 0 || isBlank)))
                        {
                            if((j + 1) < codeArray.get(i).length())
                                tempOpr = "" + c;
                        }
                        else
                            tempOpr = "" + c;
                    }

                    if (temp != null) {
                        splitWord.add(temp);
                    }
                    if (tempOpr != null) {
                        splitWord.add(tempOpr);
                    }
                    temp = null;
                    tempOpr = null;
                }
            }            
            word.add(splitWord);
            splitWord = new ArrayList<>();
        }
        return word;
    }
    
    static boolean breaker(char c)
        {
            Operators opr = new Operators();
            if(c == ' '  || c == '\'' || c == '"' || c == '\\')
                return true;
            else if(opr.getPM().validate("" + c) || opr.getMDM().validate("" + c) || opr.getINC_DEC().validate("" + c) || opr.getMDM().validate("" + c))
                return true;
            else if(opr.getAO().validate("" + c))
                return true;
            else if(opr.getRO().validate("" + c))
                return true;
            else if(opr.getAND().validate("" + c) || opr.getOR().validate("" + c) || opr.getNOT().validate("" + c))
                    return true;
            else if(opr.getAND_BIN().validate("" + c) || opr.getOR_BIN().validate("" + c) || opr.getXOR().validate("" + c) || opr.getSHIFT_BIN().validate("" + c))
                return true;
            else if(opr.getPROP_ACCESS().validate("" + c) || opr.getARG_DEL().validate("" + c) || opr.getTERMINATOR().validate("" + c) || opr.getBRACES_CLOSE().validate("" + c) || opr.getBRACES_OPEN().validate("" + c) || opr.getPAR_OPEN().validate("" + c) || opr.getPAR_CLOSE().validate("" + c) || opr.getARR_DEL_OPEN().validate("" + c) || opr.getARR_DEL_CLOSE().validate("" + c) || opr.getLIST_PAR_OPEN().validate("" + c) || opr.getLIST_PAR_CLOSE().validate("" + c) || opr.getEXCEPTIONS_OR().validate("" + c) || opr.getSEMI_COL().validate("" + c))
                return true;
            else
                return false;
        }
    
        public static boolean isInt(String str)  
        {  
          try  
          {  
            int n = Integer.parseInt(str);  
          }  
          catch(NumberFormatException nfe)  
          {  
            return false;  
          }  
          return true;  
        }
        
        public static boolean isFloat(String str)  
        {  
          try  
          {  
            double n = Double.parseDouble(str);  
          }  
          catch(NumberFormatException nfe)  
          {  
            return false;  
          }  
          return true;  
        }
}
