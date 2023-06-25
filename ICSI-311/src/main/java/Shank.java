import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
    To Do List:
        * Add more comments for later assignment files
        * Debug some more
    Text Files:
        TikTakToe.txt  - A sloppy two player tik tak toe game with no safeguards for choosing the same tile twice

 */



public class Shank {
    /*
        Lexes each line --> Passes all tokens to parser
        Parses each function --> Adds each function to interpreter hashmap
        Calls the interpreter start function --> runs program
     */
    public static void main(String[] args) {
        List<String> lines;
        List<FunctionDefinition> functionNodes = new ArrayList<>();
        Interpreter interpreter = new Interpreter();
        //Checks that there is only one filename
        if(args.length == 1) {
            try {
                lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
                ImprovedLexer lexer = new ImprovedLexer();
                for (String line : lines) {
                    lexer.lex(line);
                }
                List<Token> tokens = lexer.getAllTokens();
                Parser parser = new Parser(tokens);
                while(!tokens.isEmpty()) {
                    FunctionDefinition function = parser.parse();
                    if(function != null) {
                        functionNodes.add(function);
                        SemanticAnalysis.SemanticAnalysis(function);
                        interpreter.addFunctionDefintion(function.getName(), function);
                    }
                }
                HashMap<String, CallableNode> functions = Interpreter.functions;
                IRGeneration irg = new IRGeneration(functions);
                irg.compileFunctions();
                irg.generateFile();
                for (String fun : irg.getBril().keySet()) {
                    for(String str : irg.getBril().get(fun)) {
                        System.out.println(str);
                    }
                }

                //Interpreter.start();

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}
