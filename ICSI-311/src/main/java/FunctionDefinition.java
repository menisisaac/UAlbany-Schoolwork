import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
    Represents user defined functions
    Holds parameters, and a separate constants and variables list along with statements in the function body
 */
public class FunctionDefinition extends CallableNode{
    private HashSet<VariableNode> variables;
    private HashSet<VariableNode> constants;
    private List<statementNode> statementNodes = new ArrayList<>();
    public FunctionDefinition(String name, List<VariableNode> parameters, HashSet<VariableNode> variables, HashSet<VariableNode> constants, List<statementNode> statementNodes) {
        super(name, parameters);
        this.variables = variables;
        this.constants = constants;
        this.statementNodes = statementNodes;
    }

    public String getName() {
        return super.getName();
    }

    public List<VariableNode> getParameters() {
        return super.getParameters();
    }

    public HashSet<VariableNode> getVariables() {
        return variables;
    }

    public HashSet<VariableNode> getConstants() {
        return constants;
    }

    public List<statementNode> getStatementNodes() {
        return statementNodes;
    }

    @Override
    public String toString() {
        String result = "Function name: " + super.getName() + "\n";
        if(!super.getParameters().isEmpty()) {
            result += "Parameters: \n";
            for (VariableNode node : super.getParameters()) {
                result += "     " + node.toString();
            }
        }
        if(!constants.isEmpty()) {
            result += "Constants: \n";
            for (VariableNode node : constants) {
                result += "     " + node.toString();
            }
        }
        if(!variables.isEmpty()) {
            result += "Variables: \n";
            for (VariableNode node : variables) {
                result += "     " + node.toString();
            }
        }
        result += "Statements: \n";
        for (statementNode statementNode: statementNodes) {
            result += "     " + statementNode.toString() + "\n";
        }
        return result;
    }
}
