import java.util.List;
/*
    Class on which all functions are built off of
 */
public abstract class CallableNode extends Node{
    private String name;
    private List<VariableNode> parameters;

    public CallableNode(String name, List<VariableNode> parameters) {
        this.name = name;
        this.parameters = parameters;

    }

    public String getName() {
        return name;
    }

    public List<VariableNode> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Function Name: " + name;
    }
}
