import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewAndImprovedExecution {
    HashMap<Token.type, MathOpNode.operator> tokenToOpMapping = new HashMap(){{        //Allows you to check if type is applicable to math
        put(Token.type.PLUS, MathOpNode.operator.ADD);
        put(Token.type.MINUS, MathOpNode.operator.SUBTRACT);
        put(Token.type.DIVIDE, MathOpNode.operator.DIVIDE);
        put(Token.type.TIMES, MathOpNode.operator.MULTIPLY);
        put(Token.type.mod, MathOpNode.operator.mod);
        put(Token.type.greaterThanEqualTo, MathOpNode.operator.greaterThanEqualTo);
        put(Token.type.lessThanEqualTo, MathOpNode.operator.lessThanEqualTo);
        put(Token.type.lessThan, MathOpNode.operator.lessThan);
        put(Token.type.greaterThen, MathOpNode.operator.greaterThan);
        put(Token.type.equal, MathOpNode.operator.equalTo);
        put(Token.type.notEqual, MathOpNode.operator.notEqual);
    }};
    private List<Token> tokens = new ArrayList<>();
    public NewAndImprovedExecution(List<Token> tokens) {
        this.tokens = tokens;
        System.out.println(Expression());
    }
    public Node Expression() {
        if(matchAndRemove(Token.type.rightParen) != null) {
            return null;
        }
        Node left = PLUSMINUS();
        MathOpNode.operator operator = null;
        if(match(Token.type.thenControl) != null) {
            return left;
        }
        if(matchAndRemove(Token.type.rightParen) != null) {
            return left;
        }
        operator = tokenToOpMapping.get(tokens.get(0).getInstanceType());
        tokens.remove(0);
        left = new MathOpNode(operator, left, PLUSMINUS());
        if(matchBoolean() != null) {
            throw new RuntimeException("Syntax Error: Can only be one boolean comparator");
        }
        return left;
    }

    public Node PLUSMINUS() {
        Node left = Term();
        Node right = null;
        MathOpNode.operator operator = null;
        while(true) {
            if (tokens.isEmpty() || (match(Token.type.PLUS) == null && match(Token.type.MINUS) == null)) {
                break;
            } else if(matchAndRemove(Token.type.PLUS) != null) {
                operator = MathOpNode.operator.ADD;
                right = Term();
            } else if(matchAndRemove(Token.type.MINUS) != null){
                operator = MathOpNode.operator.SUBTRACT;
                right = Term();
            }
        }
        if(operator == null) {
            return left;
        } else {
            return new MathOpNode(operator, left, right);
        }
    }

    /*
        Used to parse * - mod operators
     */
    public Node Term() {
        Node left = Factor();
        Node right = null;
        MathOpNode.operator operator = null;
        while(true) {
            if (tokens.isEmpty() || (match(Token.type.TIMES) == null && match(Token.type.DIVIDE) == null && match(Token.type.mod) == null)) {
                break;
            } else if(matchAndRemove(Token.type.TIMES) != null) {
                operator = MathOpNode.operator.MULTIPLY;
                right = Term();
            } else if(matchAndRemove(Token.type.DIVIDE) != null){
                operator = MathOpNode.operator.DIVIDE;
                right = Term();
            } else if(matchAndRemove(Token.type.mod) != null) {
                operator = MathOpNode.operator.mod;
                right = Term();
            }
        }
        if(operator == null) {
            return left;
        } else {
            return new MathOpNode(operator, left, right);
        }
    }

    /*
        Extracts either a single number, char, string, or boolean or a new expression if a parenthesis
     */
    public Node Factor() {
        Token identifier = null;
        if(tokens.isEmpty() || matchAndRemove(Token.type.EndOfLine) != null) {
            throw new RuntimeException("Syntax Error");
        }
        if(matchAndRemove(Token.type.leftParen) != null) {
            return Expression();
        }
        if((identifier = matchAndRemove(Token.type.Identifier)) != null) {
            return new VariableReferenceNode(identifier.getValue());
        }
        if((identifier = matchAndRemove(Token.type.trueBool)) != null) {
            return new BoolNode(true);
        }
        if((identifier = matchAndRemove(Token.type.falseBool)) != null) {
            return new BoolNode(false);
        }
        if((identifier = matchAndRemove(Token.type.charType)) != null) {
            return new CharNode(identifier.getValue());
        }
        if((identifier = matchAndRemove(Token.type.Stringtype)) != null) {
            return new StringNode(identifier.getValue());
        }
        Token number = tokens.remove(0);
        if(number.getValue().contains(".")) {
            return new FloatNode(Float.parseFloat(number.getValue()));
        } else {
            return new IntegerNode(Integer.parseInt(number.getValue()));
        }
    }

    /*
        Checks if first token in list matches certain token
        Returns if true
     */
    public Token match(Token.type type) {
        if(tokens.isEmpty()) {
            throw new RuntimeException("Syntax Error");
        }
        if(tokens.get(0).getInstanceType() == type) {
            return tokens.get(0);
        }
        return null;
    }
    /*
        Checks if first token in list matches certain token and removes it
        Returns if true
     */
    public Token matchAndRemove(Token.type type) {
        if(tokens.isEmpty()) {
            throw new RuntimeException("Syntax Error");
        }
        if(tokens.get(0).getInstanceType() == type) {
            return tokens.remove(0);
        }
        return null;
    }
    /*
        Checks if first token in list matches boolean operators and removes it
        Returns if true
     */
    public Token.type matchAndRemoveBoolean() {
        Token.type[] bools = {Token.type.lessThanEqualTo, Token.type.lessThan, Token.type.greaterThanEqualTo, Token.type.greaterThen,
                Token.type.equal, Token.type.notEqual};
        for(Token.type type : bools) {
            if(matchAndRemove(type) != null) {
                return type;
            }
        }
        return null;
    }
    /*
    Checks if first token in list matches boolean operators
    Returns if true
    */
    public Token.type matchBoolean() {
        Token.type[] bools = {Token.type.lessThanEqualTo, Token.type.lessThan, Token.type.greaterThanEqualTo, Token.type.greaterThen,
                Token.type.equal, Token.type.notEqual};
        for(Token.type type : bools) {
            if(match(type) != null) {
                return type;
            }
        }
        return null;
    }
}

