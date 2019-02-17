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
public class Operators {

    /**
     * ***************************************Operators
     * Declaration*******************************************************
     */
    private Classification AO;
    private Classification PM;
    private Classification MDM;
    private Classification INC_DEC;
    private Classification EXP;
    private Classification RO;
    private Classification RO_EQUALITY;
    private Classification AND;
    private Classification OR;
    private Classification NOT;
    private Classification OR_BIN;
    private Classification AND_BIN;
    private Classification SHIFT_BIN;
    private Classification XOR;
    /**
     * ***************************************Punctuator
     * Declaration*******************************************************
     */
    private Classification ARG_DEL;
    private Classification SEMI_COL;
    private Classification ARR_DEL_OPEN;
    private Classification ARR_DEL_CLOSE;
    private Classification PAR_OPEN;
    private Classification PAR_CLOSE;
    private Classification PROP_ACCESS;
    private Classification BRACES_OPEN;
    private Classification BRACES_CLOSE;
    private Classification TERMINATOR;
    private Classification SCOPE_RES;
    private Classification LIST_PAR_OPEN;
    private Classification LIST_PAR_CLOSE;
    private Classification EXCEPTIONS_OR;
    private Classification END_MARKER;

    /**
     * ***************************************Keywords
     * Declaration*******************************************************
     */
    private Classification CLASS;
    private Classification ARROW_FUNC;
    private Classification WITHDRAW;
    private Classification BREAK;
    private Classification SKIP;
    private Classification IF;
    private Classification THEN;
    private Classification OTHERWISE;
    private Classification FOR;
    private Classification UNTIL;
    private Classification FOR_EACH;
    private Classification GLOBAL;
    private Classification START;
    private Classification DO;
    private Classification INSTANCE_OF;
    private Classification NEW;
    private Classification NULL;
    private Classification SUPER;
    private Classification TRY;
    private Classification CATCH;
    private Classification RAISE;
    private Classification FINALLY;
    private Classification THROWS;
    private Classification THROW;
    private Classification CURRENT;
    private Classification TYPE_OF;
    private Classification ABSTRACT;
    private Classification FINAL;
    private Classification ACCESS_MOD;
    private Classification STATIC;
    private Classification OVERRIDE;
    private Classification VIRTUAL;
    private Classification VOID;
    private Classification STRING;
    private Classification FILE;
    private Classification ENUM;
    private Classification DATA_TYPES;
    private Classification SIZE_MOD;
    private Classification SIGN_MOD;
    private Classification SWITCH;
    private Classification CASE;
    private Classification DEFAULT;
    private Classification COLLECTION;
    private Classification EXTENDS;
    
    
    private ArrayList<Classification> keywords = new ArrayList<>();
    private ArrayList<Classification> operators = new ArrayList<>();
    private ArrayList<Classification> punctuators = new ArrayList<>();

    public Operators() {

        /**
         * ***************************************Operator Elements
         * Assignment*******************************************************
         */
        ArrayList<String> ao = new ArrayList<>();
        ao.add(":");

        ArrayList<String> pm = new ArrayList<>();
        pm.add("+");
        pm.add("-");

        ArrayList<String> mdm = new ArrayList<>();
        mdm.add("*");
        mdm.add("/");
        mdm.add("%");

        ArrayList<String> inc_dec = new ArrayList<>();
        inc_dec.add("++");
        inc_dec.add("--");

        ArrayList<String> exp = new ArrayList<>();
        exp.add("**");

        ArrayList<String> ro = new ArrayList<>();
        ro.add("greater_than");
        ro.add("less_than");
        ro.add("greater_than_equal");
        ro.add("less_than_equal");
        
        
        ArrayList<String> ro_equality = new ArrayList<>();
        ro_equality.add("equals");
        ro_equality.add("not_equals");
        ro_equality.add("compare");
        
        ArrayList<String> and = new ArrayList<>();
        and.add("and");

        ArrayList<String> or = new ArrayList<>();
        or.add("or");

        ArrayList<String> not = new ArrayList<>();
        not.add("not");
        not.add("notb");

        ArrayList<String> andb = new ArrayList<>();
        andb.add("andb");

        ArrayList<String> orb = new ArrayList<>();
        orb.add("orb");

        ArrayList<String> shift = new ArrayList<>();
        shift.add("left_shift");
        shift.add("right_shift");

        ArrayList<String> xor = new ArrayList<>();
        xor.add("xor");

        /**
         * ***************************************Punctuator Elements
         * Assignment*******************************************************
         */
        ArrayList<String> semi_col = new ArrayList<>();
        semi_col.add(";");
        
        ArrayList<String> arg_del = new ArrayList<>();
        arg_del.add(",");

        ArrayList<String> arr_del_open = new ArrayList<>();
        arr_del_open.add("[");

        ArrayList<String> arr_del_close = new ArrayList<>();
        arr_del_close.add("]");

        ArrayList<String> par_open = new ArrayList<>();
        par_open.add("(");

        ArrayList<String> par_close = new ArrayList<>();
        par_close.add(")");
        
        ArrayList<String> list_par_open = new ArrayList<>();
        list_par_open.add("<");

        ArrayList<String> list_par_close = new ArrayList<>();
        list_par_close.add(">");

        ArrayList<String> dot = new ArrayList<>();
        dot.add(".");

        ArrayList<String> braces_open = new ArrayList<>();
        braces_open.add("{");

        ArrayList<String> braces_close = new ArrayList<>();
        braces_close.add("}");
        
        ArrayList<String> scope_res = new ArrayList<>();
        scope_res.add("::");

        ArrayList<String> newline = new ArrayList<>();
        newline.add("\r");

        ArrayList<String> exceptions_or = new ArrayList<>();
        exceptions_or.add("|");
        
        ArrayList<String> end_marker = new ArrayList<>();
        end_marker.add("$");
        /**
         * ***************************************Keywords Elements
         * Assignment*******************************************************
         */

        ArrayList<String> cls = new ArrayList<>();
        cls.add("class");

        ArrayList<String> arrow = new ArrayList<>();
        arrow.add("=>");

        ArrayList<String> withdraw = new ArrayList<>();
        withdraw.add("withdraw");

        ArrayList<String> brk = new ArrayList<>();
        brk.add("break");

        ArrayList<String> skip = new ArrayList<>();
        skip.add("skip");

        ArrayList<String> iff = new ArrayList<>();
        iff.add("if");

        ArrayList<String> then = new ArrayList<>();
        then.add("then");
        
        ArrayList<String> otherwise = new ArrayList<>();
        otherwise.add("otherwise");

        ArrayList<String> forr = new ArrayList<>();
        forr.add("for");

        ArrayList<String> until = new ArrayList<>();
        until.add("until");

        ArrayList<String> foreach = new ArrayList<>();
        foreach.add("foreach");

        ArrayList<String> global = new ArrayList<>();
        global.add("global");

        ArrayList<String> start = new ArrayList<>();
        start.add("start");

        ArrayList<String> doo = new ArrayList<>();
        doo.add("do");

        ArrayList<String> instance = new ArrayList<>();
        instance.add("instance_of");

        ArrayList<String> neww = new ArrayList<>();
        neww.add("new");

        ArrayList<String> nulll = new ArrayList<>();
        nulll.add("null");

        ArrayList<String> superr = new ArrayList<>();
        superr.add("super");

        ArrayList<String> tryy = new ArrayList<>();
        tryy.add("try");

        ArrayList<String> catchs = new ArrayList<>();
        catchs.add("catch");

        ArrayList<String> raise = new ArrayList<>();
        raise.add("raise");

        ArrayList<String> finallyy = new ArrayList<>();
        finallyy.add("finally");
        
        ArrayList<String> throwss = new ArrayList<>();
        throwss.add("throws");
        
        ArrayList<String> throow = new ArrayList<>();
        throow.add("throw");

        ArrayList<String> current = new ArrayList<>();
        current.add("current");

        ArrayList<String> type = new ArrayList<>();
        type.add("type_of");

        ArrayList<String> abs = new ArrayList<>();
        abs.add("abstract");

        ArrayList<String> finall = new ArrayList<>();
        finall.add("final");
        
        ArrayList<String> override = new ArrayList<>();
        override.add("override");
        
        ArrayList<String> virtual = new ArrayList<>();
        virtual.add("virtual");

        ArrayList<String> accessMod = new ArrayList<>();
        accessMod.add("public");
        accessMod.add("private");
        accessMod.add("protected");

        ArrayList<String> stat = new ArrayList<>();
        stat.add("static");

        ArrayList<String> voidd = new ArrayList<>();
        voidd.add("void");

        ArrayList<String> str = new ArrayList<>();
        str.add("String");

        ArrayList<String> file = new ArrayList<>();
        file.add("File");

        ArrayList<String> enumm = new ArrayList<>();
        enumm.add("enum");
        
        ArrayList<String> switchs = new ArrayList<>();
        switchs.add("switch");
        
        ArrayList<String> cases = new ArrayList<>();
        cases.add("case");
        
        ArrayList<String> defaults = new ArrayList<>();
        defaults.add("default");

        ArrayList<String> DT = new ArrayList<>();
        DT.add("int");
        DT.add("byte");
        DT.add("char");
        DT.add("float");
        DT.add("double");
        DT.add("byte");

        ArrayList<String> SIZ_M = new ArrayList<>();
        SIZ_M.add("short");
        SIZ_M.add("long");
        
        
        ArrayList<String> SIGN_M = new ArrayList<>();
        SIGN_M.add("signed");
        SIGN_M.add("unsigned");
        
        ArrayList<String> COLLEC = new ArrayList<>();
        COLLEC.add("List");
        COLLEC.add("ArrayList");
        COLLEC.add("LinkedList");
        COLLEC.add("Vector");
        COLLEC.add("Set");
        COLLEC.add("HashSet");
        COLLEC.add("LinkedHashSet");
        
        ArrayList<String> exten = new ArrayList<>();
        exten.add("extends");
        
        

        /**
         * ***************************************Operator Array
         * Assignment*******************************************************
         */
        AO = new Classification("AO", ao);
        PM = new Classification("PM", pm);
        MDM = new Classification("MDM", mdm);
        INC_DEC = new Classification("INC_DEC", inc_dec);
        EXP = new Classification("EXP", exp);
        RO = new Classification("RO", ro);
        RO_EQUALITY = new Classification("RO_EQUALITY", ro_equality);
        OR = new Classification("OR", or);
        AND = new Classification("AND", and);
        NOT = new Classification("NOT", not);
        OR_BIN = new Classification("OR_BIN", orb);
        AND_BIN = new Classification("AND_BIN", andb);
        SHIFT_BIN = new Classification("SHIFT_BIN", shift);
        XOR = new Classification("XOR", xor);

        /**
         * ***************************************Punctuator Array
         * Assignment*******************************************************
         */
        SEMI_COL = new Classification(";", semi_col);
        ARG_DEL = new Classification("ARG_DEL", arg_del);
        ARR_DEL_OPEN = new Classification("ARR_DEL_OPEN", arr_del_open);
        ARR_DEL_CLOSE = new Classification("ARR_DEL_CLOSE", arr_del_close);
        PAR_OPEN = new Classification("PAR_OPEN", par_open);
        PAR_CLOSE = new Classification("PAR_CLOSE", par_close);
        LIST_PAR_OPEN = new Classification("LIST_PAR_OPEN", list_par_open);
        LIST_PAR_CLOSE = new Classification("LIST_PAR_CLOSE", list_par_close);
        PROP_ACCESS = new Classification("PROP_ACCESS", dot);
        BRACES_OPEN = new Classification("BRACES_OPEN", braces_open);
        BRACES_CLOSE = new Classification("BRACES_CLOSE", braces_close);
        TERMINATOR = new Classification("TERMINATOR", newline);
        SCOPE_RES = new Classification("SCOPE_RES", scope_res);
        EXCEPTIONS_OR = new Classification("|", exceptions_or);
        END_MARKER = new Classification("$", end_marker);

        /**
         * ***************************************Keywords
         * Initialization*******************************************************
         */
        CLASS = new Classification("CLASS", cls);
        ARROW_FUNC = new Classification("ARROW_FUNC", arrow);
        WITHDRAW = new Classification("WITHDRAW", withdraw);
        BREAK = new Classification("BREAK", brk);
        SKIP = new Classification("SKIP", skip);
        IF = new Classification("IF", iff);
        THEN = new Classification("THEN", then);
        OTHERWISE = new Classification("OTHERWISE", otherwise);
        FOR = new Classification("FOR", forr);
        UNTIL = new Classification("UNTIL", until);
        FOR_EACH = new Classification("FOR_EACH", foreach);
        GLOBAL = new Classification("GLOBAL", global);
        START = new Classification("START", start);
        DO = new Classification("DO", doo);
        INSTANCE_OF = new Classification("INSTANCE_OF", instance);
        NEW = new Classification("NEW", neww);
        NULL = new Classification("NULL", nulll);
        SUPER = new Classification("SUPER", superr);
        TRY = new Classification("TRY", tryy);
        CATCH = new Classification("CATCH", catchs);
        RAISE = new Classification("RAISE", raise);
        FINALLY = new Classification("FINALLY", finallyy);
        THROW = new Classification("THROW", throow);
        THROWS = new Classification("THROWS", throwss);
        FINALLY = new Classification("FINALLY", finallyy);
        CURRENT = new Classification("CURRENT", current);
        TYPE_OF = new Classification("TYPE_OF", type);
        ABSTRACT = new Classification("ABSTRACT", abs);
        FINAL = new Classification("FINAL", finall);
        OVERRIDE = new Classification("OVERRIDE", override);
        VIRTUAL = new Classification("VIRTUAL", virtual);
        ACCESS_MOD = new Classification("ACCESS_MOD", accessMod);
        STATIC = new Classification("STATIC", stat);
        VOID = new Classification("VOID", voidd);
        STRING = new Classification("STRING", str);
        FILE = new Classification("FILE", file);
        ENUM = new Classification("ENUM", enumm);
        DATA_TYPES = new Classification("DATA_TYPES", DT);
        SIZE_MOD = new Classification("SIZE_MOD", SIZ_M);
        SIGN_MOD = new Classification("SIGN_MOD", SIGN_M);
        SWITCH = new Classification("SWITCH", switchs);
        CASE = new Classification("CASE", cases);
        DEFAULT = new Classification("DEFAULT", defaults);
        COLLECTION = new Classification("COLLECTION", COLLEC);
        EXTENDS = new Classification("EXTENDS", exten);
        
        
        
        
        
        
        keywords.add(CLASS);
        keywords.add(ARROW_FUNC);
        keywords.add(WITHDRAW);
        keywords.add(BREAK);
        keywords.add(SKIP);
        keywords.add(IF);
        keywords.add(THEN);
        keywords.add(OTHERWISE);
        keywords.add(FOR);
        keywords.add(UNTIL);
        keywords.add(FOR_EACH);
        keywords.add(GLOBAL);
        keywords.add(START);
        keywords.add(DO);
        keywords.add(INSTANCE_OF);
        keywords.add(NEW);
        keywords.add(NULL);
        keywords.add(SUPER);
        keywords.add(TRY);
        keywords.add(CATCH);
        keywords.add(RAISE);
        keywords.add(FINALLY);
        keywords.add(THROW);
        keywords.add(THROWS);
        keywords.add(CURRENT);
        keywords.add(TYPE_OF);
        keywords.add(ABSTRACT);
        keywords.add(FINAL);
        keywords.add(OVERRIDE);
        keywords.add(VIRTUAL);
        keywords.add(ACCESS_MOD);
        keywords.add(STATIC);
        keywords.add(VOID);
        keywords.add(STRING);
        keywords.add(FILE);
        keywords.add(ENUM);
        keywords.add(DATA_TYPES);
        keywords.add(SIZE_MOD);
        keywords.add(SIGN_MOD);
        keywords.add(SWITCH);
        keywords.add(DEFAULT);
        keywords.add(CASE);
        keywords.add(COLLECTION);
        keywords.add(EXTENDS);
        
        
 
        operators.add(AO);
        operators.add(PM);
        operators.add(MDM);
        operators.add(INC_DEC);
        operators.add(EXP);
        operators.add(RO);
        operators.add(RO_EQUALITY);
        operators.add(OR);
        operators.add(AND);
        operators.add(NOT);
        operators.add(OR_BIN);
        operators.add(AND_BIN);
        operators.add(SHIFT_BIN);
        operators.add(XOR); 
        
        punctuators.add(SEMI_COL);
        punctuators.add(ARG_DEL);
        punctuators.add(ARR_DEL_OPEN);
        punctuators.add(ARR_DEL_CLOSE);
        punctuators.add(PAR_OPEN);
        punctuators.add(PAR_CLOSE);
        punctuators.add(LIST_PAR_OPEN);
        punctuators.add(LIST_PAR_CLOSE);
        punctuators.add(PROP_ACCESS);
        punctuators.add(BRACES_OPEN);
        punctuators.add(BRACES_CLOSE);
        punctuators.add(TERMINATOR);
        punctuators.add(SCOPE_RES);
        punctuators.add(EXCEPTIONS_OR);
        punctuators.add(END_MARKER);
                
                
                
//                ARG_DEL = new Classification("ARG_DEL", arg_del);
//        ARR_DEL_OPEN = new Classification("ARR_DEL_OPEN", arr_del_open);
//        ARR_DEL_CLOSE = new Classification("ARR_DEL_CLOSE", arr_del_close);
//        PAR_OPEN = new Classification("PAR_OPEN", par_open);
//        PAR_CLOSE = new Classification("PAR_CLOSE", par_close);
//        PROP_ACCESS = new Classification("PROP_ACCESS", dot);
//        BRACES_OPEN = new Classification("BRACES_OPEN", braces_open);
//        BRACES_CLOSE = new Classification("BRACES_CLOSE", braces_close);
//        TERMINATOR = new Classification("TERMINATOR", newline);
    }

    public ArrayList<Classification> getOperators() {
        return operators;
    }

    public ArrayList<Classification> getPunctuators() {
        return punctuators;
    }

    
    public ArrayList<Classification> getKeywords() {
        return keywords;
    }

    public Classification getPM() {
        return PM;
    }

    public Classification getAO() {
        return AO;
    }

    public Classification getMDM() {
        return MDM;
    }

    public Classification getINC_DEC() {
        return INC_DEC;
    }

    public Classification getEXP() {
        return EXP;
    }

    public Classification getRO() {
        return RO;
    }
    
    public Classification getROEQUALITY() {
        return RO_EQUALITY;
    }

    public Classification getAND() {
        return AND;
    }

    public Classification getOR() {
        return OR;
    }

    public Classification getNOT() {
        return NOT;
    }

    public Classification getOR_BIN() {
        return OR_BIN;
    }

    public Classification getAND_BIN() {
        return AND_BIN;
    }

    public Classification getSHIFT_BIN() {
        return SHIFT_BIN;
    }

    public Classification getXOR() {
        return XOR;
    }

    public Classification getARG_DEL() {
        return ARG_DEL;
    }

    public Classification getARR_DEL_OPEN() {
        return ARR_DEL_OPEN;
    }

    public Classification getARR_DEL_CLOSE() {
        return ARR_DEL_CLOSE;
    }

    public Classification getPAR_OPEN() {
        return PAR_OPEN;
    }

    public Classification getPAR_CLOSE() {
        return PAR_CLOSE;
    }

    public Classification getPROP_ACCESS() {
        return PROP_ACCESS;
    }

    public Classification getBRACES_OPEN() {
        return BRACES_OPEN;
    }

    public Classification getBRACES_CLOSE() {
        return BRACES_CLOSE;
    }

    public Classification getTERMINATOR() {
        return TERMINATOR;
    }

    public Classification getCLASS() {
        return CLASS;
    }

    public Classification getARROW_FUNC() {
        return ARROW_FUNC;
    }

    public Classification getWITHDRAW() {
        return WITHDRAW;
    }

    public Classification getBREAK() {
        return BREAK;
    }

    public Classification getSKIP() {
        return SKIP;
    }

    public Classification getIF() {
        return IF;
    }

    public Classification getTHEN() {
        return THEN;
    }

    public Classification getFOR() {
        return FOR;
    }

    public Classification getUNTIL() {
        return UNTIL;
    }

    public Classification getFOR_EACH() {
        return FOR_EACH;
    }

    public Classification getGLOBAL() {
        return GLOBAL;
    }

    public Classification getSTART() {
        return START;
    }

    public Classification getDO() {
        return DO;
    }

    public Classification getINSTANCE_OF() {
        return INSTANCE_OF;
    }

    public Classification getNEW() {
        return NEW;
    }

    public Classification getNULL() {
        return NULL;
    }

    public Classification getSUPER() {
        return SUPER;
    }

    public Classification getTRY() {
        return TRY;
    }

    public Classification getEXCEPT() {
        return CATCH;
    }

    public Classification getRAISE() {
        return RAISE;
    }

    public Classification getFINALLY() {
        return FINALLY;
    }

    public Classification getCURRENT() {
        return CURRENT;
    }

    public Classification getTYPE_OF() {
        return TYPE_OF;
    }

    public Classification getABSTRACT() {
        return ABSTRACT;
    }

    public Classification getFINAL() {
        return FINAL;
    }

    public Classification getACCESS_MOD() {
        return ACCESS_MOD;
    }

    public Classification getSTATIC() {
        return STATIC;
    }

    public Classification getVOID() {
        return VOID;
    }

    public Classification getSTRING() {
        return STRING;
    }

    public Classification getFILE() {
        return FILE;
    }

    public Classification getENUM() {
        return ENUM;
    }

    public Classification getDATA_TYPES() {
        return DATA_TYPES;
    }

    public Classification getSIZE_MOD() {
        return SIZE_MOD;
    }

    public void display() {
        System.out.println("MDM");
        System.out.println("Class Name: " + this.MDM.getCp());
        for (int i = 0; i < this.MDM.getValues().size(); i++) {
            System.out.println("Value: " + this.MDM.getValues().get(i));
        }
    }

//    public String validateOpr(String word) {
//        for (int i = 0; i < 10; i++) {
//            return "invalid";
//        }
//        return "invalid";
//    }

    public Classification getRO_EQUALITY() {
        return RO_EQUALITY;
    }

    public Classification getSEMI_COL() {
        return SEMI_COL;
    }

    public Classification getSCOPE_RES() {
        return SCOPE_RES;
    }

    public Classification getLIST_PAR_OPEN() {
        return LIST_PAR_OPEN;
    }

    public Classification getLIST_PAR_CLOSE() {
        return LIST_PAR_CLOSE;
    }

    public Classification getOVERRIDE() {
        return OVERRIDE;
    }

    public Classification getVIRTUAL() {
        return VIRTUAL;
    }

    public Classification getSIGN_MOD() {
        return SIGN_MOD;
    }

    public Classification getSWITCH() {
        return SWITCH;
    }

    public Classification getCASE() {
        return CASE;
    }

    public Classification getDEFAULT() {
        return DEFAULT;
    }

    public Classification getCOLLECTION() {
        return COLLECTION;
    }

    public Classification getEXCEPTIONS_OR() {
        return EXCEPTIONS_OR;
    }

    public Classification getEND_MARKER() {
        return END_MARKER;
    }

    public Classification getOTHERWISE() {
        return OTHERWISE;
    }

    public Classification getCATCH() {
        return CATCH;
    }

    public Classification getEXTENDS() {
        return EXTENDS;
    }
}
