import java.util.*;


/*
    Takes in a list of Tokens from lexical analysis and parses them
    into an AST
    @param tokens A list of Tokens
 */
public class Parser {

    List<Token> tokens;
    HashMap<Token.type, MathOpNode.operator>  tokenToOpMapping = new HashMap(){{        //Allows you to check if type is applicable to math
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


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }



    public FunctionDefinition parse() {
        return FunctionDefinition();
    }
    /*
        Parses for a function definition, which includes define, parameters inside a parenthesis,
        an optional constants section, an optional variables section, and a function body
        Returns a function definition node
     */
    public FunctionDefinition FunctionDefinition() {
        List<VariableNode> parameters = null;
        HashSet<VariableNode> constants = null;
        HashSet<VariableNode> variables = null;
        List<statementNode> statementNodes = null;
        if(matchAndRemove(Token.type.define) != null) {
            if(match(Token.type.Identifier) != null) {
                String name = tokens.remove(0).getValue();
                if(matchAndRemove(Token.type.leftParen) != null) {
                    parameters = parameters();
                    constants = constants();
                    variables = variables();
                    bodyFunction();
                    statementNodes = statements();
                    return new FunctionDefinition(name, parameters, variables, constants, statementNodes);
                }
            }
        }
        throw new RuntimeException("Syntax Error: Could not find  function definition");
    }
    /*
        Parses for a body function with begin and remove, removes begin line
        Throws error if begin and end not found
     */
    public boolean bodyFunction() {
        if(matchAndRemove(Token.type.begin) != null) {
            if (matchAndRemove(Token.type.EndOfLine) != null) {
                boolean hasEnd = false;
                for(Token t : tokens) {
                    if(hasEnd && t.getInstanceType() == Token.type.EndOfLine) {
                        return true;
                    } else if(hasEnd){
                        throw new RuntimeException("Syntax Error: Expected EndOfLine");
                    }
                    if(t.getInstanceType() == Token.type.end) {
                        hasEnd = true;
                    } else if(t.getInstanceType() == Token.type.define) {
                        throw new RuntimeException("Syntax Error: Cannot have function definition inside body");
                    }
                }
                throw new RuntimeException("Syntax Error: No end clause");
            } else {
                throw new RuntimeException("Syntax Error: Expected End of Line");
            }
        } else {
            throw new RuntimeException("Syntax Error: No function body");
        }

    }
    /*
        Parses for a constant section
        Looks for identifier followed by an equals sign and an expression or value
        Returns list of VariableNodes with isConstant set to true
     */
    public HashSet<VariableNode> constants() {
        Token constant;
        HashSet<VariableNode> constants = new HashSet<>();
        if(matchAndRemove(Token.type.constants) != null && matchAndRemove(Token.type.EndOfLine) != null) {
            while(!tokens.isEmpty() && match(Token.type.Identifier) != null) {
                String name = tokens.remove(0).getValue();
                if(matchAndRemove(Token.type.equal) != null) {
                    if((constant = match(Token.type.NUMBER)) != null) {
                        if(constant.getValue().contains(".")) {
                            constants.add(new VariableNode(name, true, Token.type.real, new FloatNode(Float.parseFloat(matchAndRemove(Token.type.NUMBER).getValue()))));
                            if(matchAndRemove(Token.type.EndOfLine) == null) {
                                throw new RuntimeException("Syntax Error: Cannot have an expression in constants section");
                            }
                        } else {
                            constants.add(new VariableNode(name, true, Token.type.integer, new IntegerNode(Integer.parseInt(matchAndRemove(Token.type.NUMBER).getValue()))));
                            if(matchAndRemove(Token.type.EndOfLine) == null) {
                                throw new RuntimeException("Syntax Error: Cannot have an expression in constants section");
                            }
                        }
                    } else if((constant = matchAndRemove(Token.type.Stringtype)) != null) {
                        constants.add(new VariableNode(name, true, Token.type.Stringtype, new StringNode(constant.getValue())));
                        if(matchAndRemove(Token.type.EndOfLine) == null) {
                            throw new RuntimeException("Syntax Error: Cannot have an expression in constants section");
                        }
                    }
                } else {
                    throw new RuntimeException("Expected Assignment");
                }
            }
        }
        return constants;
    }
    /*
        Looks for an identifier followed by either a comma or colon
        Once a colon is hit, looks for a datatype
        Then goes to newline to find more variable declarations
     */
    public HashSet<VariableNode> processVariables() {
        HashSet<VariableNode> variables = new HashSet<>();
        while (!tokens.isEmpty() && match(Token.type.Identifier) != null) {
            HashSet<Token> currTokens = new HashSet<>();
            int i = 3;
            while (true) {
                if(i % 2 == 1) {
                    if(match(Token.type.Identifier) != null) {
                        currTokens.add(tokens.remove(0));
                        i++;
                    } else {
                        throw new RuntimeException("Syntax Error: Expected Identifier");
                    }
                } else {
                    if (matchAndRemove(Token.type.comma) != null) {
                        i++;
                    } else if(matchAndRemove(Token.type.colon) != null) {
                        break;
                    } else {
                        throw new RuntimeException("Syntax Error: Expected Comma or Colon");
                    }
                }
            }
        Token.type type = tokens.remove(0).getInstanceType();
        if(matchAndRemove(Token.type.EndOfLine) == null) {
            throw new RuntimeException("Syntax Error: Expected End of Line");
        }
        for (Token t : currTokens) {
            variables.add(new VariableNode(t.getValue(), false, type));
        }

    }
        return variables;
    }
    /*
        Checks if there is a variables section
     */
    public HashSet<VariableNode> variables() {
        if(!tokens.isEmpty() && matchAndRemove(Token.type.variables) != null) {
            tokens.remove(0);
            return processVariables();
        }
        return new HashSet<>();
    }
    /*
        Goes through statements between begin and end
        Call on statement() to check if each statement is valid
        If statement is not valid  returns null and returns the statements
     */
    public List<statementNode> statements() {
        List<statementNode> statements = new ArrayList<>();
        while(true) {
            if(matchAndRemove(Token.type.end) != null && matchAndRemove(Token.type.EndOfLine) != null) {
                return statements;
            }
            statementNode statementNode = statement();
            if (statementNode == null) {
                return statements;
            } else {
                statements.add(statementNode);
            }
        }
    }
    /*
        Checks to see if a line is valid statement,
        if it is returns specific  subclass of statementNode, else
        throws exception
     */
    public statementNode statement() {
        statementNode statementNode = null;
        if((statementNode = assignment()) != null) {
            return statementNode;
        } else if((statementNode = While()) != null) {
            return statementNode;
        } else if((statementNode = Repeat()) != null) {
            return statementNode;
        } else if((statementNode = If()) != null) {
            return statementNode;
        } else if((statementNode = For()) != null) {
            return statementNode;
        } else if((statementNode = FunctionCall()) != null) {
            return statementNode;
        } else {
            throw new RuntimeException("Syntax Errror: Invalid Statement");
        }
    }
    /*
        Statement which calls upon another function definition, collects parameters and creates functioncallnode
        Should have identifier and parameters separated by comma, includes var if variable
     */
    public FunctionCallNode FunctionCall() {
        List<ParameterNode> parameters = new ArrayList<>();
        String name = null;
        if ((name = matchAndRemove(Token.type.Identifier).getValue()) != null) {
            while (true) {
                if(matchAndRemove(Token.type.EndOfLine) != null) {
                    return new FunctionCallNode(name, parameters);
                }
                ParameterNode temp = getFunctionCallParam();
                parameters.add(temp);
                if (matchAndRemove(Token.type.comma) != null) {
                    continue;
                }
                }
            }
        throw new RuntimeException("Syntax Error: Function Call Clause");
    }
    /*
        Collects a parameter, can include raw data types or reference nodes
     */
    public ParameterNode getFunctionCallParam() {
        Token param = null;

        if((param = matchAndRemove(Token.type.NUMBER)) != null) {
            if(param.getValue().contains(".")) {
                return new ParameterNode(new FloatNode(Float.parseFloat(param.getValue())), false);
            } else {
                return new ParameterNode(new IntegerNode((Integer.parseInt(param.getValue()))), false);
            }
        } else if((param = matchAndRemove(Token.type.Identifier)) != null) {
            return new ParameterNode(new VariableReferenceNode(param.getValue()), false);
        } else if(matchAndRemove(Token.type.var) != null) {
            if((param = matchAndRemove(Token.type.Identifier)) != null) {
                return new ParameterNode(new VariableReferenceNode(param.getValue()), true);
            }
        } else if((param = matchAndRemove(Token.type.Stringtype)) != null) {
            return new ParameterNode(new StringNode(param.getValue()), false);
        } else if((param = matchAndRemove(Token.type.charType)) != null) {
            return new ParameterNode(new CharNode(param.getValue()), false);
        } else if((param  = matchAndRemove(Token.type.trueBool)) != null) {
            return new ParameterNode(new BoolNode(true), false);
        } else if((param = matchAndRemove(Token.type.falseBool)) != null) {
            return new ParameterNode(new BoolNode(false), false);
        }
        throw new RuntimeException("Syntax Error: param not found");
    }
    /*
        Returns an assignment of an expression to a target variable reference node
        Needs to be identifier followed by assignment followed by expression
     */
    public statementNode assignment() {
        String target = null;
        Node expression = null;
        if(tokens.size() >= 4) {
            if(tokens.get(1).getInstanceType() == Token.type.assignment) {
                if((target =  matchAndRemove(Token.type.Identifier).getValue()) != null) {
                    matchAndRemove(Token.type.assignment);
                    expression = Expression();
                    if(expression == null) {
                        throw new RuntimeException("Syntax Error: Expected Expression");
                    }
                    if(matchAndRemove(Token.type.EndOfLine) == null) {
                        throw new RuntimeException("Syntax Error: Expected End of line");
                    }
                    return new AssignmentNode(new VariableReferenceNode(target), expression);
                } else {
                    throw new RuntimeException("Syntax Error: Expected Identifier");
                }
            }
        }
        return null;
    }
    /*
        Processes parameters defined in the function definition,
        creates a list of VariableNodes by continuously calling on parameter() until a right paren
     */
    public List<VariableNode> parameters() {
        List<VariableNode> parameters = new ArrayList<>();
        while(matchAndRemove(Token.type.rightParen) == null) {
            List<VariableNode> variableNodes = parameter();
            for(VariableNode variableNode : variableNodes) {
                parameters.add(variableNode);
            }
            if(matchAndRemove(Token.type.comma) != null || matchAndRemove(Token.type.semicolon) != null) {

            } else if(match(Token.type.rightParen) != null) {
                continue;
            }  else{
                throw new RuntimeException("Syntax Error: Unexpected Token");
            }
        }
        if(matchAndRemove(Token.type.EndOfLine) != null) {
            return parameters;
        }
        throw new RuntimeException("Syntax Error: Expected EndofLine");
    }
    /*
        Returns a list of parameters of the same type
        These will all be the same type of constant or variable
        Parses with comma and colon separated values
     */
    public List<VariableNode> parameter() {
        List<Token> identifiers = new ArrayList<>();
        List<VariableNode> variableNodes = new ArrayList<>();
        boolean isConst = matchAndRemove(Token.type.var) == null;
        int i = 3;
        while(true) {
            if(i % 2 == 1) {
                Token name = matchAndRemove(Token.type.Identifier);
                if(name == null) {
                    throw new RuntimeException("Syntax Error: Identifier Expected");
                } else {
                    identifiers.add(name);
                    i++;
                }
            } else {
                if(matchAndRemove(Token.type.comma) != null) {
                    i++;
                } else if(matchAndRemove(Token.type.colon) != null){
                    break;
                } else {
                    throw new RuntimeException("Syntax Error: Unexpected Token");
                }
            }
        }
        Token.type dataType = tokens.remove(0).getInstanceType();
        for(Token t : identifiers) {
            variableNodes.add(new VariableNode(t.getValue(), isConst, dataType));
        }
        return variableNodes;
    }
    /*
        This will be removed very shortly, finds an expression of left and right side of conditional operator
        Will be combined with expression() sometime this week
     */
    public BooleanExpressionNode booleanExpression() {
        Node left = Expression();
        Token.type condition = matchAndRemoveBoolean();
        if(condition == null) {
            throw new RuntimeException("Syntax Error: Condition Expected");
        }
        Node right = Expression();
        return new BooleanExpressionNode(left, right, condition);
    }
    /*
        Looks for the structure of while loop
        If While is found and the rest of structure is missing error thrown
        While consists of While tokens boolean expression and then body with statements
     */
    public WhileNode While() {
        if(matchAndRemove(Token.type.whileLoop) != null) {
                Node booleanExpression = Expression();
                    if(matchAndRemove(Token.type.EndOfLine) != null) {
                        bodyFunction();
                        List<statementNode> statementNodes = statements();
                        return new WhileNode(booleanExpression, statementNodes);
                    }
        } else {
            return null;
        }
        throw new RuntimeException("Syntax Error: While Loop Clause");
    }
    /*
        Looks for the structure of for loop
        If for is found and rest of structure is missing error thrown
        For consists of target int and start and end ints
     */
    public ForNode For() {
        Token identifier = null;
        Token start =  null;
        Token end = null;
        Node startNode = null;
        Node endNode = null;
        List<statementNode> statementNodeList = new ArrayList<>();
        if(matchAndRemove(Token.type.forLoop) != null) {
            if((identifier = matchAndRemove(Token.type.Identifier)) != null) {
                if(matchAndRemove(Token.type.from) != null) {
                    if ((start = matchAndRemove(Token.type.Identifier)) != null || (start = matchAndRemove(Token.type.NUMBER)) != null) {
                        if(matchAndRemove(Token.type.to) != null) {
                            if((end = matchAndRemove(Token.type.Identifier)) != null || (end = matchAndRemove(Token.type.NUMBER)) != null) {
                                if(matchAndRemove(Token.type.EndOfLine) != null) {
                                    bodyFunction();
                                    statementNodeList = statements();
                                    if(start.getInstanceType() == Token.type.Identifier) {
                                        startNode = new VariableReferenceNode(start.getValue());
                                    } else {
                                        startNode = new IntegerNode(Integer.parseInt(start.getValue()));
                                    }
                                    if(end.getInstanceType() == Token.type.Identifier) {
                                        endNode = new VariableReferenceNode(end.getValue());
                                    } else {
                                        endNode = new IntegerNode(Integer.parseInt(end.getValue()));
                                    }
                                    return new ForNode(new VariableReferenceNode(identifier.getValue()), startNode, endNode, statementNodeList);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return null;
        }
        throw new RuntimeException("Syntax Error: For Loop Clause");
    }
    /*
        Looks for the structure of repeat loop
        If repeat is found and the rest of structure is missing error thrown
        repeat consists of repeat token then body with statements then boolean expression
     */
    public RepeatNode Repeat() {
        List<statementNode> statementNodes = null;
        Node booleanExpression = null;
        if(matchAndRemove(Token.type.repeatLoop) != null && matchAndRemove(Token.type.EndOfLine) != null) {
            bodyFunction();
            statementNodes = statements();
            if (matchAndRemove(Token.type.until) != null) {
                    booleanExpression = Expression();
                    if(matchAndRemove(Token.type.EndOfLine) != null) {
                        return new RepeatNode(booleanExpression, statementNodes);
                    }
                }
            } else {
            return null;
        }
        throw new RuntimeException("Syntax Error: Repeat Loop Clause");
    }
    /*
        Looks for structure of if loop
        If If is found and rest of structure is missing error thrown
        if consists of if token, boolean expression, then body with statements and optional else or elsif clause
        If there is an else or elsif clause then elseOrElseIf() is called to deal with the nested statements
     */
    public IfNode If() {
        List<statementNode> statementNodes = null;
        Node booleanExpression = null;
        IfNode next = null;
        if(matchAndRemove(Token.type.ifControl) != null) {
            booleanExpression = Expression();
            if(matchAndRemove(Token.type.thenControl) != null && matchAndRemove(Token.type.EndOfLine) != null) {
                bodyFunction();
                statementNodes = statements();
                next = elseOrElsif();
                return new IfNode(booleanExpression, statementNodes, next);
            }
        } else {
            return null;
        }
        throw new RuntimeException("Syntax Error: If Node clause");
    }
    /*
        Looks for else or elsif statements
        If else is found returns else node with statements
        If elsif is found adds statements and calls elseOrElsif to check if there are more nested else or elsif tokens
     */
    public IfNode elseOrElsif() {
        List<statementNode> statementNodesList = null;
        Node booleanExpression = null;
        IfNode next = null;
        if(matchAndRemove(Token.type.elseControl) != null & matchAndRemove(Token.type.EndOfLine) != null) {
            bodyFunction();
            statementNodesList = statements();
            return new ElseNode(statementNodesList);
        } else if(matchAndRemove(Token.type.elsifControl) != null){
            booleanExpression = Expression();
            if(matchAndRemove(Token.type.thenControl)!= null && matchAndRemove(Token.type.EndOfLine) != null) {
                bodyFunction();
                statementNodesList = statements();
                next = elseOrElsif();
                return new IfNode(booleanExpression, statementNodesList, next);
            }
        }
        return null;
    }

    /*
        Used to parse between multiple + -
        Soon this will parse between + - and all boolean expressions
     */
    public Node Expression() {
        if(matchAndRemove(Token.type.rightParen) != null) {
            return null;
        }
        Node left = PLUSMINUS();
        MathOpNode.operator operator = null;
        while(match(Token.type.EndOfLine) == null) {
            if (match(Token.type.thenControl) != null) {
                return left;
            }
            if (matchAndRemove(Token.type.rightParen) != null) {
                return left;
            }
            operator = tokenToOpMapping.get(tokens.get(0).getInstanceType());
            tokens.remove(0);

            left = new MathOpNode(operator, left, PLUSMINUS());
            if (matchBoolean() != null) {
                throw new RuntimeException("Syntax Error: Can only be one boolean comparator");
            }
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
                left = new MathOpNode(MathOpNode.operator.ADD, left, Term());
            } else if(matchAndRemove(Token.type.MINUS) != null){
                left = new MathOpNode(MathOpNode.operator.SUBTRACT, left, Term());
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
        MathOpNode.operator operator = null;
        while(true) {
            if (tokens.isEmpty() || (match(Token.type.TIMES) == null && match(Token.type.DIVIDE) == null && match(Token.type.mod) == null)) {
                break;
            } else if(matchAndRemove(Token.type.TIMES) != null) {
                left = new MathOpNode(MathOpNode.operator.MULTIPLY, left, Factor());
            } else if(matchAndRemove(Token.type.DIVIDE) != null){
                left = new MathOpNode(MathOpNode.operator.DIVIDE, left, Factor());
            } else if(matchAndRemove(Token.type.mod) != null) {
                left = new MathOpNode(MathOpNode.operator.mod, left, Factor());
            }
        }
        return left;
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
        if(matchAndRemove(Token.type.trueBool) != null) {
            return new BoolNode(true);
        }
        if(matchAndRemove(Token.type.falseBool) != null) {
            return new BoolNode(false);
        }
        if((identifier = matchAndRemove(Token.type.charType)) != null) {
            return new CharNode(identifier.getValue());
        }
        if((identifier = matchAndRemove(Token.type.Stringtype)) != null) {
            return new StringNode(identifier.getValue());
        }
        Token number = tokens.remove(0);
        if(number.getInstanceType() == Token.type.PLUS ) {
            if ((number = matchAndRemove(Token.type.NUMBER)) != null) {
                number = new Token(Token.type.NUMBER, "+" + number.getValue());
            }
        } else if(number.getInstanceType() == Token.type.MINUS) {
                if ((number = matchAndRemove(Token.type.NUMBER)) != null) {
                    number = new Token(Token.type.NUMBER, "-" + number.getValue());
                } else if((number = matchAndRemove(Token.type.Identifier)) != null) {
                    return new MathOpNode(MathOpNode.operator.MULTIPLY, new IntegerNode(-1), new VariableReferenceNode(number.getValue()));
                }
        }
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
