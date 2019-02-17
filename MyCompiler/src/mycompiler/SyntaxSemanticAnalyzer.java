/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import java.util.ArrayList;
import java.util.Stack;
import Assets.ClassAttribTable;
import Assets.ClassTable;
import Assets.FuncAttribTable;
import Assets.ReturnValue;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * @author moheb
 */
public class SyntaxSemanticAnalyzer {
        
    private final ArrayList<Token> tokens;
    private int index;
    
    ArrayList<ClassTable> cTable;
//    ClassAttribTable cAttTable;
//    FuncAttribTable funcAttTable;
    Stack<Integer> classScope;
    Stack<Integer> methodScope;

    public Token getTokens(int i) {
        return tokens.get(i);
    }

    public SyntaxSemanticAnalyzer(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.cTable = new ArrayList<>();
        this.classScope = new Stack<>();
        this.methodScope = new Stack<>();
    }

    public int getIndex() {
        return index;
    }

    public ReturnValue EXP(int cc, int cs) {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            ReturnValue rt = F(cc, cs);
            if (rt.isBool()) {
                String type = rt.getValue(0);
                if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                    rt = Exp_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    // F start
    public ReturnValue F(int cc, int cs) {
        String type;
        if (tokens.get(index).getCP().equals("INT_CONST") || tokens.get(index).getCP().equals("CHAR_CONST") || tokens.get(index).getCP().equals("FLOAT_CONST") || tokens.get(index).getCP().equals("STRING_CONST") || tokens.get(index).getCP().equals("BYTE_CONST") || tokens.get(index).getCP().equals("OCT_CONST") || tokens.get(index).getCP().equals("HEX_CONST")) {
            type = this.tokens.get(index).getCP();
            this.index++;
            return new ReturnValue(true, new String[]{type});
        } else if (tokens.get(index).getCP().equals("NOT")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    if(rt.isBool())
                        return new ReturnValue(true, rt.getValues());
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                ReturnValue rt = Left_Side(-1, -1);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    if(rt.isBool())
                        return new ReturnValue(true, rt.getValues());
                }
            }
        } else if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {
            ReturnValue rt = C(cc, cs);
            if (rt.isBool()) {
                cc = Integer.parseInt(rt.getValue(0));
                cs = Integer.parseInt(rt.getValue(1));
                if (tokens.get(index).getCP().equals("ID")) {
                    String name = this.tokens.get(index).getVP();
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        rt = F_2(name, cc, cs, 0);
                        if (rt.isBool()) {
                            return new ReturnValue(true, rt.getValues());
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                        if (Argument_List()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                    if (More_Constructor_Call()) {
                                        if (tokens.get(index).getCP().equals("PROP_ACCESS") ){
                                            this.index++;
                                            if (tokens.get(index).getCP().equals("ID")) {
                                                this.index++;
                                                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                                    ReturnValue rt = F_2("", -1, -1, -1);   //need for
                                                    if (rt.isBool()) {
                                                        return rt;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN"))
        {
            this.index++;
            if (compareString(new String[]{"DATA_TYPES", "STRING", "FILE", "COLLECTION", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "ID"})) {
                ReturnValue rt = F_5(cc, cs);
                if (rt.isBool()) {
                    return rt;
                }
            }

        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue F_2(String name, int cc, int cs, int dim) {
        if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
            ReturnValue rt = F_3(name, cc, cs, dim);
            if (rt.isBool()) {
                return new ReturnValue(true, rt.getValues());
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = EXP(cc, cs);
                if (rt.isBool()) {
                    if(!compareString(rt.getValue(0), new String[]{"INT_CONST", "CHAR_CONST", "int", "char"}))
                    {
                        printErrorMessage("Index should be Convertible data type", this.index);
                        return new ReturnValue(false, new String[]{""});
                    }
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        dim++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            rt = More_Array_Access(dim);
                            if (rt.isBool()) {
                                dim = Integer.parseInt(rt.getValue(0));
                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    rt = F_3(name, cc, cs, dim);
                                    if (rt.isBool()) {
                                        return rt;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    if (F_4()) {
                                        return new ReturnValue(true, new String[]{""});
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue F_3(String name, int cc, int cs, int dim) {
        if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            if(!lookup(name, cc, cs, dim))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            this.index++;
            String type = getType(name, cc, cs, dim);
            if(!isAssigned(name, cc, cs, 0))
            {
                printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                return new ReturnValue(false, new String[]{""});
            }
            ReturnValue rt = compatability(type, op);
            return rt;
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if(!lookup(name, cc, cs, dim))
            {
                printErrorMessage("Attribute Redeclaration Error!!!", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            String type = getType(name, cc, cs, dim);
            String[] w = type.split("[");
            if (tokens.get(index).getCP().equals("ID")) {
                cc = indexOfClass(w[0]);
                name = tokens.get(index).getVP();
                cs = -1;
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    ReturnValue rt = F_2(name, cc, cs, dim);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        } else if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) // for null
        {
            if(!lookup(name, cc, cs, dim))
            {
                printErrorMessage("Attribute Redeclaration Error!!!", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            String type = getType(name, cc, cs, dim);
            if(!isAssigned(name, cc, cs, 0))
            {
                printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                return new ReturnValue(false, new String[]{""});
            }
            return new ReturnValue(true, new String[]{type});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean F_4() {
        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    if ((F_2("", -1, -1, -1)).isBool()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) //for null
        {
            return true;
        }

        return false;
    }

    public ReturnValue F_5(int cc, int cs) {
        String type;
        if (compareString(new String[]{"DATA_TYPES"})) {
            ReturnValue rt = Primitive_Type();
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    rt = Multi_Array_Dec(0);
                    if (rt.isBool()) {
                        for (int i = 0; i < Integer.parseInt(rt.getValue(0)); i++) {
                            type += "[";
                        }
                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                            this.index++;
                            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                            {
                                rt = F(cc, cs);
                                if(rt.isBool())
                                {
                                    String type2 = rt.getValue(0);
                                    return compatability(type, type2, "cast");
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            type = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                ReturnValue rt = Multi_Array_Dec(0);
                if (rt.isBool()) {
                    for (int i = 0; i < Integer.parseInt(rt.getValue(0)); i++) {
                            type += "[";
                        }
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                            {
                                rt = F(cc, cs);
                                if(rt.isBool())
                                {
                                    String type2 = rt.getValue(0);
                                    return compatability(type, type2, "cast");
                                }
                            }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("File")) {
            type = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                ReturnValue rt = Multi_Array_Dec(0);
                if (rt.isBool()) {
                    for (int i = 0; i < Integer.parseInt(rt.getValue(0)); i++) {
                            type += "[";
                        }
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                            {
                                rt = F(cc, cs);
                                if(rt.isBool())
                                {
                                    String type2 = rt.getValue(0);
                                    return compatability(type, type2, "cast");
                                }
                            }
                    }
                }
            }
        } else if (compareString(new String[]{"COLLECTION"})) {
            if (Collection_Type()) {
                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                    {
                        if(F(-1, -1).isBool())
                             return new ReturnValue(true, new String[]{""});
                    }
                }
            }
        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})) {
            ReturnValue rt = Const();
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                    rt = Exp_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        type = rt.getValue(0);
                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                            this.index++;
                            return new ReturnValue(true, new String[]{type});   // need work
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NOT")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    type = rt.getValue(0);
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(type, cc, cs);
                        if (rt.isBool()) {   
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                return rt;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                ReturnValue rt = Left_Side(cc, cs);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    if(!rt.isBool())
                        return rt;
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(rt.getValue(0), cc, cs);
                        if (rt.isBool()) {
                            if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                this.index++;
                                return rt;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                        if (Argument_List()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE"))
                            {
                                this.index++;
                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                    if (More_Constructor_Call()) {
                                        if (tokens.get(index).getCP().equals("PROP_ACCESS")){
                                            this.index++;
                                            if (tokens.get(index).getCP().equals("ID")) {
                                                this.index++;
                                                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                                    if (F_2("", -1, -1, -1).isBool()) { // need work
                                                        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                                                            if (Exp_Dash("", -1, -1).isBool()) {
                                                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                                    this.index++;
                                                                    return new ReturnValue(true, new String[]{""});
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"DATA_TYPES", "STRING", "FILE", "COLLECTION", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "ID"})) {
                ReturnValue rt = F_5(cc, cs);
                if (rt.isBool()) {
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(rt.getValue(0), cc, cs);
                        if (rt.isBool()) { 
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                return rt; 
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            String name = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                ReturnValue rt = F_6(name, cc, cs);
                if (rt.isBool()) {
                    return rt;
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue F_6(String name, int cc, int cs) {
        if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            this.index++;
            String type = getType(name, cc, cs, 0);
            if(!isAssigned(name, cc, cs, 0))
            {
                printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                return new ReturnValue(false, new String[]{""});
            }
            ReturnValue rt = compatability(type, op);
            type = rt.getValue(0);
            if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                rt = Exp_Dash(type, cc, cs);
                if (rt.isBool()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        return rt;  
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("Attribute Redeclaration Error!!!", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            String type = getType(name, cc, cs, 0);
            String[] w = type.split("[");
            if (tokens.get(index).getCP().equals("ID")) {
                cc = indexOfClass(w[0]);
                name = tokens.get(index).getVP();
                cs = -1;
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    ReturnValue rt = F_2(name, cc, cs, 0);
                    if (rt.isBool()) {
                        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                            rt = Exp_Dash(rt.getValue(0), cc, cs);
                            if (rt.isBool()) {   
                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                    this.index++;
                                    return rt;  
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            ReturnValue rt = EXP(cc, cs);
            if (rt.isBool()) {    
                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                    this.index++;
                    return new ReturnValue(true, new String[]{""}); // need work
                }
            }
        }else if (tokens.get(index).getCP().equals("PAR_CLOSE")){
            if(!lookup(name))
            {
                printErrorMessage("Class \""+ name +"\" not Found", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
            {
                ReturnValue rt = F(cc, cs);
                String type2 = rt.getValue(0);
                if(rt.isBool())
                {
                    if((rt = compatability(name, type2, "cast")).isBool())
                        return rt;
                    else
                    {
                        String s = cTable.get(indexOfClass(name)).getExtend();
                        String[] w = s.split(",");
                        for (String w1 : w) {
                            if((rt = compatability(w1, type2, "cast")).isBool())
                                return rt;
                        }
                        
                        s = cTable.get(indexOfClass(name)).getExtendVirtual();
                        w = s.split(",");
                        for (String w1 : w) {
                            if((rt = compatability(w1, type2, "cast")).isBool())
                                return rt;
                        }
                        
                        for (int i = 0; i < cTable.size(); i++) {
                            if(i == indexOfClass(name))
                                continue;
                            s = cTable.get(i).getExtend();
                            w = s.split(",");
                            for (String w1 : w) {
                                if(w1.equals(type2))
                                {
                                    String type = cTable.get(i).getName();
                                    return compatability(type2, type, "cast");
                                }
                            }
                        }
                        
                        for (int i = 0; i < cTable.size(); i++) {
                            if(i == indexOfClass(name))
                                continue;
                            s = cTable.get(i).getExtendVirtual();
                            w = s.split(",");
                            for (String w1 : w) {
                                if(w1.equals(type2))
                                {
                                    String type = cTable.get(i).getName();
                                    return compatability(type2, type, "cast");
                                }
                            }
                        }
                        
                        printErrorMessage("Incompatible casting", this.index);
                    }
                }  
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    if (F_4()) {
                                        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                                            if (Exp_Dash("", -1, -1).isBool()) {        //need work
                                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                    this.index++;
                                                    return new ReturnValue(true, new String[]{""}); //need work
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_CLOSE", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F_7(name, cc, cs, 0);
                if (rt.isBool()) {
                    return rt;
                }
            }
        }
        else if(this.tokens.get(this.index).getCP().equals("PAR_CLOSE"))
        {
            this.index++;
            String type = name;
            if(!lookup(name))
            {
                printErrorMessage("Class \"" + name + "\" not Found", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
            {
                ReturnValue rt = F(cc, cs);
                if(rt.isBool())
                {
                    String type2 = rt.getValue(0);
                    return compatability(type, type2, "cast");
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue F_7(String name, int cc, int cs, int dim) {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            ReturnValue rt = EXP(cc, cs);
            if (rt.isBool()) {   
                if(!compareString(rt.getValue(0), new String[]{"INT_CONST", "CHAR_CONST", "int", "char"}))
                {
                    printErrorMessage("Index should be convertible datatype", this.index);
                    return new ReturnValue(false, new String[]{""});
                }
                if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                    dim++;
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        rt = More_Array_Access(dim);
                        if (rt.isBool()) {
                            dim = Integer.parseInt(rt.getValue(0));
                            if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                rt = F_3(name, cc, cs, dim);
                                if (rt.isBool()) {
                                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                                        rt = Exp_Dash(rt.getValue(0), cc, cs);
                                        if (rt.isBool()) {   
                                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                this.index++;
                                                return rt;  
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
            dim++;
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                ReturnValue rt = Multi_Array_Dec(dim);
                if (rt.isBool()) {
                    String type = name;
                    if(!lookup(name))
                    {
                        printErrorMessage("Class \"" + name + "\" not Found", this.index);
                        return new ReturnValue(false, new String[]{""});
                    }
                    for (int i = 0; i < Integer.parseInt(rt.getValue(0)); i++) {
                            type += "[";
                        }
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                            {
                                rt = F(cc, cs);
                                if(rt.isBool())
                                {
                                    String type2 = rt.getValue(0);
                                    return compatability(type, type2, "cast");
                                }
                            }
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    // F end
    // left side start
    public ReturnValue Left_Side(int cc, int cs) {
        String name;
        if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {
            ReturnValue rt = C(cc, cs);
            if (rt.isBool()) {
                cc = Integer.parseInt(rt.getValue(0));
                cs = Integer.parseInt(rt.getValue(1));
                if (tokens.get(index).getCP().equals("ID")) {
                    name = this.tokens.get(index).getVP();
                    this.index++;
                    if (compareString(new String[]{"PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        rt = Left_Side_Dash(name, cc, cs, 0);
                        if (rt.isBool()) {
                            return new ReturnValue(true, rt.getValues());
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                        if (Argument_List()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                    if (More_Constructor_Call()) {
                                        if (compareString(new String[]{"PROP_ACCESS"})) {
                                            if (Left_Side_3()) {
                                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                                    ReturnValue rt = Left_Side_2("", cc, cs, -1);
                                                    if (rt.isBool()) {
                                                        return new ReturnValue(true, rt.getValues());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Left_Side_Dash(String name, int cc, int cs, int dim) {
        if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
            ReturnValue rt = Left_Side_2(name, cc, cs, dim);
            if (rt.isBool()) {
                return new ReturnValue(true, rt.getValues());
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = EXP(cc, cs);
                if (rt.isBool()) {
                    if(!compareString(rt.getValue(0), new String[]{"INT_CONST", "CHAR_CONST", "int", "char"}))
                    {
                        printErrorMessage("Index should be Convertible data type", this.index);
                        return new ReturnValue(false, new String[]{""});
                    }
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        dim++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            rt = More_Array_Access(dim);
                            if (rt.isBool()) {
                                dim = Integer.parseInt(rt.getValue(0));
                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    rt = Left_Side_2(name, cc, cs, dim); 
                                    if (rt.isBool()) {
                                        return new ReturnValue(true, rt.getValues());
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"}))
                        {
                            if(More_Array_Access(dim).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS"})) {
                                    if (Left_Side_3()) {
                                        if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                            if (Left_Side_2(name, cc, cs, dim).isBool()) {
                                                return new ReturnValue(true, new String[]{""});
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Left_Side_2(String name, int cc, int cs, int dim) {
        if(!lookup(name, cc, cs, dim))
        {
            printErrorMessage("Attribute Redeclaration Error!!!", this.index);
            return new ReturnValue(false, new String[]{""});
        }
        String type = getType(name, cc, cs, dim);
        String[] w = type.split("[");
        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                cc = indexOfClass(w[0]);
                name = tokens.get(index).getVP();
                cs = -1;
                this.index++;
                if (compareString(new String[]{"PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    ReturnValue rt = Left_Side_Dash(name, cc, cs, dim);
                    if (rt.isBool()) {
                        return new ReturnValue(true, rt.getValues());
                    }
                }
            }
        } else if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) // for empty
        {
            if(!lookup(name, cc, cs, dim))
            {
                printErrorMessage("Attribute Redeclaration Error!!!", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            type = getType(name, cc, cs, dim);
            if(!isAssigned(name, cc, cs, 0))
            {
                printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                return new ReturnValue(false, new String[]{""});
            }
            
            return new ReturnValue(true, new String[]{type});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean Left_Side_3() {
        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN","ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    if (Left_Side_4()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean Left_Side_4() {
        if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS"})) {
                                    if (Left_Side_3()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                if (EXP(-1, -1).isBool()) { //need work
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (More_Array_Access(-1).isBool()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) //for null
        {
            return true;
        }

        return false;
    }
    // left side end

    public boolean Right_Side() {
        if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {
            if (C(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "ARR_DEL_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {
                        if (Right_Side_Dash()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                        if (Argument_List()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                    if (More_Constructor_Call()) {
                                        if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "PAR_CLOSE"})) {
                                            if (Right_Side_2()) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if(this.tokens.get(index).getCP().equals("STRING") || this.tokens.get(index).getCP().equals("FILE"))
            {
                this.index++;
                if(tokens.get(index).getCP().equals("PAR_OPEN")){    
                this.index ++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NEW", "CURRENT", "SUPER", "ID"}))
                {
                    if(String_Argument_List())
                    {
                        if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                            this.index ++;
                            if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                                if(Assignment_10()){   
                                    return true;
                                }
                            }
                        }
                    }
                }
                else if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                    this.index ++;
                    if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                        if(Assignment_10()){   
                            return true;
                        }
                    }
                }
            }
            }
        }

        return false;
    }

    public boolean Right_Side_Dash() {
        if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "PAR_CLOSE"})) {
            if (Right_Side_2()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                if (EXP(-1, -1).isBool()) { //need work
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (More_Array_Access(-1).isBool()) {
                                if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";" ,"ARG_DEL", "PAR_CLOSE"})) {
                                    if (Right_Side_2()) {
                                        return true;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"}))
                        {
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "PAR_CLOSE"})) {
                                    if (Right_Side_2()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Right_Side_2() {
        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "ARR_DEL_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {
                    if (Right_Side_Dash()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "PAR_CLOSE"})) // for empty
        {
            return true;
        }

        return false;
    }
    
    public boolean Argument_List() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (Values()) {
                return true;
            }
        } else if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) // for null
        {
            return true;
        }

        return false;
    }

    //Values start
    public boolean Values() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})) {
            if (Const().isBool()) { //need work
                if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                    if (Exp_Dash("", -1,-1).isBool()) {//need work
                        if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (Multi_Values()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NOT")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                if (F(-1, -1).isBool()) {
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        if (Exp_Dash("", -1, -1).isBool()) {    //need work
                            if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                if (Multi_Values()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                if (Left_Side(-1, -1).isBool()) {
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        if (Exp_Dash("", -1, -1).isBool()) {
                            if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                if (Multi_Values()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {
            if (C(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if ((F_2("", -1, -1, -1)).isBool()) {
                            if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                                if (Exp_Dash("", -1, -1).isBool()) {
                                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                        if (Multi_Values()) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "FILE", "STRING", "ID"})) {
                if (Values_Dash()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                if (Values()) {
                    if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (Multi_Values()) {
                                return true;
                            }
                        }
                    }
                }
            }
            else if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                this.index++;
                if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    if (Multi_Values()) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"DATA_TYPES", "STRING", "FILE", "COLLECTION", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "ID"})) {
                if (F_5(-1, -1).isBool()) { //need work
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        if (Exp_Dash("", -1, -1).isBool()) {
                            if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                if (Multi_Values()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Values_Dash() {
        if (tokens.get(index).getCP().equals("COLLECTION")) {
            if (compareString(new String[]{"LIST_PAR_OPEN", "PAR_OPEN"})) {
                if (Values_Dash_Extra()) {
                    if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "PAR_CLOSE"})) {
                            if (Values_Dash_Extra2()) {
                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                    this.index++;
                                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                        if (Multi_Values()) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"DATA_TYPES"})) {
            if (Primitive_Type().isBool()) {
                if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                        if (Array_Init_4_Dash()) {
                            if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                if (Multi_Values()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            this.index++;
            if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"}))
            {
                if(Assignment_9()){
                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Multi_Values()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            this.index++;
            if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"}))
            {
                if(Assignment_9()){
                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Multi_Values()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"})) {
                if (Values_3_Dash()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean Values_Dash_Extra() {
        if (tokens.get(index).getCP().equals("LIST_PAR_OPEN")) {
            this.index++;
            if (tokens.get(index).getCP().equals("LIST_PAR_CLOSE")) {
                this.index++;
                return true;
            }
        } else if (compareString(new String[]{"PAR_OPEN"})) //for null
        {
            return true;
        }

        return false;
    }

    public boolean Values_Dash_Extra2() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {  //need work
                return true;
            }
        } else if (tokens.get(index).getCP().equals("PAR_CLOSE")) //for null
        {
            return true;
        }

        return false;
    }

    public boolean Values_2_Dash() {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (Array_Init_4_Dash()) {
                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Multi_Values()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (Multi_Values()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Values_3_Dash() {
        if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE", "PAR_CLOSE"})) {
                            if (More_Constructor_Call()) {
                                if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    if (Values_4_Dash()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (Array_Init_4_Dash()) {
                    if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Multi_Values()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Values_4_Dash() {
         if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                    if ((F_2("", -1, -1, -1)).isBool()) {
                        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                            if (Exp_Dash("", -1, -1).isBool()) {
                                if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    if (Multi_Values()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
            if (Multi_Values()) {
                return true;
            }
        }
                

        return false;
    }
    //Values end

    public boolean Multi_Values() {
        if (tokens.get(index).getCP().equals("ARG_DEL")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                if (Values()) {
                    return true;
                }
            }

        } else if (compareString(new String[]{"PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) // for null
        {
            return true;
        }

        return false;
    }

    public ReturnValue Const() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})) {
            String type = this.tokens.get(index).getCP();
            this.index++;
            return new ReturnValue(true, new String[]{type});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Primitive_Type() {
        if (tokens.get(index).getCP().equals("DATA_TYPES")) {
            this.index++;
            return new ReturnValue(true, new String[]{tokens.get(index - 1).getVP()});
        }

        return null;
    }

    //Collection Start
    public boolean Collection_Type() {
        if (tokens.get(index).getCP().equals("COLLECTION")) {
            this.index++;
            if (compareString(new String[]{"LIST_PAR_OPEN", "LIST_PAR_CLOSE", "PAR_CLOSE", "ID"})) {
                if (Collection_Variation()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean Collection_Variation() {
        if (tokens.get(index).getCP().equals("LIST_PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"COLLECTION", "ID", "STRING", "FILE"})) {
                if (Collection_Variation_Dash()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"LIST_PAR_CLOSE", "PAR_CLOSE", "ID"})) // for null
        {
            return true;
        }

        return false;
    }

    public boolean Collection_Variation_Dash() {
        if (tokens.get(index).getCP().equals("COLLECTION")) {
            this.index++;
            if (compareString(new String[]{"LIST_PAR_OPEN", "LIST_PAR_CLOSE", "PAR_CLOSE", "ID"})) {
                if (Collection_Variation()) {
                    if (tokens.get(index).getCP().equals("LIST_PAR_CLOSE")) {
                        this.index++;
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"ID", "STRING", "FILE"})) {
            this.index++;
            if (tokens.get(index).getCP().equals("LIST_PAR_CLOSE")) {
                this.index++;
                return true;
            }
        }

        return false;
    }

    public boolean Collection_Init() {

        if (tokens.get(index).getCP().equals("AO")) {

            this.index++;

            if (compareString(new String[]{"SUPER", "CURRENT", "ID", "NEW", "NULL"})) {

                if (Collection_Init_2()) {

                    return true;
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

            return true;
        }

        return false;
    }

    public boolean Collection_Init_2() {

        if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {

            if (C(-1, -1).isBool()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;

                    if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

                        if (Collection_Init_3()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {

            this.index++;

            if (compareString(new String[]{"COLLECTION", "ID"})) {

                if (Collection_Init_4()) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("NULL")) {

            this.index++;
            return true;
        }

        return false;
    }

    public boolean Collection_Init_3() {

        if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

            if (Collection_Init_5()) {

                return true;
            }

        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {

                if (EXP(-1, -1).isBool()) { //need work

                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                        this.index++;
                        if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

                            if (Collection_Init_3()) {

                                return true;
                            }
                        }
                    }
                }
            }

        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", " CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {

                if (Argument_List()) {
                    if(tokens.get(index).getCP().equals("PAR_CLOSE"))
                    {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {
                                    if (Collection_Init_6()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // **************************************** RULE BREAK HO GA *****************************
    public boolean Collection_Init_4() {
        if(this.tokens.get(index).getCP().equals("COLLECTION"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("LIST_PAR_OPEN"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("LIST_PAR_CLOSE"))
                {
                    this.index++;
                    if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
                    {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                        {
                            if(EXP(-1, -1).isBool())
                            {
                                if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                                {
                                    this.index++;
                                    return true;
                                }
                            }
                        }
                        else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
                    }
                }
            }
            else if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                        {
                            if(EXP(-1, -1).isBool())
                            {
                                if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                                {
                                    this.index++;
                                    return true;
                                }
                            }
                        }
                        else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
            }
        }
        else if(this.tokens.get(index).getCP().equals("ID"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                {
                    this.index++;
                    if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"}))
                    {
                        if(More_Constructor_Call())
                        {
                            if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"}))
                            {
                                if(Collection_Init_6())
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    // **************************************** RULE BREAK HO GA *****************************

    public boolean Collection_Init_5() {

        if (compareString(new String[]{"AO", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

            if (Collection_Init()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

                    if (Collection_Init_3()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {    // for null
            return true;
        }

        return false;
    }

    public boolean Collection_Init_6() {

        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {
                    if (Collection_Init_3()) {
                        return true;
                    }
                }
            }
            
        } else if (compareString(new String[]{"TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {     // for null
            return true;
        }
        return false;
    }
    //Collection end

    // Array Start
    public boolean Array_Init() {

        if (tokens.get(index).getCP().equals("AO")) {

            this.index++;

            if (compareString(new String[]{"BRACES_OPEN", "SUPER", "CURRENT", "NEW", "ID", "NULL"})) {

                if (Array_Init_Dash()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"})) {

            return true;
        }
        return false;
    }

    //******************************RULE BREAK HO GA *********************************
    public boolean Array_Init_Dash() {

        
        if(tokens.get(index).getCP().equals("BRACES_OPEN")){
            
            this.index ++;
            
            if(compareString(new String [] {"BRACES_OPEN", "SUPER", "CURRENT", "NEW", "ID", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "PAR_OPEN", "BRACES_CLOSE"})){
                
                if(Array_Init_Dash_Extra()){
                    
                    return true;
                }
            }
        }
        
        else if (compareString(new String[]{"CURRENT", "SUPER", "ID"})){
            
            if(C(-1, -1).isBool()){
                
                if(tokens.get(index).getCP().equals("ID")){
                    
                    this.index ++;
                    
                    if (compareString(new String[]{"PROP_ACCESS", "AO", "TERMINATOR", ";", "ARG_DEL", "ARR_DEL_OPEN", "PAR_OPEN"})) {
                        
                        if(Right_Side_Dash()){
                            
                            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL", ";"})) {
                                
                                if(Array_Init()){
                                    
                                    return true;
                                }
                            }

                        }
                    }

                    
                }
            }
        }
        
        else if(tokens.get(index).getCP().equals("NEW")){
            
            this.index ++;
            
            if(compareString(new String [] {"ID", "DATA_TYPES", "STRING", "FILE"})){
                
                if(Array_Init_2_Dash()){
                    
                    return true;
                }
            }
            
        }
        else if(tokens.get(index).getCP().equals("NULL"))
        {
            this.index++;
            return true;
        }
        
        return false;
    }
    
    
    public boolean Array_Init_Dash_Extra(){
        
       if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
           
           if(Values()){
               
               if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
                   
                   this.index ++;
                   return true;
               }
           }
       }
       
       else if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
           
           this.index ++;
           return true;
       }

        return false;
    }
    //******************************RULE BREAK HO GA *********************************

    public boolean Array_Init_2_Dash() {

        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"PAR_OPEN", "ARR_DEL_OPEN"})) {

                if (Array_Init_3_Dash()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("DATA_TYPES")) {

            if (Primitive_Type().isBool()) {

                if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

                    this.index++;

                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {

                        if (Array_Init_4_Dash()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

                this.index++;

                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {

                    if (Array_Init_4_Dash()) {

                        return true;
                    }

                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

                this.index++;

                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {

                    if (Array_Init_4_Dash()) {

                        return true;
                    }

                }
            }
        }

        return false;
    }

    public boolean Array_Init_3_Dash() {

        if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                if (Argument_List()) {

                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                        this.index++;

                        if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                            if (More_Constructor_Call()) {

                                if (compareString(new String[]{"PROP_ACCESS"})) {

                                    if (Left_Side_3()) {

                                        if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {

                                            if (Left_Side_2("", -1, -1, -1).isBool()) {

                                                if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL", ";"})) {

                                                    if (Array_Init()) {

                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {

                if (Array_Init_4_Dash()) {

                    return true;
                }
            }
        }
        return false;
    }

    public boolean Array_Init_4_Dash() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE", "BRACES_OPEN", "TERMINATOR", ";", "ARG_DEL"})) {
                        if (Multi_Array_Init()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE", "BRACES_OPEN", "TERMINATOR", ";"})) {
                if (Multi_Array_Init()) {
                    if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "BRACES_CLOSE"})) {
                            if (Array_Init_4_Dash_Extra()) {
                                if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                    this.index++;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Multi_Array_Init() {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (Multi_Array_Init_Extra()) {
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE", "BRACES_OPEN", "TERMINATOR", ";"})) {
                            if (Multi_Array_Init()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE", "BRACES_OPEN", "TERMINATOR", ";", "ARG_DEL"})) //for null
        {
            return true;
        }

        return false;
    }

    public boolean Multi_Array_Init_Extra() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) //for null
        {
            return true;
        }

        return false;
    }

    public boolean Array_Init_4_Dash_Extra() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (Values()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("BRACES_CLOSE")) //for null
        {
            return true;
        }

        return false;
    }

    public ReturnValue Multi_Array_Dec(int dim) {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                this.index++;
                dim++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    ReturnValue rt = Multi_Array_Dec(dim);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        } else if (compareString(new String[]{"PAR_CLOSE", "TERMINATOR", "ID"})) //for null
        {
            return new ReturnValue(true, new String[]{dim+""});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue More_Array_Access(int dim) {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                if (EXP(-1, -1).isBool()) {
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        dim++;
                        this.index++;
                        ReturnValue rt = More_Array_Access(dim);
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (rt.isBool()) {
                                return new ReturnValue(true, rt.getValues());
                            }
                        }
                    }
                }
            } 
        }
        else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) //for null
        {
            return new ReturnValue(true, new String[]{dim + ""});
        }

        return new ReturnValue(false, new String[]{""});
    }
    // Array End

    public boolean Constructor_Body()
    {
        if(this.tokens.get(index).getCP().equals("CURRENT"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"}))
                {
                    if(Argument_List())
                    {
                        if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            if(this.tokens.get(index).getCP().equals("TERMINATOR"))
                            {
                                this.index++;
                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"}))
                                {
                                    if(MST())
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                this.index--;
            }
        }
        else if(this.tokens.get(index).getCP().equals("SUPER"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"}))
                {
                    if(Argument_List())
                    {
                        if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            if(this.tokens.get(index).getCP().equals("TERMINATOR"))
                            {
                                this.index++;
                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"}))
                                {
                                    if(MST())
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                this.index--;
            }
        }
        
        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"}))
        {
            if(MST())
                return true;
        }
        
        return false;
    }
    
    public boolean More_Constructor_Call() {
        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("NEW")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                            if (Argument_List()) {
                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                    this.index++;
                                    if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                        if (More_Constructor_Call()) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else //jugaar
            {
                this.index--;
                return true;
            }
        } else if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE", "PAR_CLOSE"})) //for null
        {
            return true;
        }

        return false;
    }

    public ReturnValue C(int cc, int cs) {
        if (tokens.get(index).getCP().equals("CURRENT")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                return new ReturnValue(true, new String[]{cc + "", "-1"});
            }
        } else if (tokens.get(index).getCP().equals("SUPER")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                String[] w = cTable.get(classScope.peek()).getExtend().split(",");
                return new ReturnValue(true, new String[]{indexOfClass(w[0]) + "", "-1"});
            }
        } else if (tokens.get(index).getCP().equals("ID")) //for null
        {
            return new ReturnValue(true, new String[]{cc + "", cs + ""});
        }

        return new ReturnValue(false, new String[]{""});
    }

    //************** NEW **************************************
    //**************** Classess CFG Start *****************************************************
    public boolean Class_Body() {
        boolean finl = false, stat = false;
        String name, am = "public", sizeMod = "short", signMod = "signed", type;
        
        if (tokens.get(index).getCP().equals("TERMINATOR")) {
            this.index++;
            if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
            {
                if(Class_Body())
                    return true;
            }
        } else if (tokens.get(index).getCP().equals("ACCESS_MOD")) 
        {
            am = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"STATIC", "CLASS", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "BRACES_CLOSE"})) {
                if (Class_Body_0(am)) {
                   if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                    {
                        if(Class_Body())
                            return true;
                    } 
                }
            }
        } else if (tokens.get(index).getCP().equals("STATIC")) {
            stat = true;
            this.index++;
            if (compareString(new String[]{"CLASS", "VOID", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "BRACES_CLOSE"})) {
                if (Class_Body_1(am, stat, finl)) {
                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                    {
                        if(Class_Body())
                            return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("CLASS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                name = tokens.get(index).getVP();
                String innerClass = name;
                this.index++;
                if(compareString(new String [] {"EXTENDS", "TERMINATOR"}))
                {
                    ReturnValue inhClass = Inherited_Class();
                    if(inhClass.isBool())
                    {
                        if(!insert(name, am, stat, finl, inhClass.getValue(1), inhClass.getValue(0), null, null))
                            return false;
                        cTable.get(classScope.peek()).setInnerClass(innerClass);
                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                    this.index++;
                                    createClassScope(cTable.size() - 1);
                                    if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"})) {
                                        if (Class_Body()) {
                                            {
                                                if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                    this.index++;
                                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                        this.index++;
                                                        destroyClassScope();
                                                        if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                                        {
                                                            if(Class_Body())
                                                                return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FINAL")) {
            finl = true;
            this.index++;
            if (compareString(new String[]{"STATIC", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
                ReturnValue rt = Non_Access_Combination2Dash();
                if (rt.isBool()) {
                    if(rt.getValue(0).equals("true"))
                        stat = true;
                    if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                        rt = Size_Mod();
                        if (rt.isBool()) {
                            sizeMod = rt.getValue(0);
                            if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                                rt = Sign_Mod();
                                if (rt.isBool()) {
                                    signMod = rt.getValue(0);
                                    if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                        if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                            if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                            {
                                                if(Class_Body())
                                                    return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD"})) {
            ReturnValue rt = Size_Mod();
            if (rt.isBool()) {
                    sizeMod = rt.getValue(0);
                    if (this.tokens.get(index).getCP().equals("SIGN_MOD")) {
                        signMod = this.tokens.get(index).getVP();
                        this.index++;
                        if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                            if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                {
                                    if(Class_Body())
                                        return true;
                                }
                            }
                        }
                    } 
                    else
                    {
                        this.index--;
                        if (this.tokens.get(index).getCP().equals("SIZE_MOD")) {
                            sizeMod = this.tokens.get(index).getVP();
                            this.index++;
                            if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                rt = Sign_Mod();
                                if(rt.isBool()){
                                    signMod = rt.getValue(0);
                                    if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                        if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                            if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                            {
                                                if(Class_Body())
                                                    return true;
                                            }
                                        }
                                    }
                                }
                            }  
                        }
                    }
            }
        } else if (tokens.get(index).getCP().equals("DATA_TYPES")) {
            ReturnValue rt = Primitive_Type();
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                    if (Class_Body_2_0(am, stat, finl, sizeMod, signMod, type)) {
                        if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                        {
                            if(Class_Body())
                                return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            type = tokens.get(index).getVP();
            this.index++;

            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_3(am, stat, finl, sizeMod, signMod, type)) {

                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                    {
                        if(Class_Body())
                            return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            type = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_2_2(am, stat, finl, sizeMod, signMod, type)) {
                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                    {
                        if(Class_Body())
                            return true;
                    }
                }

            }
        } else if (tokens.get(index).getCP().equals("COLLECTION")) {

            if (Collection_Type()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;
                    if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                        if (Class_Body_2_3()) {
                            if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                            {
                                if(Class_Body())
                                    return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("VOID")) {

            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                        if (Parameter_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;
                                if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                    if (Exception_List()) {

                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                            this.index++;
                                            if (tokens.get(index).getCP().equals("BRACES_OPEN")) {

                                                this.index++;
                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                    this.index++;
                                                    if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                        if (MST()) {

                                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                                this.index++;

                                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                                    this.index++;
                                                                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                                                    {
                                                                        if(Class_Body())
                                                                            return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            type = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"PAR_OPEN", "ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_0_Dash(am, stat, finl, sizeMod, signMod, type)) {
                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                    {
                        if(Class_Body())
                            return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ABSTRACT")) {

            this.index++;
            if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                if (Size_Mod().isBool()) {

                    if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                        if (Sign_Mod().isBool()) {

                            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID"})) {

                                if (RT()) {

                                    if (tokens.get(index).getCP().equals("ID")) {

                                        this.index++;
                                        if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                                            this.index++;
                                            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                                                if (Parameter_List()) {

                                                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                                        this.index++;
                                                        if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                                            if (Exception_List()) {

                                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                                    this.index++;
                                                                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                                                    {
                                                                        if(Class_Body())
                                                                            return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("OVERRIDE")) {

            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {

                this.index++;
                if (compareString(new String[]{"ACCESS_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN", "BRACES_CLOSE"})) {

                    if (AM().isBool()) {

                        if (compareString(new String[]{"STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {

                            if (Non_Access_Combination().isBool()) {

                                if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                                    if (Size_Mod().isBool()) {

                                        if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                                            if (Sign_Mod().isBool()) {

                                                if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID"})) {

                                                    if (RT()) {

                                                        if (tokens.get(index).getCP().equals("ID")) {

                                                            this.index++;
                                                            if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                                                                this.index++;
                                                                if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                                                                    if (Parameter_List()) {

                                                                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                                                            this.index++;
                                                                            if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                                                                if (Exception_List()) {

                                                                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                                                        this.index++;
                                                                                        if (tokens.get(index).getCP().equals("BRACES_OPEN")) {

                                                                                            this.index++;
                                                                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                                                                this.index++;
                                                                                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                                                                    if (MST()) {

                                                                                                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {

                                                                                                            this.index++;
                                                                                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                                                                                this.index++;
                                                                                                                if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                                                                                                {
                                                                                                                    if(Class_Body())
                                                                                                                        return true;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {        // for null
            return true;

        }

        return false;
    }

    public boolean Class_Body_Dash() {
        if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            this.index++;
            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "STRING", "FILE", "ID", "PAR_CLOSE"})) {

                if (Parameter_List()) {

                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                        this.index++;
                        if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                            if (Exception_List()) {

                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                    this.index++;

                                    if (tokens.get(index).getCP().equals("BRACES_OPEN")) {

                                        this.index++;
                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                            this.index++;
                                            if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                if (Constructor_Body()) {

                                                    if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {

                                                        this.index++;
                                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                            this.index++;
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        return false;
    }

    public boolean Class_Body_0(String am) {
        boolean finl = false, stat = false;
        String name, sizeMod = "short", signMod = "signed", type;
        
        if (tokens.get(index).getCP().equals("STATIC")) {
            stat = true;
            this.index++;
            if (compareString(new String[]{"CLASS", "VOID", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "BRACES_CLOSE"})) {
                if (Class_Body_1(am, stat, finl)) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("CLASS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                name = tokens.get(index).getVP();
                String innerClass = name;
                this.index++;
                if(compareString(new String [] {"EXTENDS", "TERMINATOR"}))
                {
                    ReturnValue inhClass = Inherited_Class();
                    if(inhClass.isBool())
                    {
                        if(!insert(name, am, stat, finl, inhClass.getValue(1), inhClass.getValue(0), null, null))
                            return false;
                        cTable.get(classScope.peek()).setInnerClass(innerClass);
                        
                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                    this.index++;
                                    createClassScope(cTable.size() - 1);
                                    if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"})) {
                                        if (Class_Body()) {
                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                this.index++;
                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                    this.index++;
                                                    destroyClassScope();
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FINAL")) {
            finl = true;
            this.index++;
            if (compareString(new String[]{"STATIC", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
                ReturnValue rt = Non_Access_Combination2Dash();
                if (rt.isBool()) {
                    if(rt.getValue(0).equals("true"))
                        stat = true;
                    if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                        rt = Size_Mod();
                        if (rt.isBool()) {
                            sizeMod = rt.getValue(0);
                            if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                                rt = Sign_Mod();
                                if (rt.isBool()) {
                                    signMod = rt.getValue(0);
                                    if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                        if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD"})) {
            ReturnValue rt = Size_Mod();
            if (rt.isBool()) {
                    sizeMod = rt.getValue(0);
                    if (this.tokens.get(index).getCP().equals("SIGN_MOD")) {
                        signMod = this.tokens.get(index).getVP();
                        this.index++;
                        if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                            if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                {
                                    if(Class_Body())
                                        return true;
                                }
                            }
                        }
                    } 
                    else
                    {
                        this.index--;
                        if (this.tokens.get(index).getCP().equals("SIZE_MOD")) {
                            sizeMod = this.tokens.get(index).getVP();
                            this.index++;
                            if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                rt = Sign_Mod();
                                if(rt.isBool()){
                                    signMod = rt.getValue(0);
                                    if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {
                                        if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {
                                            if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"}))
                                            {
                                                if(Class_Body())
                                                    return true;
                                            }
                                        }
                                    }
                                }
                            }  
                        }
                    }
            }
        } else if (tokens.get(index).getCP().equals("DATA_TYPES")) {
            ReturnValue rt = Primitive_Type();
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                    if (Class_Body_2_0(am, stat, finl, sizeMod, signMod, type)) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            type = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_3(am, stat, finl, sizeMod, signMod, type)) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            type = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_2_2(am, stat, finl, sizeMod, signMod, type)) {
                    return true;
                }

            }
        } else if (tokens.get(index).getCP().equals("COLLECTION")) {

            if (Collection_Type()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;

                    if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                        if (Class_Body_2_3()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("VOID")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;

                    if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                        if (Parameter_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;

                                if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                    if (Exception_List()) {

                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                            this.index++;

                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                this.index++;

                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                    this.index++;

                                                    if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                        if (MST()) {

                                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {

                                                                this.index++;

                                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                                    this.index++;
                                                                    return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            type = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"PAR_OPEN", "ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_0_Dash(am, stat, finl, sizeMod, signMod, type)) {
                    return true;
                }
            }

        } else if (tokens.get(index).getCP().equals("ABSTRACT")) {
            this.index++;

            if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                if (Size_Mod().isBool()) {

                    if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                        if (Sign_Mod().isBool()) {

                            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID"})) {

                                if (RT()) {

                                    if (tokens.get(index).getCP().equals("ID")) {
                                        this.index++;

                                        if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                                            this.index++;
                                            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                                                if (Parameter_List()) {

                                                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                        this.index++;

                                                        if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                                            if (Exception_List()) {

                                                                if (tokens.get(index).getCP().equals("TEMINATOR")) {
                                                                    this.index++;
                                                                    return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Class_Body_0_Dash(String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {

        if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            
            if (Class_Body_Dash()) {

                return true;
            }

        } else if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

            if (Class_Body_2_1(am, stat, finl, sizeMod, signMod, type)) {

                return true;
            }
        }
        return false;
    }

    public boolean Class_Body_1(String am, boolean stat, boolean finl) {
        String name, sizeMod = "short", signMod = "signed";
        
        if (tokens.get(index).getCP().equals("CLASS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                name = tokens.get(index).getVP();
                String innerClass = name;
                this.index++;
                if(compareString(new String [] {"EXTENDS", "TERMINATOR"}))
                {
                    ReturnValue inhClass = Inherited_Class();
                    if(inhClass.isBool())
                    {
                        if(!insert(name, am, stat, finl, inhClass.getValue(1), inhClass.getValue(0), null, null))
                            return false;
                        cTable.get(classScope.peek()).setInnerClass(innerClass);
                        
                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                    this.index++;
                                    createClassScope(cTable.size() - 1);
                                    if(compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"})) {
                                        if (Class_Body()) {
                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                this.index++;
                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                    this.index++;
                                                    destroyClassScope();
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }   
                    }
                }
            }
        } else if (compareString(new String[]{"FINAL", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "ID", "FILE", "STRING"})) {

            if (Non_Access_CombinationDash().isBool()) {

                if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                    if (Size_Mod().isBool()) {

                        if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                            if (Sign_Mod().isBool()) {

                                if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID"})) {

                                    if (Class_Body_2(am, stat, finl, sizeMod, signMod)) {

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("VOID")) {

            this.index++;

            if (compareString(new String[]{"START", "ID"})) {

                if (Class_Body_1_0()) {

                    return true;
                }
            }
        }

        return false;
    }

    public boolean Class_Body_1_0() {

        if (tokens.get(index).getCP().equals("START")) {

            this.index++;

            if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                this.index++;

                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {

                    if (Argument_List()) {

                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                            this.index++;

                            if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                this.index++;

                                if (tokens.get(index).getCP().equals("BRACES_OPEN")) {

                                    this.index++;

                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                        this.index++;

                                        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                            if (MST()) {

                                                if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {

                                                    this.index++;

                                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                        this.index++;
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                if (Class_Body_Dash()) {

                    return true;
                }
            }

        }

        return false;
    }

    public boolean Class_Body_2(String am, boolean stat, boolean finl, String sizeMod, String signMod) {
        String type;
        if (tokens.get(index).getCP().equals("DATA_TYPES")) {
            ReturnValue rt = Primitive_Type();
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                    if (Class_Body_2_0(am, stat, finl, sizeMod, signMod, type)) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            type = this.tokens.get(index).getVP();
            this.index++;

            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_2_1(am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            type = this.tokens.get(index).getVP();
            this.index++;

            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Class_Body_3(am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            type = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                if (Class_Body_2_2(am, stat, finl, sizeMod, signMod, type)) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("COLLECTION")) {

            if (Collection_Type()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;

                    if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                        if (Class_Body_2_3()) {

                            return true;
                        }
                    }
                }
            }
        } 
            else if (tokens.get(index).getCP().equals("VOID")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;

                    if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "VOID", "PAR_CLOSE"})) {

                        if (Parameter_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;

                                if (compareString(new String[]{"THROWS", "TERMINATOR"})) {

                                    if (Exception_List()) {

                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                            this.index++;

                                            if (tokens.get(index).getCP().equals("BRACES_OPEN")) {

                                                this.index++;

                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                    this.index++;

                                                    if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                        if (MST()) {

                                                            if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {

                                                                this.index++;

                                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {

                                                                    this.index++;
                                                                    return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Class_Body_2_0(String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {
        if (tokens.get(index).getCP().equals("ID")) {
            String name = tokens.get(index).getVP();
            if(lookup(name, classScope.peek(), -1))
            {
                System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                return false;
            }
            this.index++;
            if (compareString(new String[]{"PAR_OPEN", "AO", "ARG_DEL", "TERMINATOR"})) {

                if (Class_Body_2_0_0(name,  am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }

            }

        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;
                int dim = 1;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    ReturnValue rt = Multi_Array_Dec(dim);
                    if (rt.isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                                if (Class_Body_2_0_1()) {

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Class_Body_2_0_0(String name, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {
        boolean ass = false;
        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
            ReturnValue rt = Primitive_Init(classScope.peek(), -1, type);
            if (rt.isBool()) {
                if(rt.getValue(0).equals("true"))
                    ass = true;
                if(!insert(classScope.peek(), -1, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                    return false;
                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                    if (List_1(classScope.peek(), -1, am, stat, finl, sizeMod, signMod, type)) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {
                return true;
            }

        }

        return false;
    }

    public boolean Class_Body_2_0_1() {

        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

            if (Array_Init()) {

                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                    if (List_4()) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {

                return true;
            }
        }

        return false;
    }

    public boolean Class_Body_2_1(String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {

        if (tokens.get(index).getCP().equals("ID")) {
            String name = tokens.get(index).getVP();
            if(lookup(name, classScope.peek(), -1))
            {
                System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                return false;
            }
            this.index++;
            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                if (Class_Body_2_1_0(name,  am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;
                int dim = 1;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    ReturnValue rt = Multi_Array_Dec(dim);
                    if (rt.isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                                if (Class_Body_2_0_1()) {

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Class_Body_2_1_0(String name, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {
        boolean ass = false;
        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
            ReturnValue rt = Reference_Init(classScope.peek(), -1, type);
            if (rt.isBool()) {
                if(rt.getValue(0).equals("true"))
                    ass = true;
                if(!insert(classScope.peek(), -1, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                    return false;
                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                    if (List_2(classScope.peek(), -1, am, stat, finl, sizeMod, signMod, type)) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {

                return true;
            }
        }

        return false;
    }

    public boolean Class_Body_2_2(String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {

        if (tokens.get(index).getCP().equals("ID")) {
            String name = tokens.get(index).getVP();
            if(lookup(name, classScope.peek(), -1))
            {
                System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                return false;
            }
            this.index++;
            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                if (Class_Body_2_2_0(name,  am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;
                int dim = 1;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    ReturnValue rt = Multi_Array_Dec(dim);  //need work
                    if (rt.isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                                if (Class_Body_2_0_1()) {

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Class_Body_2_2_0(String name, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {
        boolean ass = false;
        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
            ReturnValue rt = String_Init(classScope.peek(), -1, type);
            if (rt.isBool()) {
                if(rt.getValue(0).equals("true"))
                    ass = true;
                if(!insert(classScope.peek(), -1, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                    return false;
                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                    if (List_3(classScope.peek(), -1, am, stat, finl, sizeMod, signMod, type)) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {

                return true;
            }
        }
        return false;
    }

    public boolean Class_Body_2_3() {

        if (compareString(new String[]{"AO", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL", ";"})) {

            if (Collection_Init()) {

                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                    if (List_5()) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {

                return true;
            }
        }

        return false;
    }
    
    public boolean Class_Body_3(String am, boolean stat, boolean finl, String sizeMod, String signMod, String type)
    {
        if (tokens.get(index).getCP().equals("ID")) {
            String name = tokens.get(index).getVP();
            if(lookup(name, classScope.peek(), -1))
            {
                System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                return false;
            }
            this.index++;
            if (compareString(new String[]{"AO", "ARG_DEL", "TERMINATOR", "PAR_OPEN"})) {

                if (Class_Body_3_0(name,  am, stat, finl, sizeMod, signMod, type)) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;
                int dim = 1;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                    ReturnValue rt = Multi_Array_Dec(dim);
                    if (rt.isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", "PAR_OPEN", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL"})) {

                                if (Class_Body_2_0_1()) {

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean Class_Body_3_0(String name, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type)
    {boolean ass = false;
        if(compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"}))
        {
            ReturnValue rt = File_Init(classScope.peek(), -1, type);
            if(rt.isBool())
            {
                ass = true;
                if(!insert(classScope.peek(), -1, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                    return false;
                if(compareString(new String[]{"TERMINATOR", "ARG_DEL"}))
                {
                    if(List_6(classScope.peek(), -1, am, stat, finl, sizeMod, signMod, type))
                        return true;
                }
            }
        }
        else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            if (Class_Body_Dash()) {

                return true;
            }
        }
        
        return false;
    }

    public boolean Inner_Class() {
        
     if (compareString(new String[]{"ACCESS_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN"})) {
         
         if(AM().isBool()){
             
             if(compareString(new String [] {"STATIC", "CLASS"})){
                 
                if(Inner_Class_Extra()){
                     
                    return true;
                }
             }
         }
     }
     
     else if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
         
         return true;
     }

        
        return false;
    } 
    
    public boolean Inner_Class_Extra(){
        
        if(tokens.get(index).getCP().equals("STATIC")){
            
            this.index ++;
            
            if(tokens.get(index).getCP().equals("CLASS")){
                
                this.index ++;
                
                if(tokens.get(index).getCP().equals("ID")){
                    
                    this.index ++;
                    
                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                        
                        this.index ++;
                        
                        if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"})) {
                            
                            if(Class_Body()){
                                
                                if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
                                    
                                    this.index ++;
                                    
                                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                                        
                                        this.index ++;
                                        
                                        if (compareString(new String [] {"ACCESS_MOD", "STATIC", "CLASS", "BRACES_CLOSE"})) { 
                                            
                                            if(Inner_Class()){
                                                
                                                return true;
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        
        else  if(tokens.get(index).getCP().equals("CLASS")){
                
              this.index ++;
                
                if(tokens.get(index).getCP().equals("ID")){
                    
                    this.index ++;
                    
                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                        
                        this.index ++;
                        
                        if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE" , "CLASS", "BRACES_CLOSE"})) {
                            
                            if(Class_Body()){
                                
                                if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
                                    
                                    this.index ++;
                                    
                                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                                        
                                        this.index ++;
                                        
                                        if (compareString(new String [] {"ACCESS_MOD", "STATIC", "CLASS", "BRACES_CLOSE", "TERMINATOR", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "BRACES_CLOSE"})) { 
                                            
                                            if(Inner_Class()){
                                                
                                                return true;
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
            }
        
        return false;
    }
    
    public boolean Class(){
        if (compareString(new String[]{"ACCESS_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN"})) {
            ReturnValue rt = AM();
            if(rt.isBool()){
                if(rt.getValue(0).equals("protected"))
                {
                    printErrorMessage("Use of \"protected\" in class declaration is illegal", index);
                    return false;
                }
                if(compareString(new String [] {"FINAL", "CLASS"})){

                    if(Class_Dash(rt.getValue(0))){

                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean Class_Dash(String am){
        boolean finl = false, stat = false;
        String name;
        
        if(tokens.get(index).getCP().equals("FINAL")){
            finl = true;
            this.index ++;
            if(tokens.get(index).getCP().equals("CLASS")){
                this.index++;
                if(tokens.get(index).getCP().equals("ID")){
                    name = tokens.get(index).getVP();
                    this.index ++;
                    if(compareString(new String [] {"EXTENDS", "TERMINATOR"})){
                        ReturnValue inhClass = Inherited_Class();
                        if(inhClass.isBool()){
                            if(!insert(name, am, stat, finl, inhClass.getValue(1), inhClass.getValue(0), null, null))
                                return false;
                            if(tokens.get(index).getCP().equals("TERMINATOR")){
                                this.index ++;
                                if(tokens.get(index).getCP().equals("BRACES_OPEN")){
                                    this.index ++;
                                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                                        this.index ++;
                                        createClassScope(cTable.size() - 1);
                                         if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE" , "CLASS", "BRACES_CLOSE"})) {
                                             if(Class_Body()){
                                                 if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
                                                     this.index ++;                                                     
                                                     if(tokens.get(index).getCP().equals("TERMINATOR")){
                                                         this.index ++;
                                                         destroyClassScope();
                                                         if(compareString(new String [] {"ACCESS_MOD", "FINAL", "CLASS", "$"})){
                                                             
                                                             if(More_Class()){
                                                                 
                                                                 return true;
                                                             }
                                                         }
                                                     }
                                                 }
                                             }
                                         }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(tokens.get(index).getCP().equals("CLASS")){
                this.index ++;       
                if(tokens.get(index).getCP().equals("ID")){
                    name = tokens.get(index).getVP();
                    this.index ++;
                    if(compareString(new String [] {"EXTENDS", "TERMINATOR"})){
                        ReturnValue inhClass = Inherited_Class();
                        if(inhClass.isBool()){
                            if(!insert(name, am, stat, finl, inhClass.getValue(1), inhClass.getValue(0), null, null))
                                return false;
                            if(tokens.get(index).getCP().equals("TERMINATOR")){
                                this.index ++;
                                createClassScope(cTable.size() - 1);
                                if(tokens.get(index).getCP().equals("BRACES_OPEN")){
                                    
                                    this.index ++;
                                    
                                    if(tokens.get(index).getCP().equals("TERMINATOR")){
                                        
                                        this.index ++;
                                        
                                        
                                         if (compareString(new String[]{"TERMINATOR", "ACCESS_MOD", "STATIC", "FINAL","SIGN_MOD", "SIZE_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "VOID", "ID", "ABSTRACT", "OVERRIDE", "CLASS", "BRACES_CLOSE"})) {
                                             
                                             if(Class_Body()){
                                                 
                                                 
                                                 if(tokens.get(index).getCP().equals("BRACES_CLOSE")){
                                                     
                                                     this.index ++;
                                                     
                                                     if(tokens.get(index).getCP().equals("TERMINATOR")){
                                                         this.index ++;
                                                         destroyClassScope();
                                                         if(compareString(new String [] {"ACCESS_MOD", "FINAL", "CLASS", "$"})){
                                                             
                                                             if(More_Class()){
                                                                 
                                                                 return true;
                                                             }
                                                         }
                                                     }
                                                 }
                                             }
                                         }

                                    }
                                }
                            }
                        }
                    }
                }
            }

        return false;
    }
    
    
    public boolean More_Class(){
        
        if(compareString(new String [] {"ACCESS_MOD", "FINAL", "CLASS"})){
            
            if(Class()){
                
                return true;
            }
        }
        
        else if(compareString(new String [] {"ACCESS_MOD", "FINAL", "CLASS", "$"})){
            
            return true;
        }
        return false;
    }
    
    public ReturnValue Inherited_Class()
    {
        if(this.tokens.get(index).getCP().equals("EXTENDS"))
        {
            this.index++;
            if(compareString(new String[]{"VIRTUAL", "ID"}))
            {
                ReturnValue rt = Inherited_Class_Dash();
                if(rt.isBool())
                    return new ReturnValue(true, rt.getValues());
            }
        }
        else if(compareString(new String[]{"TERMINATOR", "EXTENDS"}))   //for null
            return new ReturnValue(true, new String[]{null, null});
        
        return new ReturnValue(false, null);
    }
    
    public ReturnValue Inherited_Class_Dash(){
        String v = null;
        String e = null;
        if(tokens.get(index).getCP().equals("VIRTUAL")){
            this.index ++;
            if(tokens.get(index).getCP().equals("ID")){
                v = tokens.get(index).getVP();
                this.index ++;
                if(compareString(new String [] {"VIRTUAL", "ARG_DEL", "TERMINATOR"})){
                    ReturnValue rt = More_Inherited_Class(v, e);
                    if(rt.isBool()){
                        return new ReturnValue(true, rt.getValues());
                    }
                }
            }
        }
        
        else if(tokens.get(index).getCP().equals("ID")){
            e = tokens.get(index).getVP();
            this.index ++;
            if(compareString(new String [] {"ARG_DEL", "TERMINATOR"})){
                ReturnValue rt = More_Inherited_Class(v, e);
                if(rt.isBool()){
                    return new ReturnValue(true, rt.getValues());
                }
            }

        }
        
        return new ReturnValue(false, null);
    }
    
    public ReturnValue More_Inherited_Class(String v, String e){
        
        if(this.tokens.get(index).getCP().equals("ARG_DEL"))
        {
            this.index++;
            if(compareString(new String[]{"VIRTUAL", "ID"})){
                ReturnValue rt = More_Inherited_Class_Extra(v, e);
                if(rt.isBool())
                    return new ReturnValue(true, rt.getValues());
            }
        }
        else if(tokens.get(index).getCP().equals("TERMINATOR"))     //for null
        {  
            return new ReturnValue(true, new String[]{v, e});
        }
        
        return new ReturnValue(false, null);
    }

    public ReturnValue More_Inherited_Class_Extra(String v, String e)
    {
        if(tokens.get(index).getCP().equals("VIRTUAL")){ 
            this.index ++;
            if(tokens.get(index).getCP().equals("ID")){
                v = (v == null)? tokens.get(index).getVP(): v + "," + tokens.get(index).getVP();
                this.index ++;
                if(compareString(new String [] {"ARG_DEL", "TERMINATOR"})){
                    ReturnValue rt = More_Inherited_Class(v, e);
                    if(rt.isBool()){
                        
                        return new ReturnValue(true, rt.getValues());
                    }
                }

            }
        }
        else if(tokens.get(index).getCP().equals("ID"))
        {
            e = (e == null)? tokens.get(index).getVP(): e + "," + tokens.get(index).getVP();
            this.index ++;
            if(compareString(new String [] {"ARG_DEL", "TERMINATOR"})){
                ReturnValue rt = More_Inherited_Class(v, e);
                if(rt.isBool()){
                    return new ReturnValue(true, rt.getValues());
                }
            }

        }
        
        
        return new ReturnValue(false, null);
    }

    // DT start
    public boolean DT_0() {

        if (tokens.get(index).getCP().equals("DATA_TYPES")) {

            if (Primitive_Type().isBool()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("ID")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("STRING")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("FILE")) {

            this.index++;
            return true;
        }
        return false;
    }

    public boolean DT() {

        if (compareString(new String[]{"DATA_TYPES", "ID", "STRING", "FILE"})) {

            if (DT_0()) {

                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {

                    if (Multi_Array_Dec(-1).isBool()) {

                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("COLLECTION")) {

            if (Collection_Type()) {

                return true;
            }
        }
        return false;
    }
    // DT end

    //parameter list start
    public boolean Parameter_List() {

        if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "ID", "STRING", "FILE"})) {
            if (DT()) {
                if(this.tokens.get(index).getCP().equals("ID"))
                    {
                        this.index++;
                        if (compareString(new String[]{"ARG_DEL", "PAR_CLOSE"})) {
                            if (More_Parameter_List()) {
                                return true;
                            }
                        }
                    }
            }
        } else if (tokens.get(index).getCP().equals("VOID")) {
            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("PAR_CLOSE")) {       //for null
            return true;
        }
        return false;
    }

    public boolean More_Parameter_List() {
        if (tokens.get(index).getCP().equals("ARG_DEL")) {
            this.index++;
            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "ID", "STRING", "FILE"})) {
                if (DT()) {
                    if(this.tokens.get(index).getCP().equals("ID"))
                    {
                        this.index++;
                        if (compareString(new String[]{"ARG_DEL", "PAR_CLOSE"})) {
                            if (More_Parameter_List()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_CLOSE")) {   //for null
            return true;
        }

        return false;
    }
    //parameter list end

    // non access combi start
    public ReturnValue Non_Access_Combination() {
        String stat = "false", finl = "false";
        if (tokens.get(index).getCP().equals("STATIC")) {
            stat = "true";
            this.index++;
            if (compareString(new String[]{"STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
                ReturnValue rt = Non_Access_CombinationDash();
                if (rt.isBool()) {
                    finl = rt.getValue(0);
                    return new ReturnValue(true, new String[]{stat, finl});
                }
            }
        } else if (tokens.get(index).getCP().equals("FINAL")) {
            finl = "true";
            this.index++;
            if (compareString(new String[]{"STATIC", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
                ReturnValue rt = Non_Access_Combination2Dash();
                if (rt.isBool()) {
                    stat = rt.getValue(0);
                    return new ReturnValue(true, new String[]{stat, finl});
                }
            }
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {       //for null

            return new ReturnValue(true, new String[]{stat, finl});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Non_Access_CombinationDash() {

        if (tokens.get(index).getCP().equals("FINAL")) {
            this.index++;
            return new ReturnValue(true, new String[]{"true"});
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {  // for null
            return new ReturnValue(true, new String[]{"false"});
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Non_Access_Combination2Dash() {

        if (tokens.get(index).getCP().equals("STATIC")) {
            this.index++;
            return new ReturnValue(true, new String[]{"true"});
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
            return new ReturnValue(true, new String[]{"false"});
        }

        return new ReturnValue(false, new String[]{""});
    }
    // non access combi end

    public ReturnValue AM() {

        if (tokens.get(index).getCP().equals("ACCESS_MOD")) {

            this.index++;
            return new ReturnValue(true, new String[]{tokens.get(index - 1).getVP()});
        } else if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN", "BRACES_CLOSE"})) {   // for null
            return new ReturnValue(true, new String[]{"public"});
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Size_Mod() {

        if (tokens.get(index).getCP().equals("SIZE_MOD")) {

            this.index++;
            return new ReturnValue(true, new String[]{this.tokens.get(this.index - 1).getVP()});
        } else if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {   // for null
            return new ReturnValue(true, new String[]{"short"});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Sign_Mod() {

        if (tokens.get(index).getCP().equals("SIGN_MOD")) {

            this.index++;
            return new ReturnValue(true, new String[]{this.tokens.get(this.index - 1).getVP()});
        }

        if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

            return new ReturnValue(true, new String[]{"signed"});
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public boolean inh() {

        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"PROP_ACCESS", "ARG_DEL", "TERMINATOR", "ID"})) {

                    if (inh()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"ARG_DEL", "TERMINATOR", "ID"})) {  //for null
            return true;
        }
        return false;
    }

    public boolean Reference_Type() {

        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"PROP_ACCESS", "ARG_DEL", "TERMINATOR", "ID"})) {

                if (inh()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean More_Exceptions() {

        if (tokens.get(index).getCP().equals("ARG_DEL")) {
            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                if (Reference_Type()) {

                    if (compareString(new String[]{"ARG_DEL", "TERMINATOR"})) {

                        if (More_Exceptions()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("TERMINATOR")) { // for null

            return true;
        }
        return false;
    }

    public boolean Exception_List() {

        if (tokens.get(index).getCP().equals("THROWS")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                if (Reference_Type()) {

                    if (compareString(new String[]{"ARG_DEL", "TERMINATOR"})) {

                        if (More_Exceptions()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("TERMINATOR")) { // for null

            return true;
        }
        return false;
    }

    public boolean RT() {

        if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "ID", "STRING", "FILE"})) {

            if (DT()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("VOID")) {
            return true;
        }
        return false;
    }

    public boolean MST() {

        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER"})) {

            if (SST()) {

                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                    if (MST()) {

                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"BRACES_CLOSE", "BREAK"})) {
            return true;
        }
        return false;
    }

    //primitive init start
    public ReturnValue Primitive_Init(int cc, int cs, String type) {

        if (tokens.get(index).getCP().equals("AO")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "CURRENT", "SUPER", "ID"})) {
                ReturnValue rt = Primitive_Init_2(cc, cs, type);
                if (rt.isBool()) {
                    String ass = rt.getValue(0);
                    return new ReturnValue(true, new String[]{ass});
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"})) {

            return new ReturnValue(true, new String[]{"false"});
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Primitive_Init_2(int cc, int cs, String type) {

        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})) {
            ReturnValue rt = Const();
            if (rt.isBool()) { //need work
                String type2 = rt.getValue(0);
                if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                    rt = Exp_Dash(type2, cc, cs);
                    if (rt.isBool()) {
                        type2 = rt.getValue(0);
                        rt = compatability(type, type2, ":");
                        if(rt.isBool())
                        {
                            return new ReturnValue(true, new String[]{"true"});
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NOT")) {
            String op = this.tokens.get(index).getVP();
            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    type = rt.getValue(0);
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(type, cc, cs);
                        if (rt.isBool()) {   
                            String type2 = rt.getValue(0);
                            if(compatability(type, type2, ":").isBool())
                            {
                                return new ReturnValue(true, new String[]{"true"});
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            this.index++;

            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                ReturnValue rt = Left_Side(cc, cs);
                if (rt.isBool()) {
                    type = rt.getValue(0);
                    rt = compatability(type, op);
                    if(!rt.isBool())
                        return rt;
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(rt.getValue(0), cc, cs);
                        if (rt.isBool()) {
                            String type2 = rt.getValue(0);
                            if(compatability(type, type2, ":").isBool())
                            {
                                return new ReturnValue(true, new String[]{"true"});
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                        if (Argument_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;

                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                                    if (More_Constructor_Call()) {

                                        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

                                            this.index++;

                                            if (tokens.get(index).getCP().equals("ID")) {

                                                this.index++;
                                                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN","INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL", ";"})) {

                                                    if (Primitive_Init_3(-1, -1, "", "", -1).isBool()) {

                                                        return new ReturnValue(true, new String[]{""}); //need work
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }

                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"DATA_TYPES", "STRING", "FILE", "COLLECTION", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "ID"})) {
                ReturnValue rt = F_5(cc, cs);
                if (rt.isBool()) {
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        rt = Exp_Dash(rt.getValue(0), cc, cs);
                        if (rt.isBool()) { 
                            String type2 = rt.getValue(0);
                            if(compatability(type, type2, ":").isBool())
                            {
                                return new ReturnValue(true, new String[]{"true"});
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {
            ReturnValue rt = C(cc, cs);
            if (rt.isBool()) {
                cc = Integer.parseInt(rt.getValue(0));
                cs = Integer.parseInt(rt.getValue(1));
                if (tokens.get(index).getCP().equals("ID")) {
                    String name = this.tokens.get(index).getVP();
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN","INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL", ";"})) {
                        rt = Primitive_Init_3(cc, cs, name, type, 0);
                        if (rt.isBool()) {
                            String type2 = rt.getValue(0);
                            return  rt;
//                            if(compatability(type, type2, ":").isBool())
//                            {
//                                return new ReturnValue(true, new String[]{"true"});
//                            }
                        }
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Primitive_Init_3(int cc, int cs, String name, String type, int dim) {

        if (compareString(new String[]{"INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL"})) {
            ReturnValue rt = Primitive_Init_4(cc, cs, name, type, dim);
            if (rt.isBool()) {
                return rt;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = EXP(cc, cs);
                if (rt.isBool()) {
                    if(!compareString(rt.getValue(0), new String[]{"INT_CONST", "CHAR_CONST", "int", "char"}))
                    {
                        printErrorMessage("Index should be Convertible data type", this.index);
                        return new ReturnValue(false, new String[]{""});
                    }
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        dim++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            rt = More_Array_Access(dim);
                            if (rt.isBool()) {
                                dim = Integer.parseInt(rt.getValue(0));
                                if (compareString(new String[]{"INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL"})) {
                                    rt = Primitive_Init_4(cc, cs, name, type, dim);
                                    if (rt.isBool()) {
                                        return rt;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                if (Argument_List()) {

                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "TERMINATOR", "ARG_DEL"})) {
                                    if (Primitive_Init_5()) {
                                        return new ReturnValue(true, new String[]{""}); // need work
                                    }
                                }
                            }
                        }
                        
                    }
                }

            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue Primitive_Init_4(int cc, int cs, String name, String type, int dim) {

        if (tokens.get(index).getCP().equals("INC_DEC")) {
            String op = this.tokens.get(index).getVP();
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            this.index++;
            if(!isAssigned(name, cc, cs, 0))
            {
                printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                return new ReturnValue(false, new String[]{"false"});
            }
            String type2 = getType(name, cc, cs, 0);
            ReturnValue rt = compatability(type2, op);
            type2 = rt.getValue(0);
            if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                rt = Exp_Dash(type2, cc, cs);
                if (rt.isBool()) {
                    type2 = rt.getValue(0);
                    if(compatability(type, type2, ":").isBool())
                        return new ReturnValue(true, new String[]{"true"});
                }
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

            this.index++;
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            
            String type2 = getType(name, cc, cs, 0);
            String[] w = type2.split("[");
            if (tokens.get(index).getCP().equals("ID")) {
                cc = indexOfClass(w[0]);
                name = tokens.get(index).getVP();
                cs = -1;
                this.index++;
                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN","INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL", ";"})) {
                    ReturnValue rt = Primitive_Init_3(cc, cs, name, type, dim);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        } else if (compareString(new String[]{"AO"})) {
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            
            String type2 = getType(name, cc, cs, 0);
            if(!compatability(type, type2, ":").isBool())
                return new ReturnValue(false, new String[]{""});
            ReturnValue rt = Primitive_Init(cc, cs, type);
            if (rt.isBool()) {
                return rt;
            }

        } else if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
            if(!lookup(name, cc, cs, 0))
            {
                printErrorMessage("\""+ name +"\" is not defined", this.index);
                return new ReturnValue(false, new String[]{""});
            }
            
            String type2 = getType(name, cc, cs, 0);
            ReturnValue rt = Exp_Dash(type2, cc, cs);
                if (rt.isBool()) {
                    type2 = rt.getValue(0);
                    if(compatability(type, type2, ":").isBool())
                        if(isAssigned(name, cc, cs, 0))
                            return new ReturnValue(true, new String[]{"true"});
                        else
                        {
                            printErrorMessage("Variable \""+ name +"\" is un-assigned", index);
                            return new ReturnValue(false, new String[]{"false"});
                        }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean Primitive_Init_5() {

        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {

            if (Exp_Dash("", -1, -1).isBool()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN","INC_DEC","PROP_ACCESS","AO","OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", "TERMINATOR", "ARG_DEL", ";"})) {

                    if (Primitive_Init_3(-1, -1, "", "", -1).isBool()) {

                        return true;
                    }
                }
            }
        }

        return false;
    }
    //primitive init end
    
    
    
    
        
    public boolean Assignment(){
        
        
        if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
            if (Left_Side(-1, -1).isBool()) {
                if(tokens.get(index).getCP().equals("AO")){
                        
                    this.index ++;
                    
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                        
                        if(Assignment_2()){
                            
                            return true;
                        }
                    }
                }
            }
        }
        
        
        return false;
    }
    
    public boolean Assignment_Dash(){
        
        
        if(compareString(new String [] {"NEW", "CURRENT", "SUPER", "ID"})){
            
            if(Assignment()){
                
                if(tokens.get(index).getCP().equals("TERMINATOR")){
                    
                    this.index ++;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean Assignment_2(){
        
        if(compareString(new String [] {"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})){
            
            
            if(Const().isBool()){   //need for
                
                if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";" , "ARR_DEL_CLOSE"})) {
                  
                    if (Exp_Dash("", -1, -1).isBool()) {
                      return true;
                    }
                }
            }
        }
        
        else if(tokens.get(index).getCP().equals("NOT")){
            
            this.index ++;
            
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                    
                    if(F(-1, -1).isBool()){
                        
                        
                        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                  
                            if (Exp_Dash("", -1, -1).isBool()) {
                                return true;
                            }
                         }
                        
                        
                    }
                    
                }

            
        }
        
        
        
        else if(tokens.get(index).getCP().equals("INC_DEC")){
            
            this.index ++;
            
            
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})){
                
                if(Left_Side(-1, -1).isBool()){
                    
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                  
                        if (Exp_Dash("", -1, -1).isBool()) {
                            return true;
                        }
                    }
                    
                }
            }
        }
        
        
        
        else if(compareString(new String[]{"CURRENT", "SUPER", "ID"})){
            
            
            if(C(-1, -1).isBool()){
                
                if(tokens.get(index).getCP().equals("ID")){
                    
                    this.index ++;
                    
                    if(compareString(new String [] {"AO", "PROP_ACCESS", "INC_DEC", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", ";"})){
                        
                        if(Assignment_3()){
                            
                            return true;
                        }
                    }
                }
            }
        }
        
        
        
        else if(tokens.get(index).getCP().equals("NEW")){
            
            this.index ++;
            
            if(compareString(new String [] {"COLLECTION" , "DATA_TYPES", "STRING", "ID", "FILE"})){
                
                if(Assignment_6()){
                    
                    return true;
                }
            }
        }
        
        
        else if(tokens.get(index).getCP().equals("PAR_OPEN")){
            
            
            this.index ++;
            
            if (compareString(new String[]{"DATA_TYPES", "STRING", "FILE", "COLLECTION", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "NEW", "PAR_OPEN", "ID"})) {
            
                if(F_5(-1, -1).isBool()){   //need work
                    
                    
                    if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                        if (Exp_Dash("", -1, -1).isBool()) {
                            return true;
                        }
                    }
                }
            
            }
        }
        else if(tokens.get(index).getCP().equals("BRACES_OPEN")){            
            this.index ++;
            if(compareString(new String [] {"BRACES_OPEN", "SUPER", "CURRENT", "NEW", "ID", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "PAR_OPEN", "BRACES_CLOSE"})){

                if(Array_Init_Dash_Extra()){

                    return true;
                }
            }
        }
        else if(tokens.get(index).getCP().equals("NULL"))
        {
            this.index++;
            return true;
        }
        return false;
    }
    
    public boolean Assignment_3(){
        
        if(compareString(new String [] {"AO", "PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL",";"})){
            
            if(Assignment_4()){
                
                return true;
            }
        }
        
        
        else if(tokens.get(index).getCP().equals("ARR_DEL_OPEN")){
            
            this.index ++;
            
             if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                    if (EXP(-1, -1).isBool()) {
                        
                        
                        if(tokens.get(index).getCP().equals("ARR_DEL_CLOSE")){
                            
                            this.index ++;
                            
                            
                               if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                              
                                   if(More_Array_Access(-1).isBool()){
                                       
                                       
                                       if(compareString(new String [] {"AO", "PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL",";"})){
                                           
                                           if(Assignment_4()){
                                               
                                               return true;
                                           }
                                       }
                                   }
                               
                               }
                            
                        }
                    }
              }
             
        
        }
        
        
        else if(tokens.get(index).getCP().equals("PAR_OPEN")){
            this.index ++;
             if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                 if(Argument_List()){
                     if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                         this.index ++;
                         if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                             if(More_Array_Access(-1).isBool()){
                                 if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                                    if(Assignment_5()){
                                        return true;
                                    }
                                }
                             }
                         }
                     }
                 }
             }
        }
        else if(compareString(new String[]{";"}))   //for null
            return true;
        
        return false;
    }
    
    public boolean Assignment_4(){
        
        
        
        if(tokens.get(index).getCP().equals("AO")){
            
            this.index ++;
            
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                        
                if(Assignment_2()){
                            
                    return true;
                 }
             }
        }
        
        else if(tokens.get(index).getCP().equals("PROP_ACCESS")){
            
            this.index ++;
            
            if(tokens.get(index).getCP().equals("ID")){
                
                this.index ++;
                
                if(compareString(new String [] {"AO", "PROP_ACCESS", "INC_DEC", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", ";"})){
                    
                    if(Assignment_3()){
                        
                        return true;
                    }
                }
            }
        }
        
        else if(tokens.get(index).getCP().equals("INC_DEC")){
            
            this.index ++;
            
            if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
                if (Exp_Dash("", -1, -1).isBool()) {
                   return true;
                }
            }
        }
        
        
        else  if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
            if (Exp_Dash("", -1, -1).isBool()) {
                return true;
            }
        
        }
        else if(compareString(new String[]{";"}))   //for null
            return true;
        
        return false;
    }
    
    public boolean Assignment_5(){
        
        if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {
            if (Exp_Dash("", -1, -1).isBool()) {
                return true;
            }
        }
        
        
        else if(tokens.get(index).getCP().equals("PROP_ACCESS")){
            
            this.index ++;
            
            if(tokens.get(index).getCP().equals("ID")){
                
                this.index ++;
                
                if(compareString(new String [] {"AO", "PROP_ACCESS", "INC_DEC", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", ";"})){
                    
                    if(Assignment_3()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean Assignment_6(){
        if(this.tokens.get(index).getCP().equals("COLLECTION"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("LIST_PAR_OPEN"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("LIST_PAR_CLOSE"))
                {
                    this.index++;
                    if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
                    {
                        this.index++;
                        if(compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                        {
                            if(EXP(-1, -1).isBool())
                            {
                               if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                               {
                                   this.index++;
                                   return true;
                               }
                            }
                        }
                        else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
                    }
                }
            }
            else if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) 
                {
                    if (EXP(-1, -1).isBool())
                    {
                        if (this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
                    }
                } 
                else if (this.tokens.get(index).getCP().equals("PAR_CLOSE")) 
                {
                    this.index++;
                    return true;
                }
            }
            
        }
        else if(compareString(new String[]{"DATA_TYPES"}))
        {
            if(Primitive_Type().isBool())
            {
                if(this.tokens.get(index).getCP().equals("ARR_DEL_OPEN"))
                {
                    this.index++;
                    if(compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"}))
                    {
                        if(Array_Init_4_Dash())
                            return true;
                    }
                }
            }
        }
        else if(this.tokens.get(index).getCP().equals("STRING") || this.tokens.get(index).getCP().equals("FILE"))
        {
            this.index++;
            if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"}))
            {
                if(Assignment_9())
                    return true;
            }
        }
        else if(this.tokens.get(index).getCP().equals("ID"))
        {
            this.index++;
            if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"}))
            {
                if(Assignment_7())
                    return true;
            }
        }
        
        return false;
    }
     
    public boolean Assignment_7(){
        if(tokens.get(index).getCP().equals("ARR_DEL_OPEN")){    
            this.index ++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if(Array_Init_4_Dash()){
                    return true;
                }            
            }
            
        }
        else if(tokens.get(index).getCP().equals("PAR_OPEN")){    
            this.index ++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if(Argument_List()){
                    if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                        this.index ++;
                        if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                            if (More_Constructor_Call()) {
                                if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                                    if(Assignment_8()){   
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                 }
             }
        }
        
        return false;
    }
    
    public boolean Assignment_8()
    {
        if(this.tokens.get(index).getCP().equals("PROP_ACCESS"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if(compareString(new String [] {"AO", "PROP_ACCESS", "INC_DEC", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP", ";"})){       
                    if(Assignment_3()){        
                        return true;
                    }
                }
            }
        }
        else if(compareString(new String [] {"NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){    //for null
            return true;
        }
        
        return false;
    }
    
    public boolean Assignment_9()
    {
        if(tokens.get(index).getCP().equals("ARR_DEL_OPEN")){    
            this.index ++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if(Array_Init_4_Dash()){
                    return true;
                }            
            }
            
        }
        else if(tokens.get(index).getCP().equals("PAR_OPEN")){    
            this.index ++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NEW", "CURRENT", "SUPER", "ID"}))
            {
                if(String_Argument_List())
                {
                    if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                        this.index ++;
                        if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                            if(Assignment_10()){   
                                return true;
                            }
                        }
                    }
                }
            }
            else if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                this.index ++;
                if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                    if(Assignment_10()){   
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean Assignment_10()
    {
        if(this.tokens.get(index).getCP().equals("PROP_ACCESS"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if(tokens.get(index).getCP().equals("PAR_OPEN")){    
                this.index ++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                    if(Argument_List()){
                        if(tokens.get(index).getCP().equals("PAR_CLOSE")){
                            this.index ++;
                            if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                                if(Assignment_10()){   
                                    return true;
                                }
                            }
                        }
                     }
                 }
            }
                
            }
        }
        else if(compareString(new String [] {"NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
            return true;
        }
        
        return false;
    }
    
    //********************************************HM*****************************************************************
    
    
    
    //********************** EXPRESSION CFG's *******************************************************
    public ReturnValue Exp_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"OR", "AND", "AND_BIN", "XOR", "OR_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = E_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        }
        else if (compareString(new String[]{"TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", "BRACES_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(false, new String[]{});
    }

    public ReturnValue E_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("OR")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = E_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_1_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = E_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue E_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = E_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_1_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = E_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue E_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("OR")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = E_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});

    }

    public ReturnValue E_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = E_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_1_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = E_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_1() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_1_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_1_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("AND")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_1_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_2_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_1_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_1_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_1_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_2_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_1_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_1_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("AND")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_1_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }

        } else if (compareString(new String[]{"OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_1_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_1_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_2_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_1_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_2() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_2_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_2_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("OR_BIN")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "MDM", "PM", "EXP", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_2_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_3_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_2_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_2_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_2_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_3_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_2_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_2_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("OR_BIN")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "MDM", "PM", "EXP", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_2_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_2_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_2_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_3_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_2_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_3() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_3_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_3_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("XOR")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_3_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_4_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_3_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_3_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_3_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_4_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_3_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_3_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("XOR")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_3_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_3_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_3_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_4_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_3_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_4() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_4_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_4_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("AND_BIN")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_4_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_5_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_4_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_4_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_4_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_5_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_4_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_4_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("AND_BIN")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_2_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_4_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_4_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_5_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_4_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_5() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_5_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_5_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("RO_EQUALITY")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_5_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_6_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_5_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_5_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("RO_EQUALITY")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_5_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_5_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_5_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_6_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_5_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_5_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_5_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_6_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_5_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_6() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_6_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_6_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("RO")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_6_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_7_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_6_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_6_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_6_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_7_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_6_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_6_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("RO")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"RO", "SHIFT_BIN", "PM", "MDM", "EXP", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_6_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_6_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_6_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP"})) {
            ReturnValue rt = T_7_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_6_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_7() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP"})) {
                    if (T_7_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_7_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("SHIFT_BIN")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_7_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"PM", "MDM", "EXP"})) {
            ReturnValue rt = T_8_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_7_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_7_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_7_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"PM", "MDM", "EXP"})) {
            ReturnValue rt = T_8_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_7_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_7_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("SHIFT_BIN")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"SHIFT_BIN", "PM", "MDM", "EXP", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_7_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_7_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_7_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"PM", "MDM", "EXP"})) {
            ReturnValue rt = T_8_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_7_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_8() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"PM", "MDM", "EXP"})) {
                    if (T_8_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_8_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("PM")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"PM", "MDM", "EXP", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_8_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"MDM", "EXP"})) {
            ReturnValue rt = T_9_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_8_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_8_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_8_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"MDM", "EXP"})) {
            ReturnValue rt = T_9_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_8_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_8_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("PM")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"PM", "MDM", "EXP", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_8_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
           return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(true, new String[]{type});
    }

    public ReturnValue T_8_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_8_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (compareString(new String[]{"MDM", "EXP"})) {
            ReturnValue rt = T_9_0_Dash(type, cc, cs);
            if (rt.isBool()) {
                type = rt.getValue(0);
                if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                    rt = T_2_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_9() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (compareString(new String[]{"MDM", "EXP"})) {
                    if (T_9_0_Dash("", -1, -1).isBool()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_9_0_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("MDM")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"MDM", "EXP", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_9_0_2_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("EXP")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = T_10_Dash(cc, cs);
                if (rt.isBool()) {
                    rt = compatability(type, rt.getValue(0), op);
                    type = rt.getValue(0);
                    if (compareString(new String[]{"MDM", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_9_Dash(type, cc, cs);
                        if (rt.isBool()) {
                            return rt;
                        }
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_9_0_2_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"MDM", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_9_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (tokens.get(index).getCP().equals("EXP")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = T_10_Dash(cc, cs);
                if (rt.isBool()) {
                    rt = compatability(type, rt.getValue(0), op);
                    type = rt.getValue(0);
                    if (compareString(new String[]{"MDM", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_9_Dash(type, cc, cs);
                        if (rt.isBool()) {
                            return rt;
                        }
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_9_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("MDM")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type2 = rt.getValue(0);
                    if (compareString(new String[]{"MDM", "EXP", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_9_1_Dash(type2, cc, cs);
                        if (rt.isBool()) {
                            type2 = rt.getValue(0);
                            return compatability(type, type2, op);
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_9_1_Dash(String type, int cc, int cs) {
        if (compareString(new String[]{"MDM", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
            ReturnValue rt = T_9_Dash(type, cc, cs);
            if (rt.isBool()) {
                return rt;
            }
        } else if (tokens.get(index).getCP().equals("EXP")) {
            String op = tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = T_10_Dash(cc, cs);
                if (rt.isBool()) {
                    rt = compatability(type, rt.getValue(0), op);
                    type = rt.getValue(0);
                    if (compareString(new String[]{"MDM", "PM", "SHIFT_BIN", "RO", "RO_EQUALITY", "AND_BIN", "XOR", "OR_BIN", "AND", "OR", "BRACES_CLOSE", "TERMINATOR", "ARG_DEL", "ARR_DEL_CLOSE", "PAR_CLOSE", ";"})) {
                        rt = T_9_Dash(type, cc, cs);
                        if (rt.isBool()) {
                            return rt;
                        }
                    }
                }
            }
        }
        return new ReturnValue(false, new String[]{""});
    }

    public boolean T_10() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (F(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("EXP")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"})) {
                        if (T_10_Dash(-1, -1).isBool()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ReturnValue T_10_Dash(int cc, int cs) {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            ReturnValue rt = F(cc, cs);
                if (rt.isBool()) {
                    String type = rt.getValue(0);
                if (compareString(new String[]{"EXP", "OR", "AND", "AND_BIN", "XOR", "OR_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "PAR_CLOSE", "BRACES_CLOSE","TERMINATOR", "ARG_DEL", ";", "ARR_DEL_CLOSE"})) {
                    rt = T_10_2_Dash(type, cc, cs);
                    if (rt.isBool()) {
                        return rt;
                    }
                }
            }
        }
        
        return new ReturnValue(false, new String[]{""});
    }

    public ReturnValue T_10_2_Dash(String type, int cc, int cs) {
        if (tokens.get(index).getCP().equals("EXP")) {
            String op = this.tokens.get(index).getVP();
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                ReturnValue rt = T_10_Dash(cc, cs);
                if (rt.isBool()) {
                    return compatability(type, rt.getValue(0), op);
                }
            }
        } else if (compareString(new String[]{"OR", "AND", "AND_BIN", "XOR", "OR_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "PAR_CLOSE", "BRACES_CLOSE","TERMINATOR", "ARG_DEL", ";", "ARR_DEL_CLOSE"})) { // FOR NULL
            return new ReturnValue(true, new String[]{type});
        }
        
        return new ReturnValue(false, new String[]{""});
    }
    //*************************** EXPRESSION CFG's end*******************************************************************

   
    
    
    
    public boolean SST() {
        if (compareString(new String[]{"UNTIL"})) {
            if (While_St()) {
                return true;
            }
        } else if (compareString(new String[]{"IF"})) {
            if (If_Else_St()) {
                return true;
            }
        } else if (compareString(new String[]{"FOR"})) {
            if (For_St()) {
                return true;
            }
        } else if (compareString(new String[]{"DO"})) {
            if (Do_While_St()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            if (Left_Side(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                    this.index++;
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("WITHDRAW")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN"}))
            {
                if(Values())
                {
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("TERMINATOR")) {
            this.index++;
            return true;
        } else if (compareString(new String[]{"TRY"})) {
            if (Try_St()) {
                return true;
            }
        } else if (compareString(new String[]{"ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION"})) {
            if (Decl_Dash()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("THROW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("NEW")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                        this.index++;
                        if (tokens.get(index).getCP().equals("STRING_CONST")) {
                            this.index++;
                            if(compareString(new String[]{"PM", "TERMINATOR", "ARG_DEL", "PAR_CLOSE"}))
                            {
                                if(String_Init_6())
                                {
                                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                        this.index++;
                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                            this.index++;
                                            return true;
                                        }
                                    }
                                }
                            }
                        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
                        {
                            this.index++;
                            if(compareString(new String[]{"PM"}))
                            {
                                if(String_Init_7())
                                {
                                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                        this.index++;
                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                            this.index++;
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"FOR_EACH"})) {
            if (For_Each_St()) {
                return true;
            }
        } else if (compareString(new String[]{"SWITCH"})) {
            if (Switch_St()) {
                return true;
            }
        } else if (compareString(new String[]{"ID", "NEW", "CURRENT", "SUPER"})) {
            if (SST_Dash()) {
                return true;
            }
        }
        return false;
    }

    public boolean Decl_Dash() {
        if (compareString(new String[]{"ACCESS_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN"})) {
            if (AM().isBool()) {
                if (compareString(new String[]{"STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {
                    if (Non_Access_Combination().isBool()) {
                        if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                            if (Size_Mod().isBool()) {
                                if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {
                                    if (Sign_Mod().isBool()) {
                                        if (compareString(new String[]{"DATA_TYPES", "FILE", "STRING", "COLLECTION"})) {
                                            if (Decl_2_Dash()) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Decl_2_Dash() {
        if (compareString(new String[]{"DATA_TYPES"})) {
            if (Primitive_Type().isBool()) {
                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                    if (Decl_3()) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                if (Decl_7()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                if (Decl_5()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"COLLECTION"})) {
            if (Collection_Type()) {
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"AO", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL"})) {
                        if (Collection_Init()) {
                            if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {
                                if (List_5()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_Dash() {
        if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"ID", "AO", "INC_DEC", "PROP_ACCESS", "PAR_OPEN", "ARR_DEL_OPEN"})) {
                if (SST_2()) {
                    return true;
                }
            }
        } else if(this.tokens.get(index).getCP().equals("NEW"))
        {
            this.index++;
            if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "FILE", "STRING", "ID"})) {
                if (SST_Dash_4()) {
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("CURRENT")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS", "PAR_OPEN", "ARR_DEL_OPEN", "TERMINATOR"})) {
                        if(SST_Dash_2())
                        {
                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                return true;
                            }
                        }   
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("SUPER")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS", "PAR_OPEN", "ARR_DEL_OPEN", "TERMINATOR"})) {
                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                            this.index++;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_2() {
        if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
                if (Reference_Init(-1, -1, "").isBool()) {
                    if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {
                        if (List_2( -1, -1, "", false, false, "", "", "")) {
                            return true;
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS"})) {
            if (SST_Dash_2()) {
                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                    this.index++;
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR"})) {
                                    if (SST_Dash_3()) {
                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                            this.index++;
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (SST_3()) {
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_3() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (More_Array_Access(-1).isBool()) {
                            if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS"})) {
                                if (SST_Dash_2()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                if (Multi_Array_Dec(-1).isBool()) { //need work
                    if (tokens.get(index).getCP().equals("ID")) {
                        this.index++;
                        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
                            if (Array_Init()) {
                                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {
                                    if (List_4()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Constructor_Call() {

        if (tokens.get(index).getCP().equals("NEW")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;

                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                        if (Argument_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;

                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                                    if (More_Constructor_Call()) {

                                        return true;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("CURRENT")) {

            this.index++;

            if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                this.index++;

                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                    if (Argument_List()) {

                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                            this.index++;

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("SUPER")) {

            this.index++;

            if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                this.index++;

                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                    if (Argument_List()) {

                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                            this.index++;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Decl() {

        if (compareString(new String[]{"ACCESS_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "VOID", "FINAL", "CLASS", "PAR_OPEN", "BRACES_CLOSE"})) {

            if (AM().isBool()) {

                if (compareString(new String[]{"STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "COLLECTION", "DATA_TYPES", "VOID", "ID", "FILE", "STRING"})) {

                    if (Non_Access_Combination().isBool()) {

                        if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                            if (Size_Mod().isBool()) {

                                if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                                    if (Sign_Mod().isBool()) {

                                        if (compareString(new String[]{"DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION"})) {

                                            if (Decl_2()) {

                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Decl_2() {

        if (compareString(new String[]{"DATA_TYPES"})) {

            if (Primitive_Type().isBool()) {

                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                    if (Decl_3()) {

                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"ID"})) {

            if(Reference_Type())
            {
             if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Decl_4()) {

                    return true;
                }
            }   
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {

            this.index++;

            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Decl_6()) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {

            this.index++;

            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {

                if (Decl_5()) {

                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("COLLECTION")) {

            if (Collection_Type()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;

                    if (compareString(new String[]{"AO", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL"})) {

                        if (Collection_Init()) {

                            if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                                if (List_5()) {

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean Decl_3() {

        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                if (Primitive_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                        if (List_1(-1, -1, "", false, false, "", "", "")) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            if (Decl_6()) {

                return true;
            }
        }
        return false;
    }

    public boolean Decl_4() {

        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                if (Reference_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                        if (List_2(-1, -1, "", false, false, "", "", "")) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            if (Decl_6()) {

                return true;
            }
        }
        return false;
    }

    public boolean Decl_5() {

        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                if (String_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                        if (List_3(-1, -1, "", false, false, "", "", "")) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            if (Decl_6()) {

                return true;
            }
        }
        return false;
    }

    public boolean Decl_6() {

        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;

                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {

                    if (Multi_Array_Dec(-1).isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                                if (Array_Init()) {

                                    if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                                        if (List_4()) {

                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return false;
    }
    
    public boolean Decl_7()
    {
        if(this.tokens.get(index).getCP().equals("ID"))
        {
            this.index++;
            if(compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"}))
            {
                if(File_Init(-1, -1, "").isBool())
                {
                    if(compareString(new String[]{"TERMINATOR", "ARG_DEL"}))
                    {
                        if(List_6(-1, -1, "", false, false, "", "", ""))
                            return true;
                    }
                }
            }
        }
        else if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) 
        {
            if(Decl_6())
                return true;
        }
        
        return false;
    }

    public boolean List_1(int cc, int cs, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {
        if (tokens.get(index).getCP().equals("TERMINATOR")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {            
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                String name = tokens.get(index).getVP();
                if(lookup(name, cc, cs))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                    return false;
                }
                this.index++;
                if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
                    ReturnValue rt = Primitive_Init(cc, cs, type);
                    if (rt.isBool()) {
                        boolean ass = false;
                        if(rt.getValue(0).equals("true"))
                            ass = true;
                        if(!insert(cc, cs, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                            return false;
                        if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {
                            if (List_1(cc, cs, am, stat, finl, sizeMod, signMod, type)) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean List_2(int cc, int cs, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {

        if (tokens.get(index).getCP().equals("TERMINATOR")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {
                String name = tokens.get(index).getVP();
                if(lookup(name, cc, cs))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                    return false;
                }
                this.index++;
                if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
                    ReturnValue rt = Reference_Init(cc, cs, type);
                    if (rt.isBool()) {
                        boolean ass = false;
                        if(rt.getValue(0).equals("true"))
                            ass = true;
                        if(!insert(cc, cs, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                            return false;
                        if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                            if (List_2(cc, cs, am, stat, finl, sizeMod, signMod, type)) {

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean List_3(int cc, int cs, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type) {

        if (tokens.get(index).getCP().equals("TERMINATOR")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {
                String name = tokens.get(index).getVP();
                if(lookup(name, cc, cs))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                    return false;
                }
                this.index++;

                if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {
                    ReturnValue rt = String_Init(cc, cs, type);
                    if (rt.isBool()) {
                        boolean ass = false;
                        if(rt.getValue(0).equals("true"))
                            ass = true;
                        if(!insert(cc, cs, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                            return false;
                        if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                            if (List_3(cc, cs, am, stat, finl, sizeMod, signMod, type)) {

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean List_4() {

        if (tokens.get(index).getCP().equals("TERMINATOR")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                    if (Array_Init()) {

                        if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                            if (List_4()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean List_5() {

        if (tokens.get(index).getCP().equals("TERMINATOR")) {

            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", "TERMINATOR", "BRACES_CLOSE", "ARG_DEL"})) {

                    if (Collection_Init()) {

                        if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                            if (List_5()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    
    public boolean List_6(int cc, int cs, String am, boolean stat, boolean finl, String sizeMod, String signMod, String type)
    {
        if(this.tokens.get(index).getCP().equals("TERMINATOR"))
        {
            this.index++;
            return true;
        }
        else if(this.tokens.get(index).getCP().equals("ARG_DEL"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                String name = tokens.get(index).getVP();
                if(lookup(name, cc, cs))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                    return false;
                }
                this.index++;
                if(compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"}))
                {
                    ReturnValue rt = File_Init(cc, cs, type);
                    if(rt.isBool())
                    {
                        boolean ass = false;
                        if(rt.getValue(0).equals("true"))
                            ass = true;
                        if(!insert(cc, cs, name, sizeMod, signMod, type, am, ass, false, stat, finl, false, false, 0, false, false, 0, null))
                            return false;
                        if(compareString(new String[]{"TERMINATOR", "ARG_DEL"}))
                        {
                            if(List_6(cc, cs, am, stat, finl, sizeMod, signMod, type))
                                return true;
                        }
                    }
                }
                    
            }
        }
        
        return false;
    }

    public ReturnValue Reference_Init(int cc, int cs, String type) {

        if (tokens.get(index).getCP().equals("AO")) {

            this.index++;

            if (compareString(new String[]{"CURRENT", "SUPER", "ID", "NEW", "NULL"})) {

                if (Reference_Init_2()) {

                    return new ReturnValue(true, new String[]{""});
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"})) {

            return new ReturnValue(true, new String[]{"false"});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean Reference_Init_2() {

        if (compareString(new String[]{"CURRENT", "SUPER", "ID"})) {

            if (C(-1, -1).isBool()) {

                if (tokens.get(index).getCP().equals("ID")) {

                    this.index++;

                    if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "ARG_DEL", ";"})) {

                        if (Reference_Init_3()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;

                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                        if (Argument_List()) {

                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                                this.index++;

                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                                    if (More_Constructor_Call()) {

                                        if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR", "ARG_DEL"})) {

                                            if (Reference_Init_5()) {

                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NULL")) {

            this.index++;
            return true;
        }
        return false;
    }

    public boolean Reference_Init_3() {

        if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", "ARG_DEL", ";"})) {

            if (Reference_Init_4()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {

                if (EXP(-1, -1).isBool()) {

                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                        this.index++;

                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {

                            if (More_Array_Access(-1).isBool()) {

                                if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", "ARG_DEL", ";"})) {

                                    if (Reference_Init_4()) {

                                        return true;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                if (Argument_List()) {

                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {

                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR", "ARG_DEL"})) {
                                    if (Reference_Init_5()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Reference_Init_4() {

        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL", ";"})) {

            if (Reference_Init(-1, -1, "").isBool()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "ARG_DEL", ";"})) {

                    if (Reference_Init_3()) {

                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", " ARG_DEL", ";"})) {

            return true;
        }
        
        return false;
    }

    public boolean Reference_Init_5() {

        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", "ARG_DEL", "ID", ";"})) {

                if (Reference_Init_3()) {

                    return true;
                }

            }
            }
            
        } else if (compareString(new String[]{"TERMINATOR", " ARG_DEL", ";"})) {

            return true;
        }
        return false;
    }

    public ReturnValue String_Init(int cc, int cs, String type) {

        if (tokens.get(index).getCP().equals("AO")) {

            this.index++;

            if (compareString(new String[]{"ID", "NEW", "NULL", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})) {

                if (String_Init_2()) {

                    return new ReturnValue(true, new String[]{""});
                }
            }
        } else if (compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"})) {

            return new ReturnValue(true, new String[]{"false"});
        }

        return new ReturnValue(false, new String[]{""});
    }

    public boolean String_Init_2() {
        if(compareString(new String[]{"SUPER", "CURRENT", "ID"}))
        {
            if(C(-1, -1).isBool())
            {
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", ";", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {
                        if (String_Init_3()) {
                            return true;
                        }
                    }
                } 
            }
        }
        else if (tokens.get(index).getCP().equals("NEW")) {

            this.index++;

            if (tokens.get(index).getCP().equals("STRING")) {

                this.index++;

                if (tokens.get(index).getCP().equals("PAR_OPEN")) {

                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NEW", "CURRENT", "SUPER", "ID"}))
                    {
                        if(String_Argument_List())
                        {
                            if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                            {
                                this.index++;
                                if(compareString(new String [] {"PROP_ACCESS","NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND","OR_BIN","XOR","AND_BIN","RO_EQUALITY","RO","SHIFT_BIN","PM","MDM","EXP","PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO",";"})){
                                    if(Assignment_10())
                                        return true;
                                }
                            }
                        }
                    }
                    else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                    {
                        this.index++;
                        if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                                    if (More_Constructor_Call()) {

                                        if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR", ";"})) {

                                            if (String_Init_5()) {

                                                return true;
                                            }
                                        }
                                    }
                                }
                    }
                }

            }
        } else if (tokens.get(index).getCP().equals("NULL")) {

            this.index++;
            return true;
        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
        {
            if(String_Argument_List())
                return true;
        }

        return false;
    }

    public boolean String_Init_3() {

        if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", ";", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {

            if (String_Init_4()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {

                if (EXP(-1, -1).isBool()) {

                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                        this.index++;

                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {

                            if (More_Array_Access(-1).isBool()) {

                                if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", ";", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {

                                    if (String_Init_4()) {

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;

            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {

                if (Argument_List()) {

                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", ";"})) {
                                    if (String_Init_5()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean String_Init_4() {

        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL", ";"})) {

            if (String_Init(-1, -1, "").isBool()) {

                return true;
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", ";", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {

                    if (String_Init_3()) {

                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_CLOSE"})) {

            if (Exp_Dash("", -1, -1).isBool()) {
                return true;
            }
        }
        
        return false;
    }

    public boolean String_Init_5() {

        if (tokens.get(index).getCP().equals("PROP_ACCESS")) {

            this.index++;
            if (tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if (compareString(new String[]{"AO", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR", ";", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP"})) {

                    if (String_Init_3()) {
                        return true;
                    }
                } 
            }
            
        }
        else if(compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"}))   //for null
            return true;

        return false;
    }
    
    public boolean String_Init_6()
    {
        if(this.tokens.get(index).getCP().equals("PM"))
        {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
            {
                if(Const().isBool())
                {
                    if(compareString(new String[]{"PM", "TERMINATOR", "ARG_DEL", "PAR_CLOSE", ";"}))
                        if(String_Init_6())
                            return true;
                }
            }
        }
        else if(compareString(new String[]{"TERMINATOR", "ARG_DEL", "PAR_CLOSE", ";"}))   //for null
            return true;
            
        return false;
    }
    
    public boolean String_Init_7()
    {
        if(this.tokens.get(index).getCP().equals("PM"))
        {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
            {
                if(String_Init_8())
                    return true;
            }
        }
        
        return false;
    }
    
    public boolean String_Init_8()
    {
        if(this.tokens.get(index).getCP().equals("STRING_CONST"))
        {
            this.index++;
            if(compareString(new String[]{"PM", "TERMINATOR", "ARG_DEL", "PAR_CLOSE", ";"}))
            {
                if(String_Init_6())
                    return true;
            }
        }
        else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
        {
            this.index++;
            if(compareString(new String[]{"PM"}))
            {
                if(String_Init_7())
                    return true;
            }
        }
        
        return false;
    }
    
    public ReturnValue File_Init(int cc, int cs, String type)
    {
        if(this.tokens.get(index).getCP().equals("AO"))
        {
            this.index++;
            if(compareString(new String[]{"CURRENT", "SUPER", "ID", "NEW", "NULL"}))
            {
                if(File_Init_2())
                    return new ReturnValue(true, new String[]{""});
            }
        }
        else if(compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"})) //for null
            return new ReturnValue(true, new String[]{"false"});
        
        return new ReturnValue(false, new String[]{""});
    }
    
    public boolean File_Init_2()
    {
        if(compareString(new String[]{"SUPER", "CURRENT", "ID"}))
        {
            if(C(-1, -1).isBool())
            {
                if(this.tokens.get(index).getCP().equals("ID"))
                {
                    this.index++;
                    if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "PROP_ACCESS", "AO", "TERMINATOR", "ARG_DEL", ";"}))
                    {
                        if(File_Init_3())
                            return true;
                    }
                }
            }
        }
        else if(this.tokens.get(index).getCP().equals("NEW"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("FILE"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
                {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NEW", "CURRENT", "SUPER", "ID"}))
                    {
                        if(String_Argument_List())
                        {
                            if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                            {
                                this.index++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        else if(this.tokens.get(index).getCP().equals("NULL"))
        {
            this.index++;
            return true;
        }
        
        return false;
    }
    
    public boolean File_Init_3()
    {
        if(compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", "ARG_DEL", ";"}))
        {
            if(File_Init_4())
                return true;
        }
        else if(this.tokens.get(index).getCP().equals("ARR_DEL_OPEN"))
        {
            this.index++;
            if(compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
            {
                if(EXP(-1, -1).isBool())
                {
                    if(this.tokens.get(index).getCP().equals("ARR_DEL_CLOSE"))
                    {
                        this.index++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) 
                        {
                            if(More_Array_Access(-1).isBool())
                            {
                                if(compareString(new String[]{"AO", "PROP_ACCESS", "TERMINATOR", "ARG_DEL", ";"}))
                                {
                                    if(File_Init_4())
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
        {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"}))
            {
                if(Argument_List())
                {
                    if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                    {
                        this.index++;
                        if(compareString(new String[]{"PROP_ACCESS", "TERMINATOR", "ARG_DEL", ";"}))
                        {
                            if(File_Init_5())
                                return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    public boolean File_Init_4()
    {
        if(compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL", ";"}))
        {
            if(File_Init(-1, -1, "").isBool())
                return true;
        }
        else if(this.tokens.get(index).getCP().equals("PROP_ACCESS"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "PROP_ACCESS", "AO", "TERMINATOR", "ARG_DEL", ";"}))
                {
                    if(File_Init_3())
                        return true;
                }
            }
        }
        else if(compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"}))   //for null
            return true;
        
        return false;
    }
    
    public boolean File_Init_5()
    {
        if(this.tokens.get(index).getCP().equals("PROP_ACCESS"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN", "PROP_ACCESS", "AO", "TERMINATOR", "ARG_DEL", ";"}))
                {
                    if(File_Init_3())
                        return true;
                }
            }
        }
        else if(compareString(new String[]{"TERMINATOR", "ARG_DEL", ";"}))       //for null
            return true;
        
        return false;
    }
    
    public boolean String_Argument_List()
    {
        if (tokens.get(index).getCP().equals("STRING_CONST")) {
            this.index++;
            if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
            {
                if(String_Argument_List_2())
                    return true;
            }
        } else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("PM"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("STRING_CONST"))
                {
                    this.index++;
                    if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
                    {
                        if(String_Argument_List_2())
                            return true;
                    }
                }
                else if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"}))
                {
                    if(Right_Side())
                    {
                        if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
                        {
                            if(String_Argument_List_2())
                                return true;
                        }
                    }
                }
                else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"}))
                {
                    if(String_Argument_List())
                        return true;
                }
            }
        }
        else if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
            if(Right_Side())
            {
                if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
                {
                    if(String_Argument_List_2())
                        return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean String_Argument_List_2()
    {
        if(this.tokens.get(index).getCP().equals("PM"))
        {
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"}))
            {
                if(Right_Side())
                {
                    if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
                    {
                        if(String_Argument_List_2())
                            return true;
                    }
                }
            }
            else if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "STRING_CONST", "FLOAT_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST"})){
                if(Const().isBool())
                {
                    if(compareString(new String[]{"PM","PAR_CLOSE", "TERMINATOR", ";"}))
                    {
                        if(String_Argument_List_2())
                            return true;
                    }
                }
            }
        }
        else if(compareString(new String[]{"PAR_CLOSE", "TERMINATOR", ";"}))   //for null
            {
                return true;
            }
        
        return false;
    }
    
    public boolean SST_2_Dash() {
        if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS"})) {
            if (SST_Dash_2()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                if (EXP(-1, -1).isBool()) {
                    if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "ARR_DEL_CLOSE", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                            if (More_Array_Access(-1).isBool()) {
                                if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS"})) {
                                    if (SST_Dash_2()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if(compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";", "ARR_DEL_OPEN", "INT_CONST", "CHAR_CONST", "FLOAT_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "PAR_OPEN"})){
                            if(More_Array_Access(-1).isBool()){
                                if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR"})) {
                                    if (SST_Dash_3()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    public boolean SST_Dash_2() {
        if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("AO")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                if (Assignment_2()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR"})) {
                    if (SST_2_Dash()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean Body() {
        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER"})) {
            if (SST()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                    if (MST()) {

                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                            this.index++;

                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_Dash_3() {
        if (compareString(new String[]{"TERMINATOR"})) {
            return true;
        } else if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"AO", "INC_DEC", "PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "TERMINATOR"})) {
                    if (SST_2_Dash()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_Dash_4() {
        if(this.tokens.get(index).getCP().equals("COLLECTION"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("LIST_PAR_OPEN"))
            {
                this.index++;
                if(this.tokens.get(index).getCP().equals("LIST_PAR_CLOSE"))
                {
                    this.index++;
                    if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
                    {
                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                        {
                            if(EXP(-1, -1).isBool())
                            {
                                if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                                {
                                    this.index++;
                                    return true;
                                }
                            }
                        }
                        else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
                    }
                }
            }
            else if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
            {
                                        this.index++;
                        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"}))
                        {
                            if(EXP(-1, -1).isBool())
                            {
                                if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                                {
                                    this.index++;
                                    return true;
                                }
                            }
                        }
                        else if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                        {
                            this.index++;
                            return true;
                        }
            }
        } else if (compareString(new String[]{"DATA_TYPES"})) {
            if (Primitive_Type().isBool()) {
                if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                        if (Array_Init_4_Dash()) {
                            return true;
                        }
                    }
                }
            }
        } else if(this.tokens.get(index).getCP().equals("STRING") || this.tokens.get(index).getCP().equals("FILE"))
        {
            this.index++;
            if(compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"}))
            {
                if(Assignment_9())
                    return true;
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_OPEN"})) {
                if (SST_Dash_6()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean SST_Dash_5() {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (Array_Init_4_Dash()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean SST_Dash_6() {
        if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE"))
                    {
                        this.index++;
                        if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                            if (More_Constructor_Call()) {
                                if (compareString(new String[]{"PROP_ACCESS", "TERMINATOR"})) {
                                    if (SST_Dash_3()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (Array_Init_4_Dash()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean While_St() {
        if (tokens.get(index).getCP().equals("UNTIL")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                    if (Cond()) {
                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP"})) {
                                    if (For_Body()) {
                                        return true;
                                    }
                                }
                            }
                            else if (tokens.get(index).getCP().equals(";")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                        this.index++;
                                        return true;
                                    }
                                }
                        }
                    }
                }
            }
        } 
        
        return false;
    }

    public boolean Cond() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {
                return true;
            }
        }
        return false;
    }

    public boolean For_St() {
        if (tokens.get(index).getCP().equals("FOR")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"CURRENT", "SUPER", "NEW", "ID", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", ";"})) {
                    if (F1()) {
                        if (tokens.get(index).getCP().equals(";")) {
                            this.index++;
                            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", ";"})) {
                                if (F2()) {
                                    if (tokens.get(index).getCP().equals(";")) {
                                        this.index++;
                                        if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "PAR_CLOSE"})) {
                                            if (F3()) {
                                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                    this.index++;
                                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                        this.index++;
                                                        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP"})) {
                                                            if (For_Body()) {
                                                                return true;
                                                            }
                                                        }
                                                    }
                                                    else if (tokens.get(index).getCP().equals(";")) {
                                                        this.index++;
                                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                            this.index++;
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    public boolean F1() {
        if (tokens.get(index).getCP().equals("CURRENT")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Left_Side_Dash("", -1, -1, -1).isBool()) {
                            if (tokens.get(index).getCP().equals("AO")) {
                                this.index++;
                                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                                    if (Assignment_2()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("SUPER")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PROP_ACCESS")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"PROP_ACCESS", "ARR_DEL_OPEN", "PAR_OPEN", "ARR_DEL_CLOSE", "PAR_CLOSE", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Left_Side_Dash("", -1, -1, -1).isBool()) {
                            if (tokens.get(index).getCP().equals("AO")) {
                                this.index++;
                                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                                    if (Assignment_2()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("NEW")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                        if (Argument_List()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                if (compareString(new String[]{"ARG_DEL", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                                    if (More_Constructor_Call()) {
                                        if (compareString(new String[]{"PROP_ACCESS"})) {
                                            if (Left_Side_3()) {
                                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                                    if (Left_Side_2("", -1, -1, -1).isBool()) {
                                                        if (tokens.get(index).getCP().equals("AO")) {
                                                            this.index++;
                                                            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                                                                if (Assignment_2()) {
                                                                    return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"ID", "PAR_OPEN", "ARR_DEL_OPEN"})) {
                if (F1_0()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

            if (Size_Mod().isBool()) {

                if (compareString(new String[]{"SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION", "VOID", "ACCESS_MOD", "STATIC", "FINAL"})) {

                    if (Sign_Mod().isBool()) {

                        if (compareString(new String[]{"DATA_TYPES", "FILE", "STRING", "COLLECTION"})) {

                            if (F1_2()) {

                                return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals(";")) { // FOR NULL
            return true;
        }
        return false;
    }

    public boolean F1_0() {
        if (tokens.get(index).getCP().equals("ID")) {
            this.index++;
            if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                if (Reference_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{";", "ARG_DEL"})) {

                        if (List_2(-1, -1, "", false, false, "", "", "")) {

                            return true;
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
            if (Left_Side_2("", -1, -1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("AO")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                        if (Assignment_2()) {
                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "BRACES_OPEN", "PAR_OPEN", "PAR_CLOSE", "PROP_ACCESS", "TERMINATOR", "BRACES_CLOSE"})) {
                if (Argument_List()) {
                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                        this.index++;
                        if (compareString(new String[]{"PROP_ACCESS"})) {
                            if (Left_Side_3()) {
                                if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                                    if (Left_Side_2("", -1, -1, -1).isBool()) {
                                        if (tokens.get(index).getCP().equals("AO")) {
                                            this.index++;
                                            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                                                if (Assignment_2()) {
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN", "ARR_DEL_CLOSE"})) {
                if (F1_1()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean F1_1() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (EXP(-1, -1).isBool()) {
                if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
                    this.index++;
                    if (compareString(new String[]{"ARR_DEL_CLOSE", "PAR_CLOSE", "PROP_ACCESS", "BRACES_CLOSE", "NEW", "CURRENT", "SUPER", "ID", "INC_DEC", "OR", "AND", "OR_BIN", "XOR", "AND_BIN", "RO_EQUALITY", "RO", "SHIFT_BIN", "PM", "MDM", "EXP", "PAR_CLOSE", "TERMINATOR", "ARG_DEL", "AO", ";"})) {
                        if (Left_Side_2("", -1, -1, -1).isBool()) {
                            if (tokens.get(index).getCP().equals("AO")) {
                                this.index++;
                                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                                    if (Assignment_2()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {
            this.index++;
            if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "TERMINATOR", "ID"})) {
                if (Multi_Array_Dec(-1).isBool()) {
                    if (tokens.get(index).getCP().equals("ID")) {
                        this.index++;
                        if (compareString(new String[]{"AO", "TERMINATOR", "ARG_DEL"})) {

                            if (Array_Init()) {

                                if (compareString(new String[]{"TERMINATOR", "ARG_DEL"})) {

                                    if (List_4()) {

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean F1_2()
    {
        if (compareString(new String[]{"DATA_TYPES"})) {
            if (Primitive_Type().isBool()) {
                if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                    if (F1_3()) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FILE")) {
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                if (F1_4()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("STRING")) {
            this.index++;
            if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) {
                if (F1_5()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"COLLECTION"})) {
            if (Collection_Type()) {
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"AO", ";", "BRACES_CLOSE", "ARG_DEL"})) {
                        if (Collection_Init()) {
                            if (compareString(new String[]{";", "ARG_DEL"})) {
                                if (F1_List_5()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_3()
    {
        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                if (Primitive_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{";", "ARG_DEL"})) {

                        if (F1_List_1()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            if (F1_6()) {

                return true;
            }
        }
        
        return false;
    }

    public boolean F1_4()
    {
        if(this.tokens.get(index).getCP().equals("ID"))
        {
            this.index++;
            if(compareString(new String[]{"AO", ";", "ARG_DEL"}))
            {
                if(File_Init(-1, -1, "").isBool())
                {
                    if(compareString(new String[]{";", "ARG_DEL"}))
                    {
                        if(F1_List_6())
                            return true;
                    }
                }
            }
        }
        else if (compareString(new String[]{"ID", "ARR_DEL_OPEN"})) 
        {
            if(F1_6())
                return true;
        }
        
        return false;
    }
    
    public boolean F1_5()
    {
        if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                if (String_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{";", "ARG_DEL"})) {

                        if (F1_List_3()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {
            if (F1_6()) {

                return true;
            }
        }
        
        return false;
    }
    
    public boolean F1_6()
    {
        if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ARR_DEL_CLOSE")) {

                this.index++;

                if (compareString(new String[]{"ARR_DEL_OPEN", "PAR_CLOSE", "ID"})) {

                    if (Multi_Array_Dec(-1).isBool()) {

                        if (tokens.get(index).getCP().equals("ID")) {

                            this.index++;

                            if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                                if (Array_Init()) {

                                    if (compareString(new String[]{";", "ARG_DEL"})) {

                                        if (F1_List_4()) {

                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        
        return false;
    }
    
    public boolean F1_7()
    {
         if (tokens.get(index).getCP().equals("ID")) {

            this.index++;

            if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                if (Reference_Init(-1, -1, "").isBool()) {

                    if (compareString(new String[]{";", "ARG_DEL"})) {

                        if (F1_List_2()) {

                            return true;
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("ARR_DEL_OPEN")) {

            if (F1_6()) {

                return true;
            }
        }
        
        return false;
    }
    
    public boolean F1_List_1()
    {
        if (tokens.get(index).getCP().equals(";")) {    //for null
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                    if (Primitive_Init(-1, -1, "").isBool()) {

                        if (compareString(new String[]{";", "ARG_DEL"})) {

                            if (F1_List_1()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_List_2()
    {
        if (tokens.get(index).getCP().equals(";")) {    //for null
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                    if (Reference_Init(-1, -1, "").isBool()) {

                        if (compareString(new String[]{";", "ARG_DEL"})) {

                            if (F1_List_2()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_List_3()
    {
        if (tokens.get(index).getCP().equals(";")) {    //for null
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                    if (String_Init(-1, -1, "").isBool()) {

                        if (compareString(new String[]{";", "ARG_DEL"})) {

                            if (F1_List_3()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_List_4()
    {
        if (tokens.get(index).getCP().equals(";")) {    //for null
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", ";", "ARG_DEL"})) {

                    if (Array_Init()) {

                        if (compareString(new String[]{";", "ARG_DEL"})) {

                            if (F1_List_4()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_List_5()
    {
        if (tokens.get(index).getCP().equals(";")) {    //for null
            return true;
        } else if (tokens.get(index).getCP().equals("ARG_DEL")) {

            this.index++;

            if (tokens.get(index).getCP().equals("ID")) {

                this.index++;

                if (compareString(new String[]{"AO", ";", "BRACES_CLOSE", "ARG_DEL"})) {

                    if (Collection_Init()) {

                        if (compareString(new String[]{";", "ARG_DEL"})) {

                            if (F1_List_5()) {

                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean F1_List_6()
    {
        if(this.tokens.get(index).getCP().equals(";"))  //for null
        {
            return true;
        }
        else if(this.tokens.get(index).getCP().equals("ARG_DEL"))
        {
            this.index++;
            if(this.tokens.get(index).getCP().equals("ID"))
            {
                this.index++;
                if(compareString(new String[]{"AO", ";", "ARG_DEL"}))
                {
                    if(File_Init(-1, -1, "").isBool())
                    {
                        if(compareString(new String[]{";", "ARG_DEL"}))
                        {
                            if(F1_List_6())
                                return true;
                        }
                    }
                }
                    
            }
        }
        
        return false;
    }
    
    public boolean F2() {
        if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
            if (Cond()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals(";")) {
            return true;
        }
        return false;
    }

    public boolean F3() {
        if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
            if (Left_Side(-1, -1).isBool()) {
                if (compareString(new String[]{"INC_DEC", "AO"})) {
                    if (F3_0()) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                if (Left_Side(-1, -1).isBool()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
            return true;
        }

        return false;
    }

    public boolean For_Body() {
        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER"})) {
            if (SST()) {
                return true;
            }
        } else if (tokens.get(index).getCP().equals("BREAK")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                return true;
            }
        } else if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "SKIP", "BRACES_CLOSE"})) {
                    if (Multi_Line_For()) {
                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("SKIP")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                return true;
            }
        }
        return false;
    }

    public boolean Multi_Line_For() {
        if (tokens.get(index).getCP().equals("SKIP")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                return true;
            }
        } else if (tokens.get(index).getCP().equals("BREAK")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                return true;
            }
        } else if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER"})) {
            if (SST()) {
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "SKIP", "BRACES_CLOSE"})) {
                    if (Multi_Line_For()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"BRACES_CLOSE"})) { // FOR NULL
            return true;
        }
        return false;
    }

    public boolean F3_0() {
        if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                if (F3_Dash()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("AO")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                if (Assignment_2()) {
                    if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                        if (F3_Dash()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean F3_Dash() {
        if (tokens.get(index).getCP().equals("ARG_DEL")) {
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID", "INC_DEC"})) {
                if (F3_2Dash()) {
                    if (compareString(new String[]{"AO", "INC_DEC"})) {
                        if (F3_Dash_0()) {
                            if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                                if (F3_Dash()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
            return true;
        }
        return false;
    }

    public boolean F3_2Dash() {
        if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
            if (Left_Side(-1, -1).isBool()) {
                if (compareString(new String[]{"AO", "INC_DEC"})) {
                    if (F3_Dash_0()) {
                        if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                            if (F3_Dash()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                if (Left_Side(-1, -1).isBool()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean F3_Dash_0() {
        if (tokens.get(index).getCP().equals("INC_DEC")) {
            this.index++;
            if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                if (F3_Dash()) {
                    return true;
                }
            }
        } else if (tokens.get(index).getCP().equals("AO")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "SUPER", "CURRENT", "ID", "NEW", "PAR_OPEN", "NULL", "BRACES_OPEN"})) {
                if (Assignment_2()) {
                    if (compareString(new String[]{"ARG_DEL", "INC_DEC", "AO", "PAR_CLOSE"})) {
                        if (F3_Dash()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Do_While_St() {
        if (tokens.get(index).getCP().equals("DO")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                    this.index++;
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "SKIP", "BRACES_CLOSE"})) {
                            if (Multi_Line_For()) {
                                if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("UNTIL")) {
                                        this.index++;
                                        if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                                            this.index++;
                                            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                                                if (Cond()) {
                                                    if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                                        this.index++;
                                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                            this.index++;
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean For_Each_St() {
        if (tokens.get(index).getCP().equals("FOR_EACH")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "ID", "STRING", "FILE"})) {
                    if (DT()) {
                        if (tokens.get(index).getCP().equals("ID")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("AO")) {
                                this.index++;
                                if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                                    if (Left_Side(-1, -1).isBool()) {
                                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                            this.index++;
                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                this.index++;
                                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP"})) {
                                                    if (For_Body()) {
                                                        return true;
                                                    }
                                                }
                                            }
                                            else if (tokens.get(index).getCP().equals(";"))
                                            {
                                                this.index++;
                                                if (tokens.get(index).getCP().equals("TERMINATOR"))
                                                {
                                                    this.index++;
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("FOREACH")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"COLLECTION", "DATA_TYPES", "ID", "STRING", "FILE"})) {
                    if (DT()) {
                        if (tokens.get(index).getCP().equals("ID")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("AO")) {
                                this.index++;
                                if (compareString(new String[]{"NEW", "CURRENT", "SUPER", "ID"})) {
                                    if (Left_Side(-1, -1).isBool()) {
                                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                            this.index++;
                                            if (tokens.get(index).getCP().equals(";")) {
                                                this.index++;
                                                if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                    this.index++;
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean If_Else_St() {
        if (tokens.get(index).getCP().equals("IF")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                    if (Cond()) {
                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("THEN")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("ARG_DEL")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                        this.index++;
                                        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_OPEN"})) {
                                            if (Body()) {
                                                if (compareString(new String[]{"OTHERWISE", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                                    if (Else()) {
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Else() {
        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) { // FOR NULL
            return true;
        } else if (tokens.get(index).getCP().equals("OTHERWISE")) {
            this.index++;
            if(compareString(new String[]{"TERMINATOR", "IF"}))
            {
                if(Else_2())
                    return true;
            }    
        } 
        return false;
    }

    public boolean Else_2()
    {
        if (tokens.get(index).getCP().equals("TERMINATOR")) {
            this.index++;
            if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_OPEN"})) {
                if (Body()) {
                    return true;
                }
            }
        }
        else if (tokens.get(index).getCP().equals("IF")) {
                this.index++;
                if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                    this.index++;
                    if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                        if (Cond()) {
                            if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("THEN")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("ARG_DEL")) {
                                        this.index++;
                                        if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                            this.index++;
                                            if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_OPEN"})) {
                                                if (Body()) {
                                                    if (compareString(new String[]{"OTHERWISE", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                                        if (Else()) {
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        
        return false;
    }
    
    public boolean Try_St() {
        if (tokens.get(index).getCP().equals("TRY")) {
            this.index++;
            if (compareString(new String[]{"PAR_OPEN", "TERMINATOR"})) {
                if (With_Resources()) {
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                    if (MST()) {

                                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                            this.index++;

                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                this.index++;
                                                if (compareString(new String[]{"FINALLY", "CATCH", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                                    if (Catch_Finally_St()) {
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Catch_Finally_St() {
        if (tokens.get(index).getCP().equals("FINALLY")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                    this.index++;
                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                        this.index++;
                        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                            if (MST()) {

                                if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                    this.index++;

                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                        this.index++;
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("CATCH")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (tokens.get(index).getCP().equals("ID")) {
                    this.index++;
                    if (compareString(new String[]{"|", "ID"})) {
                        if (Multiple_Exceptions()) {
                            if (tokens.get(index).getCP().equals("ID"))
                            {
                                this.index++;
                                if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                        this.index++;
                                        if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                                            this.index++;
                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                this.index++;
                                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {

                                                    if (MST()) {

                                                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                            this.index++;

                                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                                this.index++;
                                                                if (compareString(new String[]{"FINALLY", "CATCH", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                                                    if (Catch_Finally_St()) {
                                                                        return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) { //FOR NULL
            return true;
        }
        return false;
    }

    public boolean With_Resources() {
        if(this.tokens.get(index).getCP().equals("PAR_OPEN"))
        {
            this.index++;
            if (compareString(new String[]{"ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "ID", "FILE", "STRING", "COLLECTION"})) {
                if (Decl()) {
                    if(this.tokens.get(index).getCP().equals("PAR_CLOSE"))
                    {
                        this.index++;
                        return true;
                    }
                }
        }
        }
        else if(compareString(new String[]{"TERMINATOR"}))  //for null
            return true;
        return false;
    }

    public boolean Multiple_Exceptions() {
        if (tokens.get(index).getCP().equals("|")) {
            this.index++;
            if (tokens.get(index).getCP().equals("ID")) {
                this.index++;
                if (compareString(new String[]{"|", "ID"})) {
                    if (Multiple_Exceptions()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"ID"})) {//FOR NULL
            return true;
        }
        return false;
    }

    public boolean Switch_St() {
        if (tokens.get(index).getCP().equals("SWITCH")) {
            this.index++;
            if (tokens.get(index).getCP().equals("PAR_OPEN")) {
                this.index++;
                if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", "NOT", "INC_DEC", "CURRENT", "SUPER", "ID", "NEW", "PAR_OPEN"})) {
                    if (Cond()) {
                        if (tokens.get(index).getCP().equals("PAR_CLOSE")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                this.index++;
                                if (tokens.get(index).getCP().equals("BRACES_OPEN")) {
                                    this.index++;
                                    if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                        this.index++;
                                        if (compareString(new String[]{"CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                            if (Case_Body()) {
                                                if (compareString(new String[]{"DEFAULT", "BRACES_CLOSE"})) {
                                                    if (Default_Body()) {
                                                        if (tokens.get(index).getCP().equals("BRACES_CLOSE")) {
                                                            this.index++;
                                                            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                                                                this.index++;
                                                                return true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean Conv_Const() {
        if (tokens.get(index).getCP().equals("INT_CONST")) {
            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("CHAR_CONST")) {
            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("STRING_CONST")) {
            this.index++;
            return true;
        } else if (tokens.get(index).getCP().equals("ENUM")) {
            this.index++;
            return true;
        }
        return false;
    }

    public boolean Default_Body() {
        if (tokens.get(index).getCP().equals("DEFAULT")) {
            this.index++;
            if(tokens.get(index).getCP().equals("AO"))
            {
                this.index++;
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {
                    if (MST()) {
                        if (tokens.get(index).getCP().equals("BREAK")) {
                            this.index++;
                            if (tokens.get(index).getCP().equals("TERMINATOR"))
                            {
                                this.index++;
                                return true;
                            }
                        }
                        else if(compareString(new String[]{"BRACES_CLOSE"}))    //for null
                            return true;
                    }
            }
        }
        } else if (tokens.get(index).getCP().equals("DEFAULT")) {
            this.index++;
            if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BRACES_CLOSE", "BREAK"})) {
                if (MST()) {
                    return true;
                }
            }
        } else if (compareString(new String[]{"BRACES_CLOSE"})) {//FOR NULL
            return true;
        }
        return false;
    }

    public boolean Case_Body() {
        if (tokens.get(index).getCP().equals("CASE")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "ENUM"})) {
                if (Conv_Const()) {
                    if (tokens.get(index).getCP().equals("AO")) {
                        this.index++;
                        if (compareString(new String[]{"CASE", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "DEFAULT", "BRACES_CLOSE"})) {
                            if (More_Cases()) {
                                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "CURRENT", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                    if (Switch_Body()) {
                                        if (compareString(new String[]{"CASE", "DEFAULT", "BRACES_CLOSE"})) {
                                            if (Case_Body()) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(compareString(new String[]{"DEFAULT", "BRACES_CLOSE"}))     //for null
            return true;
        
        return false;
    }

    public boolean More_Cases() {
        if (tokens.get(index).getCP().equals("CASE")) {
            this.index++;
            if (compareString(new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "ENUM"})) {
                if (Conv_Const()) {
                    if (tokens.get(index).getCP().equals("AO")) {
                        this.index++;
                        if (compareString(new String[]{"CASE", "UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "DEFAULT", "BRACES_CLOSE"})) {
                            if (More_Cases()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "DEFAULT", "BRACES_CLOSE"})) {// FOR NULL
            return true;
        }
        return false;
    }

    public boolean Switch_Body() {
        if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "ACCESS_MOD", "STATIC", "FINAL", "SIZE_MOD", "SIGN_MOD", "DATA_TYPES", "FILE", "STRING", "COLLECTION", "THROW", "FOR_EACH", "SWITCH", "ID", "NEW", "CURRENT", "SUPER"})) {
            if (SST()) {
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "CURRENT", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                    if (Switch_Body()) {
                        return true;
                    }
                }
            }
        } else if (tokens.get(index).getCP().equals("BREAK")) {
            this.index++;
            if (tokens.get(index).getCP().equals("TERMINATOR")) {
                this.index++;
                if (compareString(new String[]{"UNTIL", "IF", "FOR", "DO", "INC_DEC", "WITHDRAW", "TERMINATOR", "TRY", "SKIP", "THROW", "FOREACH", "SWITCH", "ID", "CURRENT", "CURRENT", "SUPER", "BREAK", "BRACES_OPEN", "SKIP", "CASE", "DEFAULT", "BRACES_CLOSE"})) {
                    if (Switch_Body()) {
                        return true;
                    }
                }
            }
        } else if (compareString(new String[]{"CASE", "DEFAULT", "BRACES_CLOSE"})) {//FOR NULL
            return true;
        }

        return false;
    }

    
    /*******************************************SEMANTIC CODE******************************************************/
    
    public int createClassScope(int index)
    {
        classScope.push(index);
        return classScope.size() - 1;
    }
    
    public int destroyClassScope()
    {
        classScope.pop();
        return classScope.size();
    }
    
    public boolean lookup(String name)
    {
        if(cTable == null || cTable.isEmpty())
            return false;
        
        for (int i = 0; i < cTable.size(); i++) {
            if(cTable.get(i).getName().equals(name))
                return true;
        }
        
        return false;
    }
    
    public boolean lookup(String name, int cc, int cs, int dim)
    {
        if(cc == -1)
            return false;
        
        if(cTable == null || cTable.isEmpty())
            return false;
        
        if(cs == -1)
        {
            int in = cTable.get(cc).indexOf(name);
            if(in == -1)
                return false;
            else 
            {
                if(dim <= cTable.get(cc).getLinkAtIndex(in).getArrayDim())
                    return true;
            }
        }
        else
        {
            int in;
            int j = 1;
            do
            {
                in = cTable.get(cc).getLinkAtIndex(cs).indexOf(name);
                if(in != -1)
                {
                    if(dim <= cTable.get(cc).getLinkAtIndex(cs).getLinkAtIndex(in).getArrayDim())
                        return true;
                }
                
                if(j < methodScope.size())
                    cs = methodScope.get(methodScope.size() - 1 - j++);
                else 
                    cs = -1;
                
            }while(cs != -1);
            
            in = cTable.get(cc).indexOf(name); 
            if(in != -1)
            {
                if(dim <= cTable.get(cc).getLinkAtIndex(in).getArrayDim())
                    return true;
            }
        }
        
        return false;
    }
    
        public boolean lookup(String name, int cc, int cs)
    {
        if(cc == -1)
            return false;
        
        if(cTable == null || cTable.isEmpty())
            return false;
        
        if(cs == -1)
        {
            int in = cTable.get(cc).indexOf(name);
            if(in == -1)
                return false;
            else 
                return true;
        }
        else
        {
            int in;
            int j = 1;
            do
            {
                in = cTable.get(cc).getLinkAtIndex(cs).indexOf(name);
                if(in != -1)
                    return true;
                
                if(j < methodScope.size())
                    cs = methodScope.get(methodScope.size() - 1 - j++);
                else 
                    cs = -1;
                
            }while(cs != -1);
            
            in = cTable.get(cc).indexOf(name); 
            if(in != -1)
                return true;
        }
        
        return false;
    }
    
    
    public String getType(String name, int cc, int cs, int dim)
    {   
        String type = null;
        String Dim = "";
        int orgDim;
        
        
        
        if(cs == -1)
        {
            int in = cTable.get(cc).indexOf(name);
            type = cTable.get(cc).getLinkAtIndex(in).getType();
            orgDim = cTable.get(cc).getLinkAtIndex(in).getArrayDim();
            dim = orgDim - dim;
        }
        else
        {
            int j = 1;
            do
            {
                int in = cTable.get(cc).getLinkAtIndex(cs).indexOf(name);
                if(in != -1)
                {
                    type = cTable.get(cc).getLinkAtIndex(cs).getLinkAtIndex(in).getType();
                    orgDim = cTable.get(cc).getLinkAtIndex(in).getArrayDim();
                    dim = orgDim - dim;
                    break;
                }
                
                if(j < methodScope.size())
                    cs = methodScope.get(methodScope.size() - 1 - j++);
                else 
                    cs = -1;
                
            }while(cs != -1);
            
            int in = cTable.get(cc).indexOf(name);
            if(in != -1)
            {
                type = cTable.get(cc).getLinkAtIndex(in).getType();
                orgDim = cTable.get(cc).getLinkAtIndex(in).getArrayDim();
                dim = orgDim - dim;
            }
        }
        
        for (int i = 0; i < dim; i++) {
            Dim += "[";
        }
        
        type += Dim;
        
        return type;
    }
    
    public boolean isAssigned(String name, int cc, int cs, int dim)
    {   
        boolean ass = false;
        
        
        
        
        if(cs == -1)
        {
            int in = cTable.get(cc).indexOf(name);
            ass = cTable.get(cc).getLinkAtIndex(in).isIsAssigned();
            
        }
        else
        {
            int j = 1;
            do
            {
                int in = cTable.get(cc).getLinkAtIndex(cs).indexOf(name);
                if(in != -1)
                {
                    ass = cTable.get(cc).getLinkAtIndex(cs).getLinkAtIndex(in).isIsAssigned();
                    break;
                }
                
                if(j < methodScope.size())
                    cs = methodScope.get(methodScope.size() - 1 - j++);
                else 
                    cs = -1;
                
            }while(cs != -1);
            
            int in = cTable.get(cc).indexOf(name);
            if(in != -1)
            {
                ass = cTable.get(cc).getLinkAtIndex(in).isIsAssigned();
            }
        }
        
        
        
        return ass;
    }
    
    public boolean insert(String name, String am, boolean isStatic, boolean isFinal, String extend, String extendVirtual, String innerClass, ArrayList<ClassAttribTable> link)
    {
        if(lookup(name))
        {
            System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Class Redeclaration error");
                return false;
        }
        
        if(extend != null)
        {
            String[] words = extend.split(",");
            for (String w:words) {
                if(w.equals(name))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Class Can not extend itself");
                    return false;
                }
                
                if(!(lookup(w)))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Undefined Class \"" + w + "\"");
                    return false;
                }
            }
        }
        
        if(extendVirtual != null)
        {
            String[] words = extendVirtual.split(",");
            for (String w:words) {
                if(w.equals(name))
                {
                    System.out.println("Can not extend itself");
                    return false;
                }
                
                if(!(lookup(w)))
                {
                    System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Undefined Class \"" + w + "\"");
                    return false;
                }
            }
        }
        
        if(link != null)
            cTable.add(new ClassTable(name, am, isStatic, isFinal, extend, extendVirtual, innerClass, link));
        else 
            cTable.add(new ClassTable(name, am, isStatic, isFinal, extend, extendVirtual, innerClass));
        return true;
    }
    
    public boolean insert(int cc, int cs, String name, String sizeMod, String signMod, String type, String am, boolean isAssigned, boolean isOverride, boolean isStatic, boolean isFinal, boolean isAbstract, boolean isArray, int arrayDim, boolean isList, boolean isMultiList, int listDim, ArrayList<FuncAttribTable> link)
    {
        if(lookup(name, cc, cs))
        {
            System.out.println("Error at line " + this.tokens.get(index).getLN() + ": Attribute Redeclaration error");
                return false;
        }
        
        ArrayList<ClassAttribTable> tempLink  = this.cTable.get(cc).getLink();
        
        if(link == null)
        {
            tempLink.add(new ClassAttribTable(name, sizeMod, signMod, type, am, isAssigned, isOverride, isStatic, isFinal, isAbstract, isArray, arrayDim, isList, isMultiList, listDim));
            this.cTable.get(cc).setLink(tempLink);
        }
        else 
        {
            tempLink.add(new ClassAttribTable(name, sizeMod, signMod, type, am, isAssigned, isOverride, isStatic, isFinal, isAbstract, isArray, arrayDim, isList, isMultiList, listDim, link));
            this.cTable.get(cc).setLink(tempLink);
        }
            
        return true;
    }
    
    public ReturnValue compatability(String t1, String t2, String op)
    {
        int dim1 = 0;
        int dim2 = 0;
        try {
            String[] w = t1.split("[");
            dim1 = w.length - 1;
        } catch (Exception e) {
        }
        
        try {
            String[] w = t2.split("[");
            dim2 = w.length - 1;
        } catch (Exception e) {
        }
        
        
        
        if(dim1 != dim2)
            return new ReturnValue(false, new String[]{"Error at line " + this.tokens.get(index).getLN() + ": Incompatible Operation!!!!"});  
        
        String in = "int", ch = "char", fl = "float", st = "String", file = "File";
        
        if(!compareString(op, new String[]{":", "greater_than", "less_than", "greater_than_equal", "less_than_equal", "equal", "or", "and", "andb", "orb", "xor", "not_equals", "left_shift", "right_shift", "cast"}))
        {
            if((t1.equals(ch) || t1.equals("CHAR_CONST")) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t2});
            else if((t1.equals(in) || t1.equals("INT_CONST")) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t2});
            else if((t1.equals(fl) || t1.equals("FLOAT_CONST")) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t1});
            if((t2.equals(ch) || t2.equals("CHAR_CONST")) && compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t1});
            else if((t2.equals(in) || t2.equals("INT_CONST")) && compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                 return new ReturnValue(true, new String[]{t1});
            else if((t1.equals(fl) || t1.equals("FLOAT_CONST")) && compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t2});
            else if((compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", in, ch, fl, st}) && op.equals("+") && (t2.equals("String")|| t2.equals("STRING_CONST"))) || ((t1.equals("String")|| t1.equals("STRING_CONST")) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", "STRING_CONST", "BYTE_CONST", "OCT_CONST", "HEX_CONST", in, ch, fl, st}) && op.equals("+")))
                return new ReturnValue(true, new String[]{"String"});
            else
                return new ReturnValue(false, new String[]{"Error at line " + this.tokens.get(index).getLN() + ": Incompatible Operation!!!!"});  
        }
        else if(op.equals(":"))
        {
            if(dim1 == dim2)
            {
                if(t1.equals(t2))
                    return new ReturnValue(true, new String[]{t1});
                else if((t1.equals(fl) || t1.equals("FLOAT_CONST")) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                    return new ReturnValue(true, new String[]{t1});
                else if(compareString(t1, new String[]{in, ch, "INT_CONST", "CHAR_CONST"}) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", in, ch}))
                    return new ReturnValue(true, new String[]{t1});
                else if(compareString(t1, new String[]{in, "INT_CONST"}) && compareString(t2, new String[]{"FLOAT_CONST", fl}))
                {
                    printErrorMessage("Lossy conversion from float to int", this.index);
                    return new ReturnValue(false, new String[]{""});
                }
                else if(compareString(t1, new String[]{ch, "CHAR_CONST"}) && compareString(t2, new String[]{"FLOAT_CONST", fl}))
                {
                    printErrorMessage("Lossy conversion from float to char", this.index);
                    return new ReturnValue(false, new String[]{""});
                }
                else 
                {
                    printErrorMessage("Incompatible Operation!!!!", this.index);
                    return new ReturnValue(false, new String[]{""});
                }
            }
            else
            {
                printErrorMessage("Incompatible Operation!!!!", this.index);
                return new ReturnValue(false, new String[]{""});
            }
        }
        else if(compareString(op, new String[]{"greater_than", "less_than", "greater_than_equal", "less_than_equal", "or", "and", "andb", "orb", "xor", "equal", "not_equals"}))
            return new ReturnValue(true, new String[]{"int"});
        else if(compareString(op, new String[]{"left_shift", "right_shift"}))
        {
            if(compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", in, ch}) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", in, ch}))
                return new ReturnValue(true, new String[]{t1});
            else
            {
                printErrorMessage("invalid operands of types '"+ t1 +"' and '"+ t2 +"' to binary '"+ op +"'", this.index);
                return new ReturnValue(false, new String[]{""});
            }
        }
        else if(op.equals("cast"))
        {
            if(compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}) && compareString(t2, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t1});
            else if(t1.equalsIgnoreCase(t2))
                return new ReturnValue(true, new String[]{t1});
            else
                return new ReturnValue(false, new String[]{""});
        }
        
        printErrorMessage("Incompatible Operation!!!!", this.index);
        return new ReturnValue(false, new String[]{""});
            
        
    }
    
    public ReturnValue compatability(String t1, String op)
    {
        String in = "int", ch = "char", fl = "float", st = "String", file = "File";
        
        if(op.equals("++") || op.equals("--"))
        {
            if(compareString(t1, new String[]{"INT_CONST", "CHAR_CONST", "FLOAT_CONST", in, ch, fl}))
                return new ReturnValue(true, new String[]{t1});
        }
        else if(op.equals("not") || op.equals("notb"))
            return new ReturnValue(true, new String[]{"int"});
        
        printErrorMessage("Incompatible Operation!!!!", this.index);
        return new ReturnValue(false, new String[]{""});
    }
    
    public int indexOfClass(String name)
    {
        for (int i = 0; i < cTable.size(); i++) {
            if(cTable.get(i).getName().equals(name))
                return i;
        }
        
        return -1;
    }
    
    public void printErrorMessage(String name, int index)
    {
        System.out.println("Error at line " + this.tokens.get(index).getLN() + ": " + name);
    }
    
    /*******************************************SEMANTIC CODE******************************************************/
    
    public boolean compareString(String[] values) {
        for (String value : values) {
            if (tokens.get(index).getCP().equals(value)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean compareString(String t, String[] values) {
        for (String value : values) {
            if (t.equals(value)) {
                return true;
            }
        }

        return false;
    }

}

