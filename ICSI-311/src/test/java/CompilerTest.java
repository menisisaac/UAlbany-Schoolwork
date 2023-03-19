import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CompilerTest {

    @Test
    public void assignmentOne() {
        String[] test = {"", "5", "5.23 - 8.5 + 3", "8 * -4 + 99999", "7 4 3 1", "+ - * /"};
        String[] answer = {"EndOfLine",
                "NUMBER(5) EndOfLine",
                "NUMBER(5.23) MINUS NUMBER(8.5) PLUS NUMBER(3) EndOfLine",
                "NUMBER(8) TIMES NUMBER(-4) PLUS NUMBER(99999) EndOfLine",
                "NUMBER(7) NUMBER(4) NUMBER(3) NUMBER(1) EndOfLine",
                "PLUS MINUS TIMES DIVIDE EndOfLine"};
        int count = 0;
        for (String s : test) {
            Lexer lex = new Lexer();
            List<Token> temp = lex.lex(s);
            String strTemp = "";
            for (Token t : temp) {
                if(t.getInstanceType() == Token.type.EndOfLine) {
                    strTemp += t;
                } else {
                    strTemp += t + " ";
                }
            }
            assertEquals(answer[count], strTemp);
            count++;
        }
    }

    @Test
    public void resolveTest() {
        MathOpNode one = new MathOpNode(MathOpNode.operator.ADD, new IntegerNode(5), new IntegerNode(6));
        MathOpNode two = new MathOpNode(MathOpNode.operator.MULTIPLY, one, new IntegerNode(5));
        Interpreter interpreter = new Interpreter();
        //System.out.println(interpreter.resolve(two));
    }

    @Test
    public void wordLexTest() {
        Lexer lex = new Lexer();
        List<Token> tokens = lex.lex("a,b,c : integer");
        System.out.println(tokens);
    }

    @Test
    public void functionHeaderTest() {
        Lexer lex = new Lexer();
        lex.lex("define start (var args:integer, jklj:real)");
        lex.lex("constants");
        lex.lex("pi=3.4");
        lex.lex("apple=5");
        lex.lex("(* bles");
        lex.lex("bee, apple, cat : integer *)");
        lex.lex("begin");
        lex.lex("Happy := 5 + 2");
        lex.lex("vari := 5 * 3 + 4");
        lex.lex("end");
        List<Token>tokens = lex.getAllTokens();
        Parser parser = new Parser(tokens);
        Node node = parser.parse();
        List<InterpreterDataType> params = new ArrayList<>();
        params.add(new IntDataType(7));
        params.add(new FloatDataType(5.5f));
        Interpreter.InterpretFunction(((FunctionDefinition)node), params);
        System.out.println(node);

    }

    @Test
    public void resolve() {
        MathOpNode node = new MathOpNode(MathOpNode.operator.ADD, new VariableReferenceNode("apple"), new FloatNode(5f));
        List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();
        interpreterDataTypes.add(new FloatDataType(5f));
        HashMap<String, InterpreterDataType> variables = new HashMap<>();
        variables.put("apple", interpreterDataTypes.get(0));
        Interpreter interpreter = new Interpreter();
        System.out.println(interpreter.resolveFloat(node, variables));
    }
    @Test
    public void booleanTest() {
        BooleanExpressionNode booleanExpressionNode = new BooleanExpressionNode(new FloatNode(57), new FloatNode(57), Token.type.equal);
        System.out.println(Interpreter.InterpretBoolean(booleanExpressionNode, new HashMap<>()));
    }

    @Test
    public void newLexerTest() {
        ImprovedLexer lex = new ImprovedLexer();
        //System.out.println(lex.lex("isaac := 5 * 6"));
        System.out.println(lex.lex("while 7 > 5"));
    }

    @Test
    public void LeftTest() {
        List<InterpreterDataType> interpreterDataType = new ArrayList<>();
        //interpreterDataType.add(new StringDataType("123456789"));

        interpreterDataType.add(new IntDataType(6));
        interpreterDataType.add(new FloatDataType(3.5536f));
        //interpreterDataType.add(new StringDataType(""));
        //Substring left = new Substring();
        IntegerToReal realToInteger = new IntegerToReal();
        ParameterNode params = realToInteger.Execute(interpreterDataType).get(1);
        System.out.println(params.getParameter());
    }
    @Test
    public void newExpression() {
        ImprovedLexer lexer = new ImprovedLexer();
        List<Token> tokens = new ArrayList<>(Arrays.asList(new Token(Token.type.NUMBER ,"6"), new Token(Token.type.TIMES),new Token(Token.type.NUMBER, "7"), new Token(Token.type.lessThanEqualTo), new Token(Token.type.NUMBER, "8"), new Token(Token.type.PLUS), new Token(Token.type.NUMBER, "34"),new Token(Token.type.EndOfLine)));
        NewAndImprovedExecution newAndImprovedExecution = new NewAndImprovedExecution(tokens);

    }

}
