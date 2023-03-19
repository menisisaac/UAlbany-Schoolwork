/*
    Holds the int data type in the interpreter
 */
public class IntDataType extends InterpreterDataType{
    int value;

    public IntDataType() {
        value = 0;
    }
    public IntDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
    @Override
    public void FromString(String input) {
        value = Integer.parseInt(input);
    }
}
