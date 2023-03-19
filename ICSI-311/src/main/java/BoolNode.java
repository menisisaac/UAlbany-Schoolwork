/*
    Used to hold boolean variables such as true and false
 */
public class BoolNode extends Node{
    private boolean value;
    public BoolNode(boolean value) {
        this.value = value;
    }
    public boolean isValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Value: " + value;
    }
}
