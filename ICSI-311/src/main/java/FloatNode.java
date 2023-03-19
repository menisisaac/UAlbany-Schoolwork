/*
    Holds the real number data type in the parser
 */
public class FloatNode extends Node{

    private final float value;

    public FloatNode(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Real: " + value;
    }
}
