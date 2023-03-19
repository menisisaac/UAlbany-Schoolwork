public class BooleanDataType extends InterpreterDataType{
    private boolean value;
    public BooleanDataType(boolean value) {
        this.value = value;
    }
    public BooleanDataType() {
        this.value = true;
    }
    @Override
    public String toString() {
        return "" + value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public void FromString(String input) {
        if(input.equals("true")) {
            this.value = true;
        } else if(input.equals("false")) {
            this.value = false;
        } else {
            throw new RuntimeException("Runtime Error: Must input true or false for type boolean");
        }

    }
}
