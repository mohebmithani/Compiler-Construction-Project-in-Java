/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycompiler;

import Assets.Operators;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author zainm
 */
public class MyCompiler {

    /**
     * @param args the command line arguments
     */
    static String className;

    public static void main(String[] args) throws Exception {
        // We need to provide file path as the parameter: 
        // double backquote is to avoid compiler interpret words 
        // like \test as \t (ie. as a escape sequence) 

        ArrayList<String> codeArray = new ArrayList<>();
        ArrayList<String> splitWord = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<ArrayList<String>> word = new ArrayList<>();
       // word.add(splitWord);
        
        Operators ope = new Operators();
       // ope.display();
        //  System.out.println(ope.validate("+"));
        
        FileWriter outputStream = null;
        File file = new File("src\\Files\\input.txt");
        outputStream = new FileWriter("src\\Files\\output.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        


        String readLine;
        try {
            while ((readLine = br.readLine()) != null) {
              //  outputStream.write(readLine);
              readLine += (char)13;
                codeArray.add(readLine);

            }
            codeArray.add("$");
        } finally {
            //outputStream.close();
//            System.out.println("*********CODE*************");
//            for (int i = 0; i < codeArray.size(); i++) {
//                System.out.println(codeArray.get(i));
//            }
//            System.out.println("\n\n");
        }
        
        WordSplitter w = new WordSplitter(codeArray);
        word = w.split();
        Tokenization token = new Tokenization(word);
        
        tokens = token.generateTokens();
//        token.display();
        try{
            for (int i = 0; i < tokens.size(); i++) {
                outputStream.write("( "+tokens.get(i).getCP()+" , "+tokens.get(i).getVP()+" , "+tokens.get(i).getLN()+" )"+"\n");
            }
        }finally {
            outputStream.close();
        }
        
        SyntaxSemanticAnalyzer s = new SyntaxSemanticAnalyzer(tokens);
        if(s.Class())
            if(s.getTokens(s.getIndex()).getCP().equals("$"))
                System.out.println("**********************************************************BUILD SUCCESSFUL******************************************************");
            else
                System.out.println("**********************************************************BUILD UN-SUCCESSFUL******************************************************");
        else
                System.out.println("**********************************************************BUILD UN-SUCCESSFUL******************************************************");
    }
}
