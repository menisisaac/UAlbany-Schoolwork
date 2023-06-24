import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class IRGeneration {
    final private HashMap<String, CallableNode> functions;
    private HashMap<String, ArrayList<String>> bril = new HashMap<>();
    private int integer = 0;
    private int character = 0;
    private int floating = 0;
    private int ifStatements = 0;
    private int bool = 0;
    public IRGeneration(HashMap<String, CallableNode> f) {
        functions = f;
    }
    public void compileFunctions() {

        for (String name:functions.keySet()) {
            if (functions.get(name) instanceof FunctionDefinition)
                compileFunction(name, functions.get(name));
        }
    }
    public void compileFunction(String name, CallableNode function) {
        bril.put(name, new ArrayList<String>());
        String header = "@" + name + "(";
        if (function.getParameters() != null) {
            for (VariableNode param : function.getParameters()) {
                switch (param.getDataType()) {
                    case integer:
                        header += param.getName() + ": " + "int, ";
                        break;
                    case real:
                        header += param.getName() + ": " + "float, ";
                        break;
                    case charType:
                        header += param.getName() + ": " + "char, ";
                        break;
                }
            }
            if (function.getParameters().size() > 0)
                header = header.substring(0, header.length() - 2);
        }
        header += ") {";
        bril.get(name).add(header);
        FunctionDefinition fun = (FunctionDefinition)function;
        if (fun.getConstants() != null) {
            for (VariableNode constant : fun.getConstants()) {
                switch (constant.getDataType()) {
                    case integer:
                        bril.get(name).add(constant.getName() + ": " + "int = const " + ((IntegerNode)constant.getValue()).getValue() + ";");
                        break;
                    case real:
                        bril.get(name).add(constant.getName() + ": " + "float = const " + ((FloatNode)constant.getValue()).getValue() + ";");
                        break;
                    case charType:
                        bril.get(name).add(constant.getName() + ": " + "char = const " + ((CharNode)constant.getValue()).getValue() + ";");
                        break;
                }
            }
        }
        compileStatements(name, fun.getStatementNodes(), fun.getVariables());



        bril.get(name).add("}");
        for(String str : bril.get(name)) {
            System.out.println(str);
        }
    }
    public void compileStatements(String name, List<statementNode> statements, HashSet<VariableNode> variables) {
        if (statements != null) {
            for (statementNode sn : statements) {
                if (sn instanceof AssignmentNode) {
                    AssignmentNode an = (AssignmentNode)sn;
                    String assName = an.getTarget().getName();
                    Token.type type = null;
                    for (VariableNode vn : variables) {
                        if (vn.getName().equals(assName))
                            type = vn.getDataType();
                    }
                    if (type == Token.type.integer) {
                        String exprName = compileIntExpression(name, an.getExpression());
                        bril.get(name).add(assName + ": int = id " + exprName + ";");
                    }
                } else if (sn instanceof IfNode) {
                    compileIfStatement(name, (IfNode) sn, variables);
                } else if (sn instanceof FunctionCallNode) {
                    compileFunctionCallNode(name, (FunctionCallNode) sn);
                }
            }
        }
    }
    void compileFunctionCallNode(String name, FunctionCallNode fcn) {
        String result = "call @" + fcn.getName();
        if (fcn.getName().equals("write")) {
            result = "print";
        }
        for (ParameterNode pn : fcn.getParameters()) {
            if (pn.getParameter() instanceof VariableReferenceNode) {
                result += " " + ((VariableReferenceNode) pn.getParameter()).getName();
            }
        }
        bril.get(name).add(result + ";");
    }

    public void compileIfStatement(String name, IfNode ifnode, HashSet<VariableNode> variables) {
        String dest = compileBooleanExpression(name, ifnode.getBooleanExpression());
        bril.get(name).add("br " + dest + " .IFTRUE" + ifStatements + " .IFFALSE" + ifStatements + ";");
        bril.get(name).add(".IFTRUE" + ifStatements + ":");
        ifStatements += 1;
        String label = ".IFFALSE" + (ifStatements - 1) + ":";
        String endLabel = ".IFEND" + (ifStatements - 1) + ":";
        compileStatements(name, ifnode.getStatementNodesList(), variables);
        bril.get(name).add("jmp " + endLabel.substring(0, endLabel.length() - 1) + ";");
        if(ifnode.getNext() instanceof ElseNode) {
            bril.get(name).add(label);
            compileStatements(name, ifnode.getNext().getStatementNodesList(), variables);
        }
        bril.get(name).add(endLabel);


    }

    public String compileBooleanExpression(String name, Node node) {
        if(node instanceof BoolNode) {
            int number = this.bool;
            bool++;
            bril.get(name).add("b" + number + ": bool = const " + ((BoolNode)node).isValue());
            return "b" + number;
        } else if (node instanceof VariableReferenceNode) {
            return ((VariableReferenceNode)node).getName();
        } else if (node instanceof MathOpNode) {
            MathOpNode temp = (MathOpNode) node;
            String left = compileBooleanExpression(name, temp.getLeft());
            String right = compileBooleanExpression(name, temp.getRight());
            String result = "b" + bool + ": bool = ";
            String target = "b" + bool;
            if (temp.getInstanceType() == MathOpNode.operator.greaterThan) {
                result += "gt " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.greaterThanEqualTo) {
                result += "ge " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.lessThan) {
                result += "lt " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.lessThanEqualTo) {
                result += "le " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.equalTo) {
                result += "eq " + left + " " + right;
            } else {
                result += "eq " + left + " " + right + ";";
                bril.get(name).add(result);
                bool++;
                result = "b" + bool + ": bool = not " + "b" + target;
                target = "b" + bool;
            }
            bril.get(name).add(result + ";");
            return target;
        }
        return compileIntExpression(name, node);
    }

    public String compileIntExpression(String name, Node node) {
        if(node instanceof IntegerNode) {
            int number = this.integer;
            integer++;
            bril.get(name).add("t" + number + ": int = const " + ((IntegerNode)node).getValue() + ";");
            return "t" + number;
        } else if (node instanceof VariableReferenceNode) {
            return ((VariableReferenceNode) node).getName();
        } else if (node instanceof MathOpNode) {
            MathOpNode temp = (MathOpNode)node;
            String left = compileIntExpression(name, temp.getLeft());
            String right = compileIntExpression(name, temp.getRight());
            String result = "t" + integer + ": int = ";
            String target = "t" + integer;
            if(temp.getInstanceType() == MathOpNode.operator.ADD) {
                result = "add " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.MULTIPLY) {
                result += "mul " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.DIVIDE) {
                result += "div " + left + " " + right;
            } else if (temp.getInstanceType() == MathOpNode.operator.SUBTRACT) {
                result += "sub " + left + " " + right;
            }
            bril.get(name).add(result + ";");
            integer++;
            return target;
        }
        return "";
        //throw new RuntimeException("Runtime Exception: Bad Operation");
    }




}

