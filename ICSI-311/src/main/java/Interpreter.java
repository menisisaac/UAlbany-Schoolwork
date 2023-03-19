import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Interpreter {
    public static HashMap<String, CallableNode> functions = new HashMap<>(){{
        put("read", new ReadNode());
        put("write", new WriteNode());
        put("squareRoot", new SquareRootNode());
        put("GetRandom", new GetRandom());
        put("IntegerToReal", new IntegerToReal());
        put("RealToInteger", new RealToInteger());
        put("Substring", new Substring());
        put("Left", new Left());
        put("Right", new Right());
    }};
    public Interpreter() {}

    /*
        Takes in a Math Operation Node and simplifies it to a single float
     */
    public static FloatNode resolveFloat(Node node, HashMap<String, InterpreterDataType> variables) {
        if(node instanceof FloatNode) {
            return (FloatNode)node;
        } else if (node instanceof VariableReferenceNode) {
                return new FloatNode(((FloatDataType) (variables.get(((VariableReferenceNode) node).getName()))).getValue());
        } else {
            MathOpNode temp = (MathOpNode) node;
            Node left = resolveFloat(temp.getLeft(), variables);
            Node right = resolveFloat(temp.getRight(), variables);
            if(temp.getInstanceType() == MathOpNode.operator.ADD) {
                return new FloatNode(((FloatNode)left).getValue() + ((FloatNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.MULTIPLY) {
                return new FloatNode(((FloatNode)left).getValue() * ((FloatNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.DIVIDE) {
                return new FloatNode(((FloatNode)left).getValue() / ((FloatNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.SUBTRACT) {
                return new FloatNode(((FloatNode)left).getValue() - ((FloatNode)right).getValue());
            }
        }
        return null;
    }
    public static IntegerNode resolveInt(Node node, HashMap<String, InterpreterDataType> variables) {
        if(node instanceof IntegerNode) {
            return (IntegerNode)node;
        } else if (node instanceof VariableReferenceNode) {
            return new IntegerNode(((IntDataType) (variables.get(((VariableReferenceNode) node).getName()))).getValue());
        } else if(node instanceof MathOpNode){
            MathOpNode temp = (MathOpNode) node;
            Node left = resolveInt(temp.getLeft(), variables);
            Node right = resolveInt(temp.getRight(), variables);
            if(temp.getInstanceType() == MathOpNode.operator.ADD) {
                return new IntegerNode(((IntegerNode)left).getValue() + ((IntegerNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.MULTIPLY) {
                return new IntegerNode(((IntegerNode)left).getValue() * ((IntegerNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.DIVIDE) {
                return new IntegerNode(((IntegerNode)left).getValue() / ((IntegerNode)right).getValue());
            } else if (temp.getInstanceType() == MathOpNode.operator.SUBTRACT) {
                return new IntegerNode(((IntegerNode)left).getValue() - ((IntegerNode)right).getValue());
            }

        }
        throw new RuntimeException("Runtime Exception: Bad Operation");
    }
    public static StringNode resolveString(Node node, HashMap<String, InterpreterDataType> variables) {
        if (node instanceof StringNode) {
            return (StringNode) node;
        } else if(node instanceof CharNode) {
            return new StringNode(String.valueOf(((CharNode) node).getValue()));
        }  else if(node instanceof VariableReferenceNode) {
            InterpreterDataType temp = variables.get(((VariableReferenceNode) node).getName());
            if(temp instanceof CharDataType) {
                return new StringNode(String.valueOf(((CharDataType) temp).getValue()));
            } else {
                return new StringNode(((StringDataType) (variables.get(((VariableReferenceNode) node).getName()))).getValue());
            }
        } else if(node instanceof MathOpNode){
            MathOpNode temp = (MathOpNode) node;
            Node left = resolveString(temp.getLeft(), variables);
            Node right = resolveString(temp.getRight(), variables);
            return new StringNode(((StringNode)left).getValue() +  ((StringNode)right).getValue());
        }
        throw new RuntimeException("Runtime Exception: Bad Operation");
    }
    public static List<ParameterNode> InterpretFunction(FunctionDefinition functionDefinition, List<InterpreterDataType> params) {
        HashMap<String, InterpreterDataType> variables = new HashMap<>();
        List<VariableNode> variableNodes = functionDefinition.getParameters();
        for (VariableNode variableNode : functionDefinition.getParameters()) {
            variables.put(variableNode.getName(), params.remove(0));
        }
        for (VariableNode variableNode : functionDefinition.getVariables()) {
            if (variableNode.getDataType() == Token.type.integer) {
                variables.put(variableNode.getName(), new IntDataType());
            } else if (variableNode.getDataType() == Token.type.real) {
                variables.put(variableNode.getName(), new FloatDataType());
            } else if (variableNode.getDataType() == Token.type.NUMBER) {
                variables.put(variableNode.getName(), new FloatDataType());
            } else if(variableNode.getDataType() == Token.type.Stringtype) {
                variables.put(variableNode.getName(), new StringDataType());
            } else if(variableNode.getDataType() == Token.type.charType) {
                variables.put(variableNode.getName(), new CharDataType());
            } else if(variableNode.getDataType() == Token.type.booleanType) {
                variables.put(variableNode.getName(), new BooleanDataType());
            }
        }
        for (VariableNode variableNode : functionDefinition.getConstants()) {
            if(variableNode.getDataType() == Token.type.real) {
                FloatNode node = (FloatNode) variableNode.getValue();
                FloatDataType floatDataType = new FloatDataType(node.getValue());
                variables.put(variableNode.getName(), floatDataType);
            } else if(variableNode.getDataType() == Token.type.integer) {
                IntegerNode integerNode = (IntegerNode) variableNode.getValue();
                IntDataType intDataType = new IntDataType(integerNode.getValue());
                variables.put(variableNode.getName(), intDataType);
            } else if(variableNode.getDataType() == Token.type.Stringtype) {
                StringNode stringNode = (StringNode) variableNode.getValue();
                StringDataType stringDataType = new StringDataType(stringNode.getValue());
                variables.put(variableNode.getName(), stringDataType);
            } else if(variableNode.getDataType() == Token.type.charType) {
                CharNode charNode = (CharNode) variableNode.getValue();
                CharDataType charDataType = new CharDataType(charNode.getValue());
                variables.put(variableNode.getName(), charDataType);
            } else if(variableNode.getDataType() == Token.type.trueBool || variableNode.getDataType() == Token.type.falseBool) {
                BoolNode boolNode = (BoolNode) variableNode.getValue();
                BooleanDataType booleanDataType = new BooleanDataType(boolNode.isValue());
                variables.put(variableNode.getName(), booleanDataType);
            }
        }
        InterpretBlock(variables, functionDefinition.getStatementNodes());
        List<ParameterNode> returnParams = new ArrayList<>();
        for(VariableNode parameter : functionDefinition.getParameters()) {
            returnParams.add(getParamDataType(variables.get(parameter.getName())));
        }
        return returnParams;
    }
    public static void InterpretBlock(HashMap<String, InterpreterDataType> variables, List<statementNode> statements) {
        for(statementNode statementNode : statements) {
            if(statementNode instanceof FunctionCallNode) {
                FunctionCallNode functionCallNode = (FunctionCallNode) statementNode;
                if(functions.containsKey(functionCallNode.getName())) {
                    CallableNode callableNode = functions.get(functionCallNode.getName());
                    if(callableNode instanceof BuiltInFunctions) {
                        BuiltInFunctions builtInFunctions = (BuiltInFunctions) callableNode;
                        if(builtInFunctions.isVariadic()) {
                            List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();
                            for(ParameterNode param : functionCallNode.getParameters()) {
                                interpreterDataTypes.add(getDataType(param.getParameter(), variables));
                            }
                            List<ParameterNode> updatedVariables = ((BuiltInFunctions) callableNode).Execute(interpreterDataTypes);
                            for(int i = 0; i < functionCallNode.getParameters().size(); i++) {
                                Node temp = functionCallNode.getParameters().get(i).getParameter();
                                if(temp instanceof VariableReferenceNode) {
                                    updateVariable(variables, (VariableReferenceNode) temp, updatedVariables.get(i));
                                }
                            }
                        } else {
                            List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();
                            for(ParameterNode param : functionCallNode.getParameters()) {
                                interpreterDataTypes.add(getDataType(param.getParameter(), variables));
                            }
                            List<ParameterNode> updatedvariables = builtInFunctions.Execute(interpreterDataTypes);
                            for(int i = 0; i < functionCallNode.getParameters().size(); i++) {
                                Node temp = functionCallNode.getParameters().get(i).getParameter();
                                if(temp instanceof VariableReferenceNode) {
                                    updateVariable(variables, (VariableReferenceNode) temp, updatedvariables.get(i));
                                }
                            }
                        }
                    } else {
                        FunctionDefinition functionDefinition = (FunctionDefinition) callableNode;
                        List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();
                        for(ParameterNode param : functionCallNode.getParameters()) {
                            interpreterDataTypes.add(getDataType(param.getParameter(), variables));
                        }
                        List<ParameterNode> updatedvariables = InterpretFunction(functionDefinition, interpreterDataTypes);
                        for(int i = 0; i < functionCallNode.getParameters().size(); i++) {
                            Node temp = functionCallNode.getParameters().get(i).getParameter();
                            if(temp instanceof VariableReferenceNode) {
                                if(functionCallNode.getParameters().get(i).isVar) {
                                    updateVariable(variables, (VariableReferenceNode) temp, updatedvariables.get(i));
                                }
                            }
                        }
                    }
                }
            } else if(statementNode instanceof AssignmentNode) {
                InterpretAssignment(variables, (AssignmentNode) statementNode);
            } else if(statementNode instanceof  WhileNode) {
                InterpretWhileLoop((WhileNode)statementNode, variables);
            } else if(statementNode instanceof ForNode) {
                InterpretForLoop((ForNode) statementNode, variables);
            } else if(statementNode instanceof IfNode) {
                InterpretIfStatement((IfNode) statementNode, variables);
            } else if(statementNode instanceof RepeatNode) {
                InterpretRepeatLoop((RepeatNode) statementNode, variables);
            }
        }
    }
    public static void InterpretAssignment(HashMap<String, InterpreterDataType> variables, AssignmentNode assignmentNode) {
        if(variables.containsKey(assignmentNode.getTarget().getName())) {
            if(variables.get(assignmentNode.getTarget().getName()) instanceof FloatDataType) {
                variables.put(assignmentNode.getTarget().getName(), new FloatDataType(resolveFloat(assignmentNode.getExpression(), variables).getValue()));
            } else if(variables.get(assignmentNode.getTarget().getName()) instanceof IntDataType) {
                variables.put(assignmentNode.getTarget().getName(), new IntDataType(resolveInt(assignmentNode.getExpression(), variables).getValue()));
            } else  if(variables.get(assignmentNode.getTarget().getName()) instanceof  StringDataType) {
                variables.put(assignmentNode.getTarget().getName(), new StringDataType(resolveString(assignmentNode.getExpression(), variables).getValue()));
            } else if(variables.get(assignmentNode.getTarget().getName()) instanceof  CharDataType) {
                variables.put(assignmentNode.getTarget().getName(), new CharDataType(((CharNode)assignmentNode.getExpression()).getValue()));
            } else if(variables.get(assignmentNode.getTarget().getName()) instanceof BooleanDataType) {
                variables.put(assignmentNode.getTarget().getName(), new BooleanDataType(((BoolNode)(assignmentNode.getExpression())).isValue()));
            }
        } else {
            throw new RuntimeException("Runtime Error: Variable doesn't exist");
        }
    }
    public static boolean InterpretBoolean(Node booleanExpressionNode, HashMap<String, InterpreterDataType> variables) {
        if(booleanExpressionNode instanceof BoolNode) {
            return ((BoolNode) booleanExpressionNode).isValue();
            } else if(booleanExpressionNode instanceof MathOpNode) {
            int left = resolveInt(((MathOpNode) booleanExpressionNode).getLeft(), variables).getValue();
            int right = resolveInt(((MathOpNode) booleanExpressionNode).getRight(), variables).getValue();
            if (((MathOpNode) booleanExpressionNode).getInstanceType() == MathOpNode.operator.greaterThan) {
                return left > right;
            } else if (((MathOpNode) booleanExpressionNode).getInstanceType() == MathOpNode.operator.greaterThanEqualTo) {
                return left >= right;
            } else if (((MathOpNode) booleanExpressionNode).getInstanceType() == MathOpNode.operator.lessThan) {
                return left < right;
            } else if (((MathOpNode) booleanExpressionNode).getInstanceType() == MathOpNode.operator.lessThanEqualTo) {
                return left <= right;
            } else if (((MathOpNode) booleanExpressionNode).getInstanceType() == MathOpNode.operator.equalTo) {
                return left == right;
            } else {
                return left != right;
            }
        } else if(booleanExpressionNode instanceof VariableReferenceNode) {
            return ((BooleanDataType)(variables.get(((VariableReferenceNode) booleanExpressionNode).getName()))).isValue();
        }
        throw new RuntimeException("Syntax Error: No boolean Expression");
    }
    public static void updateVariable(HashMap<String, InterpreterDataType> variables, VariableReferenceNode param, ParameterNode updatedParam) {
        InterpreterDataType variableNode = variables.get(param.getName());
        if(variableNode instanceof FloatDataType) {
            if(updatedParam.getParameter() instanceof FloatNode) {
                variables.put(param.getName(), new FloatDataType(((FloatNode) updatedParam.getParameter()).getValue()));
            }
        } else if(variableNode instanceof IntDataType) {
            variables.put(param.getName(), new IntDataType(((IntegerNode) updatedParam.getParameter()).getValue()));
        } else if(variableNode instanceof BooleanDataType) {
            variables.put(param.getName(), new BooleanDataType(((BoolNode) updatedParam.getParameter()).isValue()));
        } else if(variableNode instanceof StringDataType) {
            variables.put(param.getName(), new StringDataType(((StringNode)updatedParam.getParameter()).getValue()));
        } else if(variableNode instanceof CharDataType) {
            variables.put(param.getName(), new CharDataType(((CharNode)updatedParam.getParameter()).getValue()));
        }
    }
    public static void InterpretWhileLoop(WhileNode whileNode, HashMap<String, InterpreterDataType> variables) {
        while(InterpretBoolean(whileNode.getBooleanExpression(), variables)) {
            InterpretBlock(variables, whileNode.getStatementNodeList());
        }
    }
    public static void InterpretRepeatLoop(RepeatNode repeatNode, HashMap<String, InterpreterDataType> variables) {
        do {
            InterpretBlock(variables, repeatNode.getStatementNodeList());
        } while(!InterpretBoolean(repeatNode.getBooleanExpression(), variables));
    }
    public static void InterpretForLoop(ForNode forNode, HashMap<String, InterpreterDataType> variables) {
        int start = ((IntegerNode)forNode.getStart()).getValue();
        int end = ((IntegerNode)forNode.getEnd()).getValue();

        if(!variables.containsKey(forNode.getVariableReferenceNode().getName())) {
            throw new RuntimeException("Runtime Exception: Variable doesn't exist");
        }
        VariableReferenceNode node = forNode.getVariableReferenceNode();
        variables.put(node.getName(), new IntDataType(start));
        if(start < end) {
            while(((IntDataType)(variables.get(node.getName()))).getValue() < end) {
                InterpretBlock(variables, forNode.getStatementNodesList());
                int currValue = ((IntDataType) variables.get(node.getName())).getValue();
                variables.put(node.getName(), new IntDataType(currValue + 1));
            }
        } else {
            while(((IntDataType)variables.get(forNode.getVariableReferenceNode())).getValue() < end) {
                InterpretBlock(variables, forNode.getStatementNodesList());
                int currValue = ((IntDataType) variables.get(forNode.getVariableReferenceNode())).getValue();
                variables.put(forNode.getVariableReferenceNode().getName(), new IntDataType(currValue + 1));
            }
        }
    }
    /*
        Helper Function
        Returns the interpreter data type node for the specific AST data type
     */
    public static InterpreterDataType getDataType(Node param, HashMap<String, InterpreterDataType> variables) {
        if(param instanceof FloatNode) {
            return new FloatDataType(((FloatNode) param).getValue());
        } else if(param instanceof IntegerNode) {
            return new IntDataType(((IntegerNode) param).getValue());
        } else if(param instanceof VariableReferenceNode) {
            return variables.get(((VariableReferenceNode) param).getName());
        } else if(param instanceof StringNode) {
            return new StringDataType(((StringNode) param).getValue());
        } else if(param instanceof CharNode) {
            return new CharDataType(((CharNode) param).getValue());
        } else if(param instanceof BoolNode) {
            return new BooleanDataType(((BoolNode) param).isValue());
        }
        throw new RuntimeException("Runtime Error: Parameter Data Type Invalid");
    }
    /*
        Helper Function
        Changes the interpreter data type to a parameter node
     */
    public static ParameterNode getParamDataType(InterpreterDataType interpreterDataType) {
        if(interpreterDataType instanceof FloatDataType) {
            return new ParameterNode(new FloatNode(((FloatDataType) interpreterDataType).getValue()), true);
        } else if(interpreterDataType instanceof IntDataType) {
            return new ParameterNode(new IntegerNode(((IntDataType)interpreterDataType).getValue()), true);
        } else if(interpreterDataType instanceof StringDataType) {
            return new ParameterNode(new StringNode(((StringDataType) interpreterDataType).getValue()), true);
        } else if(interpreterDataType instanceof CharDataType) {
            return new ParameterNode(new CharNode(String.valueOf(((CharDataType) interpreterDataType).getValue())), true);
        } else if(interpreterDataType instanceof BooleanDataType) {
            return new ParameterNode(new BoolNode(((BooleanDataType) interpreterDataType).isValue()), true);
        }
        else {
            throw new RuntimeException("Runtime Error: Type not accepted yet");
        }
    }
    //Goes through if statement until a boolean expression is true or there is an else or there are no more nested elsif statements
    public static void InterpretIfStatement(IfNode ifNode, HashMap<String, InterpreterDataType> variables) {
        if(ifNode.getBooleanExpression() == null) {
            InterpretBlock(variables, ifNode.getStatementNodesList());
        } else if(InterpretBoolean(ifNode.getBooleanExpression(), variables)) {
            InterpretBlock(variables, ifNode.getStatementNodesList());
        } else if(ifNode.getNext() != null) {
            InterpretIfStatement(ifNode.getNext(), variables);
        }
    }
    //Adds user made function to the string to function hashmap
    public void addFunctionDefintion(String name, FunctionDefinition functionDefinition) {
        functions.put(name, functionDefinition);
    }
    //Calls the start function or throw an error if it doesn't exist
    public static void start() {
        List<InterpreterDataType> params = new ArrayList<>();
        if(!functions.containsKey("start")) {
            throw new RuntimeException("Program must contain from start function");
        }
        InterpretFunction((FunctionDefinition) functions.get("start"), params);
    }
    public void addCallableNode(CallableNode callableNode) {
        functions.put(callableNode.getName(), callableNode);
    }
}

