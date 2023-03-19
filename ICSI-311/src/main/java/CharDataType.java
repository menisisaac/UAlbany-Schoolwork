public class CharDataType extends InterpreterDataType{
    private char value;
    public CharDataType(char value) {
        this.value = value;
    }
    public CharDataType() {}

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public void FromString(String input) {
        this.value = input.charAt(0);
    }
}
