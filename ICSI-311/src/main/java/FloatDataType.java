/*
    Holds the real number data type in the interpreter
 */
public class FloatDataType extends InterpreterDataType {
    private Float value;

    public FloatDataType() {
        value = 0f;
    }
    public FloatDataType(Float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        //return "Int: " + value;
        return value + "";
    }
    @Override
    public void FromString(String input) {

        value = Float.parseFloat(input);
    }
}

