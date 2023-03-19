/*
    Holds information about each variable including name, constant or variable,
    data type and its value
 */
public class VariableNode extends Node{
    private String name;
    private boolean isConstant;
    private Token.type dataType;
    private Node value;

    public VariableNode(String name, boolean isConstant, Token.type dataType){
        this.name = name;
        this.isConstant = isConstant;
        this.dataType = dataType;
    }

    public VariableNode(String name, boolean isConstant, Token.type dataType, Node value) {
        this.name = name;
        this.isConstant = isConstant;
        this.dataType = dataType;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public Token.type getDataType() {
        return dataType;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        String result = "Name: " + name + "  ";
        result += "Value: " + value + "  ";
        result += "Type: " + dataType + "  ";
        if(isConstant) {
            result += "I'm a Constant\n";
        } else {
            result += "I'm a Variable\n";
        }
        return result;
    }
}
