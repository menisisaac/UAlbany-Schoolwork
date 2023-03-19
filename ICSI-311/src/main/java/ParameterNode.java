/*
    Represents a parameter in a function call
    Can contain any data type and if it's a variable or constant
 */
public class ParameterNode extends Node{

    Node parameter;
    boolean isVar;

    public ParameterNode(Node parameter, boolean isVar) {
        this.parameter = parameter;
        this.isVar = isVar;
    }

    public Node getParameter() {
        return parameter;
    }

    public boolean isVar() {
        return isVar;
    }

    @Override
    public String toString() {
        return parameter.toString();
    }
}
