# Compiler-Construction-Project-in-Java
It takes source code from input.txt file and decides if it is syntactically and semantically correct.

it does the following:
1) It takes source code from the input.txt file
2) It separates the words to generate tokens.
3) Its makes token of these words and save it into output.txt file hence completing first first phase(lexical) of compiler. The criteria of 
   token vary on every language. Details of my language will be in language.docx.
4) After generating tokens it checks syntax as well as semantic. In syntax phase it checks the tokens according to my "40 page" CFG. Semantic
   and syntax are integrated. I've made three tables for semantic analysis(ClassTable, ClassAttribTable, FuncAttribTaable).
   ClassTable:It saves every classes and it's details like accessmodifier, does it extends? etc
   ClassAttribTable: It contains attributes details as well as methods used in this class.
   FuncAttribTable: It will contain details of the variables used in some class referenced by 'ClassAttribTable'. 
   
Notes: semantic phase is not complete.
