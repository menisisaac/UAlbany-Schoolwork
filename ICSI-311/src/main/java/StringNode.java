/*
    Holds the String data type for the parser
 */
public class StringNode extends Node{
    private String value;
    public StringNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "String: " + value;
    }
}
