

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SemanticAnalysis {
    private static HashSet<MathOpNode.operator> operators = new HashSet<>(Arrays.asList(MathOpNode.operator.notEqual, MathOpNode.operator.lessThanEqualTo,
            MathOpNode.operator.lessThan, MathOpNode.operator.greaterThanEqualTo, MathOpNode.operator.greaterThan, MathOpNode.operator.equalTo));
    public static void SemanticAnalysis(FunctionDefinition functionDefinition) {
        for(statementNode statementNode : functionDefinition.getStatementNodes()) {
            if(statementNode instanceof AssignmentNode) {
                analyzeIfVariable(functionDefinition, ((AssignmentNode) statementNode).getTarget().getName());
                analyzeAssignment((AssignmentNode) statementNode, getVariables(functionDefinition));
            } else if(statementNode instanceof WhileNode) {
                analyzeBlock(((WhileNode) statementNode).getStatementNodeList(), getVariables(functionDefinition));
            } else if(statementNode instanceof ForNode) {
                analyzeBlock(((ForNode) statementNode).getStatementNodesList(), getVariables(functionDefinition));
            } else if(statementNode instanceof RepeatNode) {
                analyzeBlock(((RepeatNode) statementNode).getStatementNodeList(), getVariables(functionDefinition));
            } else if(statementNode instanceof IfNode) {
                analyzeIfStatement((IfNode) statementNode, getVariables(functionDefinition));
            }
        }
    }
    public static void analyzeAssignment(AssignmentNode assignmentNode, HashMap<String, Token.type> variables) {
        Token.type datatype = variables.get(assignmentNode.getTarget().getName());
        analyzeForType(assignmentNode.getExpression(), variables, datatype);
    }
    public static void analyzeIfStatement(IfNode ifNode, HashMap<String, Token.type> variables) {
        analyzeBlock(ifNode.getStatementNodesList(), variables);
        if(ifNode.getNext() != null) {
            analyzeIfStatement(ifNode.getNext(), variables);
        }
    }
    public static void analyzeIfVariable(FunctionDefinition functionDefinition, String target) {
        if(functionDefinition.getConstants().contains(target)) {

            throw new RuntimeException("Cannot assign values to constant:  " + target);
        }
        for(VariableNode variableNode : functionDefinition.getParameters()) {
            if(variableNode.getName().equals(target) && variableNode.isConstant()) {
                System.out.println(target);
                throw new RuntimeException("Cannot assign values  to constants: " + target);
            }
        }
    }
    public static void analyzeBlock(List<statementNode> statementNodeList, HashMap<String, Token.type> variables) {
        for(statementNode statementNode : statementNodeList) {
            if (statementNode instanceof AssignmentNode) {
                analyzeAssignment((AssignmentNode) statementNode, variables);
            } else if (statementNode instanceof WhileNode) {
                analyzeBlock(((WhileNode) statementNode).getStatementNodeList(), variables);
            } else if (statementNode instanceof ForNode) {
                analyzeBlock(((ForNode) statementNode).getStatementNodesList(), variables);
            } else if (statementNode instanceof RepeatNode) {
                analyzeBlock(((RepeatNode) statementNode).getStatementNodeList(), variables);
            } else if (statementNode instanceof IfNode) {
                analyzeIfStatement((IfNode) statementNode, variables);
            }
        }
    }
    public static void analyzeForType(Node expression, HashMap<String, Token.type> variables, Token.type type) {
        if(type == Token.type.Stringtype) {
            analyzeStringChar(expression, variables);
        } else if(type == Token.type.integer) {
            analyzeInt(expression, variables);
        } else if(type == Token.type.real) {
            analyzeFloat(expression, variables);
        } else if(type == Token.type.charType) {
            analyzeChar(expression, variables);
        }
    }
    public static void analyzeChar(Node expression, HashMap<String, Token.type> variables) {
        if(expression instanceof CharNode) {

        } else {
            throw new RuntimeException("Cannot have expression " + expression.getClass() + " to char target");
        }
    }
    public static void analyzeStringChar(Node expression, HashMap<String, Token.type> variables) {
        if(expression instanceof StringNode || expression instanceof CharNode) {

        } else if(expression instanceof VariableReferenceNode && (variables.get(((VariableReferenceNode) expression).getName()) == Token.type.Stringtype ||
                variables.get(((VariableReferenceNode)expression).getName()) == Token.type.charType)) {

        } else if(expression instanceof MathOpNode){
            analyzeStringChar(((MathOpNode) expression).getLeft(), variables);
            analyzeStringChar(((MathOpNode) expression).getRight(), variables);
            if(((MathOpNode) expression).getInstanceType() != MathOpNode.operator.ADD) {
                throw new RuntimeException("Cannot use " + ((MathOpNode) expression).getInstanceType() + " on type String");
            }
        } else {
            if(expression instanceof VariableReferenceNode) {
                throw new RuntimeException("Type error, cannot cast  " + variables.get(expression) + " to type String");
            }
            throw new RuntimeException("Type error, cannot cast  " + expression + " to type String");
        }
    }
    public static void analyzeInt(Node expression, HashMap<String, Token.type> variables) {
        if(expression instanceof IntegerNode) {
        } else if(expression instanceof VariableReferenceNode && variables.get(((VariableReferenceNode) expression).getName()) == Token.type.integer) {

        } else if(expression instanceof MathOpNode) {
            analyzeInt(((MathOpNode) expression).getLeft(), variables);
            analyzeInt(((MathOpNode) expression).getRight(), variables);
            if(operators.contains(((MathOpNode) expression).getInstanceType())) {
                throw new RuntimeException("Cannot use comparing operators on type int");
            }
        } else {
            throw new RuntimeException("Type error cannot cast " + expression + " to type int");
        }
    }
    public static void analyzeFloat(Node expression, HashMap<String, Token.type> variables) {
        if(expression instanceof FloatNode) {
        } else if(expression instanceof VariableReferenceNode && variables.get(((VariableReferenceNode) expression).getName()) == Token.type.real) {

        } else if(expression instanceof MathOpNode) {
            analyzeFloat(((MathOpNode) expression).getLeft(), variables);
            analyzeFloat(((MathOpNode) expression).getRight(), variables);
            if(operators.contains(((MathOpNode) expression).getInstanceType())) {
                throw new RuntimeException("Cannot use comparing operators on type int");
            }
        } else {
            throw new RuntimeException("Type error cannot cast " + expression + " to type float");
        }
    }


    public static HashMap<String, Token.type> getVariables(FunctionDefinition functionDefinition) {
        HashMap<String, Token.type> variables = new HashMap<>();
        for(VariableNode param  : functionDefinition.getParameters())  {
            variables.put(param.getName(), param.getDataType());
        }
        for(VariableNode variable : functionDefinition.getVariables()) {
            variables.put(variable.getName(), variable.getDataType());
        }
        for(VariableNode constant : functionDefinition.getConstants()) {
            variables.put(constant.getName(), constant.getDataType());
        }
        return variables;
    }
}
