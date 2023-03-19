/*
    Holds the int data type for the parser
 */
public class IntegerNode extends Node{

    private final int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {

        return "Integer: " + value;
    }
}
