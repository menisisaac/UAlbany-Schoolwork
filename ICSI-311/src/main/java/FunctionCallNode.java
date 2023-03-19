import java.util.List;
/*
    Holds calls to user defined and language defined functions
 */
public class FunctionCallNode extends statementNode{
    private String name;
    private List<ParameterNode> parameters;

    public FunctionCallNode(String name, List<ParameterNode> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        String result = "Function Name: " + name + "\n          Parameters: ";
        for(ParameterNode parameterNode : parameters) {
            result += parameterNode.toString() + ", ";
        }

        return result;
    }
}
