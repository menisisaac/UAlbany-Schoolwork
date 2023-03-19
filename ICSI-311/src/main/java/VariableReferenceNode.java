/*
    References the name of a variable but no value
 */
public class VariableReferenceNode extends Node{
    private String name;

    public VariableReferenceNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }
}
