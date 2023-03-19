public class StringDataType extends InterpreterDataType{

    private String value;

    public StringDataType(String value) {
        this.value = value;
    }
    public StringDataType() {}
    @Override
    public String toString() {
        return "" + value;
    }
    public String getValue() {
        return value;
    }

    @Override
    public void FromString(String input) {
        value = input;
    }
}
